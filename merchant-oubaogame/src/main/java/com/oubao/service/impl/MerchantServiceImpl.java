package com.oubao.service.impl;

import com.oubao.mapper.TMerchantMapper;
import com.oubao.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@RefreshScope
@Service("merchantService")
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private TMerchantMapper merchantMapper;


    @Override
    public void createMerchant(String merchantCode, String merchantKey, int transferMode) {
        try {
            merchantMapper.insertMerchant(merchantCode, merchantKey, transferMode);
        } catch (Exception e) {
            log.error("新增商户异常!", e);
        }
    }

    @Override
    public void updateMerchant(String merchantCode, String merchantKey, int transferMode) {
        try {
            merchantMapper.updateMerchant(merchantCode, merchantKey, transferMode);
        } catch (Exception e) {
            log.error("修改商户异常!", e);
        }
    }
}
