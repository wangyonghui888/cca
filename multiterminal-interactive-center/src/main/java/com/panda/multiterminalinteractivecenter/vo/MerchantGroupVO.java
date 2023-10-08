package com.panda.multiterminalinteractivecenter.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.multiterminalinteractivecenter.entity.TMerchantGroupInfo;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**

import com.panda.sport.merchant.common.po.bss.MerchantPO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *@ClassName MerchantGroupVO
 *@Description TODO
 *@Author ifan
 *@Date 2022/7/11
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class MerchantGroupVO implements Serializable {

    private String id;

    private List<String> merchantCodes;

    private List<MerchantResultVO> merchantList;

    //1:公共组(运维)，2:Y组(Y系),3: S组(S系),4:B组(B系)
    private Integer groupType;

    //商户组code : common,y,s,b
    private String groupCode;

    private String groupName;

    private Long updateTime;

    private Integer times;

    /*时间类型  1为分钟 2为小时 3为日  4为月*/
    private Integer timeType;

    /*状态 默认 为1 开启 2为关闭*/
    private Integer status;

    //状态 默认 为1 关闭 2为开启
    private Integer thirdStatus;

    private Integer alarmNum;

    private String domain;

    private Long programId;

    private Integer selfThreshold;

    private Integer selfNodes;

    private Integer nodes;

    private Integer threshold;

    /**
     * 域名组ID
     */
    private Long domainGroupId;

    private String programName;

    private String tab;

    private Integer merchantGroupCode;

    private List<TMerchantGroupInfo> merchantGroupInfos;

    //添加请求参数 "type" 0 选择商户 1 切换方案 2 启用/禁用 3切换频率
    private Integer type;
}
