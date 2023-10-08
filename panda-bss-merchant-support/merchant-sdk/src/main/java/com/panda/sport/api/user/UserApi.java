package com.panda.sport.api.user;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.api.conf.PandaConstant;
import com.panda.sport.api.entity.APIResponse;
import com.panda.sport.api.entity.ApiResponseEnum;
import com.panda.sport.api.util.HttpConnectionPool;
import com.panda.sport.api.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;

public class UserApi {

    /**
     * 登录
     * @param merchantCode
     * @param terminal
     * @param userName
     * @param balance
     * @param callbackUrl
     * @param merchantKey
     * @param url
     * @return
     */
    public static APIResponse login(String merchantCode, String terminal, String userName,
                                    Double balance, String callbackUrl, String merchantKey, String url) {
        return StringUtils.isAnyEmpty(userName, terminal, merchantCode, merchantKey) || balance == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                loginPanda(getParams(userName, terminal, merchantCode, merchantKey, balance, callbackUrl), url);
    }


    /**
     * 检查用户是否上线
     * @param merchantCode
     * @param userName
     * @param merchantKey
     * @param url
     * @return
     */
    public static APIResponse checkUserOnline(String merchantCode, String userName, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(userName, merchantCode, merchantKey)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", userName);
        params.add("merchantCode", merchantCode);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        params.add("signature", Md5Util.getMD5(merchantCode + PandaConstant.and + userName + PandaConstant.and + timestamp, merchantKey));
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }


    /**
     * 用户登出
     * @param merchantCode
     * @param userName
     * @param merchantKey
     * @param url
     * @return
     */
    public static APIResponse kickOutUser(String merchantCode, String userName, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(userName, merchantCode, merchantKey)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", userName);
        params.add("merchantCode", merchantCode);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        params.add("signature", Md5Util.getMD5(merchantCode + PandaConstant.and + userName + PandaConstant.and + timestamp, merchantKey));
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }



    private static APIResponse loginPanda(MultiValueMap<String, Object> params, String url) {
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }

    /**
     * 登录接口组装参数
     * 加密
     *
     * @param userName
     * @param terminal
     * @param merchantCode
     * @param merchantKey
     * @param balance
     * @param callbackUrl
     * @return
     */
    private static MultiValueMap<String, Object> getParams(String userName, String terminal, String merchantCode,
                                                           String merchantKey, Double balance, String callbackUrl) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("merchantCode", merchantCode);
        params.add("userName", userName);
        params.add("terminal", terminal);
        params.add("balance", balance);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        if (StringUtils.isNotEmpty(callbackUrl)) {
            params.add("callbackUrl", callbackUrl);
        }
        params.add("signature", Md5Util.getMD5(merchantCode + PandaConstant.and + userName + PandaConstant.and +
                terminal + PandaConstant.and + timestamp, merchantKey));
        return params;
    }

    private static HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

}
