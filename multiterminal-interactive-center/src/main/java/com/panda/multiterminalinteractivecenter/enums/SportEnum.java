package com.panda.multiterminalinteractivecenter.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author miller
 * @desc 赛种枚举
 */
public enum SportEnum {

    /**
     * 足球
     */
    SPORT_FOOTBALL(1L, "足球", "SOCCER"),
    SPORT_DOTA2(101L, "dota2","dota2"),
    SPORT_LOL(100L, "英雄联盟","lol"),
    SPORT_KOG(103L, "王者荣耀","hok"),
    SPORT_CSGO(102L, "CSGO","csgo"),



    VIRTUAL_SPORT_FOOTBALL(1001L, "虚拟足球", "VIRTUAL SOCCER"),
    VIRTUAL_SPORT_BASKETBALL(1004L, "虚拟蓝球", "VIRTUAL_BASKETBALL"),

    /**
     * 篮球
     */
    SPORT_BASKETBALL(2L, "篮球", "BASKETBALL"),

    /**
     * 网球
     */
    SPORT_TENNIS(5L, "网球", "TENNIS"),

    /**
     * 羽毛球
     */
    SPORT_BADMINTON(10L, "羽毛球", "BADMINTON"),

    /**
     * 乒乓球
     */
    SPORT_PINGPONG(8L, "乒乓球", "TABLE TENNIS"),

    /**
     * 斯诺克
     */
    SPORT_SNOOKER(7L, "斯诺克", "SNOOKER"),

    /**
     * 冰球
     */
    SPORT_ICEHOCKEY(4L, "冰球", "ICEHOCKEY"),

    /**
     * 棒球
     */
    SPORT_BASEBALL(3L, "棒球", "BASEBALL"),

    /**
     * 排球
     */
    SPORT_VOLLEYBALL(9L, "排球", "VOLLEYBALL"),

    /**
     * 美式足球
     */
    SPORT_USEFOOTBALL(6L, "美式足球", "FOOTBALL"),
    /**
     * 手球
     */
    SPORT_HANDBALL(11L, "手球", "Handball"),

    /**
     * 拳击
     */
    SPORT_BOXING(12L, "拳击", "Boxing"),

    /**
     * 沙滩排球
     */
    SPORT_BEACH_VOLLEYBALL(13L, "沙滩排球", "SandVolleball"),



    /**
     * 水球 water polo
     */
    SPORT_WATER_POLO(16L, "水球","Water Polo"),

    /**
     * 曲棍球 hockey
     */
    SPORT_HOCKEY_BALL(15L, "曲棍球","Hockey ball"),

    /**
     * 英式橄榄球 Rugby
     */
    SPORT_ENGLAND_RUGBY_BALL(14L, "英式橄榄球","England rugby ball"),
    ;


    private Long key;

    private String value;
    private String en;

    /**
     * 获取运动类型
     *
     * @param sportId
     * @return
     */
    public static SportEnum getSportEnum(Long sportId) {
        for (SportEnum e : values()) {
            if (e.key.equals(sportId)) {
                return e;
            }
        }
        return null;
    }


    SportEnum(Long key, String value, String en) {
        this.key = key;
        this.value = value;
        this.en = en;
    }

    public static String getNameById(String id) {
        if(StringUtils.isBlank(id)){
            return "UnKnown";
        }
        for(SportEnum sportEnum : SportEnum.values()){
            if(sportEnum.key.equals(Long.valueOf(id))){
                return sportEnum.getEn();
            }
        }
        return "UnKnown";
    }

    public Long getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getEn() {
        return en;
    }
}
