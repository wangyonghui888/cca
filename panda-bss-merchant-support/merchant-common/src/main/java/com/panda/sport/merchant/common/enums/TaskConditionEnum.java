package com.panda.sport.merchant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 开关枚举
 */
@Getter
public enum TaskConditionEnum {

    TASK_CONDITION1(1, "每日投注笔数"),

    TASK_CONDITION2(2, "当日单笔有效投注"),

    TASK_CONDITION3(3, "当日投注注单数"),

    TASK_CONDITION4(4, "完成笔串关玩法"),

    TASK_CONDITION5(5, "当日完成场VR体育赛事"),

    TASK_CONDITION6(6, "注册场馆天数"),

    TASK_CONDITION7(7, "首次投注场馆天数"),

    TASK_CONDITION8(8, "总投注金额"),
    TASK_CONDITION9(9, "总输赢金额"),

    TASK_GROUPING1(1, "本月累计投注天数"),

    TASK_GROUPING2(2, "本周累计有效投注"),

    TASK_GROUPING3(3, "本月累计有效投注"),

    TASK_SYMBOL1(1, ">"),

    TASK_SYMBOL2(2, ">="),

    TASK_SYMBOL3(3, "<"),

    TASK_SYMBOL4(4, "<="),

    TASK_SYMBOL5(5, "="),

    TASK_SYMBOL6(6, "介于")

   ;
    private  Integer code;
    private  String label;



    public static TaskConditionEnum getTaskConditionEnum(Integer code) {
        for (TaskConditionEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    TaskConditionEnum(Integer code, String value) {
        this.code = code;
        this.label = value;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.label;
    }
}
