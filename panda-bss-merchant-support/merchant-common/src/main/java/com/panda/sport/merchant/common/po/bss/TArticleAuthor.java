package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 赛事文章作者表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("t_article_author")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TArticleAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作者昵称
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 头像地址
     */
    @TableField("avatar_img")
    private String avatarImg;

    /**
     * 简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 0：禁用	1：启用
     */
    @TableField("author_status")
    private Integer authorStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;


}
