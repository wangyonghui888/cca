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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
@Service("merchantInfoExportServiceImpl")
@Slf4j
public class MerchantInfoExportServiceImpl extends AbstractOrderFileExportService {

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

            log.info("开始商户信息导出 param = {}", merchantFile.getExportParam());

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
            log.error("商户信息导出异常!", e);
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

            if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
                rowData.put("4", merchantInfo.getTransferMode()==null ? " " : (merchantInfo.getTransferMode() == 1 ? "免转钱包" : "转账钱包"));
                if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_1.getCode()){
                    rowData.put("5", merchantInfo.getMerchantTag() == null ? " " : (merchantInfo.getMerchantTag() == 0 ? "现金网" : "信用网"));
                    rowData.put("6", merchantInfo.getIsApp() == null ? " " : (merchantInfo.getIsApp() == 0 ? "否" : "是"));
                    rowData.put("7", merchantInfo.getSettleSwitchAdvance() == 0 ? "关" : "开");
                    rowData.put("8", merchantInfo.getOpenVrSport() == 0 ? "关" : "开");
                    rowData.put("9", merchantInfo.getOpenEsport() == 0 ? "关" : "开");
                    rowData.put("10", merchantInfo.getOpenVideo() == 0 ? "关" : "开");
                    rowData.put("11", merchantInfo.getVideoSwitch() == 0 ? "关" : "开");
                    rowData.put("12", merchantInfo.getChatRoomSwitch() == 0 ? "关" : "开");
                    rowData.put("13", merchantInfo.getEventSwitch() == 0 ? "关" : "开");
                }
            } else {
                rowData.put("4", merchantInfo.getTransferMode() == 1 ? "No Transfer Wallet" : "Transfer Wallet");
                if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_1.getCode()){
                    rowData.put("5", merchantInfo.getMerchantTag() == null ? " " : (merchantInfo.getMerchantTag() == 0 ? "Cash" : "Credit"));
                    rowData.put("6", merchantInfo.getIsApp() == null ? " " : (merchantInfo.getIsApp() == 0 ? "is" : "no"));
                    rowData.put("7", merchantInfo.getSettleSwitchAdvance() == 0 ? "off" : "on");
                    rowData.put("8", merchantInfo.getOpenVrSport() == 0 ? "off" : "on");
                    rowData.put("9", merchantInfo.getOpenEsport() == 0 ? "off" : "on");
                    rowData.put("10", merchantInfo.getOpenVideo() == 0 ? "off" : "on");
                    rowData.put("11", merchantInfo.getVideoSwitch() == 0 ? "off" : "on");
                    rowData.put("12", merchantInfo.getChatRoomSwitch() == 0 ? "off" : "on");
                    rowData.put("13", merchantInfo.getEventSwitch() == 0 ? "off" : "on");
                }
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
            header.put("4", "钱包类型");
            if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_1.getCode()){
                header.put("5", "商户标签");
                header.put("6", "APP商户");
                header.put("7", "提前结算");
                header.put("8", "VR体育");
                header.put("9", "电子竞技");
                header.put("10", "视频开关");
                header.put("11", "视频流量管控");
                header.put("12", "聊天室");
                header.put("13", "精彩回放");
            }
        } else {
            header.put("1", "NO");
            header.put("2", "Merchant ID");
            header.put("3", "Merchant Name");
            header.put("4", "Wallet Type");
            if(agentLevel!= AgentLevelEnum.AGENT_LEVEL_1.getCode()){
                header.put("5", "Merchant Tag");
                header.put("6", "APP Merchant");
                header.put("7", "CashOut");
                header.put("8", "Virtual sports");
                header.put("9", "e-sports");
                header.put("10", "video sports");
                header.put("11", "video traffic control");
                header.put("12", "chat room");
                header.put("13", "wonderful playback");
            }
        }
        return header;
    }


}
