package com.panda.sport.merchant.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  TODO
 * @Date: 2022-05-20 16:42
 */
@Data
public class RedisTransportationDTO implements Serializable {

    private static final long serialVersionUID = -2641000421903031812L;
    @ApiModelProperty("redis主键")
    private String redisKey;

    @ApiModelProperty("item")
    private String item;

    @ApiModelProperty("值")
    private Object obj;

    @ApiModelProperty("过期时间")
    private Integer expireTime;
}
