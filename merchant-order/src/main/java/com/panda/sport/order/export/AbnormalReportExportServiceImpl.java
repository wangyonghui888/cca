package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import com.panda.sport.order.feign.AbnormalApiClient;
import com.panda.sport.order.service.expot.AbstractAbnormalFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  异常会员名单导出
 * @Date: 2022-06-13 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("abnormalReportExportServiceImpl")
@Slf4j
public class AbnormalReportExportServiceImpl extends AbstractAbnormalFileExportService {

    @Value("${merchant.abnormal_url}")
    String URL;

    @Autowired
    private AbnormalApiClient abnormalApiClient;

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

            log.info("开始异常会员名单报表导出 param = {}", merchantFile.getExportParam());
            JSONObject jsonObject = JSON.parseObject(merchantFile.getExportParam());
            String language = jsonObject.getString("language");
            if (StringUtils.isEmpty(language)){
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }
            AbnormalVo sportVO = JSON.parseObject(merchantFile.getExportParam(), AbnormalVo.class);
            List<?> resultList = null;
                Map<String, Object> result = findAdnormal(sportVO);
                if (result != null) {
                     if(null != result.get("data")){
                         resultList = (List<?>)JSON.parseObject(JSONObject.toJSONString(result.get("data"))).get("userExceptionResVoList");
                     }
                }
            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }

            inputStream = new ByteArrayInputStream(exportMatchToCsv(resultList, language,0));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("异常会员名单导出异常!", e);
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

    private Map<String, Object> findAdnormal(AbnormalVo abnormalVo) throws IOException {
        Map<String, Object> map = new HashMap<>();
        log.info("queryAbnormalList: 参数:{}:" + abnormalVo);
        map= abnormalApiClient.queryAbnormalList(abnormalVo);
        return map;
    }
}
