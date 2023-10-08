package com.panda.sport.merchant.common.utils;


import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.enums.ScoreEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.panda.sport.merchant.common.enums.SportEnum.*;
import static com.panda.sport.merchant.common.enums.SportEnum.CS_GO;


public class MatchPeriodScoreEnumUtil {
    /**
     * key :赛种+玩法 value: 比分
     **/
    public final static Map<String, String> settleScoreMap = new HashMap<>();

    public final static Map<String, Map<String, Object>> compoundScoreMap = new HashMap<>();

    public final static List<String> specialPlay = new ArrayList<>();

    public final static List<String> superSpecialPlay = new ArrayList<>();

    public final static List<String> fiveMinitesPlay = new ArrayList<>();

    public final static List<String> fifteenPlay = new ArrayList<>();

    public final  static List<String> fiveMinitesPlayScoreKeyEnum = new ArrayList<>();

    public final static Map<String, Object> fifteenPlayTimeMap = new HashMap<>();

    public final static String CHILDREN = "children";

    public final static String STRATEGY = "strategy";
    public final static String COMPUTE = "compute";
    public final static String ADD = "+";
    public final static String MAX = "max";
    public final static String HOME_AWAY_ADD = "h_a_+";
    public final static String JOIN = ",";
    public final static String FROM_TO = "from-to";

    public final static String DESC = "desc";
    public final static String OT = "ot";

    static {

        //-------------------------------------------------------------settleScoreMap-------------------------------------------------------------start
        //-------------------------------------------------------------SPORT_FOOTBALL(1L, "足球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p1", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p2", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p3", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p4", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p5", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p6", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p7", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p8", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p9", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p10", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p11", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p12", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p13", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p14", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p15", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p27", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p77", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p78", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p79", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p80", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p81", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p82", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p68", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p91", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p92", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p101", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p102", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p107", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p141", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p223", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p344", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p347", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p351", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p18", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p20", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p21", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p22", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p23", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p24", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p29", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p42", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p43", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p69", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p70", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p87", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p90", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p97", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p100", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p105", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p341", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p345", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p359", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p25", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p26", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p71", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p72", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p73", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p74", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p75", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p76", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p88", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p89", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p98", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p99", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p106", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p142", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p143", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p342", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p346", ScoreEnum.SCORE_S3.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p126", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p127", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p128", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p234", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p236", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p343", ScoreEnum.SCORE_S7.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p138", ScoreEnum.SCORE_S11.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p139", ScoreEnum.SCORE_S11.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p140", ScoreEnum.SCORE_S11.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p119", ScoreEnum.SCORE_S15.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p121", ScoreEnum.SCORE_S15.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p122", ScoreEnum.SCORE_S15.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p123", ScoreEnum.SCORE_S15.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p124", ScoreEnum.SCORE_S15.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p228", ScoreEnum.SCORE_S15.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p229", ScoreEnum.SCORE_S15.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p132", ScoreEnum.SCORE_S170.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p134", ScoreEnum.SCORE_S170.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p334", ScoreEnum.SCORE_S170.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p238", ScoreEnum.SCORE_S170.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p239", ScoreEnum.SCORE_S170.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p240", ScoreEnum.SCORE_S170.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p241", ScoreEnum.SCORE_S170.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p111", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p113", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p114", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p115", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p116", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p117", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p118", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p226", ScoreEnum.SCORE_S555.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p227", ScoreEnum.SCORE_S555.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p129", ScoreEnum.SCORE_S701.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p130", ScoreEnum.SCORE_S701.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p330", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p332", ScoreEnum.SCORE_S701.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p32" + "part:1", ScoreEnum.SCORE_S1001.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p32" + "part:2", ScoreEnum.SCORE_S1002.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p32" + "part:3", ScoreEnum.SCORE_S1003.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p32" + "part:4", ScoreEnum.SCORE_S1004.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p32" + "part:5", ScoreEnum.SCORE_S1005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p32" + "part:6", ScoreEnum.SCORE_S1006.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p33" + "part:1", ScoreEnum.SCORE_S1001.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p33" + "part:2", ScoreEnum.SCORE_S1002.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p33" + "part:3", ScoreEnum.SCORE_S1003.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p33" + "part:4", ScoreEnum.SCORE_S1004.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p33" + "part:5", ScoreEnum.SCORE_S1005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p33" + "part:6", ScoreEnum.SCORE_S1006.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p34" + "part:1", ScoreEnum.SCORE_S1001.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p34" + "part:2", ScoreEnum.SCORE_S1002.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p34" + "part:3", ScoreEnum.SCORE_S1003.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p34" + "part:4", ScoreEnum.SCORE_S1004.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p34" + "part:5", ScoreEnum.SCORE_S1005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p34" + "part:6", ScoreEnum.SCORE_S1006.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p231" + "part:1", ScoreEnum.SCORE_S5001.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p231" + "part:2", ScoreEnum.SCORE_S5002.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p231" + "part:3", ScoreEnum.SCORE_S5003.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p231" + "part:4", ScoreEnum.SCORE_S5004.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p231" + "part:5", ScoreEnum.SCORE_S5005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p231" + "part:6", ScoreEnum.SCORE_S5006.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p232" + "part:1", ScoreEnum.SCORE_S5001.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p232" + "part:2", ScoreEnum.SCORE_S5002.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p232" + "part:3", ScoreEnum.SCORE_S5003.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p232" + "part:4", ScoreEnum.SCORE_S5004.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p232" + "part:5", ScoreEnum.SCORE_S5005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p232" + "part:6", ScoreEnum.SCORE_S5006.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p233" + "part:1", ScoreEnum.SCORE_S5001.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p233" + "part:2", ScoreEnum.SCORE_S5002.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p233" + "part:3", ScoreEnum.SCORE_S5003.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p233" + "part:4", ScoreEnum.SCORE_S5004.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p233" + "part:5", ScoreEnum.SCORE_S5005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p233" + "part:6", ScoreEnum.SCORE_S5006.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p327", ScoreEnum.SCORE_S14.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p324", ScoreEnum.SCORE_S12.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p333", ScoreEnum.SCORE_S17005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p335", ScoreEnum.SCORE_S17005.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p340", ScoreEnum.SCORE_S1.getKey());

        /**2304新玩法*/
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p367", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p368", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p369", ScoreEnum.SCORE_S3.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p370", ScoreEnum.SCORE_S50011.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p370", ScoreEnum.SCORE_S50012.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p370", ScoreEnum.SCORE_S50013.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p370", ScoreEnum.SCORE_S50014.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p370", ScoreEnum.SCORE_S50015.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p370", ScoreEnum.SCORE_S50016.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p371", ScoreEnum.SCORE_S50011.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p371", ScoreEnum.SCORE_S50012.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p371", ScoreEnum.SCORE_S50013.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p371", ScoreEnum.SCORE_S50014.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p371", ScoreEnum.SCORE_S50015.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p371", ScoreEnum.SCORE_S50016.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p372", ScoreEnum.SCORE_S50011.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p372", ScoreEnum.SCORE_S50012.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p372", ScoreEnum.SCORE_S50013.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p372", ScoreEnum.SCORE_S50014.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p372", ScoreEnum.SCORE_S50015.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p372", ScoreEnum.SCORE_S50016.getKey());

        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p373", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p374", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p375", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p376", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p377", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p378", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p379", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p380", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p381", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p382", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_FOOTBALL.getKey() + "p383", ScoreEnum.SCORE_S3.getKey());

