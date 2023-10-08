package com.panda.sport.api.fund;

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

public class TransferApi {

    /**
     * 查询用户转帐记录列表
     * @param username
     * @param merchantCode
     * @param pageNum
     * @return result
     * @throws Exception
     */
    public static APIResponse queryTransferList(String merchantCode, String username, String startTime, String endTime,
                                                Integer pageNum, Integer pageSize, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(username, merchantCode, startTime, endTime)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", username);
        params.add("merchantCode", merchantCode);
        params.add("startTime ", startTime);
        params.add("endTime ", endTime);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        if (pageSize == null) {
            pageSize = 1;
        }
        params.add("pageNum", pageNum);
        params.add("pageSize", pageSize);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + username + PandaConstant.and
                + startTime + PandaConstant.and + endTime + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }

    /**
     * 查询用户转帐记录详情
     * @param username
     * @param merchantCode
     * @param transferId
     * @return
     */
    public static APIResponse getTransferRecord(String merchantCode, String username, Long transferId, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(username, merchantCode) || transferId == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        String transferIdstr = transferId + "";
        params.add("userName", username);
        params.add("merchantCode", merchantCode);
        params.add("transferId", transferIdstr);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + transferIdstr + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }

    /**
     * 获取用户余额
     * @param username
     * @param merchantCode
     * @return
     */
    public static APIResponse checkBalance(String merchantCode, String username, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(username, merchantCode)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", username);
        params.add("merchantCode", merchantCode);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + username + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }


    /**
     * 上分，下分
     * @param username
     * @param merchantCode
     * @param transferId
     * @return
     */
    public static APIResponse transfer(String merchantCode, String username, String transferType, String amount,
                                       String transferId, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(username, merchantCode, transferType, amount, transferId)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", username);
        params.add("merchantCode", merchantCode);
        params.add("transferType", transferType);
        params.add("amount", amount);
        params.add("transferId", transferId);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + username + PandaConstant.and + transferType + PandaConstant.and + amount +
                PandaConstant.and + transferId + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, getHeader()), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);
    }

    private static HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }
}
