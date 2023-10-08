package com.panda.sport.merchant.common.utils;

import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import org.apache.commons.lang3.StringUtils;

public class CurrencyUtils {

    public static String convertCurrencyCode(String currencyCode){
        String currencyCn = "";
        if(StringUtils.isNotEmpty(currencyCode)){
            currencyCn = CurrencyTypeEnum.optGetCurrency(currencyCode);
        }
        return currencyCn;
    }
}
