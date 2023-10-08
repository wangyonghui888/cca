package com.panda.sport.merchant.common.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author alic
 * @desc 赛事比分枚举
 * 此枚举类禁止修改描述 by valar
 */
public enum ScoreEnum {
    //此枚举类禁止修改描述 by valar
    SCORE_S1("S1", "全场比分", "Full Time Score"),
    //常规赛比分 (篮球 美足)
    SCORE_S1111("S1111", "常规赛比分", "Full Time Score"),
    SCORE_S2("S2", "上半场比分", "1H. Score"),
    SCORE_S3("S3", "下半场比分", "2H. Score"),
    //(足球)
    SCORE_S4("S4", "发球比分", "Serve Score"),
    //(足球--总角球比分)
    SCORE_S5("S5", "角球比分", "Corner Score"),
    //(足球--常规赛)
    SCORE_S555("S555", "角球比分", "Corner Score"),
    //(足球--加时赛)
    SCORE_S500("S500", "足球--加时赛", "Overtime Corner Score"),
    //(足球--加时赛上半场)
    SCORE_S501("S501", "足球--加时赛上半场", "Overtime 1H. Corner Score"),
    //(足球--加时赛下半场)
    SCORE_S502("S502", "足球--加时赛下半场", "Overtime 2H. Corner Score"),
    //(足球)
    SCORE_S6("S6", "任意球比分", "Free Kick Score"),
    SCORE_S7("S7", "加时赛比分", "Overtime Score"),
    SCORE_S701("S701", "加时赛上半场比分", "Overtime 1H. Score"),
    SCORE_S702("S702", "加时赛下半场比分", "Overtime 2H. Score"),
    //(足球)
    SCORE_S8("S8", "危险进攻次数比分", "Dangerous Attack Score"),
    //(足球)
    SCORE_S9("S9", "两次发球失误比分", "Double Fault Score"),
    //(足球)
    SCORE_S10("S10", "点球比分", "Penalty Score"),
    //(足球)
    SCORE_S11("S11", "红牌比分", "Red Card Score"),

    SCORE_S11001("S11001", "红牌比分（常规赛）", "Red Card Score"),
    //(足球)
    SCORE_S12("S12", "黄牌比分", "Yellow Card Score"),

    SCORE_S12001("S12001", "黄牌比分（常规赛）", "Yellow Card Score"),
    SCORE_S13("S13", "上半场红牌比分", "1H. Red Card Score"),
    SCORE_S1302("S1302", "下半场红牌比分", "1H. Red Card Score"),

    SCORE_S14("S14", "上半场黄牌比分", "1H. Yellow Card Score"),
    SCORE_S1402("S1402", "下半场黄牌比分", "2H. Yellow Card Score"),
    SCORE_S15("S15", "上半场角球比分", "1H. Corner Score"),

    SCORE_S10101("S10101", "罚牌比分", "1H. Corner Score"),
    SCORE_S10102("S10102", "全场罚牌分数", "Total Corner Score"),
    SCORE_S10103("S10103", "上半场罚牌比分", "1H. Corner Score"),
    SCORE_S10104("S10104", "下半场罚牌比分", "2H. Corner Score"),

