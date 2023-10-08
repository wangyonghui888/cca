package com.panda.sport.admin.export;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.backup.mapper.BackupTransferRecordMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.TransferRecordBizTypeEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.TransferRecordVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  交易记录导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("transferRecordExportServiceImpl")
@Slf4j
public class TransferRecordExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private BackupTransferRecordMapper transferRecordMapper;

    private static final int threadSize = 40000;

    private static final int pageSize = 1000;
    @Override
    @Async("asyncServiceExecutor")
    public void export(MerchantFile merchantFile) {
        long startTime = System.currentTimeMillis();
        List<TransferRecordVO> mapList = new ArrayList<>();
        try {
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            log.info("开始交易记录导出 param = {}", merchantFile.getExportParam());
            UserAccountFindVO findVO = JSON.parseObject(merchantFile.getExportParam(), UserAccountFindVO.class);
            int total = 0;
            if (merchantFile.getDataSize() == null) {
                total = queryCount(findVO);
            }else {
                total = merchantFile.getDataSize();
            }
            log.info("组装 开始交易记录导出: 总数 = {}", merchantFile.getDataSize());
            if (total < MerchantFileService.SPLIT_FILE_DATE_SIZE_2) {
                InputStream inputStream = null;
                int size = 1000;
                int i = 0;
                int star = 0;
                super.updateFileStart(merchantFile.getId());
                super.updateRate(merchantFile.getId(), 40L);
                try {
                    do {
                        findVO.setStart(star);
                        findVO.setPageSize(size);
                        List<TransferRecordVO> list = queryList(findVO);
                        if (list != null) {
                            mapList.addAll(list);
                        }
                        i++;
                        star = i * size;
                    } while (star < total);
                    super.updateRate(merchantFile.getId(), 80L);
                    log.info("查询结束,花费时间:" + (System.currentTimeMillis() - startTime));
                    if (CollectionUtils.isEmpty(mapList)) {
                        throw new Exception("未查询到数据");
                    }
                    inputStream = new ByteArrayInputStream(exportTransferRecordCsv(mapList, findVO.getLanguage()));
                    super.uploadFile(merchantFile, inputStream);
                    log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
                }finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            log.error("流关闭异常！", e);
                        }
                    }
                }
            }else {
                String[] names = merchantFile.getFtpFileName().split(",");
                int k = 0;
                for (int j = names.length; j > 0; j--) {
                    InputStream inputStream = null;
                    try {
                        mapList = new ArrayList<>();
                        super.updateFileStart(merchantFile.getId());
                        int thrednum = 5;
                        ExecutorService service = Executors.newFixedThreadPool(thrednum);
                        log.info("export  thrednum=" + thrednum);
                        try {
                            BlockingQueue<Future<List<TransferRecordVO>>> queue = new LinkedBlockingQueue<>();
                            for (int i = 0; i < thrednum; i++) {
                                Future<List<TransferRecordVO>> future = service.submit(queryList(k, i, findVO));
                                queue.add(future);
                            }
                            int queueSize = queue.size();
                            log.info("export  queueSize=" + queueSize);
                            for (int i = 0; i < queueSize; i++) {
                                List<TransferRecordVO> list = queue.take().get();
                                log.info("export  list=" + (list == null ? 0 : list.size()));
                                mapList.addAll(list);
                            }
                            log.info("export  mapList=" + mapList.size());
                        } finally {
                            service.shutdown();
                        }
                        super.updateRate(merchantFile.getId(), 80L / j);
                        log.info("查询结束,花费时间:" + (System.currentTimeMillis() - startTime));
                        if (CollectionUtils.isEmpty(mapList)) {
                            throw new Exception("未查询到数据");
                        }
                        inputStream = new ByteArrayInputStream(exportTransferRecordCsv(mapList, findVO.getLanguage()));
                        super.uploadFileMax(merchantFile, inputStream, names[k]);
                        log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                log.error("流关闭异常3！", e);
                            }
                        }
                    }
                    k++;
                }
                super.updateFileStatusEnd(merchantFile.getId());
            }
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), "导出失败!");
            log.error("开始交易记录导出异常！", e);
        }
    }

    public List<TransferRecordVO> queryList(UserAccountFindVO findVO) {
        return transferRecordMapper.findTransferRecordExportList(findVO);
    }

    public Callable<List<TransferRecordVO>> queryList(final int j, final int i,final UserAccountFindVO findVO) {
        long starTime = System.currentTimeMillis();
        Callable<List<TransferRecordVO>> callable = new Callable<List<TransferRecordVO>>() {
            @Override
            public List<TransferRecordVO> call() throws Exception {
                List<TransferRecordVO> list = new ArrayList<>();
                int startIndex = i * threadSize + (j * MerchantFileService.SPLIT_FILE_DATE_SIZE_2);
                for (int k = 0; k < 40; k++) {
                    findVO.setStart(startIndex + (k * pageSize));
                    findVO.setPageSize(pageSize);
                    try {
                        List<TransferRecordVO> listResult = transferRecordMapper.findTransferRecordExportList(findVO);
                        if (CollectionUtil.isEmpty(listResult)){
                            break;
                        }
                        list.addAll(listResult);
                    } catch (Exception e) {
                        log.error("注单查询导出异常！开始补偿", e);
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

    public int queryCount(UserAccountFindVO findVO) {
        return transferRecordMapper.findTransferRecordExportListCount(findVO);
    }

    /**
     * 导出用户到csv文件
     *
     * @param mapList  导出的数据
     * @param language
     */
    private byte[] exportTransferRecordCsv(List<TransferRecordVO> mapList, String language) {

        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        //序号
        int index = 0;
        for (TransferRecordVO vo : mapList) {
            index++;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", "\t" + index + "\t");
            rowData.put("2", "\t" + DateUtil.format(new Date(Long.parseLong(String.valueOf(vo.getCreateTime()))), "yyyy-MM-dd HH:mm:ss" + "\t"));
            rowData.put("3", "\t" + vo.getTransferId() + "\t");
            rowData.put("4", "\t" + vo.getFakeName() + "\t");
            rowData.put("5", "\t" + vo.getUserId() + "\t");
            rowData.put("6", "\t" + vo.getMerchantCode() + "\t");
            rowData.put("7", (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ?
                    TransferRecordBizTypeEnum.getDescByCode(Integer.valueOf(String.valueOf(vo.getBizType()))) :
                    TransferRecordBizTypeEnum.getEnByCode(Integer.valueOf(String.valueOf(vo.getBizType())))));
            rowData.put("8", (("1".equals(String.valueOf(vo.getTransferType())) ? "+" : "-") + String.valueOf(vo.getAmount())));
            rowData.put("9", vo.getBeforeTransfer());
            rowData.put("10", vo.getAfterTransfer());
            rowData.put("11", ("1".equals(String.valueOf(vo.getStatus())) ? (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "成功" : "Succeed") :
                    language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "失败" : "failure"));
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getAccountheader(language), exportData);
    }

    public static void main(String[] args) {
        String a = ("1".equals(String.valueOf(1)) ? ("zs".equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "成功" : "Succeed") :
                "zs".equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "失败" : "failure");
        System.out.println(a);
    }

    private LinkedHashMap<String, String> getAccountheader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "交易时间");
            header.put("3", "交易号");
            header.put("4", "用户名");
            header.put("5", "用户ID");
            header.put("6", "所属商户");
            header.put("7", "交易类型");
            header.put("8", "交易金额");
            header.put("9", "交易前余额");
            header.put("10", "交易后余额");
            header.put("11", "交易结果");
        } else {
            header.put("1", "NO");
            header.put("2", "Time");
            header.put("3", "TransactionId");
            header.put("4", "Player");
            header.put("5", "PlayerId");
            header.put("6", "Merchant");
            header.put("7", "BizType");
            header.put("8", "Amount");
            header.put("9", "BeforeAmount");
            header.put("10", "AfterAmount");
            header.put("11", "Result");
        }
        return header;
    }
}
