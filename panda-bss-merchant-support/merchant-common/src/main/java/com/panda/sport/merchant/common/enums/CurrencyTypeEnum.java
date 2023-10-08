package com.panda.sport.merchant.common.enums;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  istio
 * @Description : 禁止添加方法
 * 如需添加方法 请在CurrencyUtils 类加
 * @see com.panda.sport.merchant.common.utils.CurrencyUtils
 */
public enum CurrencyTypeEnum {
    RMB("1", "RMB", "人民币"),
    USD("2", "USD", "美元"),
    HKD("3", "HKD", "港元"),
    VND1000("4", "VND1000", "越南盾1000"),
    SGD("5", "SGD", "新加坡"),
    GBP("6", "GBP", "英镑"),
    EUR("7", "EUR", "欧元"),
    BTC("8", "BTC", "比特币"),
    TW("9", "TWD", "台币"),
    AED("15", "AED", "阿联酋迪拉姆"),
    CAD("14", "CAD", "加元"),
    MOP("16", "MOP", "澳门元"),
    DZD("17", "DZD", "阿尔及利亚第纳尔"),
    EGP("19", "EGP", "埃及镑"),
    AUD("13", "AUD", "澳元"),
    KRW("12", "KRW", "韩元"),
    JPY("10", "JPY", "日元"),
    OMR("18", "OMR", "阿曼里亚尔"),
    PHP("11", "PHP", "菲币"),
    RUB("20", "RUB", "俄罗斯卢布"),
    IDR1000("21", "IDR1000", "印尼盾1000"),
    MYR("22", "MYR", "马来西亚林吉特"),

    VND("23", "VND", "越南盾"),

    INR("24", "INR", "印度卢比"),

    IDR("25", "IDR", "印尼盾"),

    THB("26", "THB", "泰铢"),

    BND("27", "BND", "文莱林吉特"),

    CEO("28", "CEO", "测试币"),

    BRL("29", "BRL", "巴西雷亚尔"),

    COP("30", "COP", "哥伦比亚比索"),

    TRY("31", "TRY", "土耳其里拉"),

    MMK("33", "MMK", "緬甸元"),

    AFN("34", "AFN", "阿富汗阿富汗尼"),

    ALL("35", "ALL", "阿尔巴尼亚列克"),

    AMD("36", "AMD", "亚美尼亚德拉姆"),

    ANG("37", "ANG", "荷属安的列斯盾"),

    AOA("38", "AOA", "安哥拉宽扎"),

    ARS("39", "ARS", "阿根廷比索"),

    AWG("40", "AWG", "阿鲁巴弗罗林"),

    AZN("41", "AZN", "亚塞拜然马纳特"),

    BAM("42", "BAM", "波士尼亚赫塞哥维纳可兑换马克"),

    BBD("43", "BBD", "巴贝多元"),

    BDT("44", "BDT", "孟加拉塔卡"),

    BGN("45", "BGN", "保加利亚列弗"),

    BHD("46", "BHD", "巴林戴纳"),

    BIF("47", "BIF", "蒲隆地法郎"),

    BMD("48", "BMD", "百慕达元"),

    BOB("49", "BOB", "玻利维亚诺"),

    BSD("50", "BSD", "巴哈马元"),

    BTN("52", "BTN", "不丹努尔特鲁姆"),

    BWP("53", "BWP", "波札那普拉"),

    BYN("54", "BYN", "白俄罗斯卢布"),

    BZD("55", "BZD", "贝里斯元"),

    CDF("56", "CDF", "刚果法郎"),

    CHF("57", "CHF", "瑞士法郎"),

    CLP("59", "CLP", "智利比索"),

    CNH("60", "CNH", "人民币元（离岸）"),

    CRC("61", "CRC", "哥斯大黎加科郎"),

    CUC("62", "CUC", "古巴披索"),

    CUP("63", "CUP", "古巴比索"),

    CVE("64", "CVE", "维德角埃斯库多"),

    CZK("65", "CZK", "捷克克朗"),

    DJF("66", "DJF", "吉布地法郎"),

    DKK("67", "DKK", "丹麦克朗"),

    DOP("68", "DOP", "多米尼加比索"),

    ERN("69", "ERN", "厄利垂亚奈克法"),

    ETB("70", "ETB", "衣索比亚比尔"),

    FJD("71", "FJD", "斐济元"),

    FKP("72", "FKP", "福克兰群岛镑"),

    GEL("73", "GEL", "乔治亚拉里"),

    GGP("74", "GGP", "根西岛镑"),

    GHS("75", "GHS", "加纳塞地"),

    GIP("76", "GIP", "直布罗陀镑"),

    GMD("77", "GMD", "甘比亚达拉西"),

    GNF("78", "GNF", "几内亚法郎"),

