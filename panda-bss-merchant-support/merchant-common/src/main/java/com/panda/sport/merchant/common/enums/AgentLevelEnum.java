package com.panda.sport.merchant.common.enums;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.commons.lang3.StringUtils;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.enums
 * @Description :  商户等级
 * @Date: 2020-08-01 15:28
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public enum AgentLevelEnum {

    AGENT_LEVEL_0(0, "直营商户"),
    AGENT_LEVEL_1(1, "渠道商户"),
    AGENT_LEVEL_2(2, "二级商户"),
    AGENT_LEVEL_10(10, "代理商");



    private Integer code;

    private String remark;

    private AgentLevelEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
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

    public static String getRemarkByCode(String code){
        if(StringUtils.isBlank(code) || StringPool.NULL.equals(code)) return "";
        for (AgentLevelEnum value : AgentLevelEnum.values()) {
            if(value.getCode().equals(Integer.valueOf(code))) {
                return value.getRemark();
            }
        }
        return "";
    }
}
