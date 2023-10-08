package com.panda.sport.order.controller;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.FinanceDayVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.order.service.FinanceService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.EZ;

@Slf4j
@RestController
@RequestMapping("/order/financeMonth")
public class FinanceController {

    @Autowired
    private FinanceService financeService;
    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 清算管理 - 月账单列表   /   二级商户列表
     */
    @PostMapping(value = "/queryFinanceMonthList")
    @AuthRequiredPermission("Merchant:Finance:list")
    public Response queryFinanceMonthList(HttpServletRequest request, @RequestBody MerchantFinanceMonthVo monthVo) {
        if (monthVo.getPageNum() == null) {
            monthVo.setPageNum(1);
        }
        if (monthVo.getPageSize() == null) {
            monthVo.setPageSize(10);
        }
        return financeService.queryFinanceMonthList(monthVo);
    }

    /**
     * 对账单 - 总计
     *
     * @param monthVo
     * @return
     */
    @PostMapping(value = "/queryFinanceMonthTotal")
    public Response queryFinanceMonthTotal(@RequestBody MerchantFinanceMonthVo monthVo) {
        if (monthVo.getPageNum() == null) {
            monthVo.setPageNum(1);
        }
        if (monthVo.getPageSize() == null) {
            monthVo.setPageSize(10);
        }
        log.info("/admin/financeMonth/queryFinanceMonthTotal:" + monthVo);
        return financeService.queryFinanceMonthTotal(monthVo);
    }

    /**
     * 电子账单 - 月账单子目录
     */
    @PostMapping(value = "/queryFinanceMonthDetail")
    @AuthRequiredPermission("Merchant:Finance:detail")
    public Response queryFinanceMonthDetail(HttpServletRequest request, @RequestBody MerchantFinanceMonthVo monthVo) {
        log.info("/order/financeMonth/queryFinanceMonthDetail" + monthVo);
        if (StringUtils.isEmpty(monthVo.getId())) {
            return Response.returnFail(ResponseEnum.getErrorMsg("参数id缺失"));
        }
        return financeService.queryFinanceMonthDetail(monthVo);
    }

