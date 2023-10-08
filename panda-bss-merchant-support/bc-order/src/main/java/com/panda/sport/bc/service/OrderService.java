package com.panda.sport.bc.service;

import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletResponse;

public interface OrderService {
    Response<Object> queryTicketList(BetOrderVO vo);

    Response<Object> getStatistics(BetOrderVO vo);

    void exportTicketList(HttpServletResponse response, BetOrderVO orderVO);

    Response getSportList(String language);
}
