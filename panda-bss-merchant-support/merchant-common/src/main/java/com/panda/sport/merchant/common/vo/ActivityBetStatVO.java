package com.panda.sport.merchant.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.dto
 * @Description :  活动统计返回
 * @Date: 2021-08-21 11:21
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class ActivityBetStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 投注次数
     */
    private Long validBetNums;
    /**
     * 有效投注金额
     */
    private BigDecimal validBetAmount;

    /**
     * 累计有效投注天数
     */
    private Integer validBetDays;

    public String getUid(){
        return uid.toString();
    }
}
