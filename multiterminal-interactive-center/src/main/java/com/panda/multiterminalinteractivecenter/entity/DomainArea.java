package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author :  istio
 * @Project Name :  multiterminal-interactive-center
 * @Package Name :  com.panda.multiterminalinteractivecenter.entity
 * @Description :  TODO
 * @Date: 2022-06-22 13:29:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@TableName("m_domain_area")
public class DomainArea {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    /**
     * 0未启用  1启用
     */
    private Integer status;

    /**
     * 0未删除  1删除
     */
    private Integer deleteTag;

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
     * 创建人
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;

    /**
     * 域名数量
     */
    @TableField(exist = false)
    private Integer domainCount;

}
