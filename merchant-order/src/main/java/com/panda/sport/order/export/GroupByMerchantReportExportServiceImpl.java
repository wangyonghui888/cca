package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            log.info("开始商户报表导出 param = {}",merchantFile.getExportParam());
//            MerchantOrderVO requestVO = JSON.parseObject(merchantFile.getExportParam(), MerchantOrderVO.class);
            MerchantFinanceDayVo req = JSON.parseObject(merchantFile.getExportParam(), MerchantFinanceDayVo.class);
            List<Map<String, Object>> resultList = new ArrayList<>();
            Integer pageSize = 200;
            ObjectMapper mapper = new ObjectMapper();
            int i = 1;
            while (true) {
                /*requestVO.setPageNum(i);
                requestVO.setPageSize(pageSize);*/
                req.setPageNum(i);
                req.setPageSize(pageSize);
                Response<?> res = rpcClient.queryFinanceayTotalList(req);
                Map<String, Object> resMap = (Map<String, Object>)res.getData();
                /*Response<Map<String, Object>> response = rpcClient.listGroupByMerchant(requestVO);
                Map<String, Object> resultMap = response.getData();*/
                if (resMap == null){
                    log.info("exportMerchantReport resultMap:is null id={}", merchantFile.getId());
                    break;
                }
                List<Map<String, Object>> list = (List<Map<String, Object>>) resMap.get("list");
                if (CollectionUtils.isEmpty(list)) {
                    log.info("exportMerchantReport returnDate:is null id={}", merchantFile.getId());
                    break;
                }
                /*List<MerchantFinanceDayVo> filterList = mapper.convertValue(result, new TypeReference<List<MerchantFinanceDayVo>>() {
                });
                resultList.addAll(filterList);*/
                resultList.addAll(list);
                i++;
            }
            resultList = this.resetResult(resultList);
            inputStream =  new ByteArrayInputStream(groupByExportMerchantReportToCsv(resultList, req));
            super.uploadFile(merchantFile, inputStream);
            log.info("导出结束,花费时间: {}" ,(System.currentTimeMillis() - startTime));
        }catch (Exception e){
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("exportMerchantReport导出商户报表异常! fileId ={}", merchantFile.getId(), e);
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

    /**
     * 组装二级商户数据
     * @param resultList
     * @return
     */
    private List<Map<String, Object>> resetResult(List<Map<String, Object>> resultList){
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map item : resultList){
            Integer agentLevel = (Integer)item.get("agentLevel");
            List<Object> subNodeList = (List<Object>)item.get("subNodeList");
            if(agentLevel.equals(1) && CollectionUtils.isNotEmpty(subNodeList)){
                for (Object list : subNodeList){
                    Map<String, Object> map = (Map<String, Object>)list;
                    if(map.get("agentLevel").equals(2)){
                        result.add(map);
                    }
                }
            }
            result.add(item);
        }
        return result;
    }
}
