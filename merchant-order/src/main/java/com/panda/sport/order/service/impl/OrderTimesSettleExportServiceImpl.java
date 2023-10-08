package com.panda.sport.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.backup.mapper.BackUpOrderTimesSettleInfoMapper;
import com.panda.sport.merchant.common.po.bss.OrderTimesSettleInfoPO;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoReqVO;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoRespVO;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  用户报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("orderTimesSettleExportServiceImpl")
@Slf4j
public class OrderTimesSettleExportServiceImpl extends AbstractOrderFileExportService {

    @Resource
    protected BackUpOrderTimesSettleInfoMapper orderTimesSettleInfoMapper;
    private Integer pageSize = 100000;

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
            log.info("开始二次结算报表导出 param = {}", merchantFile.getExportParam());
            OrderTimesSettleInfoReqVO reqVO = JSON.parseObject(merchantFile.getExportParam(), OrderTimesSettleInfoReqVO.class);

            // 重新设置分页条件
            reqVO.setPageNum(1);
            reqVO.setPageSize(pageSize);


            Page<Object> page = PageHelper.startPage(1, pageSize, true);

            List<OrderTimesSettleInfoPO> poList = orderTimesSettleInfoMapper.pageList(reqVO);
            // 数据转换
            List<OrderTimesSettleInfoRespVO>  voList = poList.stream().map(po -> {
                OrderTimesSettleInfoRespVO vo = new OrderTimesSettleInfoRespVO();
                BeanUtils.copyProperties(po, vo);

                String remark = vo.getRemark();

                if (remark.contains("原因")) {
                    vo.setChangeReason("官方");
                    vo .setIsDamageStr("不赔偿");
                }else{
                    vo.setChangeReason("待判定");
                    vo.setIsDamageStr("待处理");
                }
                // 分转元
                vo      .setUid(po.getUid().toString()+"\t")
                        .setAmount(BigDecimal.valueOf(po.getAmount().doubleValue()/100))
                        .setFirstChangeTime(DateUtils.transferLongToDateStrings(po.getFirstChangeTime()))
                        .setLastChangeTime(DateUtils.transferLongToDateStrings(po.getLastChangeTime()))
                        .setBeginTime(DateUtils.transferLongToDateStrings(po.getBeginTime()))
                        .setNegativeAmount( BigDecimal.valueOf(po.getNegativeAmount().doubleValue()/100))
                        .setFirstChangeAmount(BigDecimal.valueOf(po.getFirstChangeAmount().doubleValue()/100))
                        .setLastChangeAmount(BigDecimal.valueOf(po.getLastChangeAmount().doubleValue()/100))
                        .setLastAfterChangeAmount(BigDecimal.valueOf(po.getLastAfterChangeAmount().doubleValue()/100))
                        .setFirstChangeBeforeAmount(BigDecimal.valueOf(po.getFirstChangeBeforeAmount().doubleValue()/100))
                        // 防止导出后丢失精度
                        .setOrderNo(po.getOrderNo()+"\t");
                return vo;
            }).collect(Collectors.toList());

            log.info("组装 导出注单OrderTimesSettleInfoReqVO: {} , 总数 = {}", reqVO, page.getTotal());
            List<Map<String, String>> resultList;

            // 设置文件进度
            super.updateRate(merchantFile.getId(), 80L);
            log.info("查询结束,花费时间:" + (System.currentTimeMillis() - startTL));
            if (CollectionUtils.isEmpty(poList)) {
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(exportOrderTimesSettleCsv(voList));
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

    private byte[] exportOrderTimesSettleCsv(List<OrderTimesSettleInfoRespVO> records) {

        // header头数据
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "用户ID");
        header.put("2", "用户名");
        header.put("3", "商户编码");
        header.put("4", "商户名");
        header.put("5", "订单号");
        header.put("6", "多次账变金额差");
        header.put("7", "用户当前账户金额");
        header.put("8", "赛事ID");
        header.put("9", "对阵信息");
        header.put("10", " 联赛名称");
        header.put("11", " 玩法名称");
        header.put("12", " 变动原因");
        header.put("13", " 投注项名称");
        header.put("14", " 二次结算原因");
        header.put("15", " 比赛开始时间");
        header.put("16", " 最后一次账变金额");
        header.put("17", " 最后一次账变时间");
        header.put("18", " 最后一次账变后账户金额");
        header.put("19", " 第一次账变金额");
        header.put("20", " 第一次账变时间");
        header.put("21", " 第一次账变前账户金额");
        header.put("22", " 是否赔偿");


        // 填充数据
        List<LinkedHashMap<String, Object>> exportData = records.stream().map(po->{
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", po.getUid());
            rowData.put("2", po.getUsername());
            rowData.put("3", po.getMerchantCode());
            rowData.put("4", po.getMerchantName());
            rowData.put("5", po.getOrderNo());
            rowData.put("6", po.getNegativeAmount());
            rowData.put("7", po.getAmount());
            rowData.put("8", po.getMatchId());
            rowData.put("9", po.getMatchInfo());
            rowData.put("10", po.getMatchName());
            rowData.put("11", po.getPlayName());
            rowData.put("12", po.getRemark());
            rowData.put("13", po.getPlayOptionName());
            rowData.put("14", po.getChangeReason());
            rowData.put("15", po.getBeginTime());
            rowData.put("16", po.getLastChangeAmount());
            rowData.put("17", po.getLastChangeTime());
            rowData.put("18", po.getLastAfterChangeAmount());
            rowData.put("19", po.getFirstChangeAmount());
            rowData.put("20", po.getFirstChangeTime());
            rowData.put("21", po.getFirstChangeBeforeAmount());
            rowData.put("22",po.getIsDamageStr());
            return rowData;
        }).collect(Collectors.toList());

        //  csv文件转换字节数组
        return CsvUtil.exportCSV(header, exportData);
    }
}
