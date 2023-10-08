package com.panda.sport.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.bss.CurrencyRatePO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.FinanceDayVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceBillMonthVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.manage.service.impl.AbstractFinanceService;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.FinanceService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service
public class FinanceServiceImpl extends AbstractFinanceService implements FinanceService {

    @Autowired
    private MerchantReportClient rpcClient;

    private static Map<String, Object> paramMap = new HashMap<>();

    private static final String CURRENCY_INFO = "currencyInfo";

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 查询帐务月报
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response queryFinanceMonthList(MerchantFinanceMonthVo monthVo) {
        if (monthVo.getParentId() == null) {
            List<Integer> list = new ArrayList<>();
            list.add(0);
            list.add(1);
            monthVo.setAgentLevelList(list);
        }
        return rpcClient.queryFinanceMonthList(monthVo);
    }

    /**
     * 查询帐务月报总计
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response queryFinanceMonthTotal(MerchantFinanceMonthVo monthVo) {
        if (monthVo.getParentId() == null) {
            List<Integer> list = new ArrayList<>();
            list.add(0);
            list.add(1);
            monthVo.setAgentLevelList(list);
        }
        return rpcClient.queryFinanceMonthTotal(monthVo);
    }

    /**
     * 查询帐务月报详情
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response queryFinanceMonthDetail(MerchantFinanceMonthVo monthVo) {
        return rpcClient.queryFinanceMonthDetail(monthVo);
    }

    /**
     * 修改帐务金额
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response updateFinanceMonthDetail(MerchantFinanceMonthVo monthVo) {
        return rpcClient.updateFinanceMonthDetail(monthVo);
    }

    /**
     * 获取帐务操作列表
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response getFinanceOperateRecordList(String financeId) {
        return rpcClient.getFinanceOperateRecordList(financeId);
    }

    @Override
    public Response queryFinanceayTotalList(MerchantFinanceDayVo dayVo) {
        try {
            return rpcClient.queryFinanceayTotalList(dayVo);
        } catch (Exception e) {
            log.error("queryFinanceDayList:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 获取电子账单日报
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response queryFinanceDayList(MerchantFinanceDayVo dayVo) {
        try {
            return rpcClient.queryFinanceDayList(dayVo);
        } catch (Exception e) {
            log.error("queryFinanceDayList:", e);
            return Response.returnFail("查询异常!");
        }
    }

    @Override
    public Response queryFinanceDayListV2(MerchantFinanceDayVo dayVo) {
        try {
            return rpcClient.queryFinanceDayListV2(dayVo);
        } catch (Exception e) {
            log.error("queryFinanceDayListV2:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 获取电子账单日报汇总
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response queryFinanceDayTotal(MerchantFinanceDayVo dayVo) {
        return rpcClient.queryFinanceDayTotal(dayVo);
    }

    /**
     * 获取电子账单日报统计
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Response queryFinanceMonthCount(MerchantFinanceMonthVo monthVo) {
        return rpcClient.queryFinanceMonthCount(monthVo);
    }

    /**
     * 导出电子账单
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    public Map<String, Object> financeDayExport(String merchantCode,String username, MerchantFinanceDayVo dayVo) {
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

    /**
     * 导出月报
     *
     * @Param: [monthVo]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 15:04
     */
    @Override
    
    public void financeMonthExport(HttpServletResponse response, HttpServletRequest request, String id, String language) {
        try {
            MerchantFinanceMonthVo vo = rpcClient.financeMonthExportQuery(id);
            if (vo == null) {
                return;
            }
            String fileName;
            if (language.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
                fileName = "商户-" + vo.getMerchantName() + "月电子账单.csv";
            } else {
                fileName = "merchant-" + vo.getMerchantName() + "monthbill.csv";
            }
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            FileCopyUtils.copy(exportFinanceMonthCsv(vo, language), response.getOutputStream());
        } catch (Exception e) {
            log.error("导出月财务报表异常!", e);
        }
    }

    @Override
    public Response closeFinanceDay(FinanceDayVO financeDayVO) {
        try{
            return rpcClient.closeFinanceDay(financeDayVO);
        }catch (Exception ex){
            log.error("关闭对账单失败:",ex);
        }
        return Response.returnFail("关闭对账单失败!");
    }

    @Override
    public Response rebootFinanceDay( FinanceDayVO financeDayVO) {
        try{
            return rpcClient.rebootFinanceDay(financeDayVO);
        }catch (Exception ex){
           log.error("重启对账单失败:",ex);
        }
        return Response.returnFail("重启对账单失败!");
    }

