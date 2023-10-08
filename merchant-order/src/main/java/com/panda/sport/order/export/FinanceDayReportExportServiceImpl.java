package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.panda.sport.backup.mapper.MergeOrderMixMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.OrderMapper;
import com.panda.sport.backup.mapper.OrderMixExportMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.DateTimeUtils;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.panda.sport.merchant.common.constant.Constant.UTC8;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  用户报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("financeDayReportExportServiceImpl")
@Slf4j
public class FinanceDayReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    protected OrderMapper orderMapper;

    @Autowired
    protected OrderMixExportMapper orderMixExportMapper;

    @Autowired
    protected MerchantMapper merchantMapper;

    @Autowired
    protected MergeOrderMixMapper mergeOrderMixMapper;

    private static final int threadSize = 10000;

    private static final int pageSize = 1000;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {

        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            long startTL = System.currentTimeMillis();
            super.updateFileStart(merchantFile.getId());
            log.info("开始对账单报表导出 param = {}", merchantFile.getExportParam());
            MerchantFinanceDayVo dayVo = JSON.parseObject(merchantFile.getExportParam(), MerchantFinanceDayVo.class);
            Integer count  = countFinanceDayVo(dayVo);
            log.info("组装 导出注单MerchantFinanceDayVo: {} , 总数 = {}", dayVo.getCreateTime(), count);
            List<Map<String, String>> resultList;
            if (count > pageSize) {
                resultList = new ArrayList<>();
                super.updateFileStart(merchantFile.getId());
                int thrednum = (count % threadSize == 0) ? (count / threadSize) : (count / threadSize + 1);
                ExecutorService service = Executors.newFixedThreadPool(thrednum);
                log.info("export  thrednum=" + thrednum);
                try {
                    BlockingQueue<Future<List<Map<String, String>>>> queue = new LinkedBlockingQueue<Future<List<Map<String, String>>>>();
                    for (int i = 0; i < thrednum; i++) {
                        Future<List<Map<String, String>>> future = service.submit(read2List(i,  dayVo));
                        queue.add(future);
                    }
                    int queueSize = queue.size();
                    log.info("export  queueSize=" + queueSize);
                    for (int i = 0; i < queueSize; i++) {
                        List<Map<String, String>> list = queue.take().get();
                        log.info("export  list=" + (list == null ? 0 : list.size()));
                        resultList.addAll(list);
                    }
                    log.info("export  resultList=" + resultList.size());
                } finally {
                    service.shutdown();
                }
            } else {
                dayVo.setPageNum(1);
                dayVo.setPageSize(pageSize);
                resultList = financeDayExportListQuery(dayVo);
            }
            super.updateRate(merchantFile.getId(), 80L);
            log.info("查询结束,花费时间:" + (System.currentTimeMillis() - startTL));
            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(exportFinanceDayCsv(resultList, Constant.LANGUAGE_CHINESE_SIMPLIFIED));
            super.uploadFile(merchantFile, inputStream);
            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTL));
            super.updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("对账单统计报表异常!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("流关闭异常！");
                }
            }
        }
    }



    private Callable< List<Map<String, String>>> read2List(final int i, final MerchantFinanceDayVo dayVo) {
        long starTime = System.currentTimeMillis();
        Callable< List<Map<String, String>>> callable = new Callable< List<Map<String, String>>>() {
            @Override
            public  List<Map<String, String>> call() throws Exception {
                List<Map<String, String>> list = new ArrayList<>();
                int startIndex = i * threadSize;
                for (int j = 0; j < 10; j++) {
                    MerchantFinanceDayVo parms = new MerchantFinanceDayVo();
                    BeanUtils.copyProperties(dayVo, parms);
                    dayVo.setPageNum(startIndex + (j * pageSize));
                    dayVo.setPageSize(pageSize);
                    try {
                        List<Map<String, String>> listResult = financeDayExportListQuery(dayVo);
                        list.addAll(listResult);
                    } catch (Exception e) {
                        log.error("注单查询导出异常！开始补偿", e);
                        int i = 10;
                        while (i > 0) {
                            i--;
                            try {
                                List<Map<String, String>> listResult = financeDayExportListQuery(dayVo);
                                log.info("read2List list=" + list.size());
                                list.addAll(listResult);
                                i = 0;
                            } catch (Exception e1) {
                                log.error("注单查询导出异常！,补偿错误！", e1);
                            }
                        }
                    }
                }
                return list;
            }
        };
        long time = System.currentTimeMillis() - starTime;
        if (time > 300000) {
            log.info("数据库查询订单耗时 {}", time / 1000);
        }
        return callable;
    }



    private Integer countFinanceDayVo(MerchantFinanceDayVo dayVo){
        String time = dayVo.getFinanceDayId().substring(0, 10);
        String merchantCode = dayVo.getFinanceDayId().substring(11);
        Long merchantId = merchantMapper.getMerchantId(merchantCode);
        if (null == merchantId) {
            return 0;
        }

        long oneDay = 24 * 60 * 60 * 1000;
        long half = oneDay / 2;
        long createTime = DateTimeUtils.convertString2Date("yyyy-MM-dd", time).getTime();
        long endTime = createTime;
        if (UTC8.equals(dayVo.getTimeZone())) {
            //自然日
            endTime = endTime + oneDay - 1;
        } else {
            //账务日
            createTime = createTime + half;
            endTime = endTime + oneDay + half - 1;
        }
        String language = dayVo.getLanguage();
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if ("3".equals(dayVo.getFilter())) {
            //结算时间
            return mergeOrderMixMapper.countOrderExportSettleList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency());
        } else {
            return mergeOrderMixMapper.countOrderExportList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency());
        }
    }


    /**
     * 日对账单导出list
     */
    private List<Map<String, String>> financeDayExportListQuery(MerchantFinanceDayVo dayVo) {
        String time = dayVo.getFinanceDayId().substring(0, 10);
        String merchantCode = dayVo.getFinanceDayId().substring(11);
        Long merchantId = merchantMapper.getMerchantId(merchantCode);
        if (null == merchantId) {
            return null;
        }

        long oneDay = 24 * 60 * 60 * 1000;
        long half = oneDay / 2;
        long createTime = DateTimeUtils.convertString2Date("yyyy-MM-dd", time).getTime();
        long endTime = createTime;
        if (UTC8.equals(dayVo.getTimeZone())) {
            //自然日
            endTime = endTime + oneDay - 1;
        } else {
            //账务日
            createTime = createTime + half;
            endTime = endTime + oneDay + half - 1;
        }
        String language = dayVo.getLanguage();
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if ("3".equals(dayVo.getFilter())) {
            //结算时间
            // return orderMixExportMapper.queryOrderExportSettleList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency());
            return mergeOrderMixMapper.queryOrderExportSettleList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency(), dayVo.getPageNum(),dayVo.getPageSize());
        } else {
            //return orderMixExportMapper.queryOrderExportList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency());
            return mergeOrderMixMapper.queryOrderExportList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency(), dayVo.getPageNum(),dayVo.getPageSize());
        }
    }
}
