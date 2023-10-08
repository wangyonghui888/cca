package com.panda.sport.merchant.common.enums;

public enum NoticeTypeEnum {

    SPORT_NOTICE_TYPE(1, "足球赛事"),
    BASKETBALL_NOTICE_TYPE(2, "篮球赛事"),
    TENNIS_NOTICE_TYPE(3, "网球赛事"),
    BADMINTON_NOTICE_TYPE(4, "羽毛球赛事"),
    PP_NOTICE_TYPE(5, "乒乓球赛事"),
    SNOOKER_NOTICE_TYPE(6, "斯诺克赛事"),
    ICEHOCKEY_NOTICE_TYPE(7,"冰球赛事"),
    BASEBALL_NOTICE_TYPE(8,"棒球赛事"),
    VOLLEYBALL_NOTICE_TYPE(9,"排球赛事"),
    USEFOOTBALL_NOTICE_TYPE(10,"美式足球赛事"),
    POLITICS_RECREATION_NOTICE_TYPE(18, "政治娱乐赛事"),
    HANDBALL_NOTICE_TYPE(19, "手球赛事"),
    BOXING_NOTICE_TYPE(20, "拳击赛事"),
    BEACH_VOLLEYBALL_NOTICE_TYPE(21, "沙滩排球赛事"),
    WATER_POLE_NOTICE_TYPE(22, "水球赛事"),
    HOCKEY_NOTICE_TYPE(23, "曲棍球赛事"),
    RUGBY_UNION_NOTICE_TYPE(24, "联合式橄榄球赛事"),
    OTHER_MATCTH_NOTICE_TYPE(60, "其他赛事"),
    ACTIVITY_NOTICE_TYPE(100, "活动公告"),
    MAINTENANCE_NOTICE_TYPE(101, "维护公告"),
    //电竞传入体育公告
    LOL(31, "英雄联盟赛事"),
    DOTA2(32, "Dota2赛事"),
    KoG(33, "王者荣耀赛事"),
    CS_GO(34, "CS:GO赛事"),

    // 体育传入电竞公告
    NBA2K(35, "NBA2K赛事"),
    FIFA(36, "FIFA赛事"),
    ABNORMAL_EVENT_USER(37, "异常赛事用户");

    private Integer code;

    private String describe;

    NoticeTypeEnum(Integer code, String describe){
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getDescribe(){
        return this.describe;
    }

    /**
     * 获取类型
     * @param code
     * @return
     */
    public static String getNoticeType(Integer code) {

        for (NoticeTypeEnum noticeTypeEnum : NoticeTypeEnum.values()) {
            if (noticeTypeEnum.getCode().equals(code)) {
                return noticeTypeEnum.getDescribe();
            }
        }
        return "";
    }

}