    //(足球)
    SCORE_S16("S16", "下半场角球比分", "2H. Corner Score"),
    //(足球)
    SCORE_S17("S17", "射偏次数比分", "Shot off target score"),
    //(足球)
    SCORE_S18("S18", "射正次数比分", "Shot on target score"),
    //(篮球)
    SCORE_S19("S19", "第一节比分", "Quarter 1 Score"),
    SCORE_S20("S20", "第二节比分", "Quarter 2 Score"),
    SCORE_S21("S21", "第三节比分", "Quarter 3 Score"),
    //(篮球)
    SCORE_S22("S22", "第四节比分", "Quarter 4 Score"),
    //(网球)
    SCORE_S23("S23", "第1盘比分", "Set 1 Score"),
    SCORE_S24("S24", "第1盘第1局比分", "Set 1- Game 1 Score"),
    SCORE_S25("S25", "第1盘第2局比分", "Set 1- Game 2 Score"),
    SCORE_S26("S26", "第1盘第3局比分", "Set 1- Game 3 Score"),
    SCORE_S27("S27", "第1盘第4局比分", "Set 1- Game 4 Score"),
    SCORE_S28("S28", "第1盘第5局比分", "Set 1- Game 5 Score"),
    SCORE_S29("S29", "第1盘第6局比分", "Set 1- Game 6 Score"),
    SCORE_S30("S30", "第1盘第7局比分", "Set 1- Game 7 Score"),
    SCORE_S31("S31", "第1盘第8局比分", "Set 1- Game 8 Score"),
    SCORE_S32("S32", "第1盘第9局比分", "Set 1- Game 9 Score"),
    SCORE_S33("S33", "第1盘第10局比分", "Set 1- Game 10 Score"),
    SCORE_S34("S34", "第1盘第11局比分", "Set 1- Game 11 Score"),
    SCORE_S35("S35", "第1盘第12局比分", "Set 1- Game 12 Score"),
    SCORE_S36("S36", "第1盘第13局比分", "Set 1- Game 13 Score"),
    SCORE_S37("S37", "第1盘第14局比分", "Set 1- Game 14 Score"),
    SCORE_S38("S38", "第1盘第15局比分", "Set 1- Game 15 Score"),
    SCORE_S39("S39", "第2盘比分", "Set 2 Score"),
    SCORE_S40("S40", "第2盘第1局比分", "Set 2- Game 1 Score"),
    SCORE_S41("S41", "第2盘第2局比分", "Set 2- Game 2 Score"),
    SCORE_S42("S42", "第2盘第3局比分", "Set 2- Game 3 Score"),
    SCORE_S43("S43", "第2盘第4局比分", "Set 2- Game 4 Score"),
    SCORE_S44("S44", "第2盘第5局比分", "Set 2- Game 5 Score"),
    SCORE_S45("S45", "第2盘第6局比分", "Set 2- Game 6 Score"),
    SCORE_S46("S46", "第2盘第7局比分", "Set 2- Game 7 Score"),
    SCORE_S47("S47", "第2盘第8局比分", "Set 2- Game 8 Score"),
    SCORE_S48("S48", "第2盘第9局比分", "Set 2- Game 9 Score"),
    SCORE_S49("S49", "第2盘第10局比分", "Set 2- Game 10 Score"),
    SCORE_S50("S50", "第2盘第11局比分", "Set 2- Game 11 Score"),
    SCORE_S51("S51", "第2盘第12局比分", "Set 2- Game 12 Score"),
    SCORE_S52("S52", "第2盘第13局比分", "Set 2- Game 13 Score"),
    SCORE_S53("S53", "第2盘第14局比分", "Set 2- Game 14 Score"),
    SCORE_S54("S54", "第2盘第15局比分", "Set 2- Game 15 Score"),
    SCORE_S55("S55", "第3盘比分", "Set 3 Score"),
    SCORE_S56("S56", "第3盘第1局比分", "Set 3- Game 1 Score"),
    SCORE_S57("S57", "第3盘第2局比分", "Set 3- Game 2 Score"),
    SCORE_S58("S58", "第3盘第3局比分", "Set 3- Game 3 Score"),
    SCORE_S59("S59", "第3盘第4局比分", "Set 3- Game 4 Score"),
    SCORE_S60("S60", "第3盘第5局比分", "Set 3- Game 5 Score"),
    SCORE_S61("S61", "第3盘第6局比分", "Set 3- Game 6 Score"),
    SCORE_S62("S62", "第3盘第7局比分", "Set 3- Game 7 Score"),
    SCORE_S63("S63", "第3盘第8局比分", "Set 3- Game 8 Score"),
    SCORE_S64("S64", "第3盘第9局比分", "Set 3- Game 9 Score"),
    SCORE_S65("S65", "第3盘第10局比分", "Set 3- Game 10 Score"),
    SCORE_S66("S66", "第3盘第11局比分", "Set 3- Game 11 Score"),
    SCORE_S67("S67", "第3盘第12局比分", "Set 3- Game 12 Score"),
    SCORE_S68("S68", "第3盘第13局比分", "Set 3- Game 13 Score"),
    SCORE_S69("S69", "第3盘第14局比分", "Set 3- Game 14 Score"),
    SCORE_S70("S70", "第3盘第15局比分", "Set 3- Game 15 Score"),
    SCORE_S71("S71", "第4盘比分", "Set 4 Score"),
    SCORE_S72("S72", "第4盘第1局比分", "Set 4- Game 1 Score"),
    SCORE_S73("S73", "第4盘第2局比分", "Set 4- Game 2 Score"),
    SCORE_S74("S74", "第4盘第3局比分", "Set 4- Game 3 Score"),
    SCORE_S75("S75", "第4盘第4局比分", "Set 4- Game 4 Score"),
    SCORE_S76("S76", "第4盘第5局比分", "Set 4- Game 5 Score"),
    SCORE_S77("S77", "第4盘第6局比分", "Set 4- Game 6 Score"),
    SCORE_S78("S78", "第4盘第7局比分", "Set 4- Game 7 Score"),
    SCORE_S79("S79", "第4盘第8局比分", "Set 4- Game 8 Score"),
    SCORE_S80("S80", "第4盘第9局比分", "Set 4- Game 9 Score"),
    SCORE_S81("S81", "第4盘第10局比分", "Set 4- Game 10 Score"),
    SCORE_S82("S82", "第4盘第11局比分", "Set 4- Game 11 Score"),
    SCORE_S83("S83", "第4盘第12局比分", "Set 4- Game 12 Score"),
    SCORE_S84("S84", "第4盘第13局比分", "Set 4- Game 13 Score"),
    SCORE_S85("S85", "第4盘第14局比分", "Set 4- Game 14 Score"),
    SCORE_S86("S86", "第4盘第15局比分", "Set 4- Game 15 Score"),
    SCORE_S87("S87", "第5盘比分", "Set 5 Score"),
    SCORE_S88("S88", "第5盘第1局比分", "Set 5- Game 1 Score"),
    SCORE_S89("S89", "第5盘第2局比分", "Set 5- Game 2 Score"),
    SCORE_S90("S90", "第5盘第3局比分", "Set 5- Game 3 Score"),
    SCORE_S91("S91", "第5盘第4局比分", "Set 5- Game 4 Score"),
    SCORE_S92("S92", "第5盘第5局比分", "Set 5- Game 5 Score"),
    SCORE_S93("S93", "第5盘第6局比分", "Set 5- Game 6 Score"),
    SCORE_S94("S94", "第5盘第7局比分", "Set 5- Game 7 Score"),
    SCORE_S95("S95", "第5盘第8局比分", "Set 5- Game 8 Score"),
    SCORE_S96("S96", "第5盘第9局比分", "Set 5- Game 9 Score"),
    SCORE_S97("S97", "第5盘第10局比分", "Set 5- Game 10 Score"),
    SCORE_S98("S98", "第5盘第11局比分", "Set 5- Game 11 Score"),
    SCORE_S99("S99", "第5盘第12局比分", "Set 5- Game 12 Score"),
    SCORE_S100("S100", "第5盘第13局比分", "Set 5- Game 13 Score"),
    SCORE_S101("S101", "第5盘第14局比分", "Set 5- Game 14 Score"),
    SCORE_S102("S102", "第5盘第15局比分", "Set 5- Game 15 Score"),
    //(网球)
    SCORE_S103("S103", "当前盘当前局比分", "Game Score"),
    SCORE_S202("S202", "双发失误次数", "Double Fault Count"),
    //此枚举类禁止修改描述 by valar
    //(羽毛球、乒乓球、斯洛克,冰球(节))
    SCORE_S120("S120", "第1局比分", "Game 1 Score"),
    SCORE_S121("S121", "第2局比分", "Game 2 Score"),
    SCORE_S122("S122", "第3局比分", "Game 3 Score"),
    SCORE_S123("S123", "第4局比分", "Game 4 Score"),
    SCORE_S124("S124", "第5局比分", "Game 5 Score"),
    SCORE_S125("S125", "第6局比分", "Game 6 Score"),
    SCORE_S126("S126", "第7局比分", "Game 7 Score"),
    SCORE_S127("S127", "第8局比分", "Game 8 Score"),
    SCORE_S128("S128", "第9局比分", "Game 9 Score"),
    SCORE_S129("S129", "第10局比分", "Game 10 Score"),
    SCORE_S130("S130", "第11局比分", "Game 11 Score"),
    SCORE_S131("S131", "第12局比分", "Game 12 Score"),
    SCORE_S132("S132", "第13局比分", "Game 13 Score"),
    SCORE_S133("S133", "第14局比分", "Game 14 Score"),
    SCORE_S134("S134", "第15局比分", "Game 15 Score"),
    SCORE_S135("S135", "第16局比分", "Game 16 Score"),
    SCORE_S136("S136", "第17局比分", "Game 17 Score"),
    SCORE_S137("S137", "第18局比分", "Game 18 Score"),
    SCORE_S138("S138", "第19局比分", "Game 19 Score"),
    SCORE_S139("S139", "第20局比分", "Game 20 Score"),
    SCORE_S140("S140", "第21局比分", "Game 21 Score"),
    SCORE_S141("S141", "第22局比分", "Game 22 Score"),
    SCORE_S142("S142", "第23局比分", "Game 23 Score"),
    SCORE_S143("S143", "第24局比分", "Game 24 Score"),
    SCORE_S144("S144", "第25局比分", "Game 25 Score"),
    SCORE_S145("S145", "第26局比分", "Game 26 Score"),
    SCORE_S146("S146", "第27局比分", "Game 27 Score"),
    SCORE_S147("S147", "第28局比分", "Game 28 Score"),
    SCORE_S148("S148", "第29局比分", "Game 29 Score"),
    SCORE_S149("S149", "第30局比分", "Game 30 Score"),
    SCORE_S150("S150", "第31局比分", "Game 31 Score"),
    SCORE_S151("S151", "第32局比分", "Game 32 Score"),
    SCORE_S152("S152", "第33局比分", "Game 33 Score"),
    SCORE_S153("S153", "第34局比分", "Game 34 Score"),
    SCORE_S154("S154", "第35局比分", "Game 35 Score"),
    SCORE_S155("S155", "第36局比分", "Game 36 Score"),
    SCORE_S156("S156", "第37局比分", "Game 37 Score"),
    SCORE_S157("S157", "第38局比分", "Game 38 Score"),
    SCORE_S158("S158", "第39局比分", "Game 39 Score"),
    SCORE_S159("S159", "第40局比分", "Game 40 Score"),
    //(羽毛球、乒乓球、斯洛克)

