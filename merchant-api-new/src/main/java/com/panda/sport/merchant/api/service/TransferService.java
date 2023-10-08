package com.panda.sport.merchant.api.service;


import com.panda.sport.merchant.common.vo.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

public interface TransferService {


    APIResponse queryTransferList(HttpServletRequest request, String merchantCode, String user, String startTime, String end,
                                  Integer size, Integer page, Long timestamp, String signature);

    APIResponse getTransferRecord(HttpServletRequest request, String userName, String merchantCode, String transferId, Long timestamp, String signature);

    APIResponse checkBalance(HttpServletRequest request, String userName, String merchantCode, Long timestamp, String signature);

    APIResponse transfer(HttpServletRequest request, String merchantCode, String userName, String transferType, String amount, String transferId, Long timestamp, String signature, String currency) throws Exception;
    APIResponse transferV1(HttpServletRequest request, String merchantCode, String userName, String transferType, String amount, String transferId, Long timestamp, String amountType, String signature) throws Exception;
}
