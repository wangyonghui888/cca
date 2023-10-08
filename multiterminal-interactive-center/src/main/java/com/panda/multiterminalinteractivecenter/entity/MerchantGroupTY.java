package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * B端商户分组表
 * </p>
 *
 * @author amos
 * @since 2022-07-11
 */
@Getter
@Setter
@TableName("t_merchant_group_ty")
public class MerchantGroupTY implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户组名称
     */
    private String groupName;

    /**
     * 1:运维组，2:业务组，3:公用组
     */
    private Integer groupType;

    /**
     * 商户组code 1 为电竞 2为彩票
     */
    private Integer groupCode;

    /**
     * 是否开启 1为开启 2为关闭
     */
    private Integer status;

    /**
     * 时间类型  1为分钟 2为小时 3为日  4为月
     */
    private Integer timeType;

    /**
     * 时间值
     */
    private Integer times;

    /**
     * 上次更新时间
     */
    private Long updateTime;

    /**
     * 报警数字
     */
    private Long alarmNum;

    /**
     * 域名切换方案id(域名切换方案表中的主键id)
     */
    private Long programId;

    /**
     * 域名第三方检测开关 1关闭 2开启
     */
    private Integer thirdStatus;


}
