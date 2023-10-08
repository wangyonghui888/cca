package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantServiceImpl {

    @Autowired
    private MerchantFileService merchantFileService;
    
}
