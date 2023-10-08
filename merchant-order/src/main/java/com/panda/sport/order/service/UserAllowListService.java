package com.panda.sport.order.service;

import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.dto.UserAllowListReq;

import javax.servlet.http.HttpServletRequest;

public interface UserAllowListService {
    PageInfo<?> listAll(String merchantName, UserAllowListReq req, String language);

    Integer del(UserAllowListReq req);

    Integer delAll();

    Integer importUser(UserAllowListReq req);

    void exportAllowListUserList(String merchantName, HttpServletRequest request, UserAllowListReq req);
}
