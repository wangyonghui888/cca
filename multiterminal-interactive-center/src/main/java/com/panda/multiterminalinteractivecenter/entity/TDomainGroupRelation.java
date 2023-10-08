package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 域名组关系表
 * </p>
 *
 * @author amos
 * @since 2022-07-13
 */
@Getter
@Setter
@TableName("t_domain_group_relation")
public class TDomainGroupRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 域名组id(域名组表中的主键id)
     */
    private Long domainGroupId;

    /**
     * 域名id(域名表中的主键id)
     */
    private Long domainId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 最后更新人
     */
    private String lastUpdated;


}
