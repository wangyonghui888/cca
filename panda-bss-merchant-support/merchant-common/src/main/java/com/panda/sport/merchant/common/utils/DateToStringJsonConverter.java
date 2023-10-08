package com.panda.sport.merchant.common.utils;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 日期转字符
 * @Date: 2019-08-29 下午2:09:57
 */
public class DateToStringJsonConverter implements Converter<Date, String> {

    @Override
    public String convert(Date date) {

        System.out.println("...........DateToStringJsonConverter............................................");
        return DateUtils.convertFormatDateToString(DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, date);
    }

}
