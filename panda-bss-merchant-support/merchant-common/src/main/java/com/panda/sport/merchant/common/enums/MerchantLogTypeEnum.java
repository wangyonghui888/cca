package com.panda.sport.merchant.common.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.enums
 * @Description :  商户日志类型枚举
 * @Date: 2020-09-02 11:15
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public enum MerchantLogTypeEnum {


    EDIT_INFO(2, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "编辑", "Edit"),

    Add_SUB(8, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "添加下级商户", "Add subordinate merchants"),

    ADD_MERCHANT_D(51, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "添加商户", "Add merchants"),

    DEL_MERCHANT(52, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "删除确认", "Del merchants"),

    DEL_INFO(56, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "删除", "Del"),

    SYNC_BILL(53, Arrays.asList("basic", "secondary", "synchronization_bill_0", "synchronization_bill_1"
            , "synchronization_bill_3"), "对账单数据同步", "Synchronization bill"),

    SYNC_ORDER(54, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "用户投注统计同步", "Synchronization Order"),

    SYNC_MATCH(55, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "赛事投注统计同步", "Synchronization Match"),
    FUNCTION_SWITCH(10, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "功能开关", "Function switch"),
    SAVE_INFO(9, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1"
            , "merchant_info_manager_agent_2"), "新建", "Save"),
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

    ABNORMAL_IP_ADD(10, Collections.singletonList("ip"), "导入", "Add"),

    ARTICLE_ADD(116, Collections.singletonList("article"), "新增文章", "Article Add"),


    ARTICLE_EDIT(117, Collections.singletonList("article"), "编辑文章", "Article Edit"),

    AUTHOR_ADD(118, Collections.singletonList("author"), "新增作者", "Author Add"),

    ABNORMAL_IP_DEL(112, Collections.singletonList("ip"), "删除", "Delete"),

    ABNORMAL_IP_ALL_DEL(113, Collections.singletonList("ip"), "全部删除", "All Delete"),

    USER_MANAGER_UPDATE(11, Collections.singletonList("user"), "修改用户", "EditUser"),
    USER_PASSWORD_UPDATE(111, Collections.singletonList("user_password"), "修改用户密码", "EditUserPassword"),
    USER_MANAGER_DELETE(12, Collections.singletonList("user"), "删除用户", "DeleteUser"),

    PUSH_MANAGER_ODER(116, Collections.singletonList("order"), "拖曳排序", "PushOrder"),

    ADD_TAG(119, Collections.singletonList("addTag"), "新增标签", "Add Tag"),

    BATCH_ONLINE(117, Collections.singletonList("online"), "批量上线", "Batch Online"),

    BATCH_OFFLINE(118, Collections.singletonList("offline"), "批量下架", "Batch Offline"),

    EDIT_INFO_STATUS(13, Arrays.asList("secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1",
            "merchant_info_manager_agent_2"), "启用&禁用", "Open&Close"),

    EDIT_DOMAIN_STATUS(115, Arrays.asList("secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1",
            "merchant_info_manager_agent_2"), "启用/禁用", "Open/Close"),
    EDIT_SUB_INFO_STATUS(113, Arrays.asList("secondary", "merchant_info_manager_agent_0",
            "merchant_info_manager_agent_1", "merchant_info_manager_agent_2", "merchant_info_manager_agent_10"),
            "删除代理", "DeleteAgent"),

    BATCH_EXPORT(114, Collections.singletonList("batchExport"), "批量导出", "BatchExport"),
    ROLE_ADD(14, Collections.singletonList("role"), "创建角色", "CreateRole"),
    ROLE_SET(15, Arrays.asList("role"), "设置角色权限", "Set up Role Authoritation"),
    ROLE_EDIT(16, Arrays.asList("role"), "修改角色", "ModifyRole"),
    ROLE_DELETE(17, Arrays.asList("role"), "删除角色", "DeleteRole"),
    MSG_DELETE(18, Arrays.asList("mes_in_my"), "删除消息", "DeleteMessage"),

    DO_DOMAIN(60, Arrays.asList("do_domain"), "手动切换域名", "doDomain"),
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
    EDIT_MERCHANT__DOMAIN(32, Arrays.asList("set_merchant_domain"), "商户根域名更新", "ModifyDomain"),
    DELETE_BET_ORDER(34, Arrays.asList("cancel_bet"), "注单取消", "CancelBet"),
    EDIT_MERCHANT_KANACODE(35, Arrays.asList("set_merchant_kanacode"), "公司代码更新", "ModifykanaCode"),
    EDIT_MERCHANT_Domian(36, Arrays.asList("set_merchant_domain","live_matches_live_management_84"), "删除域名", "deleteDomian"),

    CENT_PUSH_DELETE(30, Arrays.asList("mes_in_cent"), "删除公告", "DeleteAnnouncement"),
    CENT_PUSH(33, Arrays.asList("mes_in_cent"), "恢复发布", "Republish"),
    ACTIVITY_ENTRANCE(38, Arrays.asList("activity_entrance"), "活动入口设置", "activityEntrance"),
    ACTIVITY_SET(39, Arrays.asList("activity_set"), "活动设置", "activitySet"),

    ACTIVITY_CONFIG(54, Arrays.asList("activity_config"), "活动配置", "activityConfig"),

    ACTIVITY_SET_TIME(53, Arrays.asList("activity_set"), "活动时间设置", "activitySet"),


    ACTIVITY_STATUS(40, Arrays.asList("activity_status"), "活动开关更新", "activityStatus"),

    REISSUE_BONUS(50, Arrays.asList("reissue_bonus"), "奖券补发", "reissueBonus"),

    CHANGE_MERCHANT_Domian(41, Arrays.asList("CHANGE_domain"), "检测域名", "checkDomain"),
    CHANGE_MERCHANT_DJ_Domian(42, Arrays.asList("CHANGE_domain"), "电竞检测域名", "checkDomain"),
    CHANGE_MERCHANT_CP_Domian(43, Arrays.asList("CHANGE_domain"), "彩票检测域名", "checkDomain"),
    MANUAL_CHANGE_MERCHANT_Domian(44, Arrays.asList("MANUAL_CHANGE_domain"), "人工修改体育域名", "changeDomain"),
    MANUAL_CHANGE_MERCHANT_DJ_Domian(45, Arrays.asList("MANUAL_CHANGE_domain"), "人工修改电竞域名", "changeDomain"),
    MANUAL_CHANGE_MERCHANT_CP_Domian(46, Arrays.asList("MANUAL_CHANGE_domain"), "人工修改彩票域名", "changeDomain"),

    MANUALLY_SWITCH_Domian(47, Arrays.asList("MANUALLY_SWITCH_domain"), "手动切换", "manually switch domain"),

    CHECK_MERCHANT_Domian(99, Arrays.asList("Check_domain"), "检测域名", "checkDomain"),
    SET_BOX_NUMBER(100, Arrays.asList("set_box_number"), "设置盲盒", "sexBoxNumber"),
    LOTTERY_MANAGEMENT(101, Arrays.asList("add_ticket", "edit_ticket", "change_state"), "奖券管理", "lotteryManagement"),
    PROPS_MANAGEMENT(102, Arrays.asList("add_props", "edit_props"), "道具管理", "propsManagement"),
    GAME_CONFIGURATION(103, Arrays.asList("change_slot_machine_state", "add_slot_machine_cfg", "edit_slot_machine_cfg"
            , "add_jackpot_cfg", "edit_jackpot_cfg", "edit_daily_number", "props_cfg"), "游戏配置", "gameConfiguration"),
    AGGREGATE_RESOURCE_MANAGEMENT(104, Arrays.asList("add_aggregate_resource", "edit_aggregate_resource",
            "del_aggregate_resource", "change_aggregate_resource_state","call_dj_api_result","call_cp_api_result"), "三端资源管理", "aggregateResourceManagement"),
    CHAT_ROOM(105, Arrays.asList("edit_chat_room"), "聊天室编辑", "edit ChatRoom"),

    CHAT_MANAGER(107, Arrays.asList("set_manager"), "设置管理员", "set Manager"),

    ADD_MERCHANT(111, Arrays.asList("add_merchat"), "添加下级商户", "add Merchat"),

    CHAT_LANGUAGE(106, Arrays.asList("edit_language"), "C端默认语言编辑", "edit Language"),

    CHAT_PASSWORD(108, Arrays.asList("edit_password"), "重置密码", "edit Password"),

    CHAT_COMPREHENSIVE(109, Arrays.asList("edit_comprehensive"), "C端综合设置", "edit Comprehensive"),

    TO_ORDER(110, Arrays.asList("edit_comprehensive"), "预约投注", "Book Bet"),

    VIDEO_TRAFFIC(111, Arrays.asList("video_traffic"), "视频流量管控", "Video traffic control"),

    CHAT_CHANNEL(112, Arrays.asList("edit_Channel"), "新建渠道商户", "add Channel"),

    SET_ACTIVE(116, Arrays.asList("set_Active"), "活动入口设置", "set Active"),


    SET_CHANNEL(29, Arrays.asList("merchant_info_manager_agent_1"), "设置二级商户", "Set up Channel"),


    DOMAIN_NAME_SETTINGS2(111, Arrays.asList("domain_name_pc"), "pc域名", "domain pc"),

    DOMAIN_NAME_SETTINGS3(112, Arrays.asList("domain_name_api"), "api域名", "domain api"),

    DOMAIN_NAME_SETTINGS4(113, Arrays.asList("domain_name_picture"), "图片域名", "domain picture"),

    DOMAIN_NAME_SETTINGS5(114, Arrays.asList( "domain_name_other"), "其他域名", "domain others"),

    DOMAIN_NAME_SETTINGS1(115, Arrays.asList("domain_name_h"), "H5域名", "domain H5"),

    ADD_ANCHOR(120, Arrays.asList("anchor_management_add"), "新增主播", "add anchor"),

    EDIT_ANCHOR(121, Arrays.asList("anchor_management_edit"), "编辑主播", "edit anchor"),

    APPLY_ANCHOR(122, Arrays.asList("anchor_management_apply"), "启用主播", "apply anchor"),

    DISABLE_ANCHOR(123, Arrays.asList("anchor_management_disable"), "禁用主播", "disable anchor"),

    SCHEDULING_LIVE(124, Arrays.asList("live_management_scheduling"), "直播排班", "scheduling live"),

    EDIT_LIVE(125, Arrays.asList("live_management_edit"), "直播编辑", "edit live"),

    START_LIVE(126, Arrays.asList("live_management_start"), "开始直播", "start live"),

    FINISH_LIVE(127, Arrays.asList("live_management_finish"), "结束直播", "finish live"),

    DEL_LIVE(72, Arrays.asList("live_management_del"), "删除直播", "delete live"),

    ON_PREGAME_SHOW(73, Arrays.asList("pregame_show_on"), "赛前节目上架", "pregame show on"),

    OFF_PREGAME_SHOW(74, Arrays.asList("pregame_show_off"), "赛前节目下架", "pregame show off"),

    EDIT_PREGAME_SHOW(75, Arrays.asList("pregame_show_edit"), "赛前节目编辑", "edit pregame show"),

    DEL_PREGAME_SHOW(76, Arrays.asList("pregame_show_del"), "赛前节目删除", "delete pregame show"),

    ADD_PREGAME_SHOW(71, Arrays.asList("Pregame_Show_del"), "赛前节目新增", "add pregame show"),

    ADD_CHAT_ROOM(72, Arrays.asList("Pregame_Show_del"), "新增聊天室", "add chat room"),

    ON_CHAT_ROOM(73, Arrays.asList("Pregame_Show_del"), "开启聊天室", "chat room on"),

    OFF_CHAT_ROOM(74, Arrays.asList("Pregame_Show_del"), "关闭聊天室", "chat room off"),

    EXPORT_CHAT_CONTENT(75, Arrays.asList("Pregame_Show_del"), "导出聊天内容", "export chat content"),

    ADD_CHAT_ROOM_ANNOUNCEMENT(76, Arrays.asList("Pregame_Show_del"), "新增聊天室公告", "add chat room announcement"),

    EDIT_CHAT_ROOM_ANNOUNCEMENT(77, Arrays.asList("Pregame_Show_del"), "编辑聊天室公告", "edit chat room announcement"),

    ON_CHAT_ROOM_ANNOUNCEMENT(78, Arrays.asList("Pregame_Show_del"), "开启聊天室公告", "chat room announcement on"),

    OFF_CHAT_ROOM_ANNOUNCEMENT(79, Arrays.asList("Pregame_Show_del"), "关闭聊天室公告", "chat room announcement off"),

    DEL_CHAT_ROOM_ANNOUNCEMENT(80, Arrays.asList("Pregame_Show_del"), "删除聊天室公告", "chat room announcement del"),

    USER_CLEAR_SCREEN(81, Arrays.asList("Pregame_Show_del"), "内容审核用户清屏", "user clear screen"),

    MERCHANT_CLEAR_SCREEN(82, Arrays.asList("Pregame_Show_del"), "内容审核商户清屏", "merchant clear screen"),

    CONTENT_AUDIT_RECALL_MESSAGE(92, Arrays.asList("Pregame_Show_del"), "内容审核撤回消息", "audit content recall"),

    ADD_SENSITIVE_WORDS(83, Arrays.asList("Pregame_Show_del"), "新增敏感词", "sensitive words add"),

    EDIT_SENSITIVE_WORDS(84, Arrays.asList("Pregame_Show_del"), "编辑敏感词", "sensitive words edit"),

    ON_SENSITIVE_WORDS(85, Arrays.asList("Pregame_Show_del"), "启用敏感词", "sensitive words on"),

    OFF_SENSITIVE_WORDS(86, Arrays.asList("Pregame_Show_del"), "禁用敏感词", "sensitive words off"),

    PROHIBITED_MEMBER_NOTES(87, Arrays.asList("Pregame_Show_del"), "禁言会员备注", "prohibited member notes"),

    PROHIBIT_BOOKLET_MEMBER_NOTES(88, Arrays.asList("Pregame_Show_del"), "禁止晒单会员备注", "prohibit booklet member notes"),

    ADD_SUPER_MEMBER(89, Arrays.asList("Pregame_Show_del"), "新增超级会员", "super member add"),

    EDIT_SUPER_MEMBER(90, Arrays.asList("Pregame_Show_del"), "编辑超级会员", "super member edit"),

    DEL_SUPER_MEMBER(91, Arrays.asList("Pregame_Show_del"), "删除超级会员", "super member del"),

    DISABLE_SEND_MSG_OPERATION(93, Arrays.asList("Pregame_Show_del"), "禁言操作", "disable send msg operation"),

    DISABLE_BASK_ORDER_OPERATION(94, Arrays.asList("Pregame_Show_del"), "禁止晒单操作", "disable bask order operation"),

    CHAT_ROOM_CLEAR_SCREEN(95, Arrays.asList("Pregame_Show_del"), "聊天室清屏", "chat room clear screen"),

    CHAT_ROOM_DISABLE_SEND_MSG(96, Arrays.asList("Pregame_Show_del"), "聊天室禁言", "chat room disable send msg"),

    CHAT_ROOM_DISABLE_BASK_ORDER(97, Arrays.asList("Pregame_Show_del"), "聊天室禁止晒单", "chat room disable bask order"),

    ANNOUNCEMENT_PUSH_AND_DROP_SORT(98, Arrays.asList("Pregame_Show_del"), "公告推拽排序", "announcement push and drop sort"),

    Enable_Send_Msg(99, Arrays.asList("Pregame_Show_del"), "解除禁言", "enable send msg"),

    ENABLE_BASK_ORDER(100, Arrays.asList("Pregame_Show_del"), "解除禁制晒单", "enable bask order"),

    DOMAIN_IMPORT(111, Arrays.asList("live_matches_live_management_84"), "域名导入", "domainImport"),
    EDIT(115, Arrays.asList("line_carrier","area_manage", "domain_manage", "domain_group_manage", "domain_program","menu_manage"), "编辑", "edit"),
    MERCHANT_SELECT(125, Arrays.asList("merchant_group","video_domain_set","front_domain_set","api_domain_set"), "选择商户", "select merchant"),

    MANUAL_SWITCH(44, Arrays.asList("merchant_group","video_domain_set","front_domain_set","api_domain_set"), "手动切换", "changeDomain"),
    SAVE(114, Arrays.asList("line_carrier","area_manage", "domain_group_manage", "domain_program", "merchant_group","menu_manage","user_manage","role_manage",
            "video_domain_set","front_domain_set","api_domain_set"), "新建", "save"),
    CLEAR_CACHE(109, Arrays.asList("merchant_group","video_domain_set","front_domain_set","api_domain_set"), "清除缓存", "clearCache"),

    ADD_MERCHANT_KEY(110, Arrays.asList("basic"), "新增商户证书", "Add merchant key"),


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

    public static MerchantLogTypeEnum getDomainName(Integer domainType) {
        MerchantLogTypeEnum pageEnum = null;
        switch (domainType){
            case 1 :
                pageEnum=MerchantLogTypeEnum.DOMAIN_NAME_SETTINGS1;
                break;
            case 2 :
                pageEnum=MerchantLogTypeEnum.DOMAIN_NAME_SETTINGS2;
                break;
            case 3:
                pageEnum=MerchantLogTypeEnum.DOMAIN_NAME_SETTINGS3;
                break;
            case 4 :
                pageEnum=MerchantLogTypeEnum.DOMAIN_NAME_SETTINGS4;
                break;
            case 5 :
                pageEnum=MerchantLogTypeEnum.DOMAIN_NAME_SETTINGS5;
                break;

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