        //-------------------------------------------------------------SPORT_DOTA2(101L, "dota2")-------------------------------------------------------------start
        //先这样写死，以防后续变动！！！！！！！！！！！！！！
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30002", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30003", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30004", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30005", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30006", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30007", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30008", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30009", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30010", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30011", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30012", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30013", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30014", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30015", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30016", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30017", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30018", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30019", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30020", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30021", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30022", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30023", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30024", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30025", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30026", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30027", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30028", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30029", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30030", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30031", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30032", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30033", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30034", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30035", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30036", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30037", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30038", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30039", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30040", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30041", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30042", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30043", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30044", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30045", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30046", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30047", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30048", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30049", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30050", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30051", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30052", ScoreEnum.SCORE_S1.getKey());
        //-------------------------------------------------------------SPORT_DOTA2(101L, "dota2")-------------------------------------------------------------end
        settleScoreMap.put(CS_GO.getKey() + "p30001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30002", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30003", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30004", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30005", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30006", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30007", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30008", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30009", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30010", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30011", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30014", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30015", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30048", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30051", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30052", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30506", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30507", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30508", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p30509", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(CS_GO.getKey() + "p32501", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32502", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32503", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32504", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32505", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32506", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32508", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32509", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32510", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32511", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32512", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32513", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32514", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32515", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32516", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32517", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32518", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32519", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32520", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32521", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32522", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32523", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32524", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32525", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32526", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32527", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32528", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32529", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32530", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32531", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32532", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32533", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32534", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32535", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32536", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32537", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32538", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32539", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32540", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32541", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32542", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32543", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32544", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32545", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32546", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32547", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32548", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(CS_GO.getKey() + "p32549", ScoreEnum.SCORE_S1.getKey());

        //-------------------------------------------------------------SPORT_KOG(103L, "KOG")-------------------------------------------------------------end
        settleScoreMap.put(SPORT_KOG.getKey() + "p30001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30004", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30002", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30003", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30006", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30007", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30008", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31502", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31503", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30009", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31504", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31505", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31506", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30012", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31509", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30013", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32002", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30014", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30015", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31513", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31514", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31515", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31516", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p31517", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32003", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32004", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32005", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32006", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32007", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32008", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32009", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32010", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32011", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32012", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32013", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32014", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32015", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30021", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32016", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30023", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p30037", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32017", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_KOG.getKey() + "p32018", ScoreEnum.SCORE_S1.getKey());

        //-------------------------------------------------------------SPORT_FOOTBALL(1L, "足球")-------------------------------------------------------------end

        //-------------------------------------------------------------VIRTUAL_SPORT_FOOTBALL(1001L, "虚拟足球")-------------------------------------------------------------start
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20002", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20003", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20004", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20005", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20006", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20007", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20008", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20009", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20010", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20011", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20012", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20020", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20021", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20024", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20025", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20026", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20027", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20028", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20029", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20030", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20031", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20013", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20014", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20015", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20016", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20017", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20018", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20019", ScoreEnum.SCORE_S2.getKey());
        //-------------------------------------------------------------VIRTUAL_SPORT_FOOTBALL(1001L, "虚拟足球")-------------------------------------------------------------end

        //-------------------------------------------------------------VIRTUAL_SPORT_BASKETBALL(1004L, "虚拟蓝球")-------------------------------------------------------------start
        settleScoreMap.put(VIRTUAL_SPORT_BASKETBALL.getKey() + "p20045", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_BASKETBALL.getKey() + "p20043", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_BASKETBALL.getKey() + "p20044", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(VIRTUAL_SPORT_BASKETBALL.getKey() + "p20046", ScoreEnum.SCORE_S1.getKey());
        //-------------------------------------------------------------VIRTUAL_SPORT_BASKETBALL(1004L, "虚拟蓝球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_BASKETBALL(2L, "篮球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p37", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p38", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p39", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p40", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p141", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p198", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p199", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p200", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p209", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p210", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p211", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p212", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p216", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p18", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p219", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p42", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p43", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p87", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p97", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p25", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p26", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p75", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p88", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p98", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p142", ScoreEnum.SCORE_S3.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p143", ScoreEnum.SCORE_S3.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p44", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p45", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p46", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p47", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p48", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p49", ScoreEnum.SCORE_S19.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p50", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p51", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p52", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p53", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p54", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p55", ScoreEnum.SCORE_S20.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p56", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p57", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p58", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p59", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p60", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p61", ScoreEnum.SCORE_S21.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p62", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p63", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p64", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p65", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p66", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p67", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p1", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p2", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p3", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p4", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p5", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p10", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p11", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p15", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p217", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p218", ScoreEnum.SCORE_S1111.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p145" + "1", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p145" + "2", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p145" + "3", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p145" + "4", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p146" + "1", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p146" + "2", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p146" + "3", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_BASKETBALL.getKey() + "p146" + "4", ScoreEnum.SCORE_S22.getKey());
        //-------------------------------------------------------------SPORT_BASKETBALL(2L, "篮球")-------------------------------------------------------------start

        //-------------------------------------------------------------SPORT_TENNIS(5L, "网球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p154", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p159", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p161", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p169", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p171", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p204", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p205", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p206", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p207", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p162" + "1", ScoreEnum.SCORE_S23.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p162" + "2", ScoreEnum.SCORE_S39.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p162" + "3", ScoreEnum.SCORE_S55.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p162" + "4", ScoreEnum.SCORE_S71.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p162" + "5", ScoreEnum.SCORE_S87.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p163" + "1", ScoreEnum.SCORE_S23.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p163" + "2", ScoreEnum.SCORE_S39.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p163" + "3", ScoreEnum.SCORE_S55.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p163" + "4", ScoreEnum.SCORE_S71.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p163" + "5", ScoreEnum.SCORE_S87.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p164" + "1", ScoreEnum.SCORE_S23.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p164" + "2", ScoreEnum.SCORE_S39.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p164" + "3", ScoreEnum.SCORE_S55.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p164" + "4", ScoreEnum.SCORE_S71.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p164" + "5", ScoreEnum.SCORE_S87.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p165" + "1", ScoreEnum.SCORE_S23.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p165" + "2", ScoreEnum.SCORE_S39.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p165" + "3", ScoreEnum.SCORE_S55.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p165" + "4", ScoreEnum.SCORE_S71.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p165" + "5", ScoreEnum.SCORE_S87.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p166" + "1", ScoreEnum.SCORE_S23.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p166" + "2", ScoreEnum.SCORE_S39.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p166" + "3", ScoreEnum.SCORE_S55.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p166" + "4", ScoreEnum.SCORE_S71.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p166" + "5", ScoreEnum.SCORE_S87.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "1", ScoreEnum.SCORE_S24.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "2", ScoreEnum.SCORE_S25.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "3", ScoreEnum.SCORE_S26.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "4", ScoreEnum.SCORE_S27.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "5", ScoreEnum.SCORE_S28.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "6", ScoreEnum.SCORE_S29.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "7", ScoreEnum.SCORE_S30.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "8", ScoreEnum.SCORE_S31.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "9", ScoreEnum.SCORE_S32.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "10", ScoreEnum.SCORE_S33.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "11", ScoreEnum.SCORE_S34.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "12", ScoreEnum.SCORE_S35.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "13", ScoreEnum.SCORE_S36.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "14", ScoreEnum.SCORE_S37.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "1" + "15", ScoreEnum.SCORE_S38.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "1", ScoreEnum.SCORE_S40.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "2", ScoreEnum.SCORE_S41.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "3", ScoreEnum.SCORE_S42.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "4", ScoreEnum.SCORE_S43.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "5", ScoreEnum.SCORE_S44.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "6", ScoreEnum.SCORE_S45.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "7", ScoreEnum.SCORE_S46.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "8", ScoreEnum.SCORE_S47.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "9", ScoreEnum.SCORE_S48.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "10", ScoreEnum.SCORE_S49.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "11", ScoreEnum.SCORE_S50.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "12", ScoreEnum.SCORE_S51.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "13", ScoreEnum.SCORE_S52.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "14", ScoreEnum.SCORE_S53.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "2" + "15", ScoreEnum.SCORE_S54.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "1", ScoreEnum.SCORE_S56.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "2", ScoreEnum.SCORE_S57.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "3", ScoreEnum.SCORE_S58.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "4", ScoreEnum.SCORE_S59.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "5", ScoreEnum.SCORE_S60.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "6", ScoreEnum.SCORE_S61.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "7", ScoreEnum.SCORE_S62.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "8", ScoreEnum.SCORE_S63.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "9", ScoreEnum.SCORE_S64.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "10", ScoreEnum.SCORE_S65.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "11", ScoreEnum.SCORE_S66.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "12", ScoreEnum.SCORE_S67.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "13", ScoreEnum.SCORE_S68.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "14", ScoreEnum.SCORE_S69.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "3" + "15", ScoreEnum.SCORE_S70.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "1", ScoreEnum.SCORE_S72.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "2", ScoreEnum.SCORE_S73.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "3", ScoreEnum.SCORE_S74.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "4", ScoreEnum.SCORE_S75.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "5", ScoreEnum.SCORE_S76.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "6", ScoreEnum.SCORE_S77.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "7", ScoreEnum.SCORE_S78.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "8", ScoreEnum.SCORE_S79.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "9", ScoreEnum.SCORE_S80.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "10", ScoreEnum.SCORE_S81.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "11", ScoreEnum.SCORE_S82.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "12", ScoreEnum.SCORE_S83.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "13", ScoreEnum.SCORE_S84.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "14", ScoreEnum.SCORE_S85.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "4" + "15", ScoreEnum.SCORE_S86.getKey());

        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "1", ScoreEnum.SCORE_S88.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "2", ScoreEnum.SCORE_S89.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "3", ScoreEnum.SCORE_S90.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "4", ScoreEnum.SCORE_S91.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "5", ScoreEnum.SCORE_S92.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "6", ScoreEnum.SCORE_S93.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "7", ScoreEnum.SCORE_S94.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "8", ScoreEnum.SCORE_S95.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "9", ScoreEnum.SCORE_S96.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "10", ScoreEnum.SCORE_S97.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "11", ScoreEnum.SCORE_S98.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "12", ScoreEnum.SCORE_S99.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "13", ScoreEnum.SCORE_S100.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "14", ScoreEnum.SCORE_S101.getKey());
        settleScoreMap.put(SPORT_TENNIS.getKey() + "p168" + "5" + "15", ScoreEnum.SCORE_S102.getKey());
        //-------------------------------------------------------------SPORT_TENNIS(5L, "网球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_BADMINTON(10L, "羽毛球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p174", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p204", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p175" + "11", ScoreEnum.SCORE_S130.getKey());

        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p176" + "11", ScoreEnum.SCORE_S130.getKey());

        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p177" + "11", ScoreEnum.SCORE_S130.getKey());

        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BADMINTON.getKey() + "p178" + "11", ScoreEnum.SCORE_S130.getKey());
        //-------------------------------------------------------------SPORT_BADMINTON(10L, "羽毛球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_PINGPONG(8L, "乒乓球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p174", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p204", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p175" + "11", ScoreEnum.SCORE_S130.getKey());

        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p176" + "11", ScoreEnum.SCORE_S130.getKey());

        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p177" + "11", ScoreEnum.SCORE_S130.getKey());

        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_PINGPONG.getKey() + "p178" + "11", ScoreEnum.SCORE_S130.getKey());
        //-------------------------------------------------------------SPORT_PINGPONG(8L, "乒乓球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_SNOOKER(7L, "斯诺克")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p1", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p180", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p181", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p182", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p183", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p204", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "20", ScoreEnum.SCORE_S139.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "21", ScoreEnum.SCORE_S140.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "22", ScoreEnum.SCORE_S141.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "23", ScoreEnum.SCORE_S142.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "24", ScoreEnum.SCORE_S143.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "25", ScoreEnum.SCORE_S144.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "26", ScoreEnum.SCORE_S145.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "27", ScoreEnum.SCORE_S146.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "28", ScoreEnum.SCORE_S147.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "29", ScoreEnum.SCORE_S148.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "30", ScoreEnum.SCORE_S149.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "31", ScoreEnum.SCORE_S150.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "32", ScoreEnum.SCORE_S151.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "33", ScoreEnum.SCORE_S152.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "34", ScoreEnum.SCORE_S153.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "35", ScoreEnum.SCORE_S154.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "36", ScoreEnum.SCORE_S155.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "37", ScoreEnum.SCORE_S156.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "38", ScoreEnum.SCORE_S157.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "39", ScoreEnum.SCORE_S158.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p184" + "40", ScoreEnum.SCORE_S159.getKey());

        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "20", ScoreEnum.SCORE_S139.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "21", ScoreEnum.SCORE_S140.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "22", ScoreEnum.SCORE_S141.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "23", ScoreEnum.SCORE_S142.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "24", ScoreEnum.SCORE_S143.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "25", ScoreEnum.SCORE_S144.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "26", ScoreEnum.SCORE_S145.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "27", ScoreEnum.SCORE_S146.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "28", ScoreEnum.SCORE_S147.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "29", ScoreEnum.SCORE_S148.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "30", ScoreEnum.SCORE_S149.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "31", ScoreEnum.SCORE_S150.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "32", ScoreEnum.SCORE_S151.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "33", ScoreEnum.SCORE_S152.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "34", ScoreEnum.SCORE_S153.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "35", ScoreEnum.SCORE_S154.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "36", ScoreEnum.SCORE_S155.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "37", ScoreEnum.SCORE_S156.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "38", ScoreEnum.SCORE_S157.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "39", ScoreEnum.SCORE_S158.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p185" + "40", ScoreEnum.SCORE_S159.getKey());

        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "20", ScoreEnum.SCORE_S139.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "21", ScoreEnum.SCORE_S140.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "22", ScoreEnum.SCORE_S141.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "23", ScoreEnum.SCORE_S142.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "24", ScoreEnum.SCORE_S143.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "25", ScoreEnum.SCORE_S144.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "26", ScoreEnum.SCORE_S145.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "27", ScoreEnum.SCORE_S146.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "28", ScoreEnum.SCORE_S147.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "29", ScoreEnum.SCORE_S148.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "30", ScoreEnum.SCORE_S149.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "31", ScoreEnum.SCORE_S150.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "32", ScoreEnum.SCORE_S151.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "33", ScoreEnum.SCORE_S152.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "34", ScoreEnum.SCORE_S153.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "35", ScoreEnum.SCORE_S154.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "36", ScoreEnum.SCORE_S155.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "37", ScoreEnum.SCORE_S156.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "38", ScoreEnum.SCORE_S157.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "39", ScoreEnum.SCORE_S158.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p186" + "40", ScoreEnum.SCORE_S159.getKey());

        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "20", ScoreEnum.SCORE_S139.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "21", ScoreEnum.SCORE_S140.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "22", ScoreEnum.SCORE_S141.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "23", ScoreEnum.SCORE_S142.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "24", ScoreEnum.SCORE_S143.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "25", ScoreEnum.SCORE_S144.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "26", ScoreEnum.SCORE_S145.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "27", ScoreEnum.SCORE_S146.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "28", ScoreEnum.SCORE_S147.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "29", ScoreEnum.SCORE_S148.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "30", ScoreEnum.SCORE_S149.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "31", ScoreEnum.SCORE_S150.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "32", ScoreEnum.SCORE_S151.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "33", ScoreEnum.SCORE_S152.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "34", ScoreEnum.SCORE_S153.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "35", ScoreEnum.SCORE_S154.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "36", ScoreEnum.SCORE_S155.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "37", ScoreEnum.SCORE_S156.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "38", ScoreEnum.SCORE_S157.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "39", ScoreEnum.SCORE_S158.getKey());
        settleScoreMap.put(SPORT_SNOOKER.getKey() + "p187" + "40", ScoreEnum.SCORE_S159.getKey());
        //-------------------------------------------------------------SPORT_SNOOKER(7L, "斯诺克")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_ICEHOCKEY(4L, "冰球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p294", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p295", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p257", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p258", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p1", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p2", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p3", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p4", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p5", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p6", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p8", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p9", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p12", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p14", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p15", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p204", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p260", ScoreEnum.SCORE_S1111.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p261" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p262" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p263" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p264" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p265" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p266" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p267" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p268" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p297" + "7", ScoreEnum.SCORE_S126.getKey());

        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p298" + "7", ScoreEnum.SCORE_S126.getKey());
        //-------------------------------------------------------------SPORT_ICEHOCKEY(4L, "冰球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_BASEBALL(3L, "棒球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p242", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p243", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p244", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p245", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p246", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p247", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p248", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p284", ScoreEnum.SCORE_S3015.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p285", ScoreEnum.SCORE_S3015.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p286", ScoreEnum.SCORE_S3015.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p275" + "20", ScoreEnum.SCORE_S139.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p276" + "20", ScoreEnum.SCORE_S139.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p280" + "20", ScoreEnum.SCORE_S139.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p281" + "20", ScoreEnum.SCORE_S139.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p282" + "20", ScoreEnum.SCORE_S139.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "9", ScoreEnum.SCORE_S128.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "10", ScoreEnum.SCORE_S129.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "11", ScoreEnum.SCORE_S130.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "12", ScoreEnum.SCORE_S131.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "13", ScoreEnum.SCORE_S132.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "14", ScoreEnum.SCORE_S133.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "15", ScoreEnum.SCORE_S134.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "16", ScoreEnum.SCORE_S135.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "17", ScoreEnum.SCORE_S136.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "18", ScoreEnum.SCORE_S137.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "19", ScoreEnum.SCORE_S138.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p283" + "20", ScoreEnum.SCORE_S139.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "1", ScoreEnum.SCORE_S1200.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "2", ScoreEnum.SCORE_S1210.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "3", ScoreEnum.SCORE_S1220.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "4", ScoreEnum.SCORE_S1230.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "5", ScoreEnum.SCORE_S1240.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "6", ScoreEnum.SCORE_S1250.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "7", ScoreEnum.SCORE_S1260.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "8", ScoreEnum.SCORE_S1270.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "9", ScoreEnum.SCORE_S1280.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "10", ScoreEnum.SCORE_S1290.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "11", ScoreEnum.SCORE_S1300.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "12", ScoreEnum.SCORE_S1310.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "13", ScoreEnum.SCORE_S1320.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "14", ScoreEnum.SCORE_S1330.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "15", ScoreEnum.SCORE_S1340.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "16", ScoreEnum.SCORE_S1350.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "17", ScoreEnum.SCORE_S1360.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "18", ScoreEnum.SCORE_S1370.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "19", ScoreEnum.SCORE_S1380.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p287" + "20", ScoreEnum.SCORE_S1390.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "1", ScoreEnum.SCORE_S1200.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "2", ScoreEnum.SCORE_S1210.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "3", ScoreEnum.SCORE_S1220.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "4", ScoreEnum.SCORE_S1230.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "5", ScoreEnum.SCORE_S1240.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "6", ScoreEnum.SCORE_S1250.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "7", ScoreEnum.SCORE_S1260.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "8", ScoreEnum.SCORE_S1270.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "9", ScoreEnum.SCORE_S1280.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "10", ScoreEnum.SCORE_S1290.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "11", ScoreEnum.SCORE_S1300.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "12", ScoreEnum.SCORE_S1310.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "13", ScoreEnum.SCORE_S1320.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "14", ScoreEnum.SCORE_S1330.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "15", ScoreEnum.SCORE_S1340.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "16", ScoreEnum.SCORE_S1350.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "17", ScoreEnum.SCORE_S1360.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "18", ScoreEnum.SCORE_S1370.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "19", ScoreEnum.SCORE_S1380.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p288" + "20", ScoreEnum.SCORE_S1390.getKey());

        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "1", ScoreEnum.SCORE_S1200.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "2", ScoreEnum.SCORE_S1210.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "3", ScoreEnum.SCORE_S1220.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "4", ScoreEnum.SCORE_S1230.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "5", ScoreEnum.SCORE_S1240.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "6", ScoreEnum.SCORE_S1250.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "7", ScoreEnum.SCORE_S1260.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "8", ScoreEnum.SCORE_S1270.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "9", ScoreEnum.SCORE_S1280.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "10", ScoreEnum.SCORE_S1290.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "11", ScoreEnum.SCORE_S1300.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "12", ScoreEnum.SCORE_S1310.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "13", ScoreEnum.SCORE_S1320.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "14", ScoreEnum.SCORE_S1330.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "15", ScoreEnum.SCORE_S1340.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "16", ScoreEnum.SCORE_S1350.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "17", ScoreEnum.SCORE_S1360.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "18", ScoreEnum.SCORE_S1370.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "19", ScoreEnum.SCORE_S1380.getKey());
        settleScoreMap.put(SPORT_BASEBALL.getKey() + "p289" + "20", ScoreEnum.SCORE_S1390.getKey());
        //-------------------------------------------------------------SPORT_BASEBALL(3L, "棒球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_VOLLEYBALL(9L, "排球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p159", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p204", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p162" + "9", ScoreEnum.SCORE_S128.getKey());

        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p253" + "9", ScoreEnum.SCORE_S128.getKey());

        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p54" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p254" + "9", ScoreEnum.SCORE_S128.getKey());

        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p255" + "9", ScoreEnum.SCORE_S128.getKey());
        //-------------------------------------------------------------SPORT_VOLLEYBALL(9L, "排球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_USEFOOTBALL(6L, "美式足球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p37", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p38", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p39", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p40", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p198", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p199", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p18", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p42", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p87", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p97", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p44", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p50", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p56", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p62", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p45", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p51", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p57", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p63", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p46", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p52", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p58", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p64", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p305", ScoreEnum.SCORE_S6014.getKey());
        //-------------------------------------------------------------SPORT_USEFOOTBALL(6L, "美式足球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_HANDBALL(11L, "手球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p1", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p2", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p4", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p5", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p6", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p15", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p42", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p43", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p70", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p18", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p127", ScoreEnum.SCORE_S7.getKey());
        settleScoreMap.put(SPORT_HANDBALL.getKey() + "p128", ScoreEnum.SCORE_S7.getKey());
        //-------------------------------------------------------------SPORT_HANDBALL(11L, "手球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_BOXING(12L, "拳击/MMA")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_BOXING.getKey() + "p2", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BOXING.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BOXING.getKey() + "p337", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BOXING.getKey() + "p338", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BOXING.getKey() + "p339", ScoreEnum.SCORE_S1.getKey());
        //-------------------------------------------------------------SPORT_BOXING(12L, "拳击/MMA")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_BEACH_VOLLEYBALL(13L, "沙滩排球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p153", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p159", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p204", ScoreEnum.SCORE_S1.getKey());

        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p162" + "9", ScoreEnum.SCORE_S128.getKey());

        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p253" + "9", ScoreEnum.SCORE_S128.getKey());

        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p254" + "9", ScoreEnum.SCORE_S128.getKey());

        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "1", ScoreEnum.SCORE_S120.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "2", ScoreEnum.SCORE_S121.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "3", ScoreEnum.SCORE_S122.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "4", ScoreEnum.SCORE_S123.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "5", ScoreEnum.SCORE_S124.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "6", ScoreEnum.SCORE_S125.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "7", ScoreEnum.SCORE_S126.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "8", ScoreEnum.SCORE_S127.getKey());
        settleScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p255" + "9", ScoreEnum.SCORE_S128.getKey());
        //-------------------------------------------------------------SPORT_BEACH_VOLLEYBALL(13L, "沙滩排球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_WATER_POLO(16L, "水球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p1", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p4", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p2", ScoreEnum.SCORE_S1111.getKey());

        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p44", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p46", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p45", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p47", ScoreEnum.SCORE_S19.getKey());

        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p50", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p52", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p51", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p53", ScoreEnum.SCORE_S20.getKey());

        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p56", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p58", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p57", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p59", ScoreEnum.SCORE_S21.getKey());

        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p62", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p64", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p63", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_WATER_POLO.getKey() + "p65", ScoreEnum.SCORE_S22.getKey());
        //-------------------------------------------------------------SPORT_WATER_POLO(16L, "水球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_HOCKEY_BALL(15L, "曲棍球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p1", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p4", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p2", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p6", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p5", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p10", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p11", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p15", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p12", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p223", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p81", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p79", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p3", ScoreEnum.SCORE_S1111.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p18", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p70", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p43", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p69", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p87", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p97", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p42", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p24", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p48", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p44", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p46", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p45", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p47", ScoreEnum.SCORE_S19.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p54", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p50", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p52", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p51", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p53", ScoreEnum.SCORE_S20.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p60", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p56", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p58", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p57", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p59", ScoreEnum.SCORE_S21.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p66", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p62", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p64", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p63", ScoreEnum.SCORE_S22.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p65", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p145" + "1", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p145" + "2", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p145" + "3", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p145" + "4", ScoreEnum.SCORE_S22.getKey());

        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p146" + "1", ScoreEnum.SCORE_S19.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p146" + "2", ScoreEnum.SCORE_S20.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p146" + "3", ScoreEnum.SCORE_S21.getKey());
        settleScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p146" + "4", ScoreEnum.SCORE_S22.getKey());
        //-------------------------------------------------------------SPORT_HOCKEY_BALL(15L, "曲棍球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_ENGLAND_RUGBY_BALL(14L, "英式橄榄球")-------------------------------------------------------------start
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p1", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p2", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p3", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p4", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p5", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p6", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p10", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p11", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p15", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p141", ScoreEnum.SCORE_S1111.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p218", ScoreEnum.SCORE_S1111.getKey());

        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p17", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p18", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p19", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p42", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p43", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p69", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p70", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p87", ScoreEnum.SCORE_S2.getKey());
        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p97", ScoreEnum.SCORE_S2.getKey());

        settleScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p126", ScoreEnum.SCORE_S7.getKey());
        //-------------------------------------------------------------SPORT_ENGLAND_RUGBY_BALL(14L, "英式橄榄球")-------------------------------------------------------------end

        //-------------------------------------------------------------SPORT_DOTA2(101L, "dota2")-------------------------------------------------------------start
        //先这样写死，以防后续变动！！！！！！！！！！！！！！
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30001", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30002", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30003", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30004", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30005", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30006", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30007", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30008", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30009", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30010", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30011", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30012", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30013", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30014", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30015", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30016", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30017", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30018", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30019", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30020", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30021", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30022", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30023", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30024", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30025", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30026", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30027", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30028", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30029", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30030", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30031", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30032", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30033", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30034", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30035", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30036", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30037", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30038", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30039", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30040", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30041", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30042", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30043", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30044", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30045", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30046", ScoreEnum.SCORE_S1.getKey());
        settleScoreMap.put(SPORT_DOTA2.getKey() + "p30047", ScoreEnum.SCORE_S1.getKey());
        //-------------------------------------------------------------SPORT_DOTA2(101L, "dota2")-------------------------------------------------------------end
        //-------------------------------------------------------------settleScoreMap-------------------------------------------------------------end

        //-------------------------------------------------------------compoundScoreMap-------------------------------------------------------------start
        //-------------------------------------------------------------COMPUTE-------------------------------------------------------------start
        Map<String, Object> strategy308Map = new HashMap<>();
        strategy308Map.put(STRATEGY, COMPUTE);
        strategy308Map.put(DESC + LanguageEnum.ZS.getCode(), "上半场罚牌分数");
        strategy308Map.put(DESC + LanguageEnum.EN.getCode(), "1H Score");
        strategy308Map.put(CHILDREN, "2*" + ScoreEnum.SCORE_S13.getKey() + "+" + ScoreEnum.SCORE_S14.getKey());
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p308", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p309", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p311", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p313", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p316", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p317", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p319", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p322", strategy308Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p323", strategy308Map);

        Map<String, Object> strategy307Map = new HashMap<>();
        strategy307Map.put(STRATEGY, COMPUTE);
        strategy307Map.put(DESC + LanguageEnum.ZS.getCode(), "全场罚牌分数");
        strategy307Map.put(DESC + LanguageEnum.EN.getCode(), "Total Score");
        strategy307Map.put(CHILDREN, "2*" + ScoreEnum.SCORE_S11001.getKey() + "+" + ScoreEnum.SCORE_S12001.getKey());
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p306", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p307", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p310", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p312", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p314", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p315", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p318", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p320", strategy307Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p321", strategy307Map);
        //-------------------------------------------------------------COMPUTE-------------------------------------------------------------end

        //-------------------------------------------------------------ADD-------------------------------------------------------------start
        Map<String, Object> strategy160Map = new HashMap<>();
        strategy160Map.put(STRATEGY, ADD);
        strategy160Map.put(DESC + LanguageEnum.ZS.getCode(), "总局数");
        strategy160Map.put(DESC + LanguageEnum.EN.getCode(), "Total");
        strategy160Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S23.getKey(),
                ScoreEnum.SCORE_S39.getKey(),
                ScoreEnum.SCORE_S55.getKey(),
                ScoreEnum.SCORE_S71.getKey(),
                ScoreEnum.SCORE_S87.getKey()));
        compoundScoreMap.put(SPORT_TENNIS.getKey() + "p160", strategy160Map);
        //-------------------------------------------------------------ADD-------------------------------------------------------------end

        //-------------------------------------------------------------MAX-------------------------------------------------------------start
        Map<String, Object> strategy296Map = new HashMap<>();
        strategy296Map.put(STRATEGY, MAX);
        strategy296Map.put("296" + DESC + LanguageEnum.ZS.getCode(), "最高得分小节");
        strategy296Map.put("296" + DESC + LanguageEnum.EN.getCode(), "Highest Score Quarter");
        strategy296Map.put("296" + CHILDREN, Arrays.asList(ScoreEnum.SCORE_S120.getKey(),
                ScoreEnum.SCORE_S121.getKey(),
                ScoreEnum.SCORE_S122.getKey(),
                ScoreEnum.SCORE_S123.getKey(),
                ScoreEnum.SCORE_S124.getKey(),
                ScoreEnum.SCORE_S125.getKey(),
                ScoreEnum.SCORE_S126.getKey(),
                ScoreEnum.SCORE_S127.getKey(),
                ScoreEnum.SCORE_S128.getKey(),
                ScoreEnum.SCORE_S129.getKey(),
                ScoreEnum.SCORE_S130.getKey(),
                ScoreEnum.SCORE_S131.getKey(),
                ScoreEnum.SCORE_S132.getKey(),
                ScoreEnum.SCORE_S133.getKey(),
                ScoreEnum.SCORE_S134.getKey(),
                ScoreEnum.SCORE_S135.getKey(),
                ScoreEnum.SCORE_S136.getKey(),
                ScoreEnum.SCORE_S137.getKey(),
                ScoreEnum.SCORE_S138.getKey(),
                ScoreEnum.SCORE_S139.getKey()));
        strategy296Map.put("213" + DESC + LanguageEnum.ZS.getCode(), "最多进球小节");
        strategy296Map.put("213" + DESC + LanguageEnum.EN.getCode(), "Highest Score Quarter");
        strategy296Map.put("213" + CHILDREN, Arrays.asList(ScoreEnum.SCORE_S19.getKey(),
                ScoreEnum.SCORE_S20.getKey(),
                ScoreEnum.SCORE_S21.getKey(),
                ScoreEnum.SCORE_S22.getKey()));
        compoundScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p296", strategy296Map);
        compoundScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p213", strategy296Map);

        compoundScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p213", strategy296Map);
        //-------------------------------------------------------------MAX-------------------------------------------------------------end

        //-------------------------------------------------------------HOME_AWAY_ADD-------------------------------------------------------------start
