package com.panda.sport.admin.export;

import com.alibaba.fastjson.JSON;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author :  javier
 *  分组商户报表导出实现类
 * --------  ---------  --------------------------
 */
@Service("groupByMerchantReportExportServiceImpl")
@Slf4j
public class GroupByMerchantReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient rpcClient;

    @Override
    @Async()
    public void export(MerchantFile merchantFile){

        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())){
                log.info("当前任务被删除！");
                return;
            }
            super.updateFileStart(merchantFile.getId());
            int size =200;
            log.info("开始商户报表导出 param = {}",merchantFile.getExportParam());
            MerchantOrderVO requestVO = JSON.parseObject(merchantFile.getExportParam(), MerchantOrderVO.class);
            List<MerchantOrderDayPO> resultList = new ArrayList<>();
                MerchantFinanceDayVo newVo = new MerchantFinanceDayVo();
                newVo.setMerchantCode(requestVO.getMerchantCode());
                newVo.setMerchantCodeList(requestVO.getMerchantCodeList());
                newVo.setAgentLevel(requestVO.getAgentLevel());
                newVo.setPageNum(requestVO.getPageNum());
                newVo.setPageSize(200);
                newVo.setStartTime(requestVO.getStartTime());
                newVo.setEndTime(requestVO.getEndTime());
                newVo.setFilter(String.valueOf(requestVO.getFilter()));
                newVo.setTimeZone(requestVO.getTimeZone());
                Map<String, Object> resultMap = new HashMap<>();
                Response<Map<String,List<Map<String,List<Map<String,Object>>>>>> response1 = rpcClient.queryFinanceayTotalList(newVo);
                if (resultMap == null || response1 == null){
                    log.info("exportMerchantReport resultMap:is null id={}", merchantFile.getId());
                }
               List<Map<String,List<Map<String,Object>>>> listResult = response1.getData().get("list");
                if (listResult == null) {
                   log.info("exportMerchantReport listResult:is null list={}", merchantFile.getId());
               }
                List<Map<String,Object>> result = new ArrayList<>();
                for (Map<String,List<Map<String,Object>>> objList: listResult){
                    result.addAll(objList.get("dayVoList"));
                    if(1==requestVO.getAgentLevel()){//如果是渠道用户
                        List<Map<String,Object>> objSubList = objList.get("subNodeList");
                        log.info("exportMerchantReport==subNodeList==objSubList={}", objSubList);
                        for(Map<String,Object> map : objSubList){
                            result.addAll((List<Map<String,Object>>)map.get("dayVoList"));
                        }
                    }
               }
               for(Object  obj: result){
                   MerchantOrderDayPO po = new MerchantOrderDayPO();
                   Map<String, Object> map =(Map<String, Object>) obj;
                       if (ObjectUtils.isNotEmpty(map.get("merchantLevel"))) {
                           po.setMerchantLevel(map.get("merchantLevel").toString());
                       }
                       if (ObjectUtils.isNotEmpty(map.get("merchantCode"))) {
                           po.setMerchantCode(map.get("merchantCode").toString());
                       }
                   if (ObjectUtils.isNotEmpty(map.get("orderAmountTotal"))) {
                           po.setBetAmount(new BigDecimal(map.get("orderAmountTotal").toString()));
                       }
                       if (ObjectUtils.isNotEmpty(map.get("platformProfit"))) {
                           po.setProfit(new BigDecimal(map.get("platformProfit").toString()));
                       }
                       if (ObjectUtils.isNotEmpty(map.get("platformProfitRate"))) {
                           po.setProfitRate(new BigDecimal(map.get("platformProfitRate").toString()).divide(new BigDecimal(100)));
                       }

                       if (ObjectUtils.isNotEmpty(map.get("orderValidNum"))) {
                           po.setOrderSum(Integer.valueOf(map.get("orderValidNum").toString()));
                       }

                       if (ObjectUtils.isNotEmpty(map.get("orderUserNum"))) {
                           po.setBetUserSum(Integer.valueOf(map.get("orderUserNum").toString()));
                       }
                       if (ObjectUtils.isNotEmpty(map.get("newUserSum"))) {
                           po.setAddUser(Integer.valueOf(map.get("newUserSum").toString()));
                       }
                       if (ObjectUtils.isNotEmpty(map.get("registerUserSum"))) {
                           po.setRegisterTotalUserSum(Integer.valueOf(map.get("registerUserSum").toString()));
                       }
                       if (ObjectUtils.isNotEmpty(map.get("betsOnRate"))) {
                           po.setBetUserRate(new BigDecimal(map.get("betsOnRate").toString()));
                       }
                       if (ObjectUtils.isNotEmpty(map.get("orderValidBetMoney"))) {
                           po.setOrderValidBetMoney(new BigDecimal(map.get("orderValidBetMoney").toString()));
                       }
                    resultList.add(po);
                   }
            inputStream =  new ByteArrayInputStream(groupByExportMerchantReportToCsv(resultList, requestVO));
            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}" ,(System.currentTimeMillis() - startTime));
        }catch (Exception e){
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("exportMerchantReport导出商户报表异常!", e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    log.error("流关闭异常！");
                }
            }
        }
    }

}
