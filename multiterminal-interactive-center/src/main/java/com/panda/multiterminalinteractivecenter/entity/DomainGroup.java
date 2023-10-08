package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : ifan
 * @Description :  域名组实体
 * @Date: 2022-07-02
 */
@Data
@TableName("t_domain_group")
public class DomainGroup implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 域名名称
     */
    private String domainGroupName;

    /**
     * 分组类型
     */
    private Integer groupType;

    /**
     * 域名数据量
     */
    @TableField(exist = false)
    private Long domainNum;

    /**
     * 域名
     */
    @TableField(exist = false)
    private String domain;

    /**
     * 报警阀值
     */
    private Integer h5Threshold;
    private Integer pcThreshold;
    private Integer apiThreshold;
    private Integer imgThreshold;

    /**
     * 专属类型
     */
    private Integer exclusiveType;

    /**
     * 所属区域
     */
    private Long belongArea;

    /**
     * 所属区域
     */
    @TableField(exist = false)
    private String belongAreaName;

    /**
     * 用户价值
     */
    private String userValue;

    /**
     * 所属方案
     */
    @TableField(exist = false)
    private String belongProgram;

    /**
     * 状态
     */
    private Long status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 最后更新人
     */
    private String lastUpdated;

    /**
     * 域名id组
     */
    @TableField(exist = false)
    private String domainIds;

    /**
     * 方案id
     */
    @TableField(exist = false)
    private String programId;

    /**
     * 方案名称
     */
    @TableField(exist = false)
    private String programName;

    private String tab;

    private Integer delTag;



}
