package com.panda.sport.admin.controller;

import com.alibaba.fastjson.JSON;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.FinanceService;
import com.panda.sport.admin.service.OutMerchantService;

import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/admin/merchantReport")
@Slf4j
public class MerchantReportController {

    @Autowired
    private OutMerchantService outMerchantService;

    @Autowired
    private LocalCacheService localCacheService;

    @Resource
    private FinanceService financeService;

    /**
     * 商户注单统计
     */
    @PostMapping(value = "/merchantList")
    @PreAuthorize("hasAnyRole('merchant_note')")
    public Response merchantList(@RequestBody MerchantOrderVO merchantOrderRequestVO) {
        log.info("/admin/merchantReport/merchantList:" + merchantOrderRequestVO);
        if (merchantOrderRequestVO.getCurrency() != null &&
                !merchantOrderRequestVO.getCurrency().equals(CurrencyTypeEnum.RMB.getId())) {
            return new Response();
        }
        JwtUser user = SecurityUtils.getUser();
        setMerchantCodesForMerchantOrderVO(user, merchantOrderRequestVO);
        return outMerchantService.listGroupByMerchant(merchantOrderRequestVO);
    }

    /**
     * 商户联想查询列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/queryMerchantList")
    public Response queryMerchantList(HttpServletRequest request) {
        log.info("/admin/merchantReport/queryMerchantList:");
        return outMerchantService.queryMerchantListByParam();
    }

    /**
     * 本月商户投注量排行
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/merchantOrderTop10")
    public Response merchantOrderTop10(HttpServletRequest request) {
        return outMerchantService.queryMerchantTop10("betAmount");
    }

    /**
     * 本月商户投注量排行
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/merchantProfitTop10")
    public Response merchantProfitTop10(HttpServletRequest request) {
        return outMerchantService.queryMerchantTop10("settleProfit");
    }


    /**
     * 本月商户投注量排行
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/amountGrowthRateTop10")
    public Response amountGrowthRateTop10(HttpServletRequest request) {
        return outMerchantService.amountGrowthRateTop10();
    }

    /**
     * 商户报表导出
     *
     * @param response
     */
    @PostMapping("/merchantFileExport")
    public Response merchantFileExport(HttpServletRequest request, HttpServletResponse response, @RequestBody MerchantOrderVO requestVO) {
        log.info("开始下载商户报表,param: {}", JSON.toJSONString(requestVO));
        JwtUser user = SecurityUtils.getUser();
        //setMerchantCodesForMerchantOrderVO(user, requestVO);
        requestVO.setPageNum(1);
        requestVO.setPageSize(100000);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return Response.returnSuccess(outMerchantService.exportMerchantReport(response, requestVO, null,language));
    }


    /**
     * 商户注单统计列表 ----》实际调用的是 admin/financeMonth/queryFinanceayTotalList 接口
     */
    @PostMapping(value = "/queryMerchantNoteStatisticsList")
    @ApiOperation(value = "/admin/merchantReport/queryMerchantNoteStatisticsList",notes = "商户后台-数据中心-商户注单统计-列表")
    public Response queryMerchantNoteStatisticsList(@RequestBody MerchantFinanceDayVo dayVo) {
        if (dayVo.getPageNum() == null) {
            dayVo.setPageNum(1);
        }
        if (dayVo.getPageSize() == null) {
            dayVo.setPageSize(10);
        }
        return financeService.queryFinanceayTotalList(dayVo);
    }

    /**
     * 商户注单统计汇总 ----》实际调用的是 admin/financeMonth/queryFinanceDayTotal 接口
     */
    @PostMapping(value = "/getMerchantNoteStatisticsSummary")
    @ApiOperation(value = "/admin/merchantReport/getMerchantNoteStatisticsSummary",notes = "商户后台-数据中心-商户注单统计-汇总")
    public Response getMerchantNoteStatisticsSummary(@RequestBody MerchantFinanceDayVo dayVo) {
        return financeService.queryFinanceDayTotal(dayVo);
    }

    /**
     * 设置参数商户code
     */
    private void setMerchantCodesForMerchantOrderVO(JwtUser user, MerchantOrderVO requestVO) {
        Integer agentLevel = user.getAgentLevel();
        if (AgentLevelEnum.AGENT_LEVEL_0.getCode().equals(agentLevel) || AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(agentLevel)) {
            requestVO.setMerchantCode(user.getMerchantCode());
        } else {
            //如果参数里已经有商户查询条件时，则不用取缓存里的下级商户树了
            if (CollectionUtils.isEmpty(requestVO.getMerchantCodeList())) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), agentLevel);
                merchantCodeList.add(user.getMerchantCode());
                requestVO.setMerchantCodeList(merchantCodeList);
            }
        }
    }
}
