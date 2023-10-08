package com.panda.sport.merchant.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 操盘手设置用户参数限制商户设置
 * </p>
 *
 * @author ${author}
 * @since 2022-04-22
 */
@Data
public class RcsTradeRestrictMerchantSettingDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("玩家ID 对应userId")
    private Long userId;

    @ApiModelProperty("体育种类Ids")
    private String sportIds;

    @ApiModelProperty("操盘手设置的特殊百分比限额")
    private BigDecimal percentageLimit;

    @ApiModelProperty("操盘手设置的投注额外延时")
    private Integer betExtraDelay;

    @ApiModelProperty("操盘手设置的标签行情等级ID（赔率分组）")
    private Integer tagMarketLevelId;

    @ApiModelProperty("操盘者Id")
    private Integer tradeId;

    @ApiModelProperty("更新时间")
    private Date updateTime;


}
