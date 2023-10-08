package com.panda.sport.merchant.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RemoteHystrix implements MerchantReportClient {


    @Override
    public List<String> queryUserSecondLevelList() {
        return null;
    }
}

