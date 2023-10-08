package com.panda.sport.admin.controller;

import com.alibaba.fastjson.JSON;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.FinanceService;
import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.EZ;

@RestController
@RequestMapping("/admin/financeMonth")
@Slf4j
public class FinanceReportController {

    @Autowired
    private FinanceService financeService;
    @Autowired
    private LocalCacheService localCacheService;

    /**
     * 清算管理 - 月账单列表   /   二级商户列表
     *
     * @param monthVo
     * @return
     */
    @PostMapping(value = "/queryFinanceMonthList")
    @PreAuthorize("hasAnyRole('liquidation')")
    public Response queryFinanceMonthList(@RequestBody MerchantFinanceMonthVo monthVo) {
        JwtUser user = SecurityUtils.getUser();
        if (user.getAgentLevel() == 2) {
            return Response.returnFail("下级代理不可展示");
        }
        assemblyMerchantFinanceMonthVo(monthVo);
        log.info("/admin/financeMonth/queryFinanceMonthList:" + monthVo);
        return financeService.queryFinanceMonthList(monthVo);
    }


    /**
     * 对账单 - 总计
     *
     * @param monthVo
     * @return
     */
    @PostMapping(value = "/queryFinanceMonthTotal")
    @PreAuthorize("hasAnyRole('liquidation')")
    public Response queryFinanceMonthTotal(@RequestBody MerchantFinanceMonthVo monthVo) {
        JwtUser user = SecurityUtils.getUser();
        if (user.getAgentLevel() == 2) {
            return Response.returnFail("下级代理不可展示");
        }
        assemblyMerchantFinanceMonthVo(monthVo);
        log.info("/admin/financeMonth/queryFinanceMonthTotal:" + monthVo);

        return financeService.queryFinanceMonthTotal(monthVo);
    }


    /**
     * 电子账单 - 月账单子目录
     *
     * @param monthVo
     * @return
     */
    @PostMapping(value = "/queryFinanceMonthDetail")
    public Response queryFinanceMonthDetail(HttpServletRequest request, @RequestBody MerchantFinanceMonthVo monthVo) {
        log.info("/admin/financeMonth/queryFinanceMonthDetail:" + monthVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return financeService.queryFinanceMonthDetail(monthVo, language);
    }

    /**
     * 对账单 - 日账单
     *
     * @param dayVo
     * @return
     */
    @PostMapping(value = "/queryFinanceDay")
    @PreAuthorize("hasAnyRole('statements')")
    public Response queryFinanceDayList(HttpServletRequest request, @RequestBody MerchantFinanceDayVo dayVo) {
        assemblyMerchantFinanceDayVo(dayVo, "");
        log.info("/admin/financeMonth/queryFinanceDayList:" + dayVo);
        return financeService.queryFinanceDayList(dayVo);
    }

    /**
     * 对账单 - 总计
     *
     * @param dayVo
     * @return
     */
    @PostMapping(value = "/queryFinanceDayTotal")
    public Response queryFinanceDayTotal(@RequestBody MerchantFinanceDayVo dayVo) {
        assemblyMerchantFinanceDayVo(dayVo, "");

        return financeService.queryFinanceDayTotal(dayVo);
    }

    /**
     * 日对账单导出
     * <p>
     * 测试注释token：http://localhost:10714/admin/financeMonth/financeDayExport?financeDayId=2020-07-04-oubao&filter=3&filterDate=1
     */
    @RequestMapping(value = "/financeDayExport")
    public Response financeDayExport(HttpServletRequest request,@RequestBody MerchantFinanceDayVo dayVo) {
        log.info("financeDayExport param = {}", JSON.toJSONString(dayVo));
        if (StringUtils.isEmpty(dayVo.getFinanceDayId())) {
            return Response.returnFail("参数异常！");
        }
        if (StringUtils.isEmpty(dayVo.getLanguage())) {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) {
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }
            dayVo.setLanguage(language);
        }
        assemblyMerchantFinanceDayVo(dayVo, dayVo.getToken());
        if (StringUtils.isEmpty(dayVo.getFilter())) {
            //结算时间
            dayVo.setFilter("3");
        }
        if (StringUtils.isEmpty(dayVo.getTimeZone())) {
            //账务日
            dayVo.setTimeZone(EZ);
        }
        JwtUser user = SecurityUtils.getUser();
        Map<String, Object>  resultMap = financeService.financeDayExport(user.getMerchantCode(),user.getUsername(), dayVo);
        return Response.returnSuccess(resultMap);
    }

