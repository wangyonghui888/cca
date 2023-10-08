package com.panda.sport.api.order;

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

public class BetApi {

    /**
     * 查询单条投注记录
     *
     * @param merchantCode
     * @param orderNo
     * @param merchantKey
     * @return APIResponse
     */
    public static APIResponse getBetDetail(String merchantCode, String orderNo, String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(orderNo, merchantCode, merchantKey)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("orderNo", orderNo);
        params.add("merchantCode", merchantCode);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + orderNo + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, headers), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);

    }

    /**
     * 查询投注记录列表
     *
     * @param merchantCode
     * @param userName
     * @param merchantKey
     * @return APIResponse
     */
    public static APIResponse queryBetList(String merchantCode, String userName, String startTime, String endTime, Integer sportId, Long tournamentId,
                                           Integer settleStatus, Integer pageNum, Integer pageSize,
                                           String merchantKey, String url) {
        if (StringUtils.isAnyEmpty(userName, merchantCode, merchantKey, startTime, endTime)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", userName);
        params.add("merchantCode", merchantCode);
        params.add("startTime", startTime);
        params.add("endTime", endTime);
        params.add("sportId", sportId);
        params.add("tournamentId", tournamentId);
        params.add("settleStatus", settleStatus);
        params.add("pageNum", pageNum);
        params.add("pageSize", pageSize);
        String timestamp = new Date().getTime() + "";
        params.add("timestamp", timestamp);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + startTime + PandaConstant.and
                + endTime + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, headers), String.class);
        return JSONObject.parseObject(response.getBody(), APIResponse.class);

    }

}
