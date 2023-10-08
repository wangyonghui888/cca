package com.panda.multiterminalinteractivecenter.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainReqDTO {

    /**
     *  ipArea:ip解析地区
     */
    @ApiModelProperty(value = "IP归属地")
    private String ipArea;

    /**
     *  ipArea:ip解析地区CODE
     */
    @ApiModelProperty(value = "IP归属地CODE")
    private String areaCode;

    /**
     *  是否VIP
     */
    @ApiModelProperty(value = "是否VIP")
    private Boolean isVip;

    /**
     *  商户
     */
    @ApiModelProperty(value = "商户")
    private String merchantAccount;

    /**
     *  DJ/CP
     */
    @ApiModelProperty(value = "平台类型",example = "dj/cp")
    private String tab;
}
