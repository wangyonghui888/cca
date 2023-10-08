package com.panda.sport.admin;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

public class AbnormalOutServiceImplTest {
    public static void main(String[] args) {
        Long abnormalClickTime = 1694922078000L;
        String startTime2 = "2023-09-22" + " 16:00:00";
        Date startTime = StringUtils.isNotBlank(startTime2)? DateUtil.parse(startTime2, DatePattern.NORM_DATETIME_PATTERN):null;
        Long startTimeLong = null != startTime ? startTime.getTime() : null;
        Long current = System.currentTimeMillis();
        if (abnormalClickTime.compareTo(startTimeLong) < 0 && startTimeLong.compareTo(current) < 0) {
            abnormalClickTime = startTimeLong;
        }
        System.out.println(abnormalClickTime);
        System.out.println(current);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println("00:00=="+calendar.getTime()); ;
        System.out.println("00:00=="+calendar.getTime().getTime()); calendar.getTime().getTime();




        // when two objects are different
        Long obj1 = 124L;
        Long obj2 = 167L;
        int compareValue = obj1.compareTo(obj2);

        if (compareValue == 0)
            System.out.println("object1 and object2 are equal");
        else if (compareValue < 0)
            System.out.println("object1 is less than object2");
        else
            System.out.println("object1 is greater than object2");





    }
}