    /**
     * 电子账单 - 月账单子目录  - 修改 调整金额/执行费率
     */
    @PostMapping(value = "/updateFinanceMonthDetail")
    @AuthRequiredPermission("Merchant:Finance:operate:update")
    public Response updateFinanceMonthDetail(HttpServletRequest request, @RequestBody MerchantFinanceMonthVo monthVo) {
        if (StringUtils.isEmpty(monthVo.getId())) {
            return Response.returnFail(ResponseEnum.getErrorMsg("参数id缺失"));
        }
        if (monthVo.getAdjustAmount() == null || monthVo.getExecuteRate() == null) {
            return Response.returnFail(ResponseEnum.getErrorMsg("待修改参数缺失"));
        }
        Response response = financeService.updateFinanceMonthDetail(monthVo);
        if (response.getStatus()) {
            //记录日志
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("adjustAmount"));
            filedVO.getAfterValues().add(monthVo.getAdjustAmount().toString());
            filedVO.getBeforeValues().add(monthVo.getOldAdjustAmount() == null ? "0" : monthVo.getOldAdjustAmount().toString());
            filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("adjustCause"));
            filedVO.getBeforeValues().add(monthVo.getOldAdjustCause());
            filedVO.getAfterValues().add(monthVo.getAdjustCause());
            String username = request.getHeader("merchantName");
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            merchantLogService.saveLog(MerchantLogPageEnum.FINANCE_IN_MANAGER, MerchantLogTypeEnum.FINANCE_EDIT, filedVO, MerchantLogConstants.MERCHANT_IN,
                    SsoUtil.getUserId(request).toString(), username, monthVo.getMerchantCode(), monthVo.getMerchantName(), monthVo.getId(), language, IPUtils.getIpAddr(request));
        }
        return response;
    }

    /**
     * 电子账单 - 调整记录
     */
    @PostMapping(value = "/getFinanceOperateRecordList")
    @AuthRequiredPermission("Merchant:Finance:operate:list")
    public Response getFinanceOperateRecordList(@RequestParam(value = "financeId") String financeId) {
        if (StringUtils.isEmpty(financeId)) {
            return Response.returnFail(ResponseEnum.getErrorMsg("参数financeId缺失"));
        }
        return financeService.getFinanceOperateRecordList(financeId);
    }

    /**
     * 对账单 - 日账单
     */
    @PostMapping(value = "/queryFinanceayTotalList")
    @AuthRequiredPermission("Merchant:Finance:Day:Totallist")
    public Response queryFinanceayTotalList(HttpServletRequest request, @RequestBody MerchantFinanceDayVo dayVo) {
        if (dayVo.getPageNum() == null) {
            dayVo.setPageNum(1);
        }
        if (dayVo.getPageSize() == null) {
            dayVo.setPageSize(10);
        }
        log.info("/order/financeMonth/queryFinanceayTotalList:" + dayVo);
        return financeService.queryFinanceayTotalList(dayVo);
    }



    /**
     * 对账单 - 日账单
     */
    @PostMapping(value = "/queryFinanceDayV2")
    @AuthRequiredPermission("Merchant:Finance:Day:listV2")
    public Response queryFinanceDayV2(HttpServletRequest request, @RequestBody MerchantFinanceDayVo dayVo) {
        if (dayVo.getPageNum() == null) {
            dayVo.setPageNum(1);
        }
        if (dayVo.getPageSize() == null) {
            dayVo.setPageSize(10);
        }
        log.info("/order/financeMonth/queryFinanceDay:" + dayVo);
        return financeService.queryFinanceDayListV2(dayVo);
    }


    /**
     * 对账单 - 日账单
     */
    @PostMapping(value = "/queryFinanceDay")
    @AuthRequiredPermission("Merchant:Finance:Day:list")
    public Response queryFinanceDayList(HttpServletRequest request, @RequestBody MerchantFinanceDayVo dayVo) {
        if (dayVo.getPageNum() == null) {
            dayVo.setPageNum(1);
        }
        if (dayVo.getPageSize() == null) {
            dayVo.setPageSize(10);
        }
        log.info("/order/financeMonth/queryFinanceDay:" + dayVo);
        return financeService.queryFinanceDayList(dayVo);
    }

    /**
     * 对账单 - 总计
     */
    @PostMapping(value = "/queryFinanceDayTotal")
    public Response queryFinanceDayTotal(@RequestBody MerchantFinanceDayVo dayVo) {
        log.info("/order/financeMonth/queryFinanceDayTotal:" + dayVo);
        return financeService.queryFinanceDayTotal(dayVo);
    }

    /**
     * 清算管理 - 查看二级商户 - 总计
     */
    @PostMapping(value = "/queryFinanceMonthCount")
    public Response queryFinanceMonthCount(@RequestBody MerchantFinanceMonthVo monthVo) {

        return financeService.queryFinanceMonthCount(monthVo);
    }

    /**
     * 日对账单导出
     * http://localhost:10711/order/financeMonth/financeDayExport?financeDayId=2020-07-04-oubao&filter=3&filterDate=1
     */
    @RequestMapping(value = "/financeDayExport")
    @AuthRequiredPermission("Merchant:Finance:exportDay")
    public Response financeDayExport( HttpServletRequest request,@RequestBody MerchantFinanceDayVo dayVo) {
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
        if (StringUtils.isEmpty(dayVo.getFilter())) {
            //结算时间
            dayVo.setFilter("3");
        }
        if (StringUtils.isEmpty(dayVo.getTimeZone())) {
            //账务日
            dayVo.setTimeZone(EZ);
        }
        Map<String, Object>  resultMap = financeService.financeDayExport(null,request.getHeader("merchantName"), dayVo);
        return Response.returnSuccess(resultMap);
    }

    /**
     * 月对账单导出
     */
    @RequestMapping(value = "/financeMonthExport")
    @AuthRequiredPermission("Merchant:Finance:exportMonth")
    public void financeMonthExport(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id") String id) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        financeService.financeMonthExport(response, request, id, language);
    }


    /**
     * 关闭对账单 - 日账单
     */
    @PostMapping(value = "/closeFinanceDay")
    @AuthRequiredPermission("Merchant:Finance:closeFinanceDay")
    public Response closeFinanceDay(HttpServletRequest request, @RequestBody FinanceDayVO financeDayVO) {
        log.info("/order/financeMonth/closeFinanceDay:" + JSON.toJSONString(financeDayVO));
        return financeService.closeFinanceDay(financeDayVO);
    }

    /**
     * 重启对账单 - 日账单
     */
    @PostMapping(value = "/rebootFinanceDay")
    @AuthRequiredPermission("Merchant:Finance:rebootFinanceDay")
    public Response rebootFinanceDay(HttpServletRequest request, @RequestBody FinanceDayVO financeDayVO) {
        if (StringUtils.isEmpty(financeDayVO.getStartTime()) || StringUtils.isEmpty(financeDayVO.getEndTime())) {
            return Response.returnFail("参数异常！");
        }
        log.info("/order/financeMonth/rebootFinanceDay:" + JSON.toJSONString(financeDayVO));
        return financeService.rebootFinanceDay(financeDayVO);
    }
}
