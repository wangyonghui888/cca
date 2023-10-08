package com.oubao.service;

import com.oubao.vo.APIResponse;


public interface SDKService {


    APIResponse sdkLogin(String userName, String merchantCode, String terminal, Double balance, String callbackUrl, String merchantKey);

    APIResponse sdkGetBetDetail(String merchantCode, String orderNo, String merchantKey);

    APIResponse sdkGetTransferRecord(String merchantCode, String userName, String transferId, String merchantKey);

    APIResponse sdkQueryTransferList(String merchantCode, String userName, String startTime, String endTime, Integer pageNum, Integer pageSize, String merchantKey);
}