    /**
     * 清算管理 - 查看二级商户 - 总计
     *
     * @param monthVo
     * @return
     */
    @PostMapping(value = "/queryFinanceMonthCount")
    public Response queryFinanceMonthCount(@RequestBody MerchantFinanceMonthVo monthVo) {
        assemblyMerchantFinanceMonthVo(monthVo);
        log.info("/admin/financeMonth/queryFinanceMonthCount:" + monthVo);

        return financeService.queryFinanceMonthCount(monthVo);
    }


    private void assemblyMerchantFinanceMonthVo(MerchantFinanceMonthVo vo) {
        JwtUser user = SecurityUtils.getUser();
        if (user.getAgentLevel() == 1 || user.getAgentLevel() == 10) {
            if (CollectionUtils.isEmpty(vo.getMerchantCodeList())) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    vo.setMerchantCodeList(merchantCodeList);
                } else {
                    vo.setMerchantCode(user.getMerchantCode());
                }
            }
            /*List<String> merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
            if (CollectionUtils.isNotEmpty(merchantCodeList) && vo.getMerchantCodeList() == null) {
                vo.setMerchantCodeList(merchantCodeList);
            } else {
                vo.setMerchantCode(user.getMerchantCode());
            }*/
        } else {
            vo.setAgentLevel(user.getAgentLevel());
            vo.setMerchantCode(user.getMerchantCode());
        }
    }

    /**
     * 不可以包含merchantName ， agentLevel 参数，不然部分数据会被过滤
     *
     * @param vo
     * @param token
     */
    private void assemblyMerchantFinanceDayVo(MerchantFinanceDayVo vo, String token) {
        String merchantCode, merchantId;
        Integer agentLevel;
        if (StringUtils.isNotEmpty(token)) {
            Map<String, String> claims = JwtTokenUtil.verifyToken(token);
            merchantCode = claims.get("merchantCode");
            merchantId = claims.get("merchantId");
            agentLevel = Integer.valueOf(claims.get("agentLevel"));
        } else {
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            merchantId = user.getMerchantId();
            agentLevel = user.getAgentLevel();
        }
        if (agentLevel == 1 || agentLevel == 10) {
            List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
            List<String> resultList = new ArrayList<>();
            if (vo.getMerchantCodeList() == null) {
                resultList.addAll(merchantCodeList);
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    resultList.add(merchantCode);
                    vo.setMerchantCodeList(resultList);
                } else {
                    vo.setMerchantCode(merchantCode);
                }
            }
        } else {
            vo.setMerchantCode(merchantCode);
            vo.setAgentLevel(agentLevel);
        }

    }


    /**
     * 对账单 - 日账单
     */
    @PostMapping(value = "/queryFinanceayTotalList")
    public Response queryFinanceayTotalList(HttpServletRequest request, @RequestBody MerchantFinanceDayVo dayVo) {
        assemblyMerchantFinanceDayVo(dayVo, "");
        log.info("/admin/financeMonth/queryFinanceayTotalList:" + dayVo);
        return financeService.queryFinanceayTotalList(dayVo);
    }



    /**
     * 对账单 - 日账单
     */
    @PostMapping(value = "/queryFinanceDayV2")
    public Response queryFinanceDayV2(HttpServletRequest request, @RequestBody MerchantFinanceDayVo dayVo) {
        assemblyMerchantFinanceDayVo(dayVo, "");
        log.info("/order/financeMonth/queryFinanceDay:" + dayVo);
        return financeService.queryFinanceDayListV2(dayVo);
    }

}
