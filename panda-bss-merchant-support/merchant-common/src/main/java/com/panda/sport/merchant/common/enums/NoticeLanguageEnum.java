package com.panda.sport.merchant.common.enums;

/**
 * @author yk
 * @desc 多语言枚举类，公告
 */
public enum NoticeLanguageEnum {

    ZS(1,"中文简体","Chinese"),
    EN(2,"英文","English"),
    ZH(3,"中文繁体","Traditional Chinese"),
    JP(4,"日语","Japanese"),
    VI(5,"越语","Vietnamese"),
    TH(6,"泰语","Thai"),
    MA(7,"马来语","Malay"),
    BI(8,"印尼语","Indonesian"),
    KO(9,"韩语","Korean"),
    PT(10,"葡萄牙语","Portuguese"),
    ES(11,"西班牙语","Spanish"),
    MYA(12,"缅甸语","Burmese"),

////    PT(5,"葡萄牙语"),
////    RU(6,"俄罗斯语"),
////    IT(7,"意大利语"),
////    DE(8,"德语")
    ;

    private Integer key;

    private String value;

    private String enValue;

    NoticeLanguageEnum(Integer key, String value, String enValue){
        this.key = key;
        this.value = value;
        this.enValue = enValue;
    }

    public Integer getKey(){
        return this.key;
    }

    public String getValue(){
        return this.value;
    }

    public String getEnValue() {
        return  this.enValue;
    }

    /**
     * 获取类型
     * @param key
     * @return
     */
    public static String getLanguageType(Integer key) {

        for (NoticeLanguageEnum noticeTypeEnum : NoticeLanguageEnum.values()) {
            if (noticeTypeEnum.getKey().equals(key)) {
                return noticeTypeEnum.getValue();
            }
        }
        return "";
    }

}