    GTQ("79", "GTQ", "瓜地马拉格查尔"),

    GYD("80", "GYD", "盖亚那元"),

    HNL("81", "HNL", "宏都拉斯伦皮拉"),

    HRK("82", "HRK", "克罗埃西亚库纳"),

    HTG("83", "HTG", "海地古德"),

    HUF("84", "HUF", "匈牙利福林"),

    ILS("85", "ILS", "以色列塞克"),

    IMP("86", "IMP", "曼岛镑"),

    IQD("87", "IQD", "伊拉克戴纳"),

    IRR("88", "IRR", "伊朗里亚尔"),

    ISK("89", "ISK", "冰岛克朗"),

    JEP("90", "JEP", "泽西岛镑"),

    JMD("91", "JMD", "牙买加元"),

    JOD("92", "JOD", "约旦戴纳"),

    KES("93", "KES", "肯亚先令"),

    KGS("94", "KGS", "吉尔吉斯斯坦索姆"),

    KHR("95", "KHR", "柬埔寨瑞尔"),

    KMF("96", "KMF", "葛摩法郎"),

    KPW("97", "KPW", "朝鲜圆"),

    KWD("98", "KWD", "科威特戴纳"),

    KYD("99", "KYD", "开曼群岛元"),

    KZT("100", "KZT", "哈萨克斯坦坚戈"),

    LAK("101", "LAK", "寮国基普"),

    LBP("102", "LBP", "黎巴嫩镑"),

    LKR("103", "LKR", "斯里兰卡卢比"),

    LRD("104", "LRD", "赖比瑞亚元"),

    LSL("105", "LSL", "赖索托洛蒂"),

    LYD("106", "LYD", "利比亚第纳尔"),

    MAD("107", "MAD", "摩洛哥迪尔汗"),

    MDL("108", "MDL", "摩尔多瓦列伊"),

    MGA("109", "MGA", "马达加斯加阿里亚里"),

    MKD("110", "MKD", "马其顿代纳尔"),

    MNT("111", "MNT", "蒙古图格里克"),

    MUR("113", "MUR", "模里西斯卢比"),

    MVR("114", "MVR", "马尔地夫拉菲亚"),

    MWK("115", "MWK", "马拉威克瓦查"),

    MXN("116", "MXN", "墨西哥比索"),

    MZN("117", "MZN", "莫三比克梅蒂卡尔"),

    NAD("118", "NAD", "纳米比亚元"),

    NGN("119", "NGN", "奈及利亚奈拉"),

    NIO("120", "NIO", "尼加拉瓜科多巴"),

    NOK("121", "NOK", "挪威克朗"),

    NPR("122", "NPR", "尼泊尔卢比"),

    NZD("123", "NZD", "纽西兰元"),

    PAB("124", "PAB", "巴拿马巴波亚"),

    PEN("125", "PEN", "秘鲁索尔"),

    PGK("126", "PGK", "巴布亚纽几内亚基那"),

    PKR("127", "PKR", "巴基斯坦卢比"),

    PLN("128", "PLN", "波兰兹罗提"),

    PYG("129", "PYG", "巴拉圭瓜拉尼"),

    QAR("130", "QAR", "卡达里亚尔"),

    RON("131", "RON", "罗马尼亚列伊"),

    RSD("132", "RSD", "塞尔维亚戴纳"),

    RWF("133", "RWF", "卢安达法郎"),

    SAR("134", "SAR", "沙特里亚尔"),

    SBD("135", "SBD", "索罗门群岛元"),

    SCR("136", "SCR", "塞席尔卢比"),

    SDG("137", "SDG", "苏丹镑"),

    SEK("138", "SEK", "瑞典克朗"),

    SHP("139", "SHP", "圣赫伦那镑"),

    SLL("140", "SLL", "狮子山利昂"),

    SOS("141", "SOS", "索马利亚先令"),

    SRD("142", "SRD", "苏利南元"),

    SSP("143", "SSP", "南苏丹镑"),

    STD("144", "STD", "圣多美多布拉"),

    STN("145", "STN", "圣多美和普林西比多布拉"),

    SVC("146", "SVC", "萨尔瓦多科郎"),

    SYP("147", "SYP", "叙利亚镑"),

    SZL("148", "SZL", "史瓦济兰里兰吉尼"),

    TJS("149", "TJS", "塔吉克斯坦索莫尼"),

    TMT("150", "TMT", "土库曼斯坦马纳特"),

    TND("151", "TND", "突尼西亚戴纳"),

    TOP("152", "TOP", "东加潘加"),

    TTD("153", "TTD", "特立尼达和多巴哥元"),

    TZS("154", "TZS", "坦尚尼亚先令"),

    UAH("155", "UAH", "乌克兰格里夫纳"),

    UGX("156", "UGX", "乌干达先令"),

