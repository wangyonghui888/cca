package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * oss域名表
 * </p>
 *
 * @author Baylee
 * @since 2021-05-28
 */
@Getter
@Setter
@ToString
@TableName("t_oss_domain")
public class OssDomain implements Serializable {


    private static final long serialVersionUID = -368957198197600844L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * oss地址
     */
    @TableField("url")
    @NotNull(message = "url is not null")
    private String url;

    /**
     * 是否失效 0：false 1：true
     */
    @TableField("status")
    @NotNull(message = "status is not null")
    private Integer status;

    /**
     * 操作人ID
     */
    @TableField("operator_id")
    @NotNull(message = "operatorId is not null")
    private Integer operatorId;

    /**
     * 操作人
     */
    @TableField("operator_name")
    @NotNull(message = "operatorName is not null")
    private String operatorName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
