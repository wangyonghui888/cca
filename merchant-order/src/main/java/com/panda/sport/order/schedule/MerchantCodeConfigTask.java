package com.panda.sport.order.schedule;

import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.po.bss.MerchantCodeConfig;
import com.panda.sport.merchant.common.po.bss.MerchantCodeConfigLog;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.manage.service.impl.MerchantConfigServiceImpl;
import com.panda.sport.order.feign.MerchantReportClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@JobHandler(value = "MerchantCodeConfigTask")
public class MerchantCodeConfigTask extends IJobHandler {

    @Qualifier("com.panda.sport.order.feign.MerchantReportClient")
    @Autowired
    private MerchantReportClient merchantReportClient;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantConfigServiceImpl merchantConfigService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("MerchantCodeConfigTask商户等级设置开始!" + s);
        try {
            List<Map<String, Object>> list = merchantReportClient.getMerchantAvgBetAmount();
            List<MerchantCodeConfig> configList = merchantConfigService.queryCodeConfigList();
            if (list != null && list.size() > 0) {
                log.info("MerchantCodeConfigTask!list" + list);
                for (Map map : list) {
                    String merchantCode = (String) map.get("merchantCode");
                    MerchantPO po = merchantMapper.getMerchant(merchantCode);
                    log.info("MerchantCodeConfigTask!po" + po);
                    if (po==null || po.getAgentLevel() == 2 || po.getAgentLevel() == 10) {
                        continue;
                    }
                    Integer betAmount = (Integer) map.get("betAmount");
                    String code = "";
                    for (MerchantCodeConfig config : configList) {
                        if (betAmount > config.getStartValue()) {
                            code = config.getCode();
                            break;
                        }
                    }
                    log.info("MerchantCodeConfigTask!code" + code);
                    if (!code.equals("")) {
                        int num = merchantMapper.updateKanaCode(code, new Date(), merchantCode);
                        MerchantCodeConfigLog configLog = new MerchantCodeConfigLog();
                        configLog.setMerchantCode(merchantCode);
                        configLog.setCreateTime(System.currentTimeMillis());
                        String month = DateUtils.formatDate(new Date(), "yyyy-MM");
                        configLog.setMonth(month);
                        configLog.setCode(code);
                        merchantConfigService.insertMerchantCodeConfigLog(configLog);
                        if (po.getAgentLevel() == 1) {
                            String serNum = "";
                            if (po.getSerialNumber() != null) {
                                serNum = po.getSerialNumber().toString();
                            }
                            merchantMapper.updateKanaCodeByParentId(serNum + code, new Date(), po.getId());
                            List<String> codelist = merchantMapper.getMerhcantCodeList(po.getId());
                            for (String codeStr : codelist) {
                                configLog.setMerchantCode(codeStr);
                                merchantConfigService.insertMerchantCodeConfigLog(configLog);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("UserLoginPreLoadTask异常！", e);
        }
        log.info("MerchantCodeConfigTask结束执行!");
        return SUCCESS;
    }
}