    UYU("157", "UYU", "乌拉圭比索"),

    UZS("158", "UZS", "乌兹别克斯坦索姆"),

    VES("159", "VES", "委内瑞拉玻利瓦尔"),

    VUV("160", "VUV", "万那杜瓦图"),

    WST("161", "WST", "萨摩亚塔拉"),

    XAF("162", "XAF", "中非法郎"),

    XCD("165", "XCD", "东加勒比元"),

    XDR("166", "XDR", "特别提款权"),

    XOF("167", "XOF", "西非法郎"),

    XPF("169", "XPF", "太平洋法郎（franc Pacifique）"),

    YER("171", "YER", "叶门里亚尔"),

    ZAR("172", "ZAR", "南非兰特"),

    ZMW("173", "ZMW", "尚比亚克瓦查"),

    ZWL("174", "ZWL", "辛巴威元A/10"),

    UGX1000("175", "UGX1000", "乌干达先令1000"),

    TZS1000("176", "TZS1000", "坦尚尼亚先令1000"),

    SYP1000("177", "SYP1000", "叙利亚镑1000"),

    RWF1000("178", "RWF1000", "卢安达法郎1000"),

    PYG1000("179", "PYG1000", "巴拉圭瓜拉尼1000"),

    MWK1000("180", "MWK1000", "马拉威克瓦查1000"),

    MNT1000("181", "MNT1000", "蒙古图格里克1000"),

    MMK1000("182", "MMK1000", "缅甸元1000"),

    MGA1000("183", "MGA1000", "马达加斯加阿里亚里1000"),

    LBP1000("184", "LBP1000", "黎巴嫩镑1000"),

    KRW1000("185", "KRW1000", "韩元1000"),

    KPW1000("186", "KPW1000", "朝鲜圆1000"),

    KHR1000("187", "KHR1000", "柬埔寨瑞尔1000"),

    IQD1000("188", "IQD1000", "伊拉克戴纳1000"),

    COP1000("189", "COP1000", "哥伦比亚比索1000"),

    CLP1000("190", "CLP1000", "智利比索1000"),

    CDF1000("191", "CDF1000", "刚果法郎1000"),

    BIF1000("192", "BIF1000", "蒲隆地法郎1000"),

    UZS1000("193", "UZS1000", "乌兹别克斯坦索姆1000"),

    STD1000("194", "STD1000", "圣多美多布拉1000"),

    SLL1000("195", "SLL1000", "狮子山利昂1000"),

    LAK1000("196", "LAK1000", "寮国基普1000"),

    IRR1000("197", "IRR1000", "伊朗里亚尔1000"),

    GNF1000("198", "GNF1000", "几内亚法郎1000"),

    ;

    protected String id;

    protected String value;

    protected String description;


    CurrencyTypeEnum(String id, String value, String description) {
        this.id = id;
        this.value = value;
        this.description = description;

    }

    private static Map<String, CurrencyTypeEnum> currencyTypeMap = new HashMap();

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static String optGetDescription(String id) {
        for (CurrencyTypeEnum e : CurrencyTypeEnum.values()) {
            if (e.getId().equals(id)) {
                return e.getDescription();
            }
        }
        return "";
    }

    public static String optGetCurrency(String id) {
        for (CurrencyTypeEnum e : CurrencyTypeEnum.values()) {
            if (e.getId().equals(id)) {
                return e.getValue();
            }
        }
        return "";
    }

    /**
     * 根据状态码获取对象
     *
     * @param flag
     * @return
     */
    public static CurrencyTypeEnum getCurrencyTypeEnum(String flag) {
        if (currencyTypeMap != null && currencyTypeMap.size() > 0) {
            CurrencyTypeEnum result = currencyTypeMap.get(flag);
            if (result == null) {
                for (CurrencyTypeEnum obj : values()) {
                    currencyTypeMap.put(obj.getId(), obj);
                }
            }
        } else {
            for (CurrencyTypeEnum obj : values()) {
                currencyTypeMap.put(obj.getId(), obj);
            }
        }
        return currencyTypeMap.get(flag) == null ? CurrencyTypeEnum.RMB : currencyTypeMap.get(flag);
    }

    /**
     * 判断币种是否存在
     */
    public static boolean validateCurrency(String currencyCode) {
        if (MapUtils.isNotEmpty(currencyTypeMap)) {
            CurrencyTypeEnum result = currencyTypeMap.get(currencyCode);
            if (result == null) {
                for (CurrencyTypeEnum obj : values()) {
                    currencyTypeMap.put(obj.getId(), obj);
                }
            }
        } else {
            for (CurrencyTypeEnum obj : values()) {
                currencyTypeMap.put(obj.getId(), obj);
            }
        }
        return currencyTypeMap.get(currencyCode) != null;
    }
}
