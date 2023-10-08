package com.panda.sport.merchant.common.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author :  valar
 * @Project Name :
 * @Package Name :
 * @Description : 盘口
 * @Date: 2019-10-02 19:10
 */
public enum MarketTypeEnum {

    EU("EU", "欧盘", 1),
    HK("HK", "香港盘", 2),
    US("US", "美式盘", 3),
    ID("ID", "印尼盘", 4),
    MY("MY", "马来盘", 5),
    GB("GB", "英式盘", 6);

    private static Map<String, MarketTypeEnum> marketTypeMap = new HashMap<>();
    private static Map<String, MarketTypeEnum> marketTypeMapByType = new HashMap<>();

    private static Map<Integer, MarketTypeEnum> marketTypeMapByCode = new HashMap<>();
    private String marketType;

    private String desc;

    private Integer code;

    MarketTypeEnum(String marketType, String desc, int code) {
        this.marketType = marketType;
        this.desc = desc;
        this.code = code;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    //通过盘口描述查询对应枚举对象
    public static MarketTypeEnum getMarketTypeEnumByDesc(String desc) {
        if (marketTypeMap == null && marketTypeMap.size() == 0) {
            for (MarketTypeEnum obj : values()) {
                marketTypeMap.put(obj.getMarketType(), obj);
            }
        }
        if (marketTypeMap.get(desc) == null) {
            for (MarketTypeEnum obj : values()) {
                marketTypeMap.put(obj.getMarketType(), obj);
            }
        }
        return marketTypeMap.get(desc);
    }

    //通过code码查询对应枚举对象
    public static MarketTypeEnum getMarketTypeEnumByCode(Integer code) {
        if (marketTypeMapByCode == null && marketTypeMapByCode.size() == 0) {
            for (MarketTypeEnum obj : values()) {
                marketTypeMapByCode.put(obj.getCode(), obj);
            }
        }
        if (marketTypeMapByCode.get(marketTypeMapByCode) == null) {
            for (MarketTypeEnum obj : values()) {
                marketTypeMapByCode.put(obj.getCode(), obj);
            }
        }
        return marketTypeMapByCode.get(code);
    }
}
