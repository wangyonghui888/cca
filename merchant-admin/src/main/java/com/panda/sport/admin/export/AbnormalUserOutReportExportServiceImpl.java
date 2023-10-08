package com.panda.sport.admin.export;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.admin.feign.PandaRcsTradeClient;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import com.panda.sport.order.service.expot.AbstractAbnormalFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  异常用户导出
 * @Date: 2022-06-13 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("abnormalUserOutReportExportServiceImpl")
@Slf4j
public class AbnormalUserOutReportExportServiceImpl  extends AbstractAbnormalFileExportService {
    @Autowired
    private PandaRcsTradeClient abnormalUserApiClient;

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

            log.info("开始异常用户导出 param = {}", merchantFile.getExportParam());
            JSONObject jsonObject = JSON.parseObject(merchantFile.getExportParam());
            String language = jsonObject.getString("language");
            String userId = jsonObject.getString("userId");
            String appId = jsonObject.getString("appId");
            if (StringUtils.isEmpty(language)) {
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }
            AbnormalUserVo sportVO = JSON.parseObject(merchantFile.getExportParam(), AbnormalUserVo.class);
            List<?> resultList = null;
            //feign调用风控后台上游接口
            Map<String, Object> result = abnormalUserApiClient.queryAbnormalUserList(sportVO);
            if (result != null) {
                resultList = (List<?>) JSON.parseObject(JSONObject.toJSONString(result.get("data"))).get("rcsUserExceptions");
            }
            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }

            inputStream = new ByteArrayInputStream(exportAbnormalUserMatchToCsv(resultList, language, 1));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("异常用户查询导出异常!", e);
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
}

