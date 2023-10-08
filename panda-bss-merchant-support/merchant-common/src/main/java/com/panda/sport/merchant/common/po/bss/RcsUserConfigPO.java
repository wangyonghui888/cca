package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author javier
 * 2021-02-09
 * 风控限额配置
 */
@Data
@ToString
public class RcsUserConfigPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 操盘者id
     */
    private Long tradeId;
    /**
     * 可用金额
     */
    private Long sportId;
    /**
     * 赛事ID
     */
    private Long betExtraDelay ;
    /**
     * 是否有特殊投注限额  0没有 1有
     */
    private Integer specialBettingLimit;
    /**
     * 修改次数
     */
    private Integer modificationNum;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建时间
     */
    private Long updateTime;

    /**
     * 备注
     */
    private String remark;

}