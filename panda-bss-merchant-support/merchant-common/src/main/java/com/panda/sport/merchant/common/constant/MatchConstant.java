package com.panda.sport.merchant.common.constant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author :  christion
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.schedule.common
 * @Description :  赛事相关常量类
 * @Date: 2019-09-04 20:41
 */
public class MatchConstant {
    /**
     * 球赛主队
     */
    public final static String HOME_TEAM = "home";

    /**
     * 球赛客队
     */
    public final static String AWAY_TEAM = "away";

    /**
     * 平局
     */
    public final static String SAME_SCOE = "dogfall";

    /**
     * 支持
     */
    public final static Integer SUPPORT = 1;


    public final static   Integer  PUSH_COMMAND_MATCH_STATUS=1;
    /**
     * 盘口数据指令
     */
    public final  static  Integer PUSH_COMMAND_MARKET_ODDS=2;
    /**
     * 盘口投注项指令
     */
    public   final static  Integer  PUSH_COMMAND_MARKET_SELECTION=3;
    /**
     * 盘中事件
     */
    public  final  static Integer PUSH_COMMAND_MARKET_EVENT=4;

    /**
     * 主盘
     */
    public  final  static Integer MAIN_MARKET=1;

    /**
     * 主副盘size
     */
    public  final  static Integer MAIN_MARKET_SIZE=2;

    /**
     * 中场休息时间
     */
    public  final  static Long HALFTIME =31L;


    /**
     * 玩法是双重机会
     */
    public  final  static Long PLAY_ID_DUPLICATO =6L;

    /**
     * 下个进球时间
     */
    public  final  static Long PLAY_ID_NEXT_TIME =31L;

    /**
     * 独赢&进球 大/小
     */
    public  final  static Long PLAY_ID_CAPOT_BIGORSAMLL  =13L;

    /**
     *玩法展示集合  投注项类型名称  total替换
     */
    public final  static String  TITLE_SHOW_NAME_TOTAL="{total}";

    /**
     *玩法展示集合  投注项类型名称  competitor1替换
     */
    public final  static String  TITLE_SHOW_NAME_COMPETITOR1_AHCP="{$competitor1}";

    /**
     *玩法展示集合  投注项类型名称  competitor1 -hcp 替换
     */
    public final  static String  TITLE_SHOW_NAME_COMPETITOR1_EHCP="{$competitor1} ({-hcp})";

    /**
     *玩法展示集合  投注项类型名称  competitor2替换
     */
    public final  static String  TITLE_SHOW_NAME_COMPETITOR2_AHCP="{$competitor2}";

    /**
     *玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final  static String  TITLE_SHOW_NAME_COMPETITOR2_EHCP="{$competitor2} ({-hcp})";

    /**
     *玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final  static String  TITLE_SHOW_NAME_COMPETITOR1="{$competitor1}";

    /**
     *玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final  static String  TITLE_SHOW_NAME_COMPETITOR2="{$competitor2}";
    /**
     * 波胆
     */
    public  final  static Long PLAY_ID_7 =7L;
    /**
     * 波胆
     */
    public  final  static Long PLAY_ID_20 =20L;

    /**
     * 上半场双重机会
     */
    public  final  static Long PLAY_ID_DUPLICATO_UPER =70L;
    /**
     * 下半场双重机会
     */
    public  final  static Long PLAY_ID_DUPLICATO_DOWN=72L;

    /**
     *玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final  static String  TITLE_SHOW_NAME_HCP1="({-hcp})";
    public final  static String  TITLE_SHOW_NAME_HCP2="({+hcp})";
    public final  static String  TITLE_SHOW_NAME_HCP="({hcp})";
    public final  static String  HCP="hcp";
    public static Map<Integer, Object> PlayIdMap = Maps.newHashMap();

    static {
        PlayIdMap.put(1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
                28, 29, 30, 31, 32, 33, 34, 35, 36, 42, 43, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
                87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
                114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138,
                139, 140, 141, 142, 143, 144, 148, 149, 150, 151, 152
        });
        PlayIdMap.put(2, new int[]{1, 2, 3, 4, 5, 10, 11, 15, 17, 18, 19, 25, 26, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
                51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 75, 87, 97, 104, 141, 142, 143, 145, 146, 147});
        PlayIdMap.put(5, new int[]{165, 166, 167, 168, 169, 170, 171});
        PlayIdMap.put(10, new int[]{153, 172, 173, 174, 175, 176, 177, 178, 179});
        PlayIdMap.put(8, new int[]{153, 172, 173, 174, 175, 176, 177, 178, 179});
        PlayIdMap.put(7, new int[]{1, 153, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197});
    }

    public static List<String> playList =
            Lists.newArrayList("!quarternr", "!framenr", "!xth", "pointnr",
                    "!gamenr", "gamenr", "!gamenrY", "!setnr", "goalnr", "cornernr", "!penaltynr");

}
