package com.panda.sport.merchant.common.utils;

import com.panda.sport.merchant.common.enums.MarketTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author :  christion
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.common.utils
 * @Description :  赔率工具类
 * @Date: 2019-10-11 19:50
 */
public class OddsCalculateUtils {

    /**
     * 赔率转化公共方法
     *
     * @param oddsValue
     * @return String
     */
    public static String oddsChange(String oddsValue) {
        //判断是否为空
        if (StringUtil.isNotBlank(oddsValue)) {
            //是否有+标识
            boolean add = false;
            //是否有+标识
            boolean mul = false;
            //包含有25或者有75投注项时需要转化
            if (oddsValue.endsWith("25") || oddsValue.endsWith("75")) {
                String newValue = "";
                //带+ - 判断
                if (oddsValue.startsWith("+")) {
                    add = true;
                    newValue = oddsValue.replace("+", "");
                } else if (oddsValue.startsWith("-")) {
                    mul = true;
                    newValue = oddsValue.replace("-", "");
                } else {
                    newValue = oddsValue;
                }
                //拆分成数组
                String[] attr = newValue.split("\\.");
                //不同类型不同转化
                if (attr != null && attr.length > 1) {
                    if ("25".equals(attr[1])) {
                        newValue = attr[0] + "/" + attr[0] + ".5";
                    } else if ("75".equals(attr[1])) {
                        newValue = attr[0] + ".5" + "/" + (Integer.valueOf(attr[0]) + 1);
                    }
                } else {
                    newValue = oddsValue;
                }

                if (add) {
                    return "+" + newValue;
                } else if (mul) {
                    return "-" + newValue;
                } else {
                    return newValue;
                }
            }
            //无需转化直接返回
            return oddsValue;
        } else {
            return oddsValue;
        }
    }


    /**
     * 计算投注时扣除金额
     *
     * @param betMoney
     * @param marketType
     * @param oddsFinally
     * @return
     */
    public static Double oddsDeductionMoney(Double betMoney, MarketTypeEnum marketType, String oddsFinally) {
        //如果是印尼或者马来盘小于0的赔率则分块计算
        if (marketType == MarketTypeEnum.ID && Double.valueOf(oddsFinally) < 0) {
            return Math.abs(ArithmeticUtils.mul(betMoney, Double.valueOf(oddsFinally)));
        } else if (marketType == MarketTypeEnum.MY && Double.valueOf(oddsFinally) < 0) {
            return Math.abs(ArithmeticUtils.mul(betMoney, Double.valueOf(oddsFinally)));
        } else {
            return betMoney;
        }
    }
}
