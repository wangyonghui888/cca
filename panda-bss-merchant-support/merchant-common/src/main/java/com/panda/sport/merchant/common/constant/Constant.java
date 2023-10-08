package com.panda.sport.merchant.common.constant;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  hooli
 * @Project Name :  panda_bss
 * @Description :  系统全局常量
 * @Creation Date:  2019-09-01 17:21
 */
public class Constant {

    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String UTC8 = "UTC8";
    public static final String EZ = "EZ";
    public static final String INFORMATION_OF_2TIMES_ORDER = "infomationOf2TimesOrder";
    public static List<String> enabledSortColumnList = Lists.newArrayList("betAmount", "profit", "profitRate", "activeDays", "amount", "betNum");


    public static Map<String, String> enabledSortColumnMap = ImmutableMap.of("createTime", "o1.create_time", "productAmountTotal", "o1.product_amount_total",
            "profitAmount", "s.profit_amount", "profitRate", "profit_rate", "activeDays", "active_days");


    public static Map<String, String> enabledSortColumnMap1 = ImmutableMap.of("createTime", "create_time", "productAmountTotal", "product_amount_total",
            "profitAmount", "profit_amount", "profitRate", "profit_rate", "activeDays", "active_days");

    //（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期）
    public static Map<Integer, String> settleStatusMap = new HashMap<>();

    public static Map<Integer, String> settleStatusEnMap = new HashMap<>();

    public static Map<Integer, String> orderStatusMap = ImmutableMap.of(0, "待处理", 1, "已处理",
            2, "取消交易", 3, "待确认", 4, "已拒绝");

    public static Map<Integer, String> preSettleStatusMap = ImmutableMap.of(0, "待处理", 1, "已处理",
            2, "取消交易", 3, "待确认", 4, "已拒绝");


    public static Map<Integer, String> orderStatusEnMap = ImmutableMap.of(0, "unsettled", 1, "settled",
            2, "cancel", 3, "confirm", 4, "refused");

    public static Map<Integer, String> preSettleStatusEnMap = ImmutableMap.of(0, "unsettled", 1, "settled",
            2, "cancel", 3, "confirm", 4, "refused");

    public static List<String> enabledMerchantSortColumnList = Lists.newArrayList("bet_user_rate", "profit", "return_rate",
            "return_amount", "merchant_level", "bet_amount", "order_num", "bet_user_amount", "settile_user_rate", "settle_profit", "settle_return",
            "settle_return_rate", "settle_bet_amount", "settle_order_num", "live_user_rate", "live_profit", "live_return", "live_return_rate", "live_bet_amount"
            , "live_order_num");
    public static Map<String, String> enabledMerchanSortColumnMap = Maps.newHashMap();
    public static Map<String, String> series_en = Maps.newHashMap();
    public static Map<String, String> series_zs = Maps.newHashMap();

