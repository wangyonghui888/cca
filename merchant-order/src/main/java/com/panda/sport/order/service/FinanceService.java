package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.FinanceDayVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface FinanceService {
    Response queryFinanceMonthList(MerchantFinanceMonthVo monthVo);
    
    Response queryFinanceMonthTotal(MerchantFinanceMonthVo monthVo);

    Response queryFinanceMonthDetail(MerchantFinanceMonthVo monthVo);

    Response updateFinanceMonthDetail(MerchantFinanceMonthVo monthVo);

    Response getFinanceOperateRecordList(String financeId);

    Response queryFinanceDayList(MerchantFinanceDayVo dayVo);

    Response queryFinanceDayListV2(MerchantFinanceDayVo dayVo);

    Response queryFinanceayTotalList(MerchantFinanceDayVo dayVo);

    Response queryFinanceDayTotal(MerchantFinanceDayVo dayVo);

    Response queryFinanceMonthCount(MerchantFinanceMonthVo monthVo);

    Map<String, Object> financeDayExport(String merchantCode, String username, MerchantFinanceDayVo dayVo);

    void financeMonthExport(HttpServletResponse response, HttpServletRequest request, String id, String language);

    Response closeFinanceDay(FinanceDayVO financeDayVO);

    Response rebootFinanceDay( FinanceDayVO financeDayVO);

}
