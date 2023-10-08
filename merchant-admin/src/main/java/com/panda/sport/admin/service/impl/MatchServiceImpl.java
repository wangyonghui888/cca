package com.panda.sport.admin.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.MatchService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.utils.HttpConnectionPool;
import com.panda.sport.merchant.common.utils.RegexUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MatchBetInfoVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetInfoDto;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("matchService")
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MerchantReportClient merchantReportClient;

    @Autowired
    private LocalCacheService localCacheService;


    @Value("${bd.tournament.domain}")
    private String getUrl;

    @Value("${bd.get.token.appid}")
    private String appid;

    @Value("${bd.get.token.api}")
    private String getTokenApi;

    @Value("${bd.reportname}")
    private String reportname;

    @Override
    public Response queryMatchStatisticList(SportVO sportVO, String language) {
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        String merchantId = user.getMerchantId();
        Integer agentLevel = user.getAgentLevel();
        if (agentLevel == 1 || agentLevel == 10) {
            List<String> tempList = sportVO.getMerchantCodeList();
            if (CollectionUtils.isEmpty(tempList)) {
                tempList = localCacheService.getMerchantCodeList(merchantId);
                sportVO.setMerchantCodeList(tempList);
            }
        } else {
            sportVO.setMerchantCode(merchantCode);
        }
        Map<String, Object> result = merchantReportClient.queryMerchantMatchStatisticList(sportVO);
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
/*                if (!RegexUtils.isLanguage(vo.getMatchInfo(), language)) {
                    String matchInfo = orderMixMapper.getMatchInfo(vo.getMatchId(), language);
                    if (StringUtils.isNotEmpty(matchInfo)) {
                        vo.setMatchInfo(matchInfo);
                    }
                }*/
            }
            result.put("list", filterList);
        }
        return Response.returnSuccess(result);
    }

    @Override
    public Response queryMatchStatisticListNew(MerchantMatchBetVo merchantMatchBetVo, String language) {
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
        Map<String, Object> result = merchantReportClient.queryMatchStatisticListNew(merchantMatchBetVo);
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
    }

    @Override
    public Response queryPlayStatisticList(SportVO sportVO, String language) {
        List<String> tempList = new ArrayList<>();
        tempList.add(sportVO.getMerchantCode());
        sportVO.setMerchantCodeList(tempList);
        Response result = merchantReportClient.queryMerchantPlayStatisticList(sportVO);
        if (result != null && result.getData() != null) {
            List<?> resultList = (List<?>) ((Map) result.getData()).get("list");
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

    @Override
    public Response getQueryTournamentUrl() {
        JwtUser user = SecurityUtils.getUser();
        String merchantId = user.getMerchantId();
        Integer  agentLevel =user.getAgentLevel();
        String userName = user.getUsername();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("merchantId", merchantId);
        jsonObject.put("merchantType", agentLevel);
        jsonObject.put("reportName", reportname);
        jsonObject.put("appId", appid);

        log.info("getQueryTournamentUrl info:"+ jsonObject);
        
        try {
            HttpEntity request = new HttpEntity<>(jsonObject, getHeader());
            JSONObject response =  HttpConnectionPool.restTemplate.postForObject(getUrl + getTokenApi, request, JSONObject.class);
            if ("200".equals(response.getString("code"))) {
                JSONObject data = response.getJSONObject("data");
                return Response.returnSuccess(getUrl + data.getString("url"));
            }else{
                log.info("getQueryTournamentUrl fail:"+response);
                return Response.returnFail(response.getString("msg"));
            }
        }catch (Exception e){
            log.error("getQueryTournamentUrl error:",e);

        }
        return Response.returnFail("system error");
    }

    private static HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

    @Override
    public Response<?> queryMatchStatisticById(MerchantMatchBetVo merchantMatchBetVo, String language) {
        try {
            Map<String, Object> result = merchantReportClient.queryMatchStatisticById(merchantMatchBetVo);
            return Response.returnSuccess(result);
        }catch (Exception e){
            log.error("RPC调用异常" + e);
            return Response.returnFail("查询异常!");
        }
    }
}
