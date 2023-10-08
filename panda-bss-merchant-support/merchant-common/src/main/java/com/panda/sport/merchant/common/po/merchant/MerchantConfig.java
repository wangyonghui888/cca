package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : ives
 * @Description : 对外商户配置类 merchant_config
 * @Date: 2022-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantConfig implements Serializable {

    private static final long serialVersionUID = -3955921951039544079L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户编码
     */
    private String merchantCode;


    /**
     * 默认时间类型|1 投注时间|2 开赛时间 |3 结算时间，默认为2 开赛时间
     */
    private Integer defaultTimeType;

    /**
     * 默认是否勾选自然日|0 不勾选 |1 勾选，默认为1 勾选
     */
    private Integer isNatureDay;

    /**
     * 商户后台重置密码1开0关
     */
    private Integer resetPasswordSwitch;

    /**
     * 异常用户名单点击时间
     */
    private Long abnormalClickTime;
}