    static {
        enabledMerchanSortColumnMap.put("betUserRate", "bet_user_rate");
        enabledMerchanSortColumnMap.put("profit", "profit");
        enabledMerchanSortColumnMap.put("returnRate", "return_rate");
        enabledMerchanSortColumnMap.put("returnAmount", "return_amount");
        enabledMerchanSortColumnMap.put("merchantLevel", "merchant_level");
        enabledMerchanSortColumnMap.put("betAmount", "bet_amount");
        enabledMerchanSortColumnMap.put("orderNum", "order_num");
        enabledMerchanSortColumnMap.put("betUserAmount", "bet_user_amount");
        enabledMerchanSortColumnMap.put("settileUserRate", "settile_user_rate");
        enabledMerchanSortColumnMap.put("settleProfit", "settle_profit");
        enabledMerchanSortColumnMap.put("settleReturn", "settle_return");
        enabledMerchanSortColumnMap.put("settleReturnRate", "settle_return_rate");
        enabledMerchanSortColumnMap.put("settleBetAmount", "settle_bet_amount");
        enabledMerchanSortColumnMap.put("settleOrderNum", "settle_order_num");
        enabledMerchanSortColumnMap.put("liveUserRate", "live_user_rate");
        enabledMerchanSortColumnMap.put("liveProfit", "live_profit");
        enabledMerchanSortColumnMap.put("liveReturn", "live_return");
        enabledMerchanSortColumnMap.put("liveReturnRate", "live_return_rate");
        enabledMerchanSortColumnMap.put("liveBetAmount", "live_bet_amount");
        enabledMerchanSortColumnMap.put("liveOrderNum", "live_order_num");
        settleStatusMap.put(2, "走水");
        settleStatusMap.put(3, "输");
        settleStatusMap.put(4, "赢");
        settleStatusMap.put(5, "半赢");
        settleStatusMap.put(6, "半输");
        settleStatusMap.put(7, "赛事取消");
        settleStatusMap.put(8, "赛事延期");
        settleStatusEnMap.put(2, "Draw");
        settleStatusEnMap.put(3, "Lose");
        settleStatusEnMap.put(4, "Win");
        settleStatusEnMap.put(5, "Half Win");
        settleStatusEnMap.put(6, "Half Lose");
        settleStatusEnMap.put(7, "Match Cancel");
        settleStatusEnMap.put(8, "Match Delay");

        series_en.put("1", "single");
        series_en.put("2001", "Double");
        series_en.put("3001", "Treble");
        series_en.put("3004", "Trixie");
        series_en.put("4001", "4-Fold");
        series_en.put("5001", "5-Fold");
        series_en.put("6001", "6-Fold");
        series_en.put("7001", "7-Fold");
        series_en.put("8001", "8-Fold");
        series_en.put("9001", "9-Fold");
        series_en.put("10001", "10-Fold");
        series_en.put("40011", "Yankee");
        series_en.put("50026", "Super Ya");
        series_en.put("60057", "Heinz");
        series_en.put("700120", "Super Hei");
        series_en.put("800247", "Goliath");
        series_en.put("900502", "Block (9)");
        series_en.put("10001013", "Block (10)");

        series_zs.put("1", "单关");
        series_zs.put("2001", "2串1");
        series_zs.put("3001", "3串1");
        series_zs.put("3004", "3串4");
        series_zs.put("4001", "4串1");
        series_zs.put("5001", "5串1");
        series_zs.put("6001", "6串1");
        series_zs.put("7001", "7串1");
        series_zs.put("8001", "8串1");
        series_zs.put("9001", "9串1");
        series_zs.put("10001", "10串1");
        series_zs.put("40011", "4串11");
        series_zs.put("50026", "5串26");
        series_zs.put("60057", "6串57");
        series_zs.put("700120", "7串120");
        series_zs.put("800247", "8串247");
        series_zs.put("900502", "9串502");
        series_zs.put("10001013", "10串1013");
    }

    public static String CREATE_TIME = "o1.id";
    public static String ID = "o1.id";
    public static String SID = "s1.id";
    public static String DESC = "desc";
    public static String ASC = "asc";
    public static String profit = "profit";
    /**
     * 中文繁体
     */
    public static String LANGUAGE_CHINESE_TRADITION = "zh";

    /**
     * 中文简体
     */
    public static final String LANGUAGE_CHINESE_SIMPLIFIED = "zs";
    /**
     * 英文
     */
    public static String LANGUAGE_ENGLISH = "en";
    /**
     * 人民币
     */
    public static String RMB = "rmb";

    /**
     * 12小时数对应的毫秒
     */
    public static long MATCH_INFO_SUB_TIME = 43200000;
    /**
     * 加一小时毫秒
     */
    public static long MATCH_IBFO_ADD_HOUS = 3600000;
    /**
     * 客户端websocket心跳返回值
     */
    public static long WEBSOCKET_HEART_RESP = 1000;
    /**
     * 欧赔赔率数据库存储*100000
     */
    public static Integer EU_MULTIPLY_FACTOR = 100000;

    public static Integer EU_MULTIPLY_FACTOR_TEMP = 100;
    /**
     * 投注额数据库存储*100
     */
    public static Integer BET_FACTOR = 100;

    /**
     * 亚盘让球
     */
    public static String ZS_CONCEDE_POINTS = "4";

    /**
     * 代表中场休息
     */
    public static Long HALF_TIME_BREAK = 31L;

    /**
     * 进球大小
     */
    public static String GOAL_SIZE = "2";

    /**
     * 上半场进球大小
     */
    public static String GOAL_SIZE_HALF = "18";

    /**
     * 全场大小含加时
     */
    public static String GOAL_SIZE_FULL = "38";


    public static Integer MAX_EXPORT_NUM = 100000;
    /**
     * 玩法展示集合  投注项类型名称  total替换
     */
    public final static String TITLE_SHOW_NAME_TOTAL = "{total}";
    public final static String TITLE_SHOW_NAME_POINT = "{!pointnr}";
    public final static String TITLE_SHOW_NAME_GOAL = "{goalnr}";
    public final static String TITLE_SHOW_NAME_QUARTER = "{!quarternr}";

    /**
     * 玩法展示集合  投注项类型名称  competitor1替换
     */
    public final static String TITLE_SHOW_NAME_COMPETITOR1_AHCP = "{$competitor1}";