    //棒球局安打
    SCORE_S1200("S1200", "第1局安打比分", "Game 1 Score"),
    SCORE_S1210("S1210", "第2局安打比分", "Game 2 Score"),
    SCORE_S1220("S1220", "第3局安打比分", "Game 3 Score"),
    SCORE_S1230("S1230", "第4局安打比分", "Game 4 Score"),
    SCORE_S1240("S1240", "第5局安打比分", "Game 5 Score"),
    SCORE_S1250("S1250", "第6局安打比分", "Game 6 Score"),
    SCORE_S1260("S1260", "第7局安打比分", "Game 7 Score"),
    SCORE_S1270("S1270", "第8局安打比分", "Game 8 Score"),
    SCORE_S1280("S1280", "第9局安打比分", "Game 9 Score"),
    SCORE_S1290("S1290", "第10局安打比分", "Game 10 Score"),
    SCORE_S1300("S1300", "第11局安打比分", "Game 11 Score"),
    SCORE_S1310("S1310", "第12局安打比分", "Game 12 Score"),
    SCORE_S1320("S1320", "第13局安打比分", "Game 13 Score"),
    SCORE_S1330("S1330", "第14局安打比分", "Game 14 Score"),
    SCORE_S1340("S1340", "第15局安打比分", "Game 15 Score"),
    SCORE_S1350("S1350", "第16局安打比分", "Game 16 Score"),
    SCORE_S1360("S1360", "第17局安打比分", "Game 17 Score"),
    SCORE_S1370("S1370", "第18局安打比分", "Game 18 Score"),
    SCORE_S1380("S1380", "第19局安打比分", "Game 19 Score"),
    SCORE_S1390("S1390", "第20局安打比分", "Game 20 Score"),


