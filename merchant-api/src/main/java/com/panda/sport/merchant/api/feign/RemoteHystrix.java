package com.panda.sport.merchant.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RemoteHystrix implements MerchantReportClient, MerchantManageClient, MerchantOrderClient {


    @Override
    public List<?> getMerchantReportApi(Integer startTime, Integer endTime, String dateType) {
        log.error("RPC调用失败!");
        return null;
    }

    @Override
    public Map<?, ?> getBetReport(Integer reportDate, String merchantCode) {
        log.error("getBetReport RPC调用失败!");
        return null;
    }

    @Override
    public Object checkSingleDomain(String domain) {
        log.error("checkSingleDomain RPC调用失败!" + domain);
        return null;
    }

    @Override
    public List<Map<String, Object>> queryOlympicBetEvery(int days, Double bet, Long startDateL, Long endDateL) {
        log.error("异常!queryOlympicBetEvery:" + bet + ",startDateL=" + startDateL + ",endDateL=" + endDateL);
        return null;
    }

    @Override
    public List<Map<String, Object>> queryOlympicMardiGras(Double betAmount, Long startDateL, Long endDateL) {
        log.error("异常!queryOlympicPlayTimesUser:" + betAmount + ",startDateL=" + startDateL + ",endDateL=" + endDateL + ",registerTimeL=");
        return null;
    }

    @Override
    public List<Map<String, Object>> queryUserIdList(String merchantCode) {
        log.error(merchantCode + "queryUserIdList RPC调用失败!");
        return null;
    }
}