    /**
     * 玩法展示集合  投注项类型名称  competitor1 -hcp 替换
     */
    public final static String TITLE_SHOW_NAME_COMPETITOR1_EHCP = "{$competitor1} ({-hcp})";

    /**
     * 玩法展示集合  投注项类型名称  competitor2替换
     */
    public final static String TITLE_SHOW_NAME_COMPETITOR2_AHCP = "{$competitor2}";

    /**
     * 玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final static String TITLE_SHOW_NAME_COMPETITOR2_EHCP = "{$competitor2} ({-hcp})";

    /**
     * 玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final static String TITLE_SHOW_NAME_COMPETITOR1 = "{$competitor1}";

    /**
     * 玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final static String TITLE_SHOW_NAME_COMPETITOR2 = "{$competitor2}";
    /**
     * 玩法展示集合  投注项类型名称  quarternr 替换
     */
    public final static String TITLE_SHOW_NAME_QUARTERNR = "{!quarternr}";

    /**
     * 玩法展示集合  投注项类型名称  competitor2 -hcp 替换
     */
    public final static String TITLE_SHOW_NAME_HCP0 = "{hcp}";
    public final static String TITLE_SHOW_NAME_HCP1 = "({-hcp})";
    public final static String TITLE_SHOW_NAME_HCP2 = "({+hcp})";
    public final static String TITLE_SHOW_NAME_HCP = "({hcp})";
    public final static String HCP = "hcp";
    public final static String regEx = "[ `!@#$%^&*()|{}':;',\\[\\].<>/?~！@#￥%……&*（）|{}【】‘；：”“’。，、？]|\n|\r|\t";

    public static List<String> SpecialUserNameList = Lists.newArrayList("|", "_", "*", "@", "/", "\\", "&");

    /**
     * 数字常量
     */
    public static final Integer INT_0 = 0;
    public static final Integer INT_1 = 1;
    public static final Integer INT_2 = 2;
    public static final Integer INT_3 = 3;
    public static final Integer INT_4 = 4;
    public static final Integer INT_5 = 5;
    public static final Integer INT_6 = 6;


    public static final List<Integer> CREDIT_MARKET_LEVEL_LIST = Lists.newArrayList(0, 1, 2, 3, 4);

    public static final List<Integer> CASH_MARKET_LEVEL_LIST = Lists.newArrayList(0, 11, 12, 13, 14, 15);
    /**
     * 重置数据开关
     */
    public static final String IS_RESET_GROWTH_TASK_DATA = "IS_RESET_GROWTH_TASK_DATA";


    //8天后的表
    public static final String T_ORDER = "t_order";
    public static final String T_ORDER_DETAIL = "t_order_detail";
    public static final String T_SETTLE = "t_settle";
    public static final String T_ORDER_INTERNATIONALIZE = "t_order_internationalize";

    //35天大表
    public static final String T_ORDER_OLD = "tybss_report.t_ticket";
    public static final String T_ORDER_DETAIL_OLD = "tybss_report.t_ticket_detail";
    public static final String T_SETTLE_OLD = "t_settle_old";
    public static final String T_ORDER_INTERNATIONALIZE_OLD = "t_order_internationalize_old";

    //查询注单预约表
    public static final String T_PRO_BET_ORDER = "t_pre_bet_order";
    public static final String T_PRO_BET_ORDER_DETAIL = "t_pre_bet_order_detail";

    public static final String SETTLE_TIME_FILTER = "3";
    public static final String BET_TIME_FILTER = "1";
    public static final String MATCH_BEGIN_FILTER = "2";

    public static final String REALTIME_TABLE = "1";
    public static final String HISTORY_TABLE = "2";

    /**
     * 开
     */
    public static int SWITCH_ON = 1;

    /**
     * 关
     */
    public static int SWITCH_OFF = 0;

    // 球赛
    public static int  MATCH_BALL = 1;

    // 联赛
    public static int  MATCH_LEAGUE = 2;

    public static final String MERCHANT_GROUP_COMMON = "common";
    public static final String MERCHANT_GROUP_S = "s";
    public static final String MERCHANT_GROUP_Y = "y";
    public static final String MERCHANT_GROUP_B = "b";

    public static final String DEFAULT_CURRENCY_ID = "1";
    public static final String DEFAULT_CURRENCY_CODE = "RMB";

    /**
     * 对内商户
     */
    public static final Integer INTERNAL_MERCHANT = 1;

    /**
     * 对外商户
     */
    public static final Integer EXTERNAL_MERCHANT = 0;

}
