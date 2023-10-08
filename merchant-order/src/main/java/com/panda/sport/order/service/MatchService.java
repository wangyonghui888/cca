package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MatchService {

    Response<Object> getSportList();

    Response<?> queryMatchStatisticList(SportVO sportVO, String language);

    Response<?> queryMatchStatisticListNew(MerchantMatchBetVo merchantMatchBetVo, String language);

    Response<?> queryMatchStatistic10();

    Response<?> queryPlayStatisticList(SportVO merchantOrderRequestVO, String language);

    void exportMatchStatistic(HttpServletResponse response, HttpServletRequest request, MerchantMatchBetVo vo, String language) throws Exception;
    void exportMatchPlayStatistic(HttpServletResponse response, HttpServletRequest request, SportVO vo, String language) throws Exception;

    Response<?> queryMatchStatisticById(MerchantMatchBetVo merchantMatchBetVo, String language);

    Response<?> getMatchInfoByMatchId(String noticeTypeId,String matchId);
}
