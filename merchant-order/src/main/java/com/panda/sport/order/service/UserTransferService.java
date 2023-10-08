package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserTransferVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;

import javax.servlet.http.HttpServletRequest;

public interface UserTransferService {

    Response<Object> queryUserTransferList(UserTransferVO vo, String language);

    Response retryTransfer(UserRetryTransferVO vo);

    void executeRetry();

    void executeRetry2();

    Response addChangeRecordHistory(AccountChangeHistoryFindVO vo, HttpServletRequest request);
}
