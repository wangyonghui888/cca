package com.panda.sport.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.panda.sport.backup.mapper.BackupOrderMapper;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.NoticeLanguageEnum;
import com.panda.sport.merchant.common.enums.NoticeTypeEnum;
import com.panda.sport.merchant.common.po.bss.SportPO;
import com.panda.sport.merchant.common.utils.RegexUtils;
import com.panda.sport.merchant.common.utils.TimeUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.MatchVO;
import com.panda.sport.merchant.common.vo.merchant.MatchBetInfoVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetInfoDto;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.MatchService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service
@RefreshScope
public class MatchServiceImpl implements MatchService {

    @Autowired
    private SportMapper sportMapper;
    @Autowired
    private MerchantReportClient rpcClient;
    @Autowired
    private MerchantFileService merchantFileService;
    @Autowired
    private LocalCacheService localCacheService;
    @Autowired
    private BackupOrderMapper backupOrderMapper;

    @Value("${abnormal.event.user.zs.context:您好，经风控部门进一步检查，此赛事以下会员为异常下注会员，为避免此类会员对贵司造成不必要的损失，已做风控处理。同时我方建议关闭此类会员的账户或者限制此类会员的投注额度。}")
    private String zsContext;
    @Value("${abnormal.event.user.en.context:Hello, After further inspection by the risk control department, the following members of this race are abnormal betting members. In order to avoid unnecessary losses to your company caused by such members, risk control has been carried out. At the same time, we propose to close the accounts of such members or limit the betting limit of such members.}")
    private String enContext;

    public static final Map<Integer, Object> matchENMap = new HashMap<>();

    public static final Map<Integer, Object> matchMap = new HashMap<>();
    public static final Map<Integer, Object> tournamentLevelMap = new HashMap<>();

    static {
        matchMap.put(0, "赛事未开始");
        matchMap.put(1, "进行中(滚球)");
        matchMap.put(2, "暂停");
        matchMap.put(3, "结束");
        matchMap.put(4, "关闭");
        matchMap.put(5, "取消");
        matchMap.put(6, "比赛放弃");
        matchMap.put(7, "延迟");
        matchMap.put(8, "未知");
        matchMap.put(9, "延期");
        matchMap.put(10, "比赛中断");
        matchENMap.put(0, "PreMatch");
        matchENMap.put(1, "Ongoing");
        matchENMap.put(2, "Paused");
        matchENMap.put(3, "Over");
        matchENMap.put(4, "Closed");
        matchENMap.put(5, "Cancel");
        matchENMap.put(6, "GivenUP");
        matchENMap.put(7, "Delayed");
        matchENMap.put(8, "Unknown");
        matchENMap.put(9, "Delayed");
        matchENMap.put(10, "Paused");
        tournamentLevelMap.put(0, "");
        tournamentLevelMap.put(1, "一级联赛");
        tournamentLevelMap.put(2, "二级联赛");
        tournamentLevelMap.put(3, "三级联赛");
        tournamentLevelMap.put(4, "四级联赛");
        tournamentLevelMap.put(5, "五级联赛");
        tournamentLevelMap.put(6, "六级联赛");
        tournamentLevelMap.put(7, "七级联赛");
        tournamentLevelMap.put(8, "八级联赛");
        tournamentLevelMap.put(9, "九级联赛");
        tournamentLevelMap.put(10, "十级联赛");
        tournamentLevelMap.put(11, "十一级联赛");
        tournamentLevelMap.put(12, "十二级联赛");
        tournamentLevelMap.put(13, "十三级联赛");
        tournamentLevelMap.put(14, "十四级联赛");
        tournamentLevelMap.put(15, "十五级联赛");
        tournamentLevelMap.put(16, "十六级联赛");
        tournamentLevelMap.put(17, "十七级联赛");
        tournamentLevelMap.put(18, "十八级联赛");
        tournamentLevelMap.put(19, "十九级联赛");
        tournamentLevelMap.put(20, "二十级联赛");
    }

