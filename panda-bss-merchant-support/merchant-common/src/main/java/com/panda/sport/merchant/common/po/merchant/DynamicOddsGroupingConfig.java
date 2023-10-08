package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  动态赔率分组配置表
 * @Date: 2022-05-07 18:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DynamicOddsGroupingConfig implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "类型 |1 盈利率 |2 胜率")
    private Integer type;

    @ApiModelProperty(value = "比率开始值")
    private String startRate;

    @ApiModelProperty(value = "比率结束值")
    private String endRate;

    @ApiModelProperty(value = "组别")
    private Integer groupNum;

    @ApiModelProperty(value = "投注笔数阈值")
    private Integer betNumThreshold;

    @ApiModelProperty(value = "修改时间")
    private Long updateTime;
}
