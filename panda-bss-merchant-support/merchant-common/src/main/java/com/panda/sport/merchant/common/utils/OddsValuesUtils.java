package com.panda.sport.merchant.common.utils;


import com.panda.sport.merchant.common.enums.SeriesTypesEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 赔率计算
 */
@Slf4j
public class OddsValuesUtils {
    //串关对象key
    private final static String SERIES_KEY = "A,B,C,D,E,F,G,H,I,J,K,M,L";


    public static Map<String, String> seriesTotalOddsValues(List<Map<String, Object>> seriesTypeList) {
        if (CollectionUtils.isEmpty(seriesTypeList)) {
            return null;
        }
        try {
            String[] seriesKeys = SERIES_KEY.split(",");
            Map<String, String> result = new HashMap<>();
            Map<String, Double> optionOddsMap = new HashMap<>();
            for (Map<String, Object> tempMap : seriesTypeList) {
                String orderNo = (String) tempMap.get("orderNo");
                Integer seriesType = (Integer) tempMap.get("seriesType");
                List<Double> oddsList = (List<Double>) tempMap.get("oddsList");
                for (int i = 0; i < oddsList.size(); i++) {
                    optionOddsMap.put(seriesKeys[i], oddsList.get(i));
                }
                List<String> series = getSeriesAssemble(new ArrayList<>(optionOddsMap.keySet()));

                SeriesTypesEnum ste = SeriesTypesEnum.getSeriesTypesEnumByCode(seriesType);
                if (ste == null) {
                    return null;
                }
                String[] seriesSum = ste.getDescribe().split("串");
                List<String> seriesGroup;
                if (Integer.parseInt(seriesSum[1]) > 1) {
                    seriesGroup = series;
                } else {
                    seriesGroup = series.stream().filter(s -> s.length() == Integer.parseInt(seriesSum[0])).collect(Collectors.toList());
                }
                double oddsTotalValue = 0;
                String oddsTotalValueStr = "0";
                for (String sg : seriesGroup) {
                    double oddsValue = 1;
                    for (String str : sg.split("")) {
                        oddsValue = ArithmeticUtils.mul(ArithmeticUtils.div1(optionOddsMap.get(str), 1, (Integer) tempMap.get("managerCode") == 4 ? 3 : 2), oddsValue);
                    }
                    oddsValue = new BigDecimal(oddsValue + "").setScale((Integer) tempMap.get("managerCode") == 4 ? 3 : 2, BigDecimal.ROUND_FLOOR).doubleValue();
                    oddsTotalValue = ArithmeticUtils.add(oddsValue, oddsTotalValue);
                    oddsTotalValueStr = new BigDecimal(oddsTotalValue + "").toPlainString();
                }
                StringBuilder strOdds = new StringBuilder(oddsTotalValueStr);
                //电竞保证至少有三位小数
                String tempStr = strOdds.substring(strOdds.indexOf(".") + 1);
                if (tempStr.length() < 3
                        && (Integer) tempMap.get("managerCode") == 4) {
                    for (int i = 3 - tempStr.length(); i > 0; i--) {
                        strOdds.append("0");
                    }
                }
                if (strOdds.toString().contains(".")) {
                    if (strOdds.length() - strOdds.indexOf(".") < 3) {
                        strOdds.append("0");
                    } else {
                        strOdds = new StringBuilder(strOdds.substring(0, strOdds.indexOf(".") + ((Integer) tempMap.get("managerCode") == 4 ? 4 : 3)));
                    }
                }
                result.put(orderNo, strOdds.toString());
                optionOddsMap.clear();
            }
            return result;
        } catch (Exception e) {
            log.error("获取赔率算法异常!", e);
            return null;
        }
    }


    /**
     * @param chs 可能组合的对象
     * @Author: Joken
     * @Description: 计算该串关所有注单组合
     * @Date: 2019/11/14 14:52
     * @Return: java.util.List<java.lang.String>
     */
    public static List<String> getSeriesAssemble(List<String> chs) {
        int len = chs.size();
        int nbits = 1 << len;
        List<String> seriesList = new ArrayList<>();
        for (int i = 0; i < nbits; ++i) {
            int t;
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < len; j++) {
                t = 1 << j;
                if ((t & i) != 0) { // 与运算，同为1时才会是1
                    sb.append(chs.get(j));
                }
            }
            if (sb.length() > 1) {
                seriesList.add(sb.toString());
            }
        }
        return seriesList;
    }
}
