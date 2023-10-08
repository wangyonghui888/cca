package com.panda.sport.admin.service.impl;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.admin.service.FinanceService;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.manage.service.impl.AbstractFinanceService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Service("financeService")
@Slf4j
public class FinanceServiceImpl extends AbstractFinanceService implements FinanceService {


    @Autowired
    private MerchantReportClient reportClient;

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 日对账单导出
     *
     * @param dayVo
     */
    @Override
    public Map<String, Object> financeDayExport(String merchantCode, String username, MerchantFinanceDayVo dayVo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = dayVo.getLanguage();
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "对账单详情导出_" : "Report Center-financeDayExport"
                    , merchantCode, username, JSON.toJSONString(dayVo),
                    language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "财务中心-对账单下载" : "Report Center-financeDayExport", "financeDayReportExportServiceImpl", null);
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Response queryFinanceMonthDetail(MerchantFinanceMonthVo monthVo, String language) {

        Response response = this.reportClient.queryFinanceMonthDetail(monthVo);
        if (response != null && response.getData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            MerchantFinanceMonthVo po = mapper.convertValue(response.getData(), new TypeReference<MerchantFinanceMonthVo>() {
            });
            if (po.getComputingStandardStr() != null &&
                    po.getComputingStandardStr().equalsIgnoreCase("盈利") && language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
                po.setComputingStandardStr("Profit");
            } else if (po.getComputingStandardStr() != null &&
                    po.getComputingStandardStr().equalsIgnoreCase("投注量") && language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
                po.setComputingStandardStr("BetAmount");
            }
            response.setData(po);
        }
        return response;
    }

    @Override
    public Response queryFinanceMonthList(MerchantFinanceMonthVo monthVo) {
        return this.reportClient.queryFinanceMonthList(monthVo);
    }

    @Override
    public Response queryFinanceDayList(MerchantFinanceDayVo dayVo) {
        return this.reportClient.queryFinanceDayList(dayVo);
    }

    @Override
    public Response queryFinanceDayTotal(MerchantFinanceDayVo dayVo) {
        return this.reportClient.queryFinanceDayTotal(dayVo);
    }

    @Override
    public Response queryFinanceMonthCount(MerchantFinanceMonthVo monthVo) {
        return this.reportClient.queryFinanceMonthCount(monthVo);
    }

    @Override
    public Response queryFinanceMonthTotal(MerchantFinanceMonthVo monthVo) {
        return this.reportClient.queryFinanceMonthTotal(monthVo);
    }

    @Override
    public Response queryFinanceDayListV2(MerchantFinanceDayVo dayVo) {
        return this.reportClient.queryFinanceDayListV2(dayVo);
    }

    @Override
    public Response queryFinanceayTotalList(MerchantFinanceDayVo dayVo) {
        return this.reportClient.queryFinanceayTotalList(dayVo);
    }
}
