package com.panda.sport.merchant.api.controller;


import com.panda.sport.merchant.api.aop.RedisAPILimit;
import com.panda.sport.merchant.api.service.BetService;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bet")
@Slf4j
public class BetController {

    @Autowired
    private BetService betService;

    /**
     * @description:获取注单记录
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping(value = "/getBetDetail")
    @RedisAPILimit(comprehensive = false, apiKey = "getBetDetail", limit = 600, sec = 60)
    @Trace
    public APIResponse<BetApiVo> getBetDetail(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                              @RequestParam(value = "orderNo") String orderNo,
                                              @RequestParam(value = "timestamp") Long timestamp,
                                              @RequestParam(value = "signature") String signature) {
        log.info("api/bet/getBetDetail merchantCode:" + merchantCode + ",orderNo:" + orderNo + ",timestamp:"
                + timestamp + ",signature" + signature);
        return StringUtils.isAnyEmpty(merchantCode, orderNo) || timestamp == null || signature == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                betService.getBetDetail(request, merchantCode, orderNo, timestamp, signature);
    }

    /**
     * @description:获取注单列表
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping(value = "/queryBetList")
//    @RedisAPILimit(comprehensive = false, apiKey = "queryBetList", limit = 30, sec = 60)
    @Trace
    public APIResponse<Object> queryBetList(HttpServletRequest request, @RequestParam(value = "userName", required = false) String userName,
                                            @RequestParam(value = "startTime") Long startTime,
                                            @RequestParam(value = "endTime") Long endTime,
                                            @RequestParam(value = "orderBy", required = false) Integer orderBy,
                                            @RequestParam(value = "merchantCode") String merchantCode,
                                            @RequestParam(value = "sportId", required = false) Integer sportId,
                                            @RequestParam(value = "tournamentId", required = false) Long tournamentId,
                                            @RequestParam(value = "settleStatus", required = false) Integer settleStatus,
                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                            @RequestParam(value = "language", required = false) String language,
                                            @RequestParam(value = "timestamp") Long timestamp,
                                            @RequestParam(value = "signature") String signature) {
        log.info("api/bet/queryBetList  merchantCode:" + merchantCode + ",sportId:" + sportId + ",startTime:"
                + startTime + ",endTime:" + endTime + ",signature" + signature + ",orderBy=" + orderBy + ",language=" + language);
        long startL = System.currentTimeMillis();
        APIResponse<Object> result = StringUtils.isAnyEmpty(merchantCode, signature) || timestamp == null || startTime == null || endTime == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                betService.queryBetList(request, userName, startTime, endTime, merchantCode, sportId, tournamentId,
                        settleStatus, pageNum, pageSize, timestamp, signature, orderBy, language);
        log.info(merchantCode + ",api/bet/queryBetList:" + startTime + "," + endTime + "," + (System.currentTimeMillis() - startL));
        return result;
    }

    /**
     * 获取商户预约投注订单
     * @param request
     * @param startTime
     * @param endTime
     * @param merchantCode
     * @param pageNum
     * @param pageSize
     * @param language
     * @param timestamp
     * @param signature
     * @return
     */
    /*@PostMapping(value = "/queryPreBetOrderList")
    @RedisAPILimit(comprehensive = false, apiKey = "queryPreBetOrderList", limit = 30, sec = 60)
    public APIResponse<Object> queryPreBetOrderList(HttpServletRequest request,
                                                    @RequestParam(value = "startTime") Long startTime,
                                                    @RequestParam(value = "endTime") Long endTime,
                                                    @RequestParam(value = "merchantCode") String merchantCode,
                                                    @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                    @RequestParam(value = "language", required = false) String language,
                                                    @RequestParam(value = "timestamp") Long timestamp,
                                                    @RequestParam(value = "signature") String signature){
        log.info("api/bet/queryPreBetOrderList merchantCode:" + merchantCode + ",startTime:"
                + startTime + ",endTime:" + endTime + ",signature" + signature + ",language=" + language);
        long startL = System.currentTimeMillis();
        APIResponse<Object> result = StringUtils.isAnyEmpty(merchantCode, signature) || timestamp == null || startTime == null || endTime == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                betService.queryPreBetOrderList(request, startTime, endTime, merchantCode,
                pageNum, pageSize, language, timestamp, signature);
        log.info(merchantCode + ",api/bet/queryPreBetOrderList:" + startTime + "," + endTime + "," + (System.currentTimeMillis() - startL));
        return result;
    }*/
}
