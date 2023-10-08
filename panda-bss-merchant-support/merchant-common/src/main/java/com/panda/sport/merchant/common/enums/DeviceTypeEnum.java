package com.panda.sport.merchant.common.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.enums
 * @Description: 设备类型枚举
 * @Date: 2019/10/9 19:42
 * @Version: 1.0
 */
public enum DeviceTypeEnum  {

    COMPUTER(0, "Computer"),
    MOBILE(1, "Mobile"),
    TABLET(2, "Tablet"),
    GAME_CONSOLE(3, "Game console"),
    DMR(4, "Digital media receiver"),
    WEARABLE(5, "Wearable computer"),
    UNKNOWN(6, "Unknown");

    private Integer code;
    private String describe;

    DeviceTypeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    Map<String,Object> deviceTypeList(){
        Map<String,Object> deviceTypeMap= new HashMap<>();
        DeviceTypeEnum[] deviceTypeEnums = DeviceTypeEnum.values();
        for(DeviceTypeEnum dte : deviceTypeEnums){
            deviceTypeMap.put(dte.getDescribe(),dte.getCode());
        }
        return deviceTypeMap;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
