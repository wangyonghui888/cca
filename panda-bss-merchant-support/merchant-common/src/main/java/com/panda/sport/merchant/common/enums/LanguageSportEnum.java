package com.panda.sport.merchant.common.enums;

/**
 * 国际化体种
 */
public enum LanguageSportEnum {

    /**
     * 足球
     */
    SPORT_FOOTBALL(1L, "足球","Football","足球"),

    /**
     * 篮球
     */
    SPORT_BASKETBALL(2L, "篮球","Basketball","籃球"),

    /**
     * 网球
     */
    SPORT_TENNIS(5L, "网球","Tennis","網球"),

    /**
     * 羽毛球
     */
    SPORT_BADMINTON(10L, "羽毛球","Badminton","羽毛球"),

    /**
     * 乒乓球
     */
    SPORT_PINGPONG(8L, "乒乓球","Ping pong","乒乓球"),

    /**
     * 斯诺克
     */
    SPORT_SNOOKER(7L, "斯诺克","Snooker","斯諾克"),

    /**
     * 冰球
     */
    SPORT_ICEHOCKEY(4L, "冰球","IceHockey","冰球"),

    /**
     * 棒球
     */
    SPORT_BASEBALL(3L, "棒球","Baseball","棒球"),

    /**
     * 排球
     */
    SPORT_VOLLEYBALL(9L, "排球","VolleyBall","排球"),

    /**
     * 美式足球
     */
    SPORT_USEFOOTBALL(6L, "美式足球","USA Football","美式足球"),

    /**
     * 政治娱乐
     */
    POLITICS_RECREATION(18L,"政治娱乐","Political Entertainment","政治娛樂");

    private Long key;

    private String value;

    private String enValue;

    private String zhValue;

    /**
     * 获取运动类型
     *
     * @param sportId
     * @return
     */
    public static LanguageSportEnum getSportEnum(Long sportId) {
        for (LanguageSportEnum e : values()) {
            if (e.key.equals(sportId)) {
                return e;
            }
        }
        return null;
    }


    LanguageSportEnum(Long key, String value,String enValue,String zhValue) {
        this.key = key;
        this.value = value;
        this.enValue = enValue;
        this.zhValue = zhValue;
    }

    public Long getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getEnValue() {
        return this.enValue;
    }

    public String getZhValue() {
        return this.zhValue;
    }
}
