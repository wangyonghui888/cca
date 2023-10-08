package com.panda.sport.merchant.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 时间操作工具类
 * @Date: 2019-08-29 下午2:31:09
 */
public final class TimeUtils {
    public final static String DF_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String DF_PATTERN_YYYY_MM = "yyyy-MM";
    public final static String DF_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private TimeUtils() {

    }

    /**
     * 一天之内之间相减
     * @param start_time
     * @param end_time
     * @return
     * @throws ParseException
     */
    public static String subtractTime(String start_time,String end_time) throws ParseException {// fixme 限于一天之内之间相减
        if(start_time != null && end_time != null && end_time.compareTo(start_time) > 0 && start_time.matches("^\\d{2,3}:[0-5][0-9](:[0-5][0-9])?") && end_time.matches("^\\d{2,3}:[0-5][0-9](:[0-5][0-9])?")) {
            if(!start_time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
                start_time = start_time + ":00";
            }
            if(!end_time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
                end_time = end_time + ":00";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
            long result = sdf.parse(end_time).getTime() - sdf.parse(start_time).getTime() + sdf1.parse("1970").getTime();//加上1970年
            Date d1 = new Date(result);
            return sdf.format(d1).toString();
        }
        return null;
    }

    /**
     * 给时间增加或减少几个月
     * @param fromDate
     * @param month
     * @return
     */
    public static Date getDateAfterMonth(Date fromDate, int month) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    /**
     * 给时间增加或减少几小时
     * @param fromDate
     * @param hours
     * @return
     */
    public static Date getDateAfterHours(Date fromDate, int hours) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    /**
     * 给时间增加或减少几分钟
     * @param fromDate
     * @param minutes
     * @return
     */
    public static Date getDateAfterMinutes(Date fromDate, int minutes) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    /**
     * 给时间增加或减少几秒
     * @param fromDate
     * @param seconds
     * @return
     */
    public static Date getDateAfterSeconds(Date fromDate, int seconds) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * 日期转字符串
     * @param d  日期
     * @param pattern  字符串模式
     * @return
     */
    public static String dateToEngString(Date d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(d);
    }

    /**
     * 字符串转日期
     * @param dateStr 日期字符串
     * @param pattern  字符串模式
     * @return
     */
    public static Date engStringToDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 只根据日期计算两个日期间相隔的天数，忽略时分秒
     *
     * @param earlyDate
     * @param laterDate
     * @return
     */
    public static int calcDayInterval(Date earlyDate, Date laterDate) {
        if (earlyDate == null || laterDate == null) {
            return -1;
        }

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(laterDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long later = c.getTimeInMillis();
        c.setTime(earlyDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long early = c.getTimeInMillis();
        return Math.abs(Integer.parseInt(String.valueOf((later - early) / (1000 * 3600 * 24))));
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的字符串
     *
     * @param date
     * @return
     */
    public static String getSimpleFullEngString(Date date) {
        return dateToEngString(date, DF_PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 返回yyyy-MM-dd格式的字符串
     *
     * @param date
     * @return
     */
    public static String getSimpleDateEngString(Date date) {
        return dateToEngString(date, DF_PATTERN_YYYY_MM_DD);
    }


    /**
     * 返回yyyy-MM格式的字符串
     *
     * @param date
     * @return
     */
    public static String getSimpleMonthEngString(Date date) {
        return dateToEngString(date, "yyyyMM");
    }

    public static String getSimpleDay(Date date) {
        return dateToEngString(date, "yyyyMMdd");
    }



    /**
     * 获取若干天之前或之后的日期
     * @param fromDate  日期开始时间
     * @param date  天数
     * @return
     */
    public static Date getDateAfterDate(Date fromDate, int date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.DATE, date);
        return c.getTime();
    }


    /**
     * 时间相加（不存在时间越界问题，但是不能超过1周的总小时数(168小时)，代码可以修改这个限制）
     * @param start_time
     * @param end_time
     * @return
     * @throws ParseException
     */
    public static String addTime(String start_time,String end_time) throws ParseException{
        if(start_time != null && end_time != null && start_time.matches("^\\d{2,3}:[0-5][0-9](:[0-5][0-9])?") && end_time.matches("^\\d{2,3}:[0-5][0-9](:[0-5][0-9])?")) {
            if(!start_time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
                start_time = start_time + ":00";
            }
            if(!end_time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
                end_time = end_time + ":00";
            }

            String[] startTime = start_time.split(":");
            String[] endTime = end_time.split(":");
            int startTimeSeconds = 0;
            int endTimeSeconds = 0;
            int countSeconds = 0;

            startTimeSeconds = timeToSec(start_time);
            endTimeSeconds = timeToSec(end_time);

            countSeconds = startTimeSeconds + endTimeSeconds;
            return secToTime(countSeconds);
        }
        return null;
    }

    /**
     *限于一天之内之间相加，存在时间越界问题
     * @param start_time
     * @param end_time
     * @return
     * @throws ParseException
     */
    public static String addTime2(String start_time,String end_time) throws ParseException{
        if(start_time != null && end_time != null && start_time.matches("^\\d{2,3}:[0-5][0-9](:[0-5][0-9])?") && end_time.matches("^\\d{2,3}:[0-5][0-9](:[0-5][0-9])?")) {
            if(!start_time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
                start_time = start_time + ":00";
            }
            if(!end_time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
                end_time = end_time + ":00";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
            long result = sdf.parse(end_time).getTime() + sdf.parse(start_time).getTime() - sdf1.parse("1970").getTime();//减去1970年
            Date d1 = new Date(result);
            return sdf.format(d1).toString();
        }
        return null;
    }

    /**
     * 时分秒格式(xx:xx:xx)转换为整数(秒数)
     * @param time
     * @return
     */
    public static int timeToSec(String time) {
        int seconds = 0;
        if(!time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
            return 0;
        }
        String[] timeArray = time.split(":");
        if(timeArray.length == 3) {
            seconds += Long.valueOf(timeArray[0])*60*60;
            seconds += Long.valueOf(timeArray[1])*60;
            seconds += Long.valueOf(timeArray[2]);
        }
        return seconds;
    }

    /**
     * 时分秒格式(xx:xx:xx)转换为小时数，忽略秒（例如12:30:00转换为12.5小时）
     * @param time
     * @return
     */
    public static Double timeToHour(String time) {
        String[] timeArray = time.split(":");
        if(!time.matches("^\\d{2,3}:[0-5][0-9]:[0-5][0-9]")){
            return 0d;
        }
        Double hour = Double.valueOf(timeArray[0]);
        Double minute = Double.valueOf(timeArray[1]) / 60;

        return hour + minute;
    }



    /**
     * 整数(秒数)转换为时分秒格式(xx:xx:xx)
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            }
            else {
                if (hour > 168){
                    return "168:00:00";
                }
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 判断两个时间段是否有重叠
     * @param beginDate1 时间段1 开始时间
     * @param endDate1 时间段1 结束时间
     * @param beginDate2 时间段2 开始时间
     * @param endDate2 时间段2 结束时间
     * @param dateformat 时间格式   HH:mm:ss或HH:mm
     * @return true 存在重叠
     * false 不重叠
     */

    public static Boolean isOverLaps(String beginDate1, String endDate1, String beginDate2, String endDate2, String dateformat){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1b = sdf.parse(beginDate1);
            Date date1e = sdf.parse(endDate1);
            Date date2b = sdf.parse(beginDate2);
            Date date2e = sdf.parse(endDate2);

            if(date2b.compareTo(date1e)*date2e.compareTo(date1b)>0) {
                return false;
            } else{//判断是否是时间交接点:08:00-12:00 12:00-15:00
                return !(date2b.equals(date1e) || date2e.equals(date1b));
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getSimpleDayBy() {
        Date date = new Date();
        if(date.getHours()<12){
            date =getDateAfterDate(date, -1);
        }
        return dateToEngString(date, "yyyyMMdd");
    }
}
