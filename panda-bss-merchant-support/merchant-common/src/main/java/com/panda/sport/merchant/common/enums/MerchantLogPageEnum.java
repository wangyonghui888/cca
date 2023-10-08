package com.panda.sport.merchant.common.enums;

import java.util.Arrays;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.enums
 * @Description :  商户日志页面配置
 * @Date: 2020-09-02 11:28
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public enum MerchantLogPageEnum {

    ACCOUNT(2, 0, 0, "account", "账户中心", "User Center"),
    BASIC(3, 2, 0, "basic", "账户中心-商户信息管理", "MerchantInfo"),
    SECONDARY(4, 2, 0, "secondary", "账户中心-二级商户管理", "ChildrenManage"),
    MY_KEY(5, 2, 0, "mykey", "账户中心-我的证书", "My Key"),
    //BETTING(6,2,0,"betting","账户中心-投注用户管理"),
    AUTH(7, 0, 0, "auth", "授权中心", "Authorization"),
    USER(8, 7, 0, "user", "授权中心-用户管理", "User Manage"),
    ROLE(9, 7, 0, "role", "授权中心-角色管理", "Role Manage"),
    /*    FINANCE(10,0,0,"finance","财务中心"),
        LIQUIDATION(11,10,0,"liquidation","财务中心-电子账单"),
        STATEMENTS(12,10,0,"statements","财务中心-对账单"),*/
/*    DATA_CENTER(13,0,0,"data_center","数据中心"),
    BET_SLIP(14,13,0,"bet_slip","数据中心-注单查询"),
    MERCHANT_NOTE(15,13,0,"merchant_note","数据中心-商户注单统计"),
    USERS(16,13,0,"users","数据中心-用户投注统计"),*/
    //MESSAGE_CENTER(17,0,0,"message_center","消息中心"),
    //BULLETIN(18,17,0,"bulletin","消息中心-公告栏"),
    //MY_MESSAGE(19,17,0,"mymessage","消息中心-我的消息"),
    //内部商户端
    MERCHANT_CENTER(1003, 0, 1, "merchant_center", "商户中心", "Merchant Center"),
    MERCHANT_INFO_MANAGER(1004, 1003, 1, "merchant_info_manager", "商户中心-商户管理", "Merchant Manage"),
    MERCHANT_INFO_MANAGER_AGENT_0(1005, 1004, 1, "merchant_info_manager_agent_0", "商户中心-商户管理-直营商户", "Merchant Manage Direct"),

    MERCHANT_INFO_MANAGER_AGENT_9(1026, 1004, 1, "merchant_info_manager_agent_9", "商户中心-商户管理-直营商户-聊天室", "Merchant Manage Direct ChatRoom"),

    MERCHANT_INFO_MANAGER_AGENT_12(1028, 1004, 1, "merchant_info_manager_agent_11", "商户中心-商户管理-直营商户-设置管理员", "Merchant Manage Direct Set"),

    MERCHANT_INFO_MANAGER_AGENT_11(1027, 1004, 1, "merchant_info_manager_agent_27", "商户中心-商户管理-直营商户-C端默认语言", "Merchant Manage Direct Default Language"),

    MERCHANT_INFO_MANAGER_AGENT_13(1029, 1004, 1, "merchant_info_manager_agent_29", "商户中心-商户管理-直营商户-ip白名单设置", "Merchant Manage Direct Default whiteIp"),

    MERCHANT_INFO_MANAGER_AGENT_14(1030, 1004, 1, "merchant_info_manager_agent_30", "商户中心-商户管理-直营商户-C端综合设置", "Merchant Manage Direct Comprehensive"),

    MERCHANT_INFO_MANAGER_AGENT_15(1031, 1004, 1, "merchant_info_manager_agent_31", "商户中心-商户管理-直营商户-商户球种设置", "Merchant Manage Direct Merchant Ball"),

    MERCHANT_INFO_MANAGER_AGENT_16(1032, 1004, 1, "merchant_info_manager_agent_32", "商户中心-商户管理-渠道商戶-新建渠道商户", "Merchant Manage Sec-Agent add"),

    TASK_DAY_SET(1110,2, 0, "task_day_set_order", "任务设置-体育任务设置-每日任务", "Task day Set Order"),

    BOOK_DELETE(1115,2, 0, "book_set_delete", "赛事文章-采集管理", "Book manage Delete"),

    TAG_MANAGE(1116,2, 0, "tag_manage", "赛事文章-标签管理", "Tag manage"),

    AUTHOR_MANAGE(1117,2, 0, "author_manage", "赛事文章-作者管理", "Author manage"),

    OPERATION_BOX_XET(1113,2, 0, "operation_box_set_order", "运营管理-盲盒奖品设置-体育盲盒奖品设置", "Operation Box Set Order"),

    MATCH_FILE_MANAGE(1112,2, 0, "match_file_manage", "赛事文章-采集管理", "Match File Manage"),

    TASK_BOX_SET1(1114,2, 0, "task_day_set_box_order", "游戏配置-拖曳排序", "Task Box Set Order"),

    MERCHANT_INFO_MANAGER_AGENT_17(1033, 1004, 1, "merchant_info_manager_agent_33", "商户中心-商户管理-渠道商戶", "Merchant Manage Sec-Agent add Manager"),

    MERCHANT_INFO_MANAGER_AGENT_18(1034, 1004, 1, "merchant_info_manager_agent_33", "商户中心-商户管理-二级商户", "Merchant Manage Third_Agent add Manager"),

    MERCHANT_INFO_MANAGER_AGENT_20(1036, 1004, 1, "merchant_info_manager_agent_36", "商户中心-代理商管理", "Merchant Manage Third_Agent add Agent"),

    MERCHANT_INFO_MANAGER_AGENT_19(1035, 1004, 1, "merchant_info_manager_agent_35", "商户中心-商户管理-直营商户", "Merchant Manage Direct add Manager"),

    MERCHANT_INFO_MANAGER_AGENT_21(1038, 1004, 1, "merchant_info_manager_agent_37", "商户中心-商户管理-代理商管理户-新增下级商户", "Merchant Manage Third_Agent Add subordinate merchants"),

    MERCHANT_INFO_MANAGER_AGENT_47(1047, 1004, 1, "merchant_info_manager_agent_47", "设置中心-域名设置-TY域名池-详细商户-添加商户", "Merchant Manage Direct add Manager Detail add"),

    MERCHANT_INFO_MANAGER_AGENT_48(1048, 1004, 1, "merchant_info_manager_agent_48", "设置中心-域名设置-TY域名池-详细商户-删除确认", "Merchant Manage Direct add Manager Detail del"),

    MERCHANT_INFO_MANAGER_AGENT_49(1049, 1004, 1, "merchant_info_manager_agent_49", "设置中心-域名设置-TY域名池-详细商户-切换频率", "Merchant Manage Direct add Manager Update num"),


    MERCHANT_INFO_MANAGER_AGENT_50(1050, 1004, 1, "merchant_info_manager_agent_50", "设置中心-域名设置-TY域名池-详细商户-域名提醒设置", "Merchant Manage Domain Reminder set"),

    MERCHANT_INFO_MANAGER_AGENT_51(1051, 1004, 1, "merchant_info_manager_agent_51", "设置中心-域名设置-TY域名池-禁用/启用", "Merchant Manage Domain Status set"),

    MERCHANT_INFO_MANAGER_AGENT_52(1052, 1004, 1, "merchant_info_manager_agent_52", "运营活动设置-活动入口設置", "Operation activity settings set"),


    MERCHANT_INFO_MANAGER_AGENT_72(1072, 1004, 1, "merchant_info_manager_agent_72", "运营管理-运营位-新建运营位", "Operation activity settings Add"),

    MERCHANT_INFO_MANAGER_AGENT_74(1074, 1004, 1, "merchant_article_manager_agent_74", "赛事文章-文章管理", "Operation Article Manage Add"),

    MERCHANT_INFO_MANAGER_AGENT_1(1006, 1004, 1, "merchant_info_manager_agent_1", "商户中心-商户管理-渠道商户", "Merchant Manage Sec-Agent"),
    MERCHANT_INFO_MANAGER_AGENT_2(1007, 1004, 1, "merchant_info_manager_agent_2", "商户中心-商户管理-二级商户", "Merchant Manage Third_Agent"),
    MERCHANT_INFO_MANAGER_AGENT_10(1015, 1004, 1, "merchant_info_manager_agent_10", "商户中心-商户管理-代理商", "Merchant Manage First_Agent"),
    MERCHANT_INFO_betting_USER_MANAGER(1008, 1003, 1, "merchant_info_betting_user_manager", "商户中心-投注用户管理", "Betting user Manage"),

    MERCHANT_INFO_KEY(1008, 1003, 1, "merchant_info_key", "商户中心-证书管理", "Merchant Manage Key"),
    MERCHANT_INFO_EDITKEY(1026, 1003, 1, "merchant_info_key_edit", "商户中心-证书管理-更新证书", "Merchant Manage Edit Key"),
    FINANCE_IN(1009, 0, 1, "finance", "财务中心", "Finance Center"),
    FINANCE_IN_MANAGER(1010, 1009, 1, "finance_manager", "财务中心-清算管理", "Finance Clearing"),
    MES_IN(1013, 0, 1, "mes_in", "消息中心", "Message Center"),
    MES_IN_CENT(1014, 1013, 1, "mes_in_cent", "消息中心-公告栏", "Message Billboard"),
    MES_IN_CENT_DELETE(1064, 1013, 1, "mes_in_cent_delete", "消息中心-公告栏-删除公告", "Message Billboard Delete"),

    MES_IN_CENT_RELEASE(1065, 1013, 1, "mes_in_cent_release", "消息中心-公告栏-取消公告", "Message Billboard Release"),
    MES_IN_MY(1015, 1013, 1, "mes_in_my", "消息中心-我的消息-删除消息", "My Message"),
    SET(1000, 0, 1, "set", "设置中心", "Setting UP"),
    SET_MERCHANT_LEVEL(1016, 1000, 1, "set_merchant_level", "设置中心-商户等级设置", "Set Merchant Level"),
    SET_MERCHANT_RATE(1017, 1000, 1, "set_merchant_rate", "设置中心-平台费率设置", "Set Merchant Rate"),
    SET_MERCHANT_domain(1018, 1000, 1, "set_merchant_domain", "设置中心-商户域名设置", "Set Merchant domain"),

    SET_MERCHANT_domain_edit(1040, 1000, 1, "set_merchant_domain", "设置中心-商户前端域名管理-商户根域名更新", "Set Merchant domain edit"),

    SET_MERCHANT_Appdomain_edit(1041, 1000, 1, "set_merchant_appdomain", "设置中心-商户App域名管理-商户根域名更新", "Set Merchant appdomain edit"),


    SET_MERCHANT_APPDOMAIN_ALL_edit(1042, 1000, 1, "set_merchant_app_all_domain", "设置中心-商户App域名管理-批量更新", "Set Merchant appdomain all edit"),


    SET_MERCHANT_INFO_MANAGER_AGENT_43(1043, 1004, 1, "merchant_info_manager_agent_43", "域名设置-TY域名池-数据商动画域名设置", "Set animation domain name"),

    SET_MERCHANT_INFO_MANAGER_AGENT_55(1055, 1004, 1, "data_synchronization_tool_55", "数据同步工具-对账单数据同步", "Synchronization Bill"),

    SET_MERCHANT_INFO_MANAGER_AGENT_56(1056, 1004, 1, "data_synchronization_tool_56", "数据同步工具-用户投注统计同步", "Synchronization Order"),

    SET_MERCHANT_INFO_MANAGER_AGENT_57(1057, 1004, 1, "data_synchronization_tool_57", "数据同步工具-赛事投注统计同步", "Synchronization Match"),


    SET_TASK_MANAGER_EXPORT_58(1058, 1004, 1, "data_synchronization_tool_58", "任务中心-我的导出任务", "Center Task  Export"),


    SET_MERCHANT_INFO_MANAGER_AGENT_45(1045, 1004, 1, "merchant_info_manager_agent_45", "异常ip管理-导入异常ip ", "Abnormal Ip Manage"),

    SET_MERCHANT_INFO_MANAGER_AGENT_59(1059, 1004, 1, "merchant_info_manager_agent_59", "异常ip管理-删除", "Abnormal Ip Manage Delete"),

    SET_MERCHANT_INFO_MANAGER_AGENT_58(1058, 1004, 1, "merchant_info_manager_agent_58", "异常ip管理-全部移除 ", "Abnormal Ip All Delete"),

    SET_MERCHANT_INFO_MANAGER_AGENT_46(1046, 1004, 1, "merchant_info_manager_agent_46", "运营活动设置-一键开启设置", "Activity Set Open"),

    SET_MERCHANT_INFO_MANAGER_AGENT_44(1044, 1004, 1, "merchant_info_manager_agent_44", "设置中心-域名设置-TY域名池-详细商户-17ce", "Set animation domain 17ce"),

    SET_MERCHANT_kanacode(1019, 1000, 1, "set_merchant_kanacode", "设置中心-公司代码设置", "Set Merchant kanacode"),

    SET_MERCHANT_Domian(1022, 1000, 1, "set_merchant_domain", "设置中心-域名管理", "Set Merchant doman"),

    SET_BOX_NUMER(1100002,0,1,"set_box_numer","盲盒奖品设置","Set Box Number"),
    SET_MERCHANT_ACTIVITY(1100003, 0, 1, "set_merchant_activity", "运营活动设置-活动开关", "Set Merchant Activity"),

    SET_MERCHANT_ACTIVITY_CONFIG(1100005, 0, 1, "set_merchant_activity", "运营活动设置-活动配置", "Set Merchant Activity Config"),
    SET_MERCHANT_ACTIVITY_SET(1100004, 0, 1, "set_merchant_activity", "运营活动设置-活动时间设置", "Set Merchant Activity Time"),

    DATA_CENTER(1020, 0, 1, "data_center", "数据中心", "Data Center"),
    DATA_CENTER_ORDER(1021, 1020, 1, "data_center_order", "数据中心-注单查询", "Data Center Order"),


    DOMAIN_CENTER(1024, 0, 1, "domain_center", "域名管理", "Domain Center"),

    DOMAIN_NAME_SETTINGS(1025, 0, 1, "domain_name_settings", "设置中心-域名设置-TY域名-手动切换", "domain TY name settings"),

    DOMAIN_NAME_DJ_SETTINGS(1026, 0, 1, "domain_name_settings", "设置中心-域名设置-DJ域名-手动切换", "domain DJ name settings"),

    DOMAIN_NAME_CP_SETTINGS(1027, 0, 1, "domain_name_settings", "设置中心-域名设置-CP域名-手动切换", "domain CP name settings"),
    PLUS_DEDUCTION(27, 2, 1, "PLUS_DEDUCTION", "商户中心-投注用戶管理-帐变记录-加扣款", "PLUS DEDUCTION"),

    PLUS_DATA_DEDUCTION(28, 2, 1, "PLUS_DATA_DEDUCTION", "数据中心-交易记录查询-帐变记录-加扣款", "PLUS Data DEDUCTION"),

    UPLOAD_VIPUSER  (1028, 2, 1, "UPLOAD_VIPUSER", "商户中心-投注用戶管理-导入线路vip", "UPLOAD VIPUSER "),


    BATCH_DISABLE(1029, 2, 1, "BATCH_DISABLE", "商户中心-投注用戶管理-批量启&禁用", "BATCH DISABLED"),

    SYSTEM_SWITCH(1030, 2, 1, "SYSTEM_SWITCH", "商户中心-系统级别开关-视频流量管控", "SYSTEM SWITCH"),

    CHATROOM_SWITCH(1031, 2, 1, "CHATROOM_SWITCH", "商户中心-系统级别开关-视频流量管控", "CHATROOM SWITCH"),

    MERCHANT_LEVEL_SET(1032, 2, 1, "MERCHANT_LEVEL_SET", "设置中心-商户等级设置2-编辑", "MERCHANT LEVEL SET"),

    MERCHANT_TRATE_SET(1033, 2, 1, "MERCHANT_TRATE_SET", "设置中心-平台汇率设置-新建费率", "MERCHANT TRATE SET"),
    UPLOAD_CHANGE_MERCHANT_NAME(1034, 2, 1, "UPLOAD_CHANGE_MERCHANT_NAME", "设置中心-脱敏设置", "DESENSITIZATION SET"),

    MERCHANT_ACTIVITY_EDIT(1061, 2, 1, "MERCHANT_ACTIVITY_EDIT", "运营活动设置-活动维护设置", "MERCHANT ACTIVITY EDIT"),
    SET_MERCHANT_ACTIVITY_TIME(1062, 2, 1, "SET_MERCHANT_ACTIVITY_TIME", "活动时间设置", "SET_MERCHANT_ACTIVITYTIME"),

    MERCHANT_INFO_MANAGER_AGENT_62(1063, 1004, 1, "merchant_info_manager_agent_62", "设置中心-域名设置-DJ域名池-详细商户-添加商户", "Merchant Manage djDirect add Manager Detail add"),

    MERCHANT_INFO_MANAGER_AGENT_63(1064, 1004, 1, "merchant_info_manager_agent_63", "设置中心-域名设置-DJ域名池-详细商户-删除确认", "Merchant Manage djDirect add Manager Detail del"),

    MERCHANT_INFO_MANAGER_AGENT_64(1065, 1004, 1, "merchant_info_manager_agent_64", "设置中心-域名设置-DJ域名池-详细商户-切换频率", "Merchant Manage djDirect add Manager Update num"),

    MERCHANT_INFO_MANAGER_AGENT_65(1066, 1004, 1, "merchant_info_manager_agent_65", "设置中心-域名设置-DJ域名池-详细商户-域名提醒设置", "Merchant Manage djDomain Reminder set"),

    MERCHANT_INFO_MANAGER_AGENT_66(1067, 1004, 1, "merchant_info_manager_agent_66", "设置中心-域名设置-DJ域名池-禁用/启用", "Merchant Manage djDomain Status set"),


    MERCHANT_INFO_MANAGER_AGENT_67(1068, 1004, 1, "merchant_info_manager_agent_67", "设置中心-域名设置-cp域名池-详细商户-添加商户", "Merchant Manage cpDirect add Manager Detail add"),

    MERCHANT_INFO_MANAGER_AGENT_68(1069, 1004, 1, "merchant_info_manager_agent_68", "设置中心-域名设置-cp域名池-详细商户-删除确认", "Merchant Manage cpDirect add Manager Detail del"),

    MERCHANT_INFO_MANAGER_AGENT_69(1070, 1004, 1, "merchant_info_manager_agent_69", "设置中心-域名设置-cp域名池-详细商户-切换频率", "Merchant Manage cpDirect add Manager Update num"),

    MERCHANT_INFO_MANAGER_AGENT_70(1071, 1004, 1, "merchant_info_manager_agent_70", "设置中心-域名设置-cp域名池-详细商户-域名提醒设置", "Merchant Manage cpDomain Reminder set"),

    MERCHANT_INFO_MANAGER_AGENT_71(1072, 1004, 1, "merchant_info_manager_agent_71", "设置中心-域名设置-cp域名池-禁用/启用", "Merchant Manage cpDomain Status set"),

    SET_MERCHANT_INFO_MANAGER_AGENT_72(1073, 1004, 1, "merchant_info_manager_agent_72", "域名设置-TY域名池-商户默认视频域名设置", "Set merchant default videoDomain name"),

    SET_MERCHANT_INFO_MANAGER_AGENT_73(1074, 1004, 1, "live_matches_anchor_management_73", "赛事直播-主播管理-新增编辑启用禁用主播", "Anchor management add, edit, enable, disable anchors"),

    SET_MERCHANT_INFO_MANAGER_AGENT_74(1075, 1004, 1, "live_matches_live_management_74", "赛事直播-直播管理-排版、编辑、开始直播、结束直播和删除直播", "Live management layout, edit, start, end and delete live broadcast"),

    SET_MERCHANT_INFO_MANAGER_AGENT_75(1076, 1004, 1, "live_matches_live_management_75", "赛事直播-直播管理-赛前节目上架下架编辑删除", "Live broadcast management pre-game programs on, off, edit, delete"),

    SET_MERCHANT_INFO_MANAGER_AGENT_76(1077, 1004, 1, "live_matches_live_management_76", "赛事直播-内容管理-聊天室列表", "Live matches-Content Management-Chat Room List"),

    SET_MERCHANT_INFO_MANAGER_AGENT_77(1078, 1004, 1, "live_matches_live_management_77", "赛事直播-内容管理-聊天室公告", "Live matches-Content Management-Chat room announcement"),

    SET_MERCHANT_INFO_MANAGER_AGENT_78(1079, 1004, 1, "live_matches_live_management_78", "赛事直播-内容管理-内容审核", "Live matches-Content Management-Content review"),

    SET_MERCHANT_INFO_MANAGER_AGENT_79(1080, 1004, 1, "live_matches_live_management_79", "赛事直播-内容管理-敏感词管理", "Live matches-Content Management-Sensitive word management"),

    SET_MERCHANT_INFO_MANAGER_AGENT_80(1081, 1004, 1, "live_matches_live_management_80", "赛事直播-内容管理-异常会员", "Live matches-Content Management-Abnormal Members"),

    SET_MERCHANT_INFO_MANAGER_AGENT_81(1082, 1004, 1, "live_matches_live_management_81", "赛事直播-内容管理-超级会员", "Live matches-Content Management-Super Member"),

    SET_MERCHANT_INFO_MANAGER_AGENT_82(1083, 1004, 1, "live_matches_live_management_82", "三端-前端域名设置-商户分组", "multiterminal-frontend domain name settings-merchant grouping"),
    SET_MERCHANT_INFO_MANAGER_AGENT_83(1084, 1004, 1, "live_matches_live_management_83", "三端-前端域名设置-跳转域名设置", "multiterminal-frontend domain name settings-skip-domain-setting"),
    SET_MERCHANT_INFO_MANAGER_AGENT_84(1085, 1004, 1, "live_matches_live_management_84", "设置中心-域名设置-TY域名池-域名池设置", "ty domain pool set"),



    //三端
    MERCHANT_GROUP(33, 11, 2, "merchant_group", "域名设置新-商户组设置", "MERCHANT GROUP"),

    API_DOMAIN_SET(43, 13, 2, "api_domain_set", "域名设置旧-api域名设置", "API DOMAIN SET"),

    API_DOMAIN_POOL_SET(431, 43, 2, "api_domain_pool_set", "域名设置旧-api域名设置-域名池设置", "API DOMAIN POOL SET"),



    ;

    /**
     * 页面编码
     */
    private String code;

    /**
     * 页面ID
     */
    private Integer id;

    /**
     * 父级ID
     */
    private Integer prentId;

    /**
     * 页面标识 0 外部商户 1内部商户
     */
    private Integer tag;

    /**
     * 页面描述
     */
    private String remark;

    private String en;

    private MerchantLogPageEnum(Integer id, Integer prentId, Integer tag, String code, String remark, String en) {
        this.code = code;
        this.remark = remark;
        this.id = id;
        this.prentId = prentId;
        this.tag = tag;
        this.en = en;
    }

    public static MerchantLogPageEnum getMerchantLogPageEnumByAgentLevel(Integer agentLevel) {
        MerchantLogPageEnum pageEnum = null;
        if (AgentLevelEnum.AGENT_LEVEL_0.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0;
        } else if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_1;
        } else if (AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_2;
        } else if (AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_10;

        }
        return pageEnum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrentId() {
        return prentId;
    }

    public void setPrentId(Integer prentId) {
        this.prentId = prentId;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
