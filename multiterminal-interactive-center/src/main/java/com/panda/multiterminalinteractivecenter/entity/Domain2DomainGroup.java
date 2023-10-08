package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : istio
 * @Description :  域名和域名组关系表
 * @Date: 2022-07-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_domain_group_relation")
public class Domain2DomainGroup implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 域名名称
     */
    @TableField("domain_id")
    private Long domainId;

    /**
     * 域名组id
     */
    @TableField("domain_group_id")
    private Long domainGroupId;

    /**
     * tab
     */
    @TableField("tab")
    private String tab;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Long updateTime;

    /**
     * 最后更新人
     */
    @TableField("last_updated")
    private String lastUpdated;

}
