package com.panda.sport.merchant.api.service;


import com.panda.sport.merchant.common.vo.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    APIResponse create(HttpServletRequest request, String username, String nickname, String merchantCode, Long timestamp, String currency, String agentId, String signature) throws Exception;

    APIResponse<Object> login(HttpServletRequest request, String username, String terminal, String merchantCode, String currency, String callbackUrl,
                              Long timestamp, String signature, String jumpsupport, String jumpfrom, String ip, String stoken, String agentId, Boolean imitate, String language) throws Exception;


    APIResponse<Object> token(HttpServletRequest request, String username, String merchantCode,
                              Long timestamp, String signature, String token) throws Exception;

    void refreshFrontDomain();

}