    /**
     * 进攻次数(足球)
     */
    SCORE_S104("S104", "进攻次数(足球)", "Attacks"),
    /**
     * 球权(足球)占比
     */
    SCORE_S105("S105", "球权(足球)占比", ""),
    SCORE_S1101("S1101", "足球射门", "Score"),
    /**
     * 点球大战比分
     */
    SCORE_S170("S170", "点球大战比分", "Penalty Shootout Score"),

    SCORE_S17005("S17005", "点球大战前五轮比分", "Quarter 1 Penalty Shootout Score"),

    SCORE_S1701("S1701", "点球大战第一局比分", "Quarter 1 Penalty Shootout Score"),
    SCORE_S1702("S1702", "点球大战第二局比分", "Quarter 2 Penalty Shootout Score"),
    SCORE_S1703("S1703", "点球大战第三局比分", "Quarter 3 Penalty Shootout Score"),
    SCORE_S1704("S1704", "点球大战第四局比分", "Quarter 4 Penalty Shootout Score"),
    SCORE_S1705("S1705", "点球大战第五局比分", "Quarter 5 Penalty Shootout Score"),
    SCORE_S1706("S1706", "点球大战第六局比分", "Quarter 6 Penalty Shootout Score"),
    SCORE_S1707("S1707", "点球大战第七局比分", "Quarter 7 Penalty Shootout Score"),
    SCORE_S1708("S1708", "点球大战第八局比分", "Quarter 8 Penalty Shootout Score"),
    SCORE_S1709("S1709", "点球大战第九局比分", "Quarter 9 Penalty Shootout Score"),

