package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Joken
 * @Description: TODO
 * @Date: 2019/11/11 16:24
 * @Return:
*/
public enum MarketStatusEnum {

    ACTIVE(0,"active"),
    SUSPENDED(1,"suspended"),
    DEACTIVATED(2,"deactivated"),
    SETTLED(3,"settled"),
    CANCELLED(4,"cancelled"),
    HANDED_OVER(5,"handedOver"),
    ;

    private Integer status;
    private String  desc;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    MarketStatusEnum(Integer status, String  desc){

        this.status = status;
        this.desc = desc;
    }

    private static Map<Integer,MarketStatusEnum> marketStatusMap = new HashMap();

    /**
     * 根据状态码获取对象
     * @param states
     * @return
     */
    public static MarketStatusEnum getMarketStatusEnum(Integer states){
        if(marketStatusMap != null && marketStatusMap.size() > 0){
            MarketStatusEnum result = marketStatusMap.get(states);
            if(result == null){
                for(MarketStatusEnum obj : values()){
                    marketStatusMap.put(obj.getStatus(),obj);
                }
            }
        }else{
            for(MarketStatusEnum obj : values()){
                marketStatusMap.put(obj.getStatus(),obj);
            }
        }
        return marketStatusMap.get(states);
    }
}
