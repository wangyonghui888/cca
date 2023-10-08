package com.panda.sport.merchant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserAllowListSourceEnum {

    SOURCE_MERCHANT_ENABLE(0,"启用"),
    SOURCE_MERCHANT_DISABLE(1,"禁用"),
    SOURCE_MERCHANT_VIP(2,"商户vip"),
    SOURCE_TEST_MERCHANT(3,"测试商户"),
    SOURCE_TEST_USER_BY_C(4,"C端测试账号"),
    SOURCE_OTHER(5,"其他"),
        ;

    private Integer code;

    private String source;

    public static Map<Integer,String> getAllSource(){
        Map<Integer, String> mapResult = new HashMap<>();

        Arrays.stream(UserAllowListSourceEnum.values()).forEach(globalExceptionEnum -> {
            mapResult.put(globalExceptionEnum.getCode(), globalExceptionEnum.getSource());
        });
        return mapResult;
    }

    public static Map<Integer,String> getUseSource(){
        Map<Integer, String> mapResult = new HashMap<>();

        Arrays.stream(UserAllowListSourceEnum.values()).forEach(globalExceptionEnum -> {
            if(globalExceptionEnum.getCode()!=0 || globalExceptionEnum.getCode()!= 1){
                mapResult.put(globalExceptionEnum.getCode(), globalExceptionEnum.getSource());
            }
        });
        return mapResult;
    }

    public static String getSourceNameByCode(Integer source){
        String sourceName = "";
        if(source == null || source == 0){
            return sourceName;
        }
        for (UserAllowListSourceEnum userAllowListSourceEnum : values()) {
            if (userAllowListSourceEnum.getCode().equals(source)) {
                return userAllowListSourceEnum.getSource();
            }
        }
        return sourceName;
    }


}
