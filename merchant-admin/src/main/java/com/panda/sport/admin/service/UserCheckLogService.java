package com.panda.sport.admin.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.user.UserCheckLogVO;

import java.util.List;

public interface UserCheckLogService {

    Response checkUserList(List<UserCheckLogVO> checkLogVOS);

    Response getCheckLogList(UserCheckLogVO userCheckLogVO);

    Response<Object> getUserByUserName(String userName, String language);
}
