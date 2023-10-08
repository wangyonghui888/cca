package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  sklee
 * @Project Name :
 * @Package Name :
 * @Description : 赛事状态枚举
 * @Date: 2019-09-18 13:12
 */
public enum MatchStatusEnum {
    not_started(0,"赛事未开始"),
    live(1,"滚球阶段"),
    suspended(2,"暂停"),
    ended(3,"结束"),
    closed(4,"关闭"),
    cancelled(5,"取消"),
    abandoned(6,"比赛放弃"),
    delayed(7,"延迟"),
    unknown(8,"未知"),
    postponed(9,"延期"),
    interrupted(10,"比赛中断"),
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
    MatchStatusEnum(Integer status, String  desc){

        this.status = status;
        this.desc = desc;
    }

    private static Map<Integer,MatchStatusEnum> matchStatusMap = new HashMap();

    /**
     * 根据状态码获取对象
     * @param matchStatus
     * @return
     */
    public static MatchStatusEnum getMatchStatusEnum(Integer matchStatus){
        if(matchStatusMap != null && matchStatusMap.size() > 0){
            MatchStatusEnum result = matchStatusMap.get(matchStatus);
            if(result == null){
                for(MatchStatusEnum obj : values()){
                    matchStatusMap.put(obj.getStatus(),obj);
                }
            }
        }else{
            for(MatchStatusEnum obj : values()){
                matchStatusMap.put(obj.getStatus(),obj);
            }
        }
        return matchStatusMap.get(matchStatus);
    }

}