//        Map<String, Object> strategy333Map = new HashMap<>();
//        strategy333Map.put(STRATEGY, HOME_AWAY_ADD);
//        strategy333Map.put(DESC + LanguageEnum.ZS.getCode(), "点球大战-前5轮比分");
//        strategy333Map.put(DESC + LanguageEnum.EN.getCode(), "PEN - Over Under");
//        strategy333Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1701.getKey(),
//                ScoreEnum.SCORE_S1702.getKey(),
//                ScoreEnum.SCORE_S1703.getKey(),
//                ScoreEnum.SCORE_S1704.getKey(),
//                ScoreEnum.SCORE_S1705.getKey()));
//        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p333", strategy333Map);

//        Map<String, Object> strategy335Map = new HashMap<>();
//        strategy335Map.put(STRATEGY, HOME_AWAY_ADD);
//        strategy335Map.put(DESC + LanguageEnum.ZS.getCode(), "点球大战-前5轮比分");
//        strategy335Map.put(DESC + LanguageEnum.EN.getCode(), "PEN - Over Under");
//        strategy335Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1701.getKey(),
//                ScoreEnum.SCORE_S1702.getKey(),
//                ScoreEnum.SCORE_S1703.getKey(),
//                ScoreEnum.SCORE_S1704.getKey(),
//                ScoreEnum.SCORE_S1705.getKey()));
//        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p335", strategy335Map);

        Map<String, Object> strategy259Map = new HashMap<>();
        strategy259Map.put(STRATEGY, HOME_AWAY_ADD);
        strategy259Map.put(DESC + LanguageEnum.ZS.getCode(), "全场比分(含加时点球)");
        strategy259Map.put(DESC + LanguageEnum.EN.getCode(), "Winner (incl. overtime and penalties)");
        strategy259Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1111.getKey(),
                ScoreEnum.SCORE_S7.getKey(),
                ScoreEnum.SCORE_S170.getKey()));
        compoundScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p259", strategy259Map);

        compoundScoreMap.put(SPORT_HANDBALL.getKey() + "p259", strategy259Map);

        Map<String, Object> strategy259Map2 = new HashMap<>();
        strategy259Map2.put(STRATEGY, HOME_AWAY_ADD);
        strategy259Map2.put(DESC + LanguageEnum.ZS.getCode(), "全场比分(含加时点球)");
        strategy259Map2.put(DESC + LanguageEnum.EN.getCode(), "Winner (incl. overtime and penalties)");
        strategy259Map2.put(CHILDREN, Collections.singletonList(ScoreEnum.SCORE_S1.getKey()));
        compoundScoreMap.put(SPORT_WATER_POLO.getKey() + "p259", strategy259Map2);

        Map<String, Object> strategy156Map = new HashMap<>();
        strategy156Map.put(STRATEGY, HOME_AWAY_ADD);
        strategy156Map.put(DESC + LanguageEnum.ZS.getCode(), "总局数比分");
        strategy156Map.put(DESC + LanguageEnum.EN.getCode(), "Total Points");
        strategy156Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S23.getKey(),
                ScoreEnum.SCORE_S39.getKey(),
                ScoreEnum.SCORE_S55.getKey(),
                ScoreEnum.SCORE_S71.getKey(),
                ScoreEnum.SCORE_S87.getKey()));
        compoundScoreMap.put(SPORT_TENNIS.getKey() + "p155", strategy156Map);
        compoundScoreMap.put(SPORT_TENNIS.getKey() + "p156", strategy156Map);
        compoundScoreMap.put(SPORT_TENNIS.getKey() + "p157", strategy156Map);
        compoundScoreMap.put(SPORT_TENNIS.getKey() + "p202", strategy156Map);

        Map<String, Object> strategy172Map = new HashMap<>();
        strategy172Map.put(STRATEGY, HOME_AWAY_ADD);
        strategy172Map.put(DESC + LanguageEnum.EN.getCode(), "Total Points");
        strategy172Map.put(DESC + LanguageEnum.ZS.getCode(), "总分");
        strategy172Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S120.getKey(),
                ScoreEnum.SCORE_S121.getKey(),
                ScoreEnum.SCORE_S122.getKey(),
                ScoreEnum.SCORE_S123.getKey(),
                ScoreEnum.SCORE_S124.getKey(),
                ScoreEnum.SCORE_S125.getKey(),
                ScoreEnum.SCORE_S126.getKey(),
                ScoreEnum.SCORE_S127.getKey(),
                ScoreEnum.SCORE_S128.getKey(),
                ScoreEnum.SCORE_S129.getKey(),
                ScoreEnum.SCORE_S130.getKey(),
                ScoreEnum.SCORE_S131.getKey(),
                ScoreEnum.SCORE_S132.getKey(),
                ScoreEnum.SCORE_S133.getKey(),
                ScoreEnum.SCORE_S134.getKey(),
                ScoreEnum.SCORE_S135.getKey(),
                ScoreEnum.SCORE_S136.getKey(),
                ScoreEnum.SCORE_S137.getKey(),
                ScoreEnum.SCORE_S138.getKey(),
                ScoreEnum.SCORE_S139.getKey(),
                ScoreEnum.SCORE_S140.getKey(),
                ScoreEnum.SCORE_S141.getKey(),
                ScoreEnum.SCORE_S142.getKey(),
                ScoreEnum.SCORE_S143.getKey(),
                ScoreEnum.SCORE_S144.getKey(),
                ScoreEnum.SCORE_S145.getKey(),
                ScoreEnum.SCORE_S146.getKey(),
                ScoreEnum.SCORE_S137.getKey(),
                ScoreEnum.SCORE_S148.getKey(),
                ScoreEnum.SCORE_S149.getKey(),
                ScoreEnum.SCORE_S150.getKey(),
                ScoreEnum.SCORE_S151.getKey(),
                ScoreEnum.SCORE_S152.getKey(),
                ScoreEnum.SCORE_S153.getKey(),
                ScoreEnum.SCORE_S154.getKey(),
                ScoreEnum.SCORE_S155.getKey(),
                ScoreEnum.SCORE_S156.getKey(),
                ScoreEnum.SCORE_S157.getKey(),
                ScoreEnum.SCORE_S158.getKey(),
                ScoreEnum.SCORE_S159.getKey()
        ));
        compoundScoreMap.put(SPORT_BADMINTON.getKey() + "p172", strategy172Map);
        compoundScoreMap.put(SPORT_BADMINTON.getKey() + "p173", strategy172Map);

        compoundScoreMap.put(SPORT_PINGPONG.getKey() + "p172", strategy172Map);
        compoundScoreMap.put(SPORT_PINGPONG.getKey() + "p173", strategy172Map);

        compoundScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p172", strategy172Map);
        compoundScoreMap.put(SPORT_BEACH_VOLLEYBALL.getKey() + "p173", strategy172Map);

        Map<String, Object> strategy156To202Map = new HashMap<>();
        strategy156To202Map.put(STRATEGY, HOME_AWAY_ADD);
        strategy156To202Map.put(DESC + LanguageEnum.ZS.getCode(), "前5局比分");
        strategy156To202Map.put(DESC + LanguageEnum.EN.getCode(), "First Five Score");
        strategy156To202Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S120.getKey(),
                ScoreEnum.SCORE_S121.getKey(),
                ScoreEnum.SCORE_S122.getKey(),
                ScoreEnum.SCORE_S123.getKey(),
                ScoreEnum.SCORE_S124.getKey()));
        strategy156To202Map.put("290" + DESC + LanguageEnum.ZS.getCode(), "前5局安打比分");
        strategy156To202Map.put("290" + DESC + LanguageEnum.EN.getCode(), "First Five Score");
        strategy156To202Map.put("291" + DESC + LanguageEnum.ZS.getCode(), "前5局安打比分");
        strategy156To202Map.put("291" + DESC + LanguageEnum.EN.getCode(), "First Five Score");
        strategy156To202Map.put("292" + DESC + LanguageEnum.ZS.getCode(), "前5局安打比分");
        strategy156To202Map.put("292" + DESC + LanguageEnum.EN.getCode(), "First Five Score");
        strategy156To202Map.put("290" + CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1200.getKey(),
                ScoreEnum.SCORE_S1210.getKey(),
                ScoreEnum.SCORE_S1220.getKey(),
                ScoreEnum.SCORE_S1230.getKey(),
                ScoreEnum.SCORE_S1240.getKey()));
        strategy156To202Map.put("291" + CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1200.getKey(),
                ScoreEnum.SCORE_S1210.getKey(),
                ScoreEnum.SCORE_S1220.getKey(),
                ScoreEnum.SCORE_S1230.getKey(),
                ScoreEnum.SCORE_S1240.getKey()));
        strategy156To202Map.put("292" + CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1200.getKey(),
                ScoreEnum.SCORE_S1210.getKey(),
                ScoreEnum.SCORE_S1220.getKey(),
                ScoreEnum.SCORE_S1230.getKey(),
                ScoreEnum.SCORE_S1240.getKey()));
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p249", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p250", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p251", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p252", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p273", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p290", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p291", strategy156To202Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p292", strategy156To202Map);

        Map<String, Object> volleyballStrategy172Map = new HashMap<>();
        volleyballStrategy172Map.put(STRATEGY, HOME_AWAY_ADD);
        volleyballStrategy172Map.put(DESC + LanguageEnum.ZS.getCode(), "总分");
        volleyballStrategy172Map.put(DESC + LanguageEnum.EN.getCode(), "Total Points");
        volleyballStrategy172Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S120.getKey(),
                ScoreEnum.SCORE_S121.getKey(),
                ScoreEnum.SCORE_S122.getKey(),
                ScoreEnum.SCORE_S123.getKey(),
                ScoreEnum.SCORE_S124.getKey(),
                ScoreEnum.SCORE_S125.getKey(),
                ScoreEnum.SCORE_S126.getKey()));
        compoundScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p172", volleyballStrategy172Map);
        compoundScoreMap.put(SPORT_VOLLEYBALL.getKey() + "p173", volleyballStrategy172Map);
        //-------------------------------------------------------------HOME_AWAY_ADD-------------------------------------------------------------end

        //-------------------------------------------------------------JOIN-------------------------------------------------------------start
        Map<String, Object> strategy83Map = new HashMap<>();
        strategy83Map.put(STRATEGY, JOIN);
        strategy83Map.put(DESC + LanguageEnum.ZS.getCode(), "上/下半场比分");
        strategy83Map.put(DESC + LanguageEnum.EN.getCode(), "First Half/Second Half");
        strategy83Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S2.getKey(),
                ScoreEnum.SCORE_S3.getKey()));
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p16", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p83", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p84", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p85", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p86", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p93", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p94", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p95", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p96", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p108", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p109", strategy83Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p110", strategy83Map);

        compoundScoreMap.put(SPORT_ENGLAND_RUGBY_BALL.getKey() + "p16", strategy83Map);

        Map<String, Object> strategy103Map = new HashMap<>();
        strategy103Map.put(STRATEGY, JOIN);
        strategy103Map.put(DESC + LanguageEnum.ZS.getCode(), "半/全场比分");
        strategy103Map.put(DESC + LanguageEnum.EN.getCode(), "Half/Total Points");
        strategy103Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S2.getKey(),
                ScoreEnum.SCORE_S1.getKey()));
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p103", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p104", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p348", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p349", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p350", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p354", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p355", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p356", strategy103Map);
        compoundScoreMap.put(SPORT_FOOTBALL.getKey() + "p360", strategy103Map);

        compoundScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20022", strategy103Map);
        compoundScoreMap.put(VIRTUAL_SPORT_FOOTBALL.getKey() + "p20023", strategy103Map);

        Map<String, Object> strategy103Map1 = new HashMap<>();
        strategy103Map1.put(STRATEGY, JOIN);
        strategy103Map1.put(DESC + LanguageEnum.ZS.getCode(), "半/全场比分");
        strategy103Map1.put(DESC + LanguageEnum.EN.getCode(), "Half/Total Points");
        strategy103Map1.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S2.getKey(),
                ScoreEnum.SCORE_S1111.getKey()));
        compoundScoreMap.put(SPORT_BASKETBALL.getKey() + "p103", strategy103Map1);
        compoundScoreMap.put(SPORT_BASKETBALL.getKey() + "p104", strategy103Map1);

        Map<String, Object> strategy103Map2 = new HashMap<>();
        strategy103Map2.put(STRATEGY, JOIN);
        strategy103Map.put(DESC + LanguageEnum.ZS.getCode(), "半/全场比分");
        strategy103Map.put(DESC + LanguageEnum.EN.getCode(), "Half/Total Points");
        strategy103Map2.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S2.getKey(),
                ScoreEnum.SCORE_S1111.getKey()));
        compoundScoreMap.put(SPORT_WATER_POLO.getKey() + "p103", strategy103Map2);
        compoundScoreMap.put(SPORT_WATER_POLO.getKey() + "p104", strategy103Map2);

        compoundScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p103", strategy103Map2);
        compoundScoreMap.put(SPORT_HOCKEY_BALL.getKey() + "p104", strategy103Map2);
        //-------------------------------------------------------------JOIN-------------------------------------------------------------end

        //-------------------------------------------------------------FROM_TO-------------------------------------------------------------start
        Map<String, Object> strategy274Map = new HashMap<>();
        strategy274Map.put(STRATEGY, FROM_TO);
        strategy274Map.put(DESC + LanguageEnum.ZS.getCode(), "a-b 局比分");
        strategy274Map.put(DESC + LanguageEnum.EN.getCode(), "a-b ");
        Map<Integer, String> tempMap = new HashMap<>();
        tempMap.put(1, ScoreEnum.SCORE_S120.getKey());
        tempMap.put(2, ScoreEnum.SCORE_S121.getKey());
        tempMap.put(3, ScoreEnum.SCORE_S122.getKey());
        tempMap.put(4, ScoreEnum.SCORE_S123.getKey());
        tempMap.put(5, ScoreEnum.SCORE_S124.getKey());
        tempMap.put(6, ScoreEnum.SCORE_S125.getKey());
        tempMap.put(7, ScoreEnum.SCORE_S126.getKey());
        tempMap.put(8, ScoreEnum.SCORE_S127.getKey());
        tempMap.put(9, ScoreEnum.SCORE_S128.getKey());
        tempMap.put(10, ScoreEnum.SCORE_S129.getKey());
        tempMap.put(11, ScoreEnum.SCORE_S130.getKey());
        tempMap.put(12, ScoreEnum.SCORE_S131.getKey());
        tempMap.put(13, ScoreEnum.SCORE_S132.getKey());
        tempMap.put(14, ScoreEnum.SCORE_S133.getKey());
        tempMap.put(15, ScoreEnum.SCORE_S134.getKey());
        tempMap.put(16, ScoreEnum.SCORE_S135.getKey());
        tempMap.put(17, ScoreEnum.SCORE_S136.getKey());
        tempMap.put(18, ScoreEnum.SCORE_S137.getKey());
        tempMap.put(19, ScoreEnum.SCORE_S138.getKey());
        tempMap.put(20, ScoreEnum.SCORE_S139.getKey());
        strategy274Map.put(CHILDREN, tempMap);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p274", strategy274Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p277", strategy274Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p278", strategy274Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p279", strategy274Map);
        //-------------------------------------------------------------FROM_TO-------------------------------------------------------------end

        //-------------------------------------------------------------OT-------------------------------------------------------------start
        Map<String, Object> strategy41Map = new HashMap<>();
        strategy41Map.put(STRATEGY, OT);
        strategy41Map.put(DESC + LanguageEnum.ZS.getCode(), "常规(加时)");
        strategy41Map.put(DESC + LanguageEnum.EN.getCode(), "Common(OT)");
        strategy41Map.put(CHILDREN, Arrays.asList(ScoreEnum.SCORE_S1111.getKey(),
                ScoreEnum.SCORE_S7.getKey()));
        compoundScoreMap.put(SPORT_BASKETBALL.getKey() + "p41", strategy41Map);
        compoundScoreMap.put(SPORT_BASEBALL.getKey() + "p248", strategy41Map);

        compoundScoreMap.put(SPORT_ICEHOCKEY.getKey() + "p41", strategy41Map);

        compoundScoreMap.put(SPORT_USEFOOTBALL.getKey() + "p41", strategy41Map);
        //-------------------------------------------------------------OT-------------------------------------------------------------end
        //-------------------------------------------------------------compoundScoreMap-------------------------------------------------------------end


        //-------------------------------------------------------------specialPlay-------------------------------------------------------------start
        specialPlay.add(SPORT_VOLLEYBALL.getKey() + "p162");
        specialPlay.add(SPORT_VOLLEYBALL.getKey() + "p253");
        specialPlay.add(SPORT_VOLLEYBALL.getKey() + "p254");
        specialPlay.add(SPORT_VOLLEYBALL.getKey() + "p255");

        specialPlay.add(SPORT_BEACH_VOLLEYBALL.getKey() + "p162");
        specialPlay.add(SPORT_BEACH_VOLLEYBALL.getKey() + "p253");
        specialPlay.add(SPORT_BEACH_VOLLEYBALL.getKey() + "p254");
        specialPlay.add(SPORT_BEACH_VOLLEYBALL.getKey() + "p255");

        specialPlay.add(SPORT_BASKETBALL.getKey() + "p145");
        specialPlay.add(SPORT_BASKETBALL.getKey() + "p146");

        specialPlay.add(SPORT_HOCKEY_BALL.getKey() + "p145");
        specialPlay.add(SPORT_HOCKEY_BALL.getKey() + "p146");

        specialPlay.add(SPORT_TENNIS.getKey() + "p162");
        specialPlay.add(SPORT_TENNIS.getKey() + "p163");
        specialPlay.add(SPORT_TENNIS.getKey() + "p164");
        specialPlay.add(SPORT_TENNIS.getKey() + "p165");
        specialPlay.add(SPORT_TENNIS.getKey() + "p166");

        specialPlay.add(SPORT_BADMINTON.getKey() + "p175");
        specialPlay.add(SPORT_BADMINTON.getKey() + "p176");
        specialPlay.add(SPORT_BADMINTON.getKey() + "p177");
        specialPlay.add(SPORT_BADMINTON.getKey() + "p178");

        specialPlay.add(SPORT_SNOOKER.getKey() + "p184");
        specialPlay.add(SPORT_SNOOKER.getKey() + "p185");
        specialPlay.add(SPORT_SNOOKER.getKey() + "p186");
        specialPlay.add(SPORT_SNOOKER.getKey() + "p187");

        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p261");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p262");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p263");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p264");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p265");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p266");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p267");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p268");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p297");
        specialPlay.add(SPORT_ICEHOCKEY.getKey() + "p298");

        specialPlay.add(SPORT_BASEBALL.getKey() + "p275");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p276");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p280");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p281");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p282");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p283");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p287");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p288");
        specialPlay.add(SPORT_BASEBALL.getKey() + "p289");

        specialPlay.add(SPORT_PINGPONG.getKey() + "p175");
        specialPlay.add(SPORT_PINGPONG.getKey() + "p176");
        specialPlay.add(SPORT_PINGPONG.getKey() + "p177");
        specialPlay.add(SPORT_PINGPONG.getKey() + "p178");
        //-------------------------------------------------------------specialPlay-------------------------------------------------------------end


        //-------------------------------------------------------------superSpecialPlay-------------------------------------------------------------start
        superSpecialPlay.add(SPORT_TENNIS.getKey() + "p167");
        superSpecialPlay.add(SPORT_TENNIS.getKey() + "p168");

        superSpecialPlay.add(SPORT_BADMINTON.getKey() + "p179");

        superSpecialPlay.add(SPORT_PINGPONG.getKey() + "p179");

        superSpecialPlay.add(SPORT_PINGPONG.getKey() + "p188");
        superSpecialPlay.add(SPORT_PINGPONG.getKey() + "p195");
        //-------------------------------------------------------------superSpecialPlay-------------------------------------------------------------end


        //-------------------------------------------------------------fiveMinitesPlay-------------------------------------------------------------start
        fiveMinitesPlay.add(SPORT_FOOTBALL.getKey() + "p362");
        //-------------------------------------------------------------fiveMinitesPlay-------------------------------------------------------------end


        //-------------------------------------------------------------fifteenPlay-------------------------------------------------------------start
        fifteenPlay.add(SPORT_FOOTBALL.getKey() + "p32");
        fifteenPlay.add(SPORT_FOOTBALL.getKey() + "p33");
        fifteenPlay.add(SPORT_FOOTBALL.getKey() + "p34");
        fifteenPlay.add(SPORT_FOOTBALL.getKey() + "p231");
        fifteenPlay.add(SPORT_FOOTBALL.getKey() + "p232");
        fifteenPlay.add(SPORT_FOOTBALL.getKey() + "p233");

        fifteenPlayTimeMap.put("115", 1);
        fifteenPlayTimeMap.put("1630", 2);
        fifteenPlayTimeMap.put("3145", 3);
        fifteenPlayTimeMap.put("4660", 4);
        fifteenPlayTimeMap.put("6175", 5);
        fifteenPlayTimeMap.put("7690", 6);
        //-------------------------------------------------------------fifteenPlay-------------------------------------------------------------end


        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10011.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10012.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10013.getKey());

        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10021.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10022.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10023.getKey());

        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10031.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10032.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10033.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10034.getKey());

        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10041.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10042.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10043.getKey());

        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10051.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10052.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10053.getKey());

        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10061.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10062.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10063.getKey());
        fiveMinitesPlayScoreKeyEnum.add(ScoreEnum.SCORE_S10064.getKey());
    }
}