    /**
     * 获取体种
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Response<Object> getSportList() {
        List<SportPO> list = sportMapper.selectAll();
        List<SportVO> sportList = Lists.newArrayList();
        for (SportPO po : list) {
            SportVO sportVO = new SportVO();
            BeanUtils.copyProperties(po, sportVO);
            sportList.add(sportVO);
        }
        return Response.returnSuccess(sportList);
    }

    /**
     * 获取赛事前10
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Response<?> queryMatchStatistic10() {
        try {
            SportVO merchantOrderRequestVO = new SportVO();
            String startTime = TimeUtils.getSimpleDayBy();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(startTime);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            startTime = sdf1.format(date);
            merchantOrderRequestVO.setPageNum(1);
            merchantOrderRequestVO.setStartTime(startTime);
            merchantOrderRequestVO.setEndTime(startTime);
            merchantOrderRequestVO.setOrderBy("validBetAmount");
            merchantOrderRequestVO.setSort("desc");
            merchantOrderRequestVO.setPageSize(10);
            return Response.returnSuccess(rpcClient.queryMatchStatisticList(merchantOrderRequestVO));
        } catch (Exception e) {
            log.error("HomeController.matchTop10,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 获取赛事统计列表
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Response<?> queryMatchStatisticList(SportVO sportVO, String language) {
        try {
            Map<String, Object> result;
            if (CollectionUtils.isEmpty(sportVO.getMerchantCodeList())) {
                result = rpcClient.queryMatchStatisticList(sportVO);
            } else {
                result = rpcClient.queryMerchantMatchStatisticList(sportVO);
            }
            if (result != null && result.get("list") != null) {
                List<?> resultList = (List<?>) result.get("list");
                ObjectMapper mapper = new ObjectMapper();
                List<MatchBetInfoVO> filterList = mapper.convertValue(resultList, new TypeReference<List<MatchBetInfoVO>>() {
                });
                Map<Integer, String> sportMap = localCacheService.getSportMap(language);
                for (MatchBetInfoVO vo : filterList) {
                    vo.setMerchantName(vo.getMerchantCode());
                    if (sportMap != null && vo.getSportId() != null) {
                        vo.setSportName(sportMap.get(vo.getSportId()));
                    }
/*                    if (!RegexUtils.isLanguage(vo.getMatchInfo(), language)) {
                        String matchInfo = orderMixMapper.getMatchInfo(vo.getMatchId(), language);
                        if (StringUtils.isNotEmpty(matchInfo)) {
                            vo.setMatchInfo(matchInfo);
                        }
                    }*/
                }
                result.put("list", filterList);
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("HomeController.matchTop10,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    @Override
    public Response<?> queryMatchStatisticListNew(MerchantMatchBetVo merchantMatchBetVo, String language) {
        try {
            Map<String, Object> result = rpcClient.queryMatchStatisticListNew(merchantMatchBetVo);

            if (result != null && result.get("list") != null) {
                List<?> resultList = (List<?>) result.get("list");
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantMatchBetInfoDto> filterList = mapper.convertValue(resultList, new TypeReference<List<MerchantMatchBetInfoDto>>() {
                });
                Map<Integer, String> sportMap = localCacheService.getSportMap(language);
                for (MerchantMatchBetInfoDto vo : filterList) {
                    vo.setMerchantName(vo.getMerchantCode());
                    if (sportMap != null && vo.getSportId() != null) {
                        vo.setSportName(sportMap.get(vo.getSportId()));
                    }
                }
                result.put("list", filterList);
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("HomeController.matchTop10,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 获取玩法统计列表
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Response<?> queryPlayStatisticList(SportVO sportVO, String language) {
        List<Integer> agentLevelList = Lists.newArrayList();
        agentLevelList.add(0);
        agentLevelList.add(2);
        sportVO.setAgentLevelList(agentLevelList);
        Response result = StringUtils.isBlank(sportVO.getMerchantCode()) ?
                rpcClient.queryPlayStatisticList(sportVO) : rpcClient.queryMerchantPlayStatisticList(sportVO);
        if (result != null && result.getData() != null) {
            List<?> resultList = (List<?>) ((Map) result.getData()).get("list");
            //log.info("queryPlayStatisticList resultList"+resultList.size());
            ObjectMapper mapper = new ObjectMapper();
            List<MatchBetInfoVO> filterList = mapper.convertValue(resultList, new TypeReference<List<MatchBetInfoVO>>() {
            });
            Map<String, String> playNameMap = localCacheService.getPlayNameMap(language);
            if (CollectionUtil.isNotEmpty(filterList)) {
                for (MatchBetInfoVO vo : filterList) {
                    if (!RegexUtils.isLanguage(vo.getPlayName(), language)) {
                        if (playNameMap != null) {
                            vo.setPlayName(playNameMap.get(vo.getPlayId() + ""));
                        }
                    }
                }
                ((Map) result.getData()).put("list", filterList);
            }
        }
        return result;
    }

    /**
     * 获取结果对象列表，ES最多返回1000条记录
     *
     * @param response
     * @param request
     * @param language
     * @throws Exception
     */
    @Override
    public void exportMatchStatistic(HttpServletResponse response, HttpServletRequest request,MerchantMatchBetVo vo, String language) throws Exception {
        vo.setPageNum(1);
        vo.setPageSize(100000);
        List<Integer> agentLevelList = Lists.newArrayList();
        agentLevelList.add(0);
        agentLevelList.add(2);
        assemblyMatchCriteria(vo, vo.getStartTime(), vo.getEndTime());
        log.info("导出参数{}", JSON.toJSONString(vo));
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "赛事投注统计_" : "MatchReport_"),
                null, request.getHeader("merchantName"), JSON.toJSONString(vo),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-赛事投注统计-导出赛事统计" : "Report Center-Match Report-matchExport",
                "orderMatchReportExportServiceImpl");
    }

    /**
     * 赛事玩法统计导出
     *
     * @param response
     * @param request
     * @param language
     * @throws Exception
     */
    @Override
    public void exportMatchPlayStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody SportVO vo, String language) throws Exception {
        Integer sportId = vo.getSportId();
        String startTime = vo.getStartTime();
        String endTime = vo.getEndTime();
        Long tournamentId = vo.getTournamentId();
        Integer tournamentLevel = vo.getTournamentLevel();
        Long matchId = vo.getMatchId();
        String orderBy = vo.getOrderBy();
        String sort = vo.getSort();
        List<String> merchantCodeList = vo.getMerchantCodeList();
        String merchantCode = vo.getMerchantCode();
        Map<String, Object> params = new HashMap<>();
        params.put("from", 0);
        params.put("pageSize", 1000000);
        //设置时间范围,时间参数接受long值
        if (sportId != null) {
            params.put("sportId", sportId);
        }
        if (null != tournamentId) {
            params.put("tournamentId", tournamentId);
        }
        if (null != tournamentLevel) {
            params.put("tournamentLevel", tournamentLevel);
        }
        if (null != matchId) {
            params.put("matchId", matchId);
        }
        if (null != merchantCode) {
            params.put("merchantCode", merchantCode);
        }
        if (null != merchantCodeList) {
            params.put("merchantCodeList", merchantCodeList);
        }
        List<Integer> agentLevelList = Lists.newArrayList();
        agentLevelList.add(0);
        agentLevelList.add(2);
        params.put("agentLevelList", agentLevelList);
        assemblyPlayMatchCriteria(params, startTime, endTime);
        params.put("orderBy", StringUtils.isNotEmpty(orderBy) ? orderBy : "beginTime");
        params.put("sort", StringUtils.isNotEmpty(sort) ? sort : "desc");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        params.put("language", language);
        merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "玩法投注统计_" : "PlayReport_", null, request.getHeader("merchantName"), JSON.toJSONString(params),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-赛事投注统计-导出玩法统计" : "Report Center-Match Report-playExport", "playReportExportServiceImpl");
    }

    /**
     * 初始化查询参数，帐务日或自然日的时间
     *
     * @param vo
     * @param
     * @param
     * @throws Exception
     */
    public void assemblyMatchCriteria(MerchantMatchBetVo vo, String startTime1, String endTime1) throws Exception {
        Date endTime = DateUtil.parse(vo.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
        Date startTime = DateUtil.parse(vo.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
        vo.setEndTimeL(endTime.getTime());
        vo.setStartTimeL(startTime.getTime());
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
        Date endTime1 = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_PATTERN);
        Date startTime1 = DateUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN);
        params.put("startTime", startTime1.getTime());
        params.put("endTime", endTime1.getTime());
    }

    @Override
    public Response<?> queryMatchStatisticById(MerchantMatchBetVo merchantMatchBetVo, String language) {
        try {
            Map<String, Object> result = rpcClient.queryMatchStatisticById(merchantMatchBetVo);
            return Response.returnSuccess(result);
        }catch (Exception e){
            log.error("RPC调用异常" + e);
            return Response.returnFail("查询异常!");
        }
    }

    @Override
    public Response<?> getMatchInfoByMatchId(String noticeTypeId,String matchId) {
        //不是异常赛事用户的，直接返回
        if(!NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode().toString().equals(noticeTypeId)) {
            return Response.returnSuccess("");
        }
        String zscontext = zsContext, encontext = enContext;
        if(StringUtils.isNotBlank(matchId)) {
            MatchVO result = backupOrderMapper.selectMatchInfo(Long.valueOf(matchId));
            if(result == null) {
                return Response.returnFail(String.format("matchId 【%s】没有找到对应的赛事信息！", matchId));
            }
            if(result.getMatchId() == null) {
                return Response.returnFail("该赛事没有对应的投注记录！");
            }
            zscontext = zscontext + "\nMatch ID：" + matchId + "\n" + result.getMatchInfoZs() + "\n" +
                    result.getSportNameZs() + StringPool.SLASH + result.getTournamentNameZs() + "\n" + result.getBeginTimeZs();
            encontext = encontext + "\nMatch ID：" + matchId + "\n" + result.getMatchInfoEn() + "\n" +
                    result.getSportNameEn() + StringPool.SLASH + result.getTournamentNameEn() + "\n" + result.getBeginTimeEn();
        }

        Map<Integer, Map<String, Object>> resultMap = new HashMap<>(2);
        Map<String, Object> zsmap = new HashMap<>(2);
        zsmap.put("langType", NoticeLanguageEnum.ZS.getKey());
        zsmap.put("context", zscontext);
        resultMap.put(0, zsmap);
        Map<String, Object> enmap = new HashMap<>(2);
        enmap.put("langType", NoticeLanguageEnum.EN.getKey());
        enmap.put("context", encontext);
        resultMap.put(1, enmap);
        return Response.returnSuccess(resultMap);
    }
}
