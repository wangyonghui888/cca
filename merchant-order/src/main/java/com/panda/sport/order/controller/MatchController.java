package com.panda.sport.order.controller;


import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.DateTimeUtils;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.manage.service.TournamentService;
import com.panda.sport.order.service.MatchService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@RestController
@RequestMapping("/order/match")
public class MatchController {

    @Autowired
    private MatchService sportService;

    @Autowired
    private TournamentService tournamentService;

    /**
     * 获取赛事报表
     *
     * @Param: [request]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 11:58
     */
    @GetMapping(value = "/list")
    public Response detail(HttpServletRequest request) {
        log.info("/order/match/list");
        try {
            return sportService.getSportList();
        } catch (Exception e) {
            log.error("MatchController.list,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 赛事注单统计
     */
    @PostMapping(value = "/queryMatchStatisticList")
    @AuthRequiredPermission("Merchant:Report:match:list")
    public Response queryMatchStatisticList(HttpServletRequest request, @RequestBody SportVO sportVO) {
        log.info("/order/match/queryMatchStatisticList:" + sportVO);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return sportService.queryMatchStatisticList(sportVO, language);
    }

    /**
     * 赛事注单统计
     */
    @PostMapping(value = "/queryMatchStatisticListNew")
    public Response queryMatchStatisticListNew(HttpServletRequest request, @RequestBody MerchantMatchBetVo merchantMatchBetVo) {
        log.info("/order/match/queryMatchStatisticList:" + merchantMatchBetVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return sportService.queryMatchStatisticListNew(merchantMatchBetVo, language);
    }

    @PostMapping(value = "/queryMatchStatisticById")
    public Response queryMatchStatisticById(HttpServletRequest request, @RequestBody MerchantMatchBetVo merchantMatchBetVo) {
        log.info("/order/match/queryMatchStatisticById:" + merchantMatchBetVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return sportService.queryMatchStatisticById(merchantMatchBetVo, language);
    }

    /**
     * 赛事注单统计-联赛下拉
     */
    @PostMapping(value = "/pullDownTournament")
    public Response pullDownTournament(HttpServletRequest request, @RequestBody SportVO sportVO) {
        log.info("/order/match/pullDownTournament:" + sportVO);
        if (StringUtils.isBlank(sportVO.getStartTime())) {
            Date date = DateUtils.addMonth(new Date(), -3);
            sportVO.setStartTime(DateTimeUtils.convertDate2String("yyyy-MM-dd", date));
        }
        if (StringUtils.isBlank(sportVO.getEndTime())) {
            sportVO.setEndTime(DateTimeUtils.convertDate2String("yyyy-MM-dd", new Date()));
        }
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return Response.returnSuccess(tournamentService.pullDownTournament(sportVO, language));
    }

    /**
     * 赛事注单统计
     */
    @PostMapping(value = "/queryPlayStatisticList")
    @AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryPlayStatisticList(HttpServletRequest request, @RequestBody SportVO merchantOrderRequestVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        log.info("/order/match/queryPlayStatisticList:" + merchantOrderRequestVO);
        merchantOrderRequestVO.setLanguage(language);
        return sportService.queryPlayStatisticList(merchantOrderRequestVO, language);
    }

    /**
     * 商户报表导出
     */
    @RequestMapping("/exportMatchStatistic")
    @AuthRequiredPermission("Merchant:Report:match:export")
    public Response exportMatchStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody MerchantMatchBetVo vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        log.info("/order/match/exportMatchStatistic:" + vo);
        try {
            sportService.exportMatchStatistic(response, request, vo, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("MatchController.exportMatchStatistic,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 商户报表导出
     */
    @RequestMapping("/exportMatchPlayStatistic")
    @AuthRequiredPermission("Merchant:Report:match:export")
    public Response exportMatchPlayStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody SportVO vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        log.info("/order/match/exportMatchPlayStatistic:" + vo);
        try {
            try {
                sportService.exportMatchPlayStatistic(response, request, vo, language);
            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
        } catch (Exception e) {
            log.error("MatchController.exportMatchPlayStatistic,exception:", e);
        }
        return Response.returnSuccess(resultMap);
    }


    /**
     *  获取赛事信息
     * @param request
     * @param noticeTypeId 公告类型id
     * @param matchId 赛事id
     * @return
     */
    @GetMapping(value = "/getMatchInfoByMatchId")
    public Response getMatchInfoByMatchId(HttpServletRequest request,
                                          @RequestParam("noticeTypeId") String noticeTypeId,
                                          @RequestParam(value = "matchId",required = false) String matchId) {
        log.info("/order/match/getMatchInfoByMatchId noticeTypeId：{}，matchId：{}" ,noticeTypeId, matchId);
        return sportService.getMatchInfoByMatchId(noticeTypeId, matchId);
    }
}
