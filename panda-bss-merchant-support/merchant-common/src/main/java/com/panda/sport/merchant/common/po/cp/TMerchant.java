package com.panda.sport.merchant.common.po.cp;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 用户信息
 * @author duwan
 * @date 2022-01-07
 */
public class TMerchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 商户账号
     */
    private String merchantAccount;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 状态 0-停用 1-启用
     */
    private int status;

    /**
     * 投注模式(厘1、分10、角100、元1000)
     */
    private String amountMode;

    /**
     * 双面盘模式 1代理 2 直客
     */
    private int doubleMode;

    /**
     * 标准盘模式 1代理 2 直客
     */
    private int normalMode;

    /**
     * 特色游戏模式 1代理 2直客
     */
    private int featuresMode;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * skype
     */
    private String skype;

    /**
     * telegram
     */
    private String telegram;

    /**
     * 优先排序:1标准盘 2双面盘 3特色
     */
    private int groupSort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 结算方案类别：1现金方案 2信用方案
     */
    private int settlementType;

    /**
     * 结算方案id
     */
    private Integer settlementId;

    /**
     * 结算方案名称
     */
    private String settlementName;

    /**
     * 转账方式: 1-免转 2-非免转
     */
    private int transferType;

    /**
     * 允许平台内发起转账:1-允许 0-不允许
     */
    private int transferAllow;

    /**
     * 方案切换时间
     */
    private Date schemeSwitchAt;

    /**
     * 父商户id
     */
    private Integer parentId;

    /**
     * 父商户树结构
     */
    private String parentTree;

    /**
     * 顶级商户id，0:该商户就是顶级商户
     */
    private Integer topId;

    /**
     * 商户层级
     */
    private Integer level;

    /**
     * 是否有子商户，0:没有 1:有
     */
    private int hasSub;

    /**
     * 商户账号
     */
    private String parentMerchantAccount;

    /**
     * 商户密钥
     */
    private String secretKey;

    /**
     * 0:普通单租户模式 1:多租户模式
     */
    private int merchantType;

    /**
     * 联系电话-商户
     */
    private String merchantPhone;

    /**
     * 电子邮件-商户
     */
    private String merchantEmail;

    /**
     * telegram-商户
     */
    private String merchantTelegram;

    /**
     * skype-商户
     */
    private String merchantSkype;

    /**
     * 备注-商户
     */
    private String merchantRemark;

    /**
     * 免转商户钱包地址
     */
    private String moneyAddress;

    /**
     * 赔率模式：1:代理 2:直客
     */
    private int agentMode;

    /**
     * 品牌 ：1:ob彩票 2:自有
     */
    private int brand;

    /**
     * 合作模式：1:自营 2:扶持
     */
    private int cooperate;

    /**
     * 直属下级数量
     */
    private Integer childrensNum;

    /**
     * 直属下级数量最大值
     */
    private Integer childrensLimit;


    public TMerchant() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmountMode() {
        return amountMode;
    }

    public void setAmountMode(String amountMode) {
        this.amountMode = amountMode;
    }

    public int getDoubleMode() {
        return doubleMode;
    }

    public void setDoubleMode(int doubleMode) {
        this.doubleMode = doubleMode;
    }

    public int getNormalMode() {
        return normalMode;
    }

    public void setNormalMode(int normalMode) {
        this.normalMode = normalMode;
    }

    public int getFeaturesMode() {
        return featuresMode;
    }

    public void setFeaturesMode(int featuresMode) {
        this.featuresMode = featuresMode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public int getGroupSort() {
        return groupSort;
    }

    public void setGroupSort(int groupSort) {
        this.groupSort = groupSort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(int settlementType) {
        this.settlementType = settlementType;
    }

    public Integer getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Integer settlementId) {
        this.settlementId = settlementId;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getTransferAllow() {
        return transferAllow;
    }

    public void setTransferAllow(int transferAllow) {
        this.transferAllow = transferAllow;
    }

    public Date getSchemeSwitchAt() {
        return schemeSwitchAt;
    }

    public void setSchemeSwitchAt(Date schemeSwitchAt) {
        this.schemeSwitchAt = schemeSwitchAt;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentTree() {
        return parentTree;
    }

    public void setParentTree(String parentTree) {
        this.parentTree = parentTree;
    }

    public Integer getTopId() {
        return topId;
    }

    public void setTopId(Integer topId) {
        this.topId = topId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public int getHasSub() {
        return hasSub;
    }

    public void setHasSub(int hasSub) {
        this.hasSub = hasSub;
    }

    public String getParentMerchantAccount() {
        return parentMerchantAccount;
    }

    public void setParentMerchantAccount(String parentMerchantAccount) {
        this.parentMerchantAccount = parentMerchantAccount;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(int merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantTelegram() {
        return merchantTelegram;
    }

    public void setMerchantTelegram(String merchantTelegram) {
        this.merchantTelegram = merchantTelegram;
    }

    public String getMerchantSkype() {
        return merchantSkype;
    }

    public void setMerchantSkype(String merchantSkype) {
        this.merchantSkype = merchantSkype;
    }

    public String getMerchantRemark() {
        return merchantRemark;
    }

    public void setMerchantRemark(String merchantRemark) {
        this.merchantRemark = merchantRemark;
    }

    public String getMoneyAddress() {
        return moneyAddress;
    }

    public void setMoneyAddress(String moneyAddress) {
        this.moneyAddress = moneyAddress;
    }

    public int getAgentMode() {
        return agentMode;
    }

    public void setAgentMode(int agentMode) {
        this.agentMode = agentMode;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public int getCooperate() {
        return cooperate;
    }

    public void setCooperate(int cooperate) {
        this.cooperate = cooperate;
    }

    public Integer getChildrensNum() {
        return childrensNum;
    }

    public void setChildrensNum(Integer childrensNum) {
        this.childrensNum = childrensNum;
    }

    public Integer getChildrensLimit() {
        return childrensLimit;
    }

    public void setChildrensLimit(Integer childrensLimit) {
        this.childrensLimit = childrensLimit;
    }

}