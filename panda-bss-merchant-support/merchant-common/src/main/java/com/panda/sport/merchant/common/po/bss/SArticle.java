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
 * 三方文章表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_article")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    @TableField("article_id")
    private Integer articleId;

    /**
     * 文章标题
     */
    @TableField("article_tittle")
    private String articleTittle;

    /**
     * 缩略图（多个用;分隔）
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 上线时间
     */
    @TableField("online_time")
    private Long onlineTime;

    /**
     * 文章内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Long modifyTime;

    /**
     * 是否导出	0：否	1：是
     */
    @TableField("article_status")
    private Integer articleStatus;

    /**
     * 链路ID
     */
    @TableField("link_id")
    private String linkId;

    /**
     * 数据源
     */
    @TableField("data_source_code")
    private String dataSourceCode;


}
