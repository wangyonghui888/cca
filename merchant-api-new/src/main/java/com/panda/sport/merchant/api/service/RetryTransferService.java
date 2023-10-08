package com.panda.sport.merchant.api.service;


import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

public interface RetryTransferService {

    APIResponse retryTransferRecord(String transferId, Long retryCount, String userName) throws Exception;

    APIResponse addChangeRecordHistory(AccountChangeHistoryFindVO accountChangeHistoryFindVO, HttpServletRequest request);

}
