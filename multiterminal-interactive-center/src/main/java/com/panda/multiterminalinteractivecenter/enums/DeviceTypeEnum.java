package com.panda.multiterminalinteractivecenter.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.enums
 * @Description :  设备类型
 * @Date: 2022-03-16 13:17:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public enum DeviceTypeEnum {

    PC(1),
    H5(2),
    APP(3);

    private Integer code;

    private DeviceTypeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private static final Map<Integer,DeviceTypeEnum> ENUM_CODE_MAP = new HashMap<>();
    static {
        for(DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()){
            ENUM_CODE_MAP.put(deviceTypeEnum.getCode(),deviceTypeEnum);
        }
    }

    public static DeviceTypeEnum getByCode(Integer code){
        return ENUM_CODE_MAP.get(code);
    }
}