    /*犯规次数*/
    SCORE_S106("S106", "犯规次数", "Foul Count"),

    SCORE_S10606("S10606", "上半场犯规次数", "1H. Foul Count"),

    SCORE_S10607("S10607", "下半场犯规次数", "2H. Foul Count"),

    SCORE_S10601("S10601", "第一节犯规次数", "Quarter 1 Foul Count"),

    SCORE_S10602("S10602", "第二节犯规次数", "Quarter 2 Foul Count"),

    SCORE_S10603("S10603", "第三节犯规次数", "Quarter 3 Foul Count"),

    SCORE_S10604("S10604", "第四节犯规次数", "Quarter 4 Foul Count"),

    SCORE_S10605("S10605", "加时赛犯规次数", "Overtime Foul Count"),
    /**
     * 2分次数
     */
    SCORE_S107("S107", "2分次数", "2 points Times"),
    /**
     * 3分次数
     */
    SCORE_S108("S108", "3分次数", "3 points Times"),
    /**
     * 暂停次数
     */
    SCORE_S109("S109", "暂停次数", "Pause Times"),
    SCORE_S10901("S10901", "第一节暂停次数", "Quarter 1 Timeout Count"),
    SCORE_S10902("S10902", "第二节暂停次数", "Quarter 2 Timeout Count"),
    SCORE_S10903("S10903", "第三节暂停次数", "Quarter 3 Timeout Count"),
    SCORE_S10904("S10904", "第四节暂停次数", "Quarter 4 Timeout Count"),
    /**
     * 罚球得分次数
     */
    SCORE_S110("S110", "罚球得分次数", "Penalty Score Count"),
    /**
     * 罚球得分率
     */
    SCORE_S111("S111", "罚球得分率", "Penalty Score Rate"),
    /**
     * 罚球次数
     */
    SCORE_S190("S190", "罚球次数", "Penalty Count"),
    /**
     * 罚球未命中次数 (篮球)
     */
    SCORE_S191("S191", "罚球未命中次数", "Penalty Unmade Rate"),
    /**发球得分次数(网球)*/
    //SCORE_S112("S112", "发球得分次数(网球)"),
    /**
     * 发球失误次数(网球)
     */
    SCORE_S113("S113", "发球失误次数", "Service Fault Count"),
    /**
     * 破发成功率(网球)
     */
    SCORE_S114("S114", "破发成功率", "Break Success Rate"),
    /**
     * 得分次数(羽毛球)
     */
    SCORE_S115("S115", "得分次数", "Score Count"),
    /**
     * 接收点得分次数(羽毛球)
     */
    SCORE_S116("S116", "接收点得分次数", "Received Score Count"),
    /**
     * 发球得分率(羽毛球)
     */
    SCORE_S117("S117", "发球得分率", "Service Winner Rate"),
    /**
     * 犯规次数(斯洛克)
     */
    SCORE_S118("S118", "犯规次数", "Foul Count"),
    /**
     * 单杆最高(斯洛克)  总比分
     */
    SCORE_S119("S119", "单杆最高", "Max Bureau"),
    //单杆最高(斯洛克)  局比分
    SCORE_S1190("S1190", "单杆最高", "Max Bureau"),

