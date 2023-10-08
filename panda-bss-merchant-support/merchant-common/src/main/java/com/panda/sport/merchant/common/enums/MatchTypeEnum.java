package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  valar
 * @Project Name :
 * @Package Name :
 * @Description : 注单中心编码类型枚举
 * @Date: 2019-09-02 19:10
 */
public enum MatchTypeEnum {
    BEFORE_MATCH(1, "早盘赛事"),
    LIVE_MATCH(2, "滚球盘赛事"),
    CHAMPION_MATCH(3, "冠军盘赛事");

    private Integer codeType;
    private String desc;

    MatchTypeEnum(Integer codeType, String desc) {
        this.codeType = codeType;
        this.desc = desc;
    }

    private static Map<Integer,MatchTypeEnum> matchTypeMap = new HashMap();

    /**
     * 根据状态码获取对象
     * @param matchCodeType
     * @return
     */
    public static MatchTypeEnum getMatchTypeEnum(Integer matchCodeType){
        if(matchTypeMap != null && matchTypeMap.size() > 0){
            MatchTypeEnum result = matchTypeMap.get(matchCodeType);
            if(result == null){
                for(MatchTypeEnum obj : values()){
                    matchTypeMap.put(obj.getCodeType(),obj);
                }
            }
        }else{
            for(MatchTypeEnum obj : values()){
                matchTypeMap.put(obj.getCodeType(),obj);
            }
        }
        return matchTypeMap.get(matchCodeType);
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
