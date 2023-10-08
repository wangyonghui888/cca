package com.panda.sport.order.schedule;

import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.order.feign.MerchantApiClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@JobHandler(value = "MerchantKeyAesTask")
public class MerchantKeyAesTask extends IJobHandler {


    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantApiClient merchantApiClient;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("MerchantCodeConfigTask商户等级设置开始!" + s);
        try {
            MerchantPO merchantPO = new MerchantPO();
            List<MerchantPO> list = merchantMapper.selectList(merchantPO);
            if (list != null && list.size() > 0) {
                for (MerchantPO tMerchantKey : list) {
                    if(!StringUtil.isBlankOrNull(tMerchantKey.getMerchantKey())){
                        merchantMapper.updateHistoryKey( tMerchantKey.getMerchantCode(), AESUtils.aesEncode(tMerchantKey.getMerchantKey()), tMerchantKey.getMerchantKey());
                        if(!StringUtil.isBlankOrNull(tMerchantKey.getAdminPassword())) {
                            merchantMapper.updateMerchantAdminPassword(tMerchantKey.getMerchantCode(), AESUtils.aesEncode(tMerchantKey.getAdminPassword()));
                        }
                        merchantApiClient.kickoutMerchant(merchantPO.getMerchantCode());
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