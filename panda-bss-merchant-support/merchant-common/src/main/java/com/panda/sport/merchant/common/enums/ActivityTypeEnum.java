package com.panda.sport.merchant.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum ActivityTypeEnum {
    ALL(1, "全部活动"),
    Blind_box_activity(2, "幸运盲盒"),
    Daily_task(3, "每日任务"),
    Growth_task(4, "成长任务"),
    TIGER_DRAW_task(5, "老虎机活动");
    private Integer code;
    private String describe;

    ActivityTypeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    public static String getDescByCode(Integer code) {
        for (ActivityTypeEnum activityTypeEnum : values()) {
            if (activityTypeEnum.getCode().equals(code)) {
                return activityTypeEnum.getDescribe();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String describe) {
        for (ActivityTypeEnum activityTypeEnum : values()) {
            if (activityTypeEnum.getDescribe().equals(describe)) {
                return activityTypeEnum.getCode();
            }
        }
        return 0;
    }

    public static List<Integer> removeRepeat(List<Integer> list) {
        List<Integer> list1 = new ArrayList<>();
        for (ActivityTypeEnum activityTypeEnum : values()) {
            if (activityTypeEnum.getCode() != 1) {
                list1.add(activityTypeEnum.getCode());
            }
        }
        if (null != list) {
            list1.removeAll(list);
        }
        return list1;
    }

    public static List<Integer> removeOtherList(List<String> list, List<Integer> activity) {
        List<Integer> list2 = new ArrayList<>();
        List<Integer> mylist = new ArrayList<>();
        list.forEach(e -> {
            mylist.add(Integer.valueOf(e));
        });
        list2.addAll(activity);
        list2.addAll(mylist);
        list2.removeAll(mylist);
        return list2;
    }

    public static List<Integer> removeOther(List<Integer> list, List<String> activity) {
        List<Integer> mylist = new ArrayList<>();
        activity.forEach(e -> {
            mylist.add(Integer.valueOf(e));
        });
        mylist.addAll(list);
        mylist.removeAll(list);
        return mylist;
    }
}
