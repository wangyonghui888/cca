package com.panda.sport.admin.service;


import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;

public interface MatchService {

    Response queryMatchStatisticList(SportVO sportVO, String language);

    Response queryMatchStatisticListNew(MerchantMatchBetVo merchantMatchBetVo, String language);

    Response queryPlayStatisticList(SportVO sportVO, String language);
    Response getQueryTournamentUrl();

    Response<?> queryMatchStatisticById(MerchantMatchBetVo merchantMatchBetVo, String language);
}
