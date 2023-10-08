package com.panda.multiterminalinteractivecenter.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**

import lombok.Data;

import java.io.Serializable;

/**
 *@ClassName MerchantGroupPO
 *@Description TODO
 *@Author ifan
 *@Date 2022/7/11
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantGroupPO implements Serializable {

    private Long id;

    //1:公共组(运维)，2:Y组(Y系),3: S组(S系),4:B组(B系)
    private Integer groupType;

    private String groupName;

    // 商户组code: common,y,s,b
    private String groupCode;

    private Long updateTime;

    private Integer times;

    /*时间类型  1为分钟 2为小时 3为日  4为月*/
    private Integer timeType;

    /*状态 默认 为1 开启 2为关闭*/
    private Integer status;

    //状态 默认 为1 关闭 2为开启
    private Integer thirdStatus;

    private Integer alarmNum;

    private Long programId;

    private String programName;

    private String tab;

}
