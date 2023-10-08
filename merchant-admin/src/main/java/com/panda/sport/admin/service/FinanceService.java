package com.panda.sport.admin.service;


import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface FinanceService {


    public Map<String, Object> financeDayExport(String merchantCode, String username, MerchantFinanceDayVo dayVo);

    Response queryFinanceMonthDetail(MerchantFinanceMonthVo monthVo, String language);

    Response queryFinanceMonthList(MerchantFinanceMonthVo monthVo);

    Response queryFinanceDayList(MerchantFinanceDayVo dayVo);

    Response queryFinanceDayTotal(MerchantFinanceDayVo dayVo);

    Response queryFinanceMonthCount(MerchantFinanceMonthVo monthVo);

    Response queryFinanceMonthTotal(MerchantFinanceMonthVo monthVo);


    Response queryFinanceDayListV2(MerchantFinanceDayVo dayVo);

    Response queryFinanceayTotalList(MerchantFinanceDayVo dayVo);


}
