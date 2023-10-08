package com.panda.sport.bc.controller;

import com.panda.sport.bc.feign.MerchantReportClient;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/bc/match")
@Slf4j
public class MatchController {


    @Autowired
    private MerchantReportClient merchantReportClient;


    /**
     * 查询BC赛事注单统计 UTF8
     * @param: [request, vo]
     * @return:  com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @author: Butr
     * @date: 2020/9/08
     */
    @PostMapping(value = "/queryMatchStatisticList")
    public Response queryMatchStatisticList(HttpServletRequest request, @RequestBody SportVO vo) {
        log.info("/bc/match/queryMatchStatisticList:" + vo);
        return merchantReportClient.queryBCMatchStatisticList(vo);
    }


    /**
     * 查询BC赛事汇总统计
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @author: Butr
     * @date: 2020/9/08
     */
    @PostMapping(value = "/getMatchStatistics")
    public Response getMatchStatistics(HttpServletRequest request, @RequestBody SportVO vo) {
        log.info("/bc/match/getMatchStatistics:" + vo);
        return merchantReportClient.getBCMatchStatistics(vo);
    }

}
