package com.panda.sport.merchant.api.service;

import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.api.BetApiVo;

import javax.servlet.http.HttpServletRequest;

public interface BetService {

    APIResponse<BetApiVo> getBetDetail(HttpServletRequest request, String merchantCode, String orderId, Long timestamp, String signature);

    APIResponse<Object> queryBetList(HttpServletRequest request, String userName, Long startTime, Long endTime, String merchantCode, Integer sportId, Long tournamentId,
                                     Integer settleStatus, Integer pageNum, Integer pageSize, Long timestamp, String signature, Integer orderBy, String language);

    /*APIResponse<Object> queryPreBetOrderList(HttpServletRequest request, Long startTime, Long endTime, String merchantCode,
                                             Integer pageNum, Integer pageSize, String language, Long timestamp, String signature);*/
}
