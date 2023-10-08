package com.panda.sport.merchant.common.po.bss;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局系统配置表
 *
 * @author ives
 */
@Data
public class TSystemConfig implements Serializable {

    private static final long serialVersionUID = 4521117190167047353L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "配置键(唯一)")
    private String configKey;

    @ApiModelProperty(value = "配置值")
    private String configValue;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}
