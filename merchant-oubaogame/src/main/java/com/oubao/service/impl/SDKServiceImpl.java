package com.oubao.service.impl;


import com.oubao.service.SDKService;
import com.oubao.vo.*;
import com.panda.sport.api.fund.TransferApi;
import com.panda.sport.api.order.BetApi;
import com.panda.sport.api.user.UserApi;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Slf4j
@Service("sdkService")
public class SDKServiceImpl implements SDKService {

    @Value("${sdk_panda_login_url}")
    String loginUrl;


    @Value("${sdk_panda_getBetDetail_url}")
    String betDetailUrl;

    @Value("${sdk_panda_getTransferRecord_url}")
    String transferRecordUrl;


    @Value("${sdk_panda_queryTransferList_url}")
    String queryTransferListUrl;


    @Override
    public APIResponse sdkLogin(String userName, String merchantCode, String terminal, Double balance, String callbackUrl, String merchantKey) {
        try {
            return APIResponse.returnSuccess(UserApi.login(userName, terminal, merchantCode, balance, callbackUrl, merchantKey, loginUrl));
        } catch (Exception e) {
            log.error("sdkLogin:", e);
            return APIResponse.returnFail("sdkLogin");
        }
    }

    @Override
    public APIResponse sdkGetBetDetail(String merchantCode, String orderNo, String merchantKey) {
        try {
            return APIResponse.returnSuccess(BetApi.getBetDetail(merchantCode, orderNo, merchantKey, betDetailUrl));
        } catch (Exception e) {
            log.error("sdkGetBetDetail:", e);
            return APIResponse.returnFail("sdkGetBetDetail");
        }
    }

    @Override
    public APIResponse sdkGetTransferRecord(String merchantCode, String userName, String transferId, String merchantKey) {
        try {

            return APIResponse.returnSuccess(TransferApi.getTransferRecord(merchantCode, userName, Long.valueOf(transferId), merchantKey, transferRecordUrl));

        } catch (Exception e) {
            log.error("sdkGetTransferRecord:", e);
            return APIResponse.returnFail("sdkGetTransferRecord");
        }
    }

    @Override
    public APIResponse sdkQueryTransferList(String merchantCode, String userName, String startTime, String endTime, Integer pageNum, Integer pageSize, String merchantKey) {

        try {
            return APIResponse.returnSuccess(TransferApi.queryTransferList(merchantCode, userName, startTime, endTime, pageNum, pageSize, merchantKey, queryTransferListUrl));
        } catch (Exception e) {
            log.error("sdkQueryTransferList:", e);
            return APIResponse.returnFail("sdkQueryTransferList");
        }

    }
}
