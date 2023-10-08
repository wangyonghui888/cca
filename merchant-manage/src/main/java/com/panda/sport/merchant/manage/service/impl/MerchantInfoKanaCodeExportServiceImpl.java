package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.merchant.MerchantVO;
import com.panda.sport.merchant.manage.service.MerchantService;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author :  istio
 * @Project Name :  panda-merchant
 * @Description :  商户信息导出实现类
 * @Date: 2022-05-20 12:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("merchantInfoKanaCodeExportServiceImpl")
@Slf4j
public class MerchantInfoKanaCodeExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantService merchantService;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {

        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            super.updateFileStart(merchantFile.getId());

            log.info("开始公司代码信息导出 param = {}", merchantFile.getExportParam());

            MerchantVO merchantVO = JSON.parseObject(merchantFile.getExportParam(), MerchantVO.class);
            List<MerchantVO> resultList = merchantService.queryListByParam(merchantVO);
            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }

            inputStream = new ByteArrayInputStream(exportMerchantInfoToCsv(resultList, merchantVO));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("公司代码信息导出异常!", e);
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

    private byte[] exportMerchantInfoToCsv(List<?> resultList, MerchantVO merchantVO) {
        String language = merchantVO.getLanguage();
        int agentLevel = merchantVO.getAgentLevel();
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        int i = 0;
        ObjectMapper mapper = new ObjectMapper();
        List<MerchantVO> filterList = mapper.convertValue(resultList, new TypeReference<List<MerchantVO>>() {
        });
        for (MerchantVO merchantInfo : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", "\t"+merchantInfo.getMerchantCode() + " ");
            rowData.put("3", merchantInfo.getMerchantName() + " ");

            String kanaCode = merchantInfo.getKanaCode();
            if(merchantInfo.getSerialNumber()!=null){
                kanaCode=merchantInfo.getSerialNumber()+merchantInfo.getKanaCode();
            }
            String date ="";
            if(merchantInfo.getKanaCodeTime()!=null) {
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    date =sdf.format(merchantInfo.getKanaCodeTime());
                }catch (Exception e){
                    log.error("exportMerchantInfoToCsv  format",e);
                }

            }
           if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_2.getCode()){
                    rowData.put("4", merchantInfo.getSerialNumber());
                    rowData.put("5", merchantInfo.getKanaCode());
                    rowData.put("6", kanaCode);
               if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
                   rowData.put("7", merchantInfo.getMerchantTag() == null ? " " : (merchantInfo.getMerchantTag() == 0 ? "现金网" : "信用网"));
               }else {
                   rowData.put("7", merchantInfo.getMerchantTag() == null ? " " : (merchantInfo.getMerchantTag() == 0 ? "Cash" : "Credit"));
               }
               rowData.put("8", date);
            } else {
               rowData.put("4", merchantInfo.getParentName());
               rowData.put("5", merchantInfo.getParentKanaCode());
               rowData.put("6", merchantInfo.getSerialNumber());
               rowData.put("7", merchantInfo.getKanaCode());
               rowData.put("8",   merchantInfo.getParentKanaCode()+merchantInfo.getSerialNumber());
               if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
                   rowData.put("9", merchantInfo.getMerchantTag() == null ? " " : (merchantInfo.getMerchantTag() == 0 ? "现金网" : "信用网"));
               }else {
                   rowData.put("9", merchantInfo.getMerchantTag() == null ? " " : (merchantInfo.getMerchantTag() == 0 ? "Cash" : "Credit"));
               }
               rowData.put("10", date);
            }
            exportData.add(rowData);
        }

        return CsvUtil.exportCSV(getMerchantHeader(merchantVO), exportData);
    }

    private LinkedHashMap<String, String> getMerchantHeader(MerchantVO merchantVO) {
        String language = merchantVO.getLanguage();
        int agentLevel = merchantVO.getAgentLevel();
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "商户编号");
            header.put("3", "商户名称");
            if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_2.getCode()){
                header.put("4", "商户流水号");
                header.put("5", "商户等级");
                header.put("6", "公司代码");
                header.put("7", "商户标签");
                header.put("8", "最后更新时间");
            }else{
                header.put("4", "渠道商户");
                header.put("5", "渠道公司代码");
                header.put("6", "商户流水号");
                header.put("7", "商户等级");
                header.put("8", "公司代码");
                header.put("9", "商户标签");
                header.put("10", "最后更新时间");
            }
        } else {
            header.put("1", "NO");
            header.put("2", "Merchant ID");
            header.put("3", "Merchant Name");
            if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_2.getCode()){
                header.put("4", "Merchant serial number");
                header.put("5", "Merchant class");
                header.put("6", "Company code");
                header.put("7", "Merchants label");
                header.put("8", "Last updated");
            }else{
                header.put("4", "Channel merchant");
                header.put("5", "Channel Company code");
                header.put("6", "Merchant serial number");
                header.put("7", "Merchant class");
                header.put("8", "Company code");
                header.put("9", "Merchants label");
                header.put("10", "Last updated");
            }
        }
        return header;
    }


}
