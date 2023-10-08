package com.panda.multiterminalinteractivecenter.enums;

/**
 * @author :  ifan
 * @Description :  商户日志页面配置
 * @Date: 2022-07-11
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
    MERCHANT_INFO_MANAGER_AGENT_1(1006, 1004, 1, "merchant_info_manager_agent_1", "商户中心-商户管理-渠道商户", "Merchant Manage Sec-Agent"),
    MERCHANT_INFO_MANAGER_AGENT_2(1007, 1004, 1, "merchant_info_manager_agent_2", "商户中心-商户管理-二级商户", "Merchant Manage Third_Agent"),
    MERCHANT_INFO_MANAGER_AGENT_10(1007, 1004, 1, "merchant_info_manager_agent_10", "商户中心-商户管理-代理商", "Merchant Manage First_Agent"),
    MERCHANT_INFO_betting_USER_MANAGER(1008, 1003, 1, "merchant_info_betting_user_manager", "商户中心-投注用户管理", "Betting user Manage"),

    MERCHANT_INFO_KEY(1008, 1003, 1, "merchant_info_key", "商户中心-证书管理", "Merchant Manage Key"),
    FINANCE_IN(1009, 0, 1, "finance", "财务中心", "Finance Center"),
    FINANCE_IN_MANAGER(1010, 1009, 1, "finance_manager", "财务中心-清算管理", "Finance Clearing"),
    MES_IN(1013, 0, 1, "mes_in", "消息中心", "Message Center"),
    MES_IN_CENT(1014, 1013, 1, "mes_in_cent", "消息中心-公告栏", "Message Billboard"),
    MES_IN_MY(1015, 1013, 1, "mes_in_my", "消息中心-我的消息", "My Message"),
    SET(1000, 0, 1, "set", "设置中心", "Setting UP"),
    SET_MERCHANT_LEVEL(1016, 1000, 1, "set_merchant_level", "设置中心-商户等级设置", "Set Merchant Level"),
    SET_MERCHANT_RATE(1017, 1000, 1, "set_merchant_rate", "设置中心-平台费率设置", "Set Merchant Rate"),
    SET_MERCHANT_domain(1018, 1000, 1, "set_merchant_domain", "设置中心-商户域名设置", "Set Merchant domain"),
    SET_MERCHANT_kanacode(1019, 1000, 1, "set_merchant_kanacode", "设置中心-公司代码设置", "Set Merchant kanacode"),
    SET_MERCHANT_Domian(1022, 1000, 1, "set_merchant_domain", "设置中心-域名管理", "Set Merchant domain"),

    SET_BOX_NUMER(1100002,0,1,"set_box_numer","盲盒奖品设置","Set Box Number"),
    SET_MERCHANT_ACTIVITY(1100003, 0, 1, "set_merchant_activity", "运营活动设置", "Set Merchant Activity"),

    DATA_CENTER(1020, 0, 1, "data_center", "数据中心", "Data Center"),
    DATA_CENTER_ORDER(1021, 1020, 1, "data_center_order", "数据中心-注单查询", "Data Center Order"),


    DOMAIN_CENTER(1024, 0, 1, "domain_center", "域名管理", "Domain Center"),

    PLUS_DEDUCTION(27, 2, 1, "PLUS_DEDUCTION", "财务中心-手动加扣款", "PLUS DEDUCTION"),

    ALL(0, 999, 2, "all", "全部", "ALL"),
    MAINTENANCE_SET(10, 0, 2, "maintenance_set", "维护设置", "MAINTENANCE SET"),
    DOMAIN_SET_NEW(11, 0, 2, "domain_set_new", "域名设置新", "DOMAIN SET NEW"),
    SYSTEM_MANAGE(12, 0, 2, "system_manage", "系统管理", "SYSTEM MANAGE"),
    DOMAIN_SET_OLD(13, 0, 2, "domain_set_old", "域名设置旧", "DOMAIN SET OLD"),

    MAINTENANCE_CONSOLE(20, 10, 2, "maintenance_console", "维护设置-维护控制台", "maintenance console"),
    KICK_USER(21, 10, 2, "kick_user", "维护设置-踢用户", "kick user"),

    LINE_CARRIER(28, 11, 2, "line_carrier", "域名设置新-线路商管理", "LINE CARRIER"),
    AREA_MANAGE(29, 11, 2, "area_manage", "域名设置新-区域管理", "AREA MANAGE"),
    DOMAIN_MANAGE(30, 11, 2, "domain_manage", "域名设置新-域名管理", "DOMAIN MANAGE"),
    DOMAIN_GROUP_MANAGE(31, 11, 2, "domain_group_manage", "域名设置新-域名组管理", "DOMAIN GROUP MANAGE"),
    DOMAIN_PROGRAM(32, 11, 2, "domain_program", "域名设置新-域名切换方案管理", "DOMAIN PROGRAM"),
    DOMAIN_PROGRAM_INFO(321, 32, 2, "domain_program_info", "域名设置新-域名切换方案管理-方案详情", "DOMAIN PROGRAM INFO"),
    MERCHANT_GROUP(33, 11, 2, "merchant_group", "域名设置新-商户组设置", "MERCHANT GROUP"),
    DOMAIN_SOLD_OUT(34, 11, 2, "domain_sold_out", "域名设置新-域名下架", "DOMAIN SOLD OUT"),
    ABNORMAL_IP(35, 11, 2, "abnormal_ip", "域名设置新-异常ip池管理", "ABNORMAL IP DOMAIN POOL"),
    ABNORMAL_IP_OLD(41, 13, 2, "abnormal_ip", "域名设置旧-异常ip池管理", "ABNORMAL IP DOMAIN POOL OLD"),

    MENU_MANAGE(36, 12, 2, "menu_manage", "系统管理-菜单管理", "MENU MANAGE"),
    USER_MANAGE(37, 12, 2, "user_manage", "系统管理-用户管理", "USER MANAGE"),
    ROLE_MANAGE(38, 12, 2, "role_manage", "系统管理-角色管理", "ROLE MANAGE"),
    GLOBAL_INTERFACE(39, 13, 2, "global_interface", "域名设置旧-全局接口配置", "GLOBAL INTERFACE"),
    VIDEO_DOMAIN_SET(40, 13, 2, "video_domain_set", "域名设置旧-视频域名设置", "VIDEO DOMAIN SET"),
    VIDEO_DOMAIN_POOL_SET(401, 40, 2, "video_domain_pool_set", "域名设置旧-视频域名设置-域名池设置", "VIDEO DOMAIN POOL SET"),
    FRONT_DOMAIN_SET(42, 13, 2, "front_domain_set", "域名设置旧-前端域名设置", "FRONT DOMAIN SET"),
    FRONT_DOMAIN_POOL_SET(421, 42, 2, "front_domain_pool_set", "域名设置旧-前端域名设置-域名池设置", "FRONT DOMAIN POOL SET"),
    API_DOMAIN_SET(43, 13, 2, "api_domain_set", "域名设置旧-api域名设置", "API DOMAIN SET"),
    API_DOMAIN_POOL_SET(431, 43, 2, "api_domain_pool_set", "域名设置旧-api域名设置-域名池设置", "API DOMAIN POOL SET"),

    //manage 异常ip 页面
    SET_MERCHANT_INFO_MANAGER_AGENT_45(1045, 1004, 1, "merchant_info_manager_agent_45", "异常ip管理-导入异常ip ", "Abnormal Ip Manage"),

    SET_MERCHANT_INFO_MANAGER_AGENT_59(1059, 1004, 1, "merchant_info_manager_agent_59", "异常ip管理-删除", "Abnormal Ip Manage Delete"),

    SET_MERCHANT_INFO_MANAGER_AGENT_58(1058, 1004, 1, "merchant_info_manager_agent_58", "异常ip管理-全部移除 ", "Abnormal Ip All Delete"),


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
     * 页面标识 0 外部商户 1内部商户 2 三端
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
            pageEnum = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_2;

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
