package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author javier
 * 2021-02-09
 * 风控限额配置
 */
@Data
@ToString
@Accessors(chain = true)
public class RcsUserSpecialBetLimitConfigPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 所属的特殊限额的种类  1 无  2特殊百分比限额 3特殊单注单场限额 4特殊vip限额
     */
    private Integer specialBettingLimitType;
    /**
     * 订单类型  1单关  2串关
     */
    private Integer orderType;
    /**
     * 体育种类  0其他   -1全部
     */
    private Integer sportId ;
    /**
     * 单注赔付限额
     */
    private Long singleNoteClaimLimit;
    /**
     * 单场赔付限额
     */
    private Long singleGameClaimLimit;
    /**
     * 百分比限额数据
     */
    private BigDecimal percentageLimit;

    /**
     * 0 无效  1有效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}