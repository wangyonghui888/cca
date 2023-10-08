package com.panda.center.enums;

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


    EDIT_INFO(2, Arrays.asList("basic", "secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1", "merchant_info_manager_agent_2"), "编辑", "Edit"),
    SET_MANAGER(3, Arrays.asList("merchant_info_manager_agent_0", "merchant_info_manager_agent_1", "merchant_info_manager_agent_2", "secondary"), "设置超级管理员", "Set Up Administrator"),
    NEW_CREATE_MERCHANT(1, Arrays.asList("merchant_info_manager_agent_0"), "新建直营商户", "Create DirectMerchant"),
    NEW_CREATE_CHANNEL_MERCHANT(4, Arrays.asList("merchant_info_manager_agent_1"), "新建渠道商户", "CreateChannelMerchant"),
    NEW_LEVEL_TOW_MERCHANT(5, Arrays.asList("merchant_info_manager_agent_2", "secondary"), "新建二级商户", "CreateChildrenMerchant"),
    NEW_AGENT_TOW_MERCHANT(105, Collections.singletonList("merchant_info_manager_agent_10"), "新建代理商户", "CreateBigAgent"),

    NEW_CREATE_NEWS(6, Collections.singletonList("bulletin"), "发布公告", "PublishAnnoucememnt"),
    DEL_NEWS(7, Collections.singletonList("bulletin"), "删除公告", "DeleteAnnouncement"),
    USER_MANAGER_ADD(10, Collections.singletonList("user"), "新增用户", "CreateUser"),
    USER_MANAGER_UPDATE(11, Collections.singletonList("user"), "修改用户", "EditUser"),
    USER_PASSWORD_UPDATE(111, Collections.singletonList("user_password"), "修改用户密码", "EditUserPassword"),
    USER_MANAGER_DELETE(12, Collections.singletonList("user"), "删除用户", "DeleteUser"),
    EDIT_INFO_STATUS(13, Arrays.asList("secondary", "merchant_info_manager_agent_0", "merchant_info_manager_agent_1", "merchant_info_manager_agent_2"), "启用&禁用", "Open&Close"),
    EDIT_SUB_INFO_STATUS(1013, Arrays.asList("secondary", "merchant_info_manager_agent_0",
            "merchant_info_manager_agent_1", "merchant_info_manager_agent_2", "merchant_info_manager_agent_10"), "删除代理", "DeleteAgent"),


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
    REISSUE_BONUS(50,Arrays.asList("reissue_bonus"),"奖券补发","reissueBonus"),

    CHANGE_MERCHANT_Domian(41, Arrays.asList("CHANGE_domain"), "检测域名", "checkDomain"),
    CHECK_MERCHANT_Domian(99, Arrays.asList("Check_domain"), "检测域名", "checkDomain"),
    SET_BOX_NUMBER(100,Arrays.asList("set_box_number"),"设置盲盒","sexBoxNumber")
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
