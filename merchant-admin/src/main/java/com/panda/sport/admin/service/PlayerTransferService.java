package com.panda.sport.admin.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserTransferVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;

public interface PlayerTransferService {

    Response<Object> queryUserTransferList(UserTransferVO vo, String language);

    void retryTransfer(UserRetryTransferVO vo);
}
