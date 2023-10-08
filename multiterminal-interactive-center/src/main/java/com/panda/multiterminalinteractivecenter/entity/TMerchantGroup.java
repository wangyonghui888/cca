package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class TMerchantGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 主键
     */
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
     * 时间类型 1为分钟 2为小时 3为日 4为月
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

    private String tab;

    private List<TMerchantGroupInfo> merchantGroupInfos;

    public TMerchantGroup() {}

    private List<String> merchantCodes;

    private List<TMerchantGroupInfo> merchantList;

    //状态 默认 为1 关闭 2为开启
    private Integer thirdStatus;


    private String domain;

    private Long programId;

    /**
     * 域名组ID
     */
    private Long domainGroupId;

    private String programName;

    private Integer merchantGroupCode;

    //添加请求参数 "type" 0 选择商户 1 切换方案 2 启用/禁用 3切换频率
    private Integer type;

}