    SCORE_S200("S200", "发球次数", "Serve count score"),

    SCORE_S201("S201", "破发成功次数", "Break count"),

    SCORE_S2001("S2001", "破发点出现次数", "Break Point count"),

    SCORE_S2011("S2011", "上一局最后比分", "Last Score"),

    SCORE_S2012("S2012", "上一次得分比分", "Last Score"),

    SCORE_S20112("S20112", "上一次三方事件Id", ""),

    SCORE_S3015("S3015", "全场安打比分", "Hit Score"),
    SCORE_S3016("S3016", "当前局之前局安达", "Hit Score"),

    SCORE_S4011("S4011", "大罚比分", "Major Penalty Score"),
    SCORE_S4012("S4012", "小罚比分", "Minor Penalty Score"),
    SCORE_S4013("S4013", "射门比分", "Field Goal Score"),
    SCORE_S4014("S4014", "点球前比分", ""),

    SCORE_S6011("S6011", "冲球数比分", "Rush Score"),
    SCORE_S6012("S6012", "射门比分", "Field Goal Score"),
    SCORE_S6013("S6013", "进攻比分", ""),
    SCORE_S6014("S6014", "达阵比分", "Touchdown Score"),

    SCORE_S1001("S1001", "开场-14:59比分", "First 15 min Score"),
    SCORE_S1002("S1002", "15:00-29:59比分", "Second 15 min Score"),
    SCORE_S1003("S1003", "30:00-半场比分", "Third 15 min Score"),
    SCORE_S1004("S1004", "下半场开始-59:59比分", "Fourth 15 min Score"),
    SCORE_S1005("S1005", "60:00-74:59比分", "Fifth 15 min Score"),
    SCORE_S1006("S1006", "75:00-全场比分", "Sixth 15 min Score"),


    SCORE_S5001("S5001", "开场-14:59角球比分", "First 15 min Corner Score"),
    SCORE_S5002("S5002", "15:00-29:59角球比分", "Second 15 min Corner Score"),
    SCORE_S5003("S5003", "30:00-半场角球比分", "Third 15 min Corner Score"),
    SCORE_S5004("S5004", "下半场开始-59:59角球比分", "Fourth 15 min Corner Score"),
    SCORE_S5005("S5005", "60:00-74:59角球比分", "Fifth 15 min Corner Score"),
    SCORE_S5006("S5006", "75:00-全场角球比分", "Sixth 15 min Corner Score"),

