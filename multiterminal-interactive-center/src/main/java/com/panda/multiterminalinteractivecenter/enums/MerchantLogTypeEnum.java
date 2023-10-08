package com.panda.multiterminalinteractivecenter.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author :  ifan
 * @Description :  商户日志类型枚举
 * @Date: 2022-07-11
 */
public enum MerchantLogTypeEnum {


    EDIT_INFO(2, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2","api_domain_set"), "编辑", "Edit"),
    SET_MANAGER(3, Arrays.asList("merchant_info_manager_agent_0", "merchant_info_manager_agent_1",
            "merchant_info_manager_agent_2", "secondary"), "设置超级管理员", "Set Up Administrator"),
    NEW_CREATE_MERCHANT(1, Arrays.asList("merchant_info_manager_agent_0"), "新建直营商户", "Create DirectMerchant"),
    NEW_CREATE_CHANNEL_MERCHANT(4, Arrays.asList("merchant_info_manager_agent_1"), "新建渠道商户", "CreateChannelMerchant"),
    NEW_LEVEL_TOW_MERCHANT(5, Arrays.asList("merchant_info_manager_agent_2", "secondary"), "新建二级商户",
            "CreateChildrenMerchant"),
    NEW_AGENT_TOW_MERCHANT(105, Collections.singletonList("merchant_info_manager_agent_10"), "新建代理商户",
            "CreateBigAgent"),

    NEW_CREATE_NEWS(6, Collections.singletonList("bulletin"), "发布公告", "PublishAnnoucememnt"),
    DEL_NEWS(7, Collections.singletonList("bulletin"), "删除公告", "DeleteAnnouncement"),
    USER_MANAGER_ADD(10, Collections.singletonList("user"), "新增用户", "CreateUser"),
    USER_MANAGER_UPDATE(11, Collections.singletonList("user"), "修改用户", "EditUser"),
    USER_PASSWORD_UPDATE(111, Collections.singletonList("user_password"), "修改用户密码", "EditUserPassword"),
    USER_MANAGER_DELETE(12, Collections.singletonList("user"), "删除用户", "DeleteUser"),
    EDIT_INFO_STATUS(13, Arrays.asList("secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1",
            "merchant_info_manager_agent_2","line_carrier","area_manage","domain_manage","domain_group_manage","domain_program",
            "merchant_group","menu_manage","user_manage","video_domain_set","front_domain_set","api_domain_set"), "启用&禁用", "Open&Close"),
    EDIT_SUB_INFO_STATUS(1013, Arrays.asList("secondary", "merchant_info_manager_agent_0",
            "merchant_info_manager_agent_1", "merchant_info_manager_agent_2", "merchant_info_manager_agent_10"),
            "删除代理", "DeleteAgent"),


    ROLE_ADD(14, Collections.singletonList("role"), "创建角色", "CreateRole"),
    ROLE_SET(15, Arrays.asList("role"), "设置角色权限", "Set up Role Authoritation"),
    ROLE_EDIT(16, Arrays.asList("role"), "修改角色", "ModifyRole"),
    ROLE_DELETE(17, Arrays.asList("role"), "删除角色", "DeleteRole"),
    MSG_DELETE(18, Arrays.asList("mes_in_my"), "删除消息", "DeleteMessage"),
    FINANCE_EDIT(19, Arrays.asList("finance_manager"), "调整电子账单", "ModifyBill"),
    NEW_MERCHANT_LEVEL(20, Arrays.asList("set_merchant_level"), "新建商户等级", "CreateMerchantLevel"),
    EDIT_MERCHANT_LEVEL(21, Arrays.asList("set_merchant_level"), "编辑商户等级", "EditMerchantLevel"),
    NEW_MERCHANT_RATE(22, Arrays.asList("set_merchant_rate"), "新建平台费率", "CreateRate"),
    EDIT_MERCHANT_RATE(23, Arrays.asList("set_merchant_rate"), "编辑平台费率", "ModifyRate"),
    CENT_ADD(24, Arrays.asList("mes_in_cent"), "发布公告", "Publish Announcement"),
    CENT_DELETE(25, Arrays.asList("mes_in_cent"), "删除草稿", "Delete Draft"),
    CENT_EDIT(26, Arrays.asList("mes_in_cent"), "编辑公告", "ModifyBill"),
    CENT_CANCEL(27, Arrays.asList("mes_in_cent"), "取消发布", "CancelAnnouncenment"),

    EDIT_MERCHANT_INFO_KEY(28, Arrays.asList("merchant_info_key", "mykey"), "更新证书", "ModifyKey"),
    SET_SECONDARY(29, Arrays.asList("merchant_info_manager_agent_1"), "设置二级商户", "Set up Children"),
    EDIT_INFO_PARENT_ID(31, Arrays.asList("basic"), "代理商添加渠道商户", "CreateChannelToBigAgent"),
    EDIT_MERCHANT__DOMAIN(32, Arrays.asList("set_merchant_domain"), "商户域名更新", "ModifyDomain"),
    DELETE_BET_ORDER(34, Arrays.asList("cancel_bet"), "注单取消", "CancelBet"),
    EDIT_MERCHANT_KANACODE(35, Arrays.asList("set_merchant_kanacode"), "公司代码更新", "ModifykanaCode"),
    EDIT_MERCHANT_Domian(36, Arrays.asList("set_merchant_domain"), "删除域名", "deleteDomian"),

    CENT_PUSH_DELETE(30, Arrays.asList("mes_in_cent"), "删除公告", "DeleteAnnouncement"),
    CENT_PUSH(33, Arrays.asList("mes_in_cent"), "恢复发布", "Republish"),
    ACTIVITY_ENTRANCE(38, Arrays.asList("activity_entrance"), "活动入口设置", "activityEntrance"),
    ACTIVITY_SET(39, Arrays.asList("activity_set"), "活动设置", "activitySet"),
    ACTIVITY_STATUS(40, Arrays.asList("activity_status"), "活动开关更新", "activityStatus"),
    REISSUE_BONUS(50, Arrays.asList("reissue_bonus"), "奖券补发", "reissueBonus"),

    CHANGE_MERCHANT_Domian(41, Arrays.asList("CHANGE_domain"), "检测域名", "checkDomain"),
    CHANGE_MERCHANT_DJ_Domian(42, Arrays.asList("CHANGE_domain"), "电竞检测域名", "checkDomain"),
    CHANGE_MERCHANT_CP_Domian(43, Arrays.asList("CHANGE_domain"), "彩票检测域名", "checkDomain"),
    MANUAL_CHANGE_MERCHANT_Domian(44, Arrays.asList("MANUAL_CHANGE_domain"), "人工修改体育域名", "changeDomain"),
    MANUAL_CHANGE_MERCHANT_DJ_Domian(45, Arrays.asList("MANUAL_CHANGE_domain"), "人工修改电竞域名", "changeDomain"),
    MANUAL_CHANGE_MERCHANT_CP_Domian(46, Arrays.asList("MANUAL_CHANGE_domain"), "人工修改彩票域名", "changeDomain"),

    CHECK_MERCHANT_Domian(99, Arrays.asList("Check_domain"), "检测域名", "checkDomain"),
    SET_BOX_NUMBER(100, Arrays.asList("set_box_number"), "设置盲盒", "sexBoxNumber"),
    LOTTERY_MANAGEMENT(101, Arrays.asList("add_ticket", "edit_ticket", "change_state"), "奖券管理", "lotteryManagement"),
    PROPS_MANAGEMENT(102, Arrays.asList("add_props", "edit_props"), "道具管理", "propsManagement"),
    GAME_CONFIGURATION(103, Arrays.asList("change_slot_machine_state", "add_slot_machine_cfg", "edit_slot_machine_cfg"
            , "add_jackpot_cfg", "edit_jackpot_cfg", "edit_daily_number", "props_cfg"), "游戏配置", "gameConfiguration"),
    AGGREGATE_RESOURCE_MANAGEMENT(104, Arrays.asList("add_aggregate_resource", "edit_aggregate_resource",
            "del_aggregate_resource", "change_aggregate_resource_state","call_dj_api_result","call_cp_api_result"), "三端资源管理", "aggregateResourceManagement"),


    // tob 后台的
    ABNORMAL_IP_ADD(10, Collections.singletonList("abnormal_ip"), "导入", "Add"),
    ABNORMAL_IP_DEL(112, Collections.singletonList("abnormal_ip"), "删除", "Delete"),
    ABNORMAL_IP_ALL_DEL(113, Arrays.asList("abnormal_ip","video_domain_pool_set","front_domain_pool_set","api_domain_pool_set"), "全部删除", "All Delete"),
    SET_MERCHANT_INFO_MANAGER_AGENT_43(1043, Arrays.asList("merchant_group","api_domain_set"), "数据商动画域名设置", "Set animation domain name"),

    //三端
    LINE_CARRIER_SELECT(107, Collections.singletonList("line_carrier"), "线路商选择", "lineCarrierSelect"),

    SAVE(114, Arrays.asList("line_carrier","area_manage", "domain_group_manage", "domain_program", "merchant_group","menu_manage","user_manage","role_manage",
            "video_domain_set","front_domain_set","api_domain_set"), "新建", "save"),
    EDIT(115, Arrays.asList("line_carrier","area_manage", "domain_manage", "domain_group_manage", "domain_program","menu_manage","merchant_group","video_domain_set","front_domain_set"), "编辑", "edit"),
    DEL(56, Arrays.asList("line_carrier","area_manage", "domain_manage", "domain_group_manage", "domain_program", "merchant_group","abnormal_ip_domain_pool",
            "menu_manage","video_domain_pool_set","front_domain_pool_set","api_domain_pool_set","domain_program_info"), "删除", "del"),

    CLEAR_CACHE(109, Arrays.asList("merchant_group","video_domain_set","front_domain_set","api_domain_set"), "清除缓存", "clearCache"),
    MANUAL_SWITCH(44, Arrays.asList("merchant_group","video_domain_set","front_domain_set","api_domain_set"), "手动切换", "changeDomain"),
    DOMAIN_SELECT(110, Arrays.asList("domain_manage"), "域名选择", "domainSelect"),
    DOMAIN_IMPORT(111, Arrays.asList("domain_manage","video_domain_pool_set","front_domain_pool_set","api_domain_pool_set"), "域名导入", "domainImport"),
    DOMAIN_SOLD_OUT(117, Arrays.asList("domain_sold_out"), "下架", "domainSoldOut"),
    ADD_ROLE(118, Collections.singletonList("user_manage"), "添加角色", "addRole"),
    REST_KEY(119, Collections.singletonList("user_manage"), "重置密钥", "restKey"),
    UPDATE_PASSWORD(120, Collections.singletonList("user_manage"), "修改密码", "updatePassword"),
    MENU_AUTH_SET(121, Collections.singletonList("role_manage"), "菜单权限设置", "menuAuthSet"),
    DATA_AUTH_SET(122, Collections.singletonList("role_manage"), "数据权限设置", "dataAuthSet"),

    DOMAIN_DEL(123, Arrays.asList("video_domain_query"), "域名删除", "domainDel"),
    GLOBAL_INTERFACE_SET(124, Arrays.asList("video_domain_set","front_domain_set","api_domain_set"), "全局接口配置", "All Delete"),
    MERCHANT_SELECT(125, Arrays.asList("merchant_group","video_domain_set","front_domain_set","api_domain_set"), "选择商户", "select merchant"),
    DOMAIN_GROUP_SELECT(126, Collections.singletonList("domain_program_info"), "选择域名组", "select domain group"),
    CUT_PROGRAM(127, Collections.singletonList("merchant_group"), "切换方案", "cut program"),
    ONE_KEY_REPLACE(98, Arrays.asList("video_domain_set","front_domain_set"), "一键切换", "one key replace"),
    CE_SWITCH_17(97, Collections.singletonList("api_domain_set"), "17ce开关", "17ce switch"),


    // 维护日志、踢用户页面操作类型 由前端提供
    MAINTAIN_TYPE_01(1, Collections.singletonList("maintenance_console"), "设置维护", ""),
    MAINTAIN_TYPE_02(2, Collections.singletonList("maintenance_console"), "开始维护", ""),
    MAINTAIN_TYPE_03(3, Collections.singletonList("maintenance_console"), "延长30分钟", ""),
    MAINTAIN_TYPE_04(4, Collections.singletonList("maintenance_console"), "结束维护", ""),
    MAINTAIN_TYPE_05(5, Collections.singletonList("maintenance_console"), "发送维护公告", ""),
    MAINTAIN_TYPE_06(6, Collections.singletonList("maintenance_console"), "弹出用户提醒", ""),
    MAINTAIN_TYPE_07(7, Collections.singletonList("maintenance_console"), "踢用户", ""),
    KICK_USER_TYPE_01(1, Collections.singletonList("kick_user"), "按设备", ""),
    KICK_USER_TYPE_02(2, Collections.singletonList("kick_user"), "按用户", ""),
    KICK_USER_TYPE_03(3, Collections.singletonList("kick_user"), "按商户", ""),
    KICK_USER_TYPE_04(4, Collections.singletonList("kick_user"), "踢全部", ""),

    ;

    /**
     * 操作类型编码
     */
    private Integer code;

    private List<String> pageCode;

    /**
     * 描述
     */
    private String remark;
    private String remarkEn;

    private MerchantLogTypeEnum(Integer code, List<String> pageCode, String remark, String remarkEn) {
        this.code = code;
        this.pageCode = pageCode;
        this.remark = remark;
        this.remarkEn = remarkEn;
    }

    public static MerchantLogTypeEnum getMerchantLogTypeEnumByAgentLevel(Integer agentLevel) {
        MerchantLogTypeEnum pageEnum = null;
        if (AgentLevelEnum.AGENT_LEVEL_0.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogTypeEnum.NEW_CREATE_MERCHANT;
        } else if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogTypeEnum.NEW_CREATE_CHANNEL_MERCHANT;
        } else if (AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogTypeEnum.NEW_LEVEL_TOW_MERCHANT;
        } else if (AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(agentLevel)) {
            pageEnum = MerchantLogTypeEnum.NEW_AGENT_TOW_MERCHANT;
        }
        return pageEnum;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getPageCode() {
        return pageCode;
    }

    public void setPageCode(List<String> pageCode) {
        this.pageCode = pageCode;
    }

    public String getRemarkEn() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }
}
