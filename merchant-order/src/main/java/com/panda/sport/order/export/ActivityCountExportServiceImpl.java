package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  活动导统计出
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("activityCountExportServiceImpl")
@Slf4j
public class ActivityCountExportServiceImpl extends AbstractOrderFileExportService {
    @Autowired
    private MerchantReportClient reportClient;

    @Autowired
    protected MerchantMapper merchantMapper;

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
            log.info("开始活动统计报表导出 param = {}", merchantFile.getExportParam());
            ActivityBetStatDTO req = JSON.parseObject(merchantFile.getExportParam(), ActivityBetStatDTO.class);
            List<?> resultList = reportClient.exportExcel(req);
            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(exportActivtyDayCsv(resultList, req.getLanguage()));
            super.uploadFile(merchantFile, inputStream);
            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("活动统计报表异常!", e);
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

    private byte[] exportActivtyDayCsv(List<?> mapList,String language) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> filterList = mapper.convertValue(mapList, new TypeReference<List<Map<String, String>>>() {
        });
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        for (Map<String, String> map : filterList) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", map.get("uid").concat("\t"));
            rowData.put("2", map.get("username"));
            rowData.put("3", map.get("merchantCode").concat("\t"));
            rowData.put("4", map.get("validBetNums"));
            rowData.put("5", map.get("validBetAmount"));
            rowData.put("6", map.get("validBetDays"));
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getHeader(), exportData);
    }

    private LinkedHashMap<String, String> getHeader() {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "用户ID");
        header.put("2", "用户名");
        header.put("3", "所属商户编码");
        header.put("4", "投注笔数");
        header.put("5", "累计投注额度");
        header.put("6", "累计投注有效天数");
        return header;
    }

}