    /**
     * 导出用户到csv文件
     *
     * @param monthVo  导出的数据
     * @param language
     */
    private byte[] exportFinanceMonthCsv(MerchantFinanceMonthVo monthVo, String language) {
        List<MerchantFinanceBillMonthVo> billMonthVos = monthVo.getBillMonthVoList();
        int billSize = CollectionUtils.isEmpty(billMonthVos) ? 0 : billMonthVos.size();
        int size = billSize + 5;
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(size);
        List<CurrencyRatePO> currencyRatePOS = (List<CurrencyRatePO>) paramMap.get(CURRENCY_INFO);
        if (CollectionUtils.isEmpty(currencyRatePOS)) {
            currencyRatePOS = orderMixMapper.queryCurrencyRateList();
            paramMap.put(CURRENCY_INFO, currencyRatePOS);
        }

        Map<String, CurrencyRatePO> rateMap = currencyRatePOS.stream().collect(Collectors.toMap(CurrencyRatePO::getCurrencyCode, a -> a, (k1, k2) -> k1));
        for (int i = 0; i < size; i++) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            if (i == 0) {
                rowData.put("1", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "投注分成" : "Bet");
                rowData.put("2", (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "总计-" : "Total") + (rateMap.get(monthVo.getCurrency()) == null ? "Unknown" : rateMap.get(monthVo.getCurrency()).getCountryCn()));
                rowData.put("3", monthVo.getOrderAmountTotal());
                rowData.put("4", monthVo.getOrderNum());
                rowData.put("5", monthVo.getProfitAmount());
                rowData.put("6", monthVo.getOrderPaymentAmount());

            }
            if (i > 0 && i <= billSize) {
                rowData.put("1", "");
                rowData.put("2", rateMap.get(billMonthVos.get(i - 1).getCurrency()) == null ? "Unknown" : rateMap.get(billMonthVos.get(i - 1).getCurrency()).getCountryCn());
                rowData.put("3", billMonthVos.get(i - 1).getBillOrderAmount());
                rowData.put("4", billMonthVos.get(i - 1).getBillOrderNum());
                rowData.put("5", billMonthVos.get(i - 1).getBillProfitAmount());
                rowData.put("6", "");
            }
            if (i == billSize + 1) {
                rowData.put("1", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "会员" : "member");
                rowData.put("2", rateMap.get(monthVo.getCurrency()) == null ? "Unknown" : rateMap.get(monthVo.getCurrency()).getCountryCn());
                rowData.put("3", "-");
                rowData.put("4", "-");
                rowData.put("5", "-");
                rowData.put("6", monthVo.getVipAmount());
            }
            if (i == billSize + 2) {
                rowData.put("1", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "技术支持费" : "techSupport");
                rowData.put("2", rateMap.get(monthVo.getCurrency()) == null ? "Unknown" : rateMap.get(monthVo.getCurrency()).getCountryCn());
                rowData.put("3", "-");
                rowData.put("4", "-");
                rowData.put("5", "-");
                rowData.put("6", monthVo.getTechniqueAmount());
            }
            if (i == billSize + 3) {
                rowData.put("1", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "调整金额" : "Adjusted Amount");
                rowData.put("2", rateMap.get(monthVo.getCurrency()) == null ? "Unknown" : rateMap.get(monthVo.getCurrency()).getCountryCn());
                rowData.put("3", "");
                rowData.put("4", "");
                rowData.put("5", "");
                rowData.put("6", monthVo.getAdjustAmount());

            }
            if (i == billSize + 4) {
                rowData.put("1", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "总计：" : "Total:");
                rowData.put("2", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? changeMoneyChar(String.valueOf(monthVo.getBillAmount())) : String.valueOf(monthVo.getBillAmount()));
                rowData.put("3", "");
                rowData.put("4", "");
                rowData.put("5", "");
                rowData.put("6", monthVo.getBillAmount());
            }
            exportData.add(rowData);
        }

        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "科目" : "Subject");
        header.put("2", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "币种" : "Currency");
        header.put("3", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "本期投注额" : "CurrentBetAmount");
        header.put("4", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "本期注单笔数" : "CurrentTickets");
        header.put("5", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "本期盈利" : "CurrentProfit");
        header.put("6", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "应缴费用" : "Cost");

        return CsvUtil.exportCSV(header, exportData);
    }


    /**
     * 截取金额的整数部分，并将其转换成中文大写格式
     */
    public static String changeMoneyChar(String s) {
        StringBuilder moneyChar = new StringBuilder();
        //以“.”将ss分成两段,part[0]为整数部分，part[1]为小数部分
        String[] part = s.split("\\.");
        if (part[0].length() > 10) { // 超出可转换位数
            return "";
        }
        for (int i = 0; i < part[0].length(); i++) {
            char perchar = part[0].charAt(i);
            if (perchar == '0') {
                moneyChar.append("零");
            }
            if (perchar == '1') {
                moneyChar.append("壹");
            }
            if (perchar == '2') {
                moneyChar.append("贰");
            }
            if (perchar == '3') {
                moneyChar.append("叁");
            }
            if (perchar == '4') {
                moneyChar.append("肆");
            }
            if (perchar == '5') {
                moneyChar.append("伍");
            }
            if (perchar == '6') {
                moneyChar.append("陆");
            }
            if (perchar == '7') {
                moneyChar.append("柒");
            }
            if (perchar == '8') {
                moneyChar.append("捌");
            }
            if (perchar == '9') {
                moneyChar.append("玖");
            }
            int j = part[0].length() - i - 1;
            if (j == 0) {
                moneyChar.append("圆");
            }
            if (j == 1 && perchar != 0) {
                moneyChar.append("拾");
            }
            if (j == 2 && perchar != 0) {
                moneyChar.append("佰");
            }
            if (j == 3 && perchar != 0) {
                moneyChar.append("仟");
            }
            if (j == 4 && perchar != 0) {
                moneyChar.append("万");
            }
            if (j == 5 && perchar != 0) {
                moneyChar.append("拾");
            }
            if (j == 6 && perchar != 0) {
                moneyChar.append("佰");
            }
            if (j == 7 && perchar != 0) {
                moneyChar.append("仟");
            }
            if (j == 8 && perchar != 0) {
                moneyChar.append("亿");
            }
            if (j == 9 && perchar != 0) {
                moneyChar.append("拾");
            }
        }
        return moneyChar.toString();
    }
}
