package com.panda.sport.admin.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.MatchService;
import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.utils.DateTimeUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.manage.service.TournamentService;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@RestController
@RequestMapping("/admin/match")
@Slf4j
public class MatchReportController {
    @Autowired
    protected OutMerchantService outMerchantService;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    /**
     * 赛事投注统计
     */
    @PostMapping(value = "/queryMatchStatisticList")
    @PreAuthorize("hasAnyRole('match_bonus')")
    public Response queryMerchantMatchStatisticList(HttpServletRequest request, @RequestBody SportVO sportVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return matchService.queryMatchStatisticList(sportVO, language);

    }

    /**
     * 赛事注单统计
     */
    @PostMapping(value = "/queryMatchStatisticListNew")
    public Response queryMatchStatisticListNew(HttpServletRequest request, @RequestBody MerchantMatchBetVo merchantMatchBetVo) {
        log.info("/order/match/queryMatchStatisticList:" + merchantMatchBetVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return matchService.queryMatchStatisticListNew(merchantMatchBetVo, language);
    }

    @PostMapping(value = "/queryMatchStatisticById")
    public Response queryMatchStatisticById(HttpServletRequest request, @RequestBody MerchantMatchBetVo merchantMatchBetVo) {
        log.info("/admin/match/queryMatchStatisticById:" + merchantMatchBetVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return matchService.queryMatchStatisticById(merchantMatchBetVo, language);
    }

    /**
     * 玩法投注统计
     *
     * @param request
     * @param sportVO
     * @return
     */
    @PostMapping(value = "/queryPlayStatisticList")
    public Response queryMerchantPlayStatisticList(HttpServletRequest request, @RequestBody SportVO sportVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return matchService.queryPlayStatisticList(sportVO, language);
    }



    /**
     * 获取联赛投注统计地直
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/getQueryTournamentUrl")
    public Response getQueryTournamentUrl(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return matchService.getQueryTournamentUrl();
    }

    /**
     * 赛事注单统计-联赛下拉
     */
    @PostMapping(value = "/pullDownTournament")
    public Response pullDownTournament(HttpServletRequest request, @RequestBody SportVO sportVO) {
        log.error("/order/match/pullDownTournament:" + sportVO);
        if (StringUtils.isBlank(sportVO.getStartTime())) {
            Date date = com.panda.sport.merchant.common.utils.DateUtils.addMonth(new Date(), -3);
            sportVO.setStartTime(DateTimeUtils.convertDate2String("yyyy-MM-dd", date));
        }
        if (StringUtils.isBlank(sportVO.getEndTime())) {
            sportVO.setEndTime(DateTimeUtils.convertDate2String("yyyy-MM-dd", new Date()));
        }
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return Response.returnSuccess(tournamentService.pullDownTournament(sportVO, language));
    }


    /**
     * 赛事投注统计导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportMatchPlayStatistic")
    public Response exportMatchPlayStatistic(HttpServletRequest request, HttpServletResponse response, @RequestBody SportVO sportVO) throws Exception {
        Integer sportId = sportVO.getSportId();
        String startTime = sportVO.getStartTime();
        String endTime = sportVO.getEndTime();
        Integer tournamentLevel = sportVO.getTournamentLevel();
        Long matchId = sportVO.getMatchId();
        String matchInfo = sportVO.getMatchInfo();
        String merchantCode = sportVO.getMerchantCode();
        String orderBy = sportVO.getOrderBy();
        String sort = sportVO.getSort();
        Map<String, Object> params = new HashMap<>();
        JwtUser user = SecurityUtils.getUser();
        if (CollectionUtils.isEmpty(sportVO.getMerchantCodeList())) {
            String merchantId = user.getMerchantId();
            Integer agentLevel = user.getAgentLevel();
            if (agentLevel == 2 || agentLevel == 0) {
                params.put("merchantCode", user.getMerchantCode());
            } else {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                params.put("merchantCodeList", merchantCodeList);
            }
        } else {
            params.put("merchantCodeList", sportVO.getMerchantCodeList());
        }
        params.put("from", 0);
        params.put("pageSize", 100000);
        //设置时间范围,时间参数接受long值
        if (sportId != null) {
            params.put("sportId", sportId);
        }
        assemblyMatchCriteria(params, startTime, endTime);
        if (null != tournamentLevel) {
            params.put("tournamentLevel", tournamentLevel);
        }
        if (null != matchId) {
            params.put("matchId", matchId);
        }
        if (null != matchInfo) {
            params.put("matchInfo", "*" + matchInfo + "*");
        }
        params.put("orderBy", StringUtils.isNotEmpty(orderBy) ? orderBy : "beginTime");
        params.put("sort", StringUtils.isNotEmpty(sort) ? sort : "desc");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        params.put("language", language);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "玩法投注统计_" : "PlayReport_", user.getMerchantCode(), user.getUsername(), JSON.toJSONString(params),
                    language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-赛事投注统计-导出玩法统计" : "Report Center-Match Report-playExport", "playReportExportServiceImpl");
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 赛事投注统计导出
     *
     * @throws Exception
     */
    @RequestMapping("/exportMatchStatistic")
    public Response exportMerchantMatchStatistic(HttpServletRequest request, @RequestBody MerchantMatchBetVo merchantMatchBetVo) throws Exception {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        merchantMatchBetVo.setLanguage(language);
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        String merchantId = user.getMerchantId();
        Integer agentLevel = user.getAgentLevel();
        if (agentLevel == 1 || agentLevel == 10) {
            if (agentLevel == 10){
                List<String> codeList = new ArrayList<>();
                List<String> tempList = merchantMatchBetVo.getMerchantCodeList();
                if (CollectionUtils.isEmpty(tempList)) {
                    tempList = localCacheService.getMerchantCodeList(merchantId);
                    for (String code : tempList){
                        Long id = localCacheService.getMerchantId(code);
                        List<String> codes = localCacheService.getMerchantCodeList(String.valueOf(id));
                        codeList.addAll(codes);
                    }
                    merchantMatchBetVo.setMerchantCodeList(codeList);
                }
            }else {
                List<String> tempList = merchantMatchBetVo.getMerchantCodeList();
                if (CollectionUtils.isEmpty(tempList)) {
                    tempList = localCacheService.getMerchantCodeList(merchantId);
                    merchantMatchBetVo.setMerchantCodeList(tempList);
                }
            }
        } else {
            List<String> tempList = new ArrayList<>();
            tempList.add(merchantCode);
            merchantMatchBetVo.setMerchantCodeList(tempList);
        }
        merchantMatchBetVo.setPageNum(1);
        merchantMatchBetVo.setPageSize(100000);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "赛事投注统计_" : "MatchReport_"), merchantCode, user.getUsername(), JSON.toJSONString(merchantMatchBetVo),
                    language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-赛事投注统计-导出赛事统计" : "Report Center-Match Report-matchExport", "matchReportExportServiceImpl");
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 初始化查询参数，帐务日或自然日的时间
     *
     * @param vo
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    public void assemblyMatchCriteria(MerchantMatchBetVo vo, String startTime, String endTime) throws Exception {
        Date before90 = DateUtils.addDays(new Date(), -90);
        endTime = StringUtils.isNotEmpty(endTime) ? endTime : DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        Date startDate;
        if (StringUtils.isEmpty(startTime)) {
            startDate = before90;
        } else {
            startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd");
            if (startDate.before(before90)) {
                startDate = before90;
            }
        }
        startTime = DateFormatUtils.format(startDate, "yyyy-MM-dd");
        LocalTime max = LocalTime.of(11, 59, 59, 999_999_999);
        LocalTime min = LocalTime.of(12, 0, 0, 0);
        LocalDate startLocalTime = LocalDate.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime todayStart = LocalDateTime.of(startLocalTime, min);
        LocalDate endLocalTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime todayEnd = LocalDateTime.of(endLocalTime.plusDays(1), max);
        Long startTimeL = todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long endTimeL = todayEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        vo.setStartTimeL(startTimeL);
        vo.setEndTimeL(endTimeL);
    }
    /**
     * 初始化查询参数，帐务日或自然日的时间
     *
     * @param params
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    public void assemblyMatchCriteria(Map<String, Object> params, String startTime, String endTime) throws Exception {
        Date endTime1 = DateUtil.parse(endTime, DatePattern.NORM_DATE_PATTERN);
        Date startTime1 = DateUtil.parse(startTime, DatePattern.NORM_DATE_PATTERN);
        params.put("startTime", startTime1.getTime());
        params.put("endTime", endTime1.getTime());
    }

    /**
     * 初始化查询参数，帐务日或自然日的时间
     *
     * @param params
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    public void assemblyPlayMatchCriteria(Map<String, Object> params, String startTime, String endTime) throws Exception {
        Date before90 = DateUtils.addDays(new Date(), -90);
        endTime = StringUtils.isNotEmpty(endTime) ? endTime : DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        Date startDate;
        if (StringUtils.isEmpty(startTime)) {
            startDate = before90;
        } else {
            startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd");
            if (startDate.before(before90)) {
                startDate = before90;
            }
        }
        startTime = DateFormatUtils.format(startDate, "yyyy-MM-dd");
        LocalTime max = LocalTime.of(11, 59, 59, 999_999_999);
        LocalTime min = LocalTime.of(12, 0, 0, 0);
        LocalDate startLocalTime = LocalDate.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime todayStart = LocalDateTime.of(startLocalTime, min);
        LocalDate endLocalTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime todayEnd = LocalDateTime.of(endLocalTime.plusDays(1), max);
        Long startTimeL = todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long endTimeL = todayEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        params.put("startTime", startTimeL);
        params.put("endTime", endTimeL);
    }
}