    SCORE_S50011("S50011", "0-14:59 分钟罚牌比分", "First 15 min Corner Score"),
    SCORE_S50012("S50012", "15-29:59 分钟罚牌比分", "Second 15 min Corner Score"),
    SCORE_S50013("S50013", "30-44:59 分钟罚牌比分", "Third 15 min Corner Score"),
    SCORE_S50014("S50014", "45-59:59 分钟罚牌比分", "Fourth 15 min Corner Score"),
    SCORE_S50015("S50015", "60-74:59 分钟罚牌比分", "Fifth 15 min Corner Score"),
    SCORE_S50016("S50016", "75-89:59 分钟罚牌比分", "Sixth 15 min Corner Score"),
    

    /**
     * 5分钟玩法
     */
    SCORE_S10011("S10011", "0:00-4:59 分钟进球", "First 5 min Score"),
    SCORE_S10012("S10012", "5:00-9:59 分钟进球", "Second 5 min Score"),
    SCORE_S10013("S10013", "10:00-14:59 分钟进球", "Third 5 min Score"),
    SCORE_S10021("S10021", "15:00-19:59 分钟进球", "Fourth 5 min Score"),
    SCORE_S10022("S10022", "20:00-24:59 分钟进球", "Fifth 5 min Score"),
    SCORE_S10023("S10023", "25:00-29:59 分钟进球", "Sixth 5 min Score"),
    SCORE_S10031("S10031", "30:00-34:59 分钟进球", "Seventh 5 min Score"),
    SCORE_S10032("S10032", "35:00-39:59 分钟进球", "Eighth 5 min Score"),
    SCORE_S10033("S10033", "40:00-45:00 分钟进球", "Ninth 5 min Score"),
    SCORE_S10034("S10034", "上半场绝杀", "1H Last-minute Goal"),
    SCORE_S10041("S10041", "45:00-49:59 分钟进球", "Tenth 5 min Score"),
    SCORE_S10042("S10042", "50:00-54:59 分钟进球", "Eleventh 5 min Score"),
    SCORE_S10043("S10043", "55:00-59:59 分钟进球", "Twelfth 5 min Score"),
    SCORE_S10051("S10051", "60:00-64:59 分钟进球", "Thirteenth 5 min Score"),
    SCORE_S10052("S10052", "65:00-69:59  分钟进球", "Fourteenth 5 min Score"),
    SCORE_S10053("S10053", "70:00-74:59  分钟进球", "Fifteenth 5 min Score"),
    SCORE_S10061("S10061", "75:00-79:59  分钟进球", "Sixteenth 5 min Score"),
    SCORE_S10062("S10062", "80:00-84:59  分钟进球", "Seventeenth 5 min Score"),
    SCORE_S10063("S10063", "85:00-90:00  分钟进球", "Eighteenth 5 min Score"),
    SCORE_S10064("S10064", "下半场绝杀", "2H Last-minute Goal");

    //此枚举类禁止修改描述 by valar
    /**
     * 比分分割
     */
    public static final String SCORE_DIVISION = "|";

    private String key;

    private String value;

    private String en;

    ScoreEnum(String key, String value, String en) {
        this.key = key;
        this.value = value;
        this.en = en;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public static Map<String, Object> ScoreEnumMap = new HashMap();

    public static ScoreEnum getScoreEnumByKey(String key) {
        if (ScoreEnumMap.get(key) != null) return null;
        ScoreEnum[] scoreEnums = ScoreEnum.values();
        for (ScoreEnum sEnum : scoreEnums) {
            ScoreEnumMap.put("zs" + sEnum.getKey(), sEnum.getValue());
            ScoreEnumMap.put("zh" + sEnum.getKey(), sEnum.getValue());
            ScoreEnumMap.put("en" + sEnum.getKey(), sEnum.getEn());
            if (sEnum.getKey().equals(key.toUpperCase())) {
                return sEnum;
            }
        }
        return null;
    }
}
