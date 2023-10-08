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
 * 赛事文章标签表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("t_article_tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 联赛名称
     */
    @TableField("league_name")
    private String leagueName;

    /**
     * 标签颜色
     */
    @TableField("tag_color")
    private String tagColor;

    /**
     * 标签名称
     */
    @TableField("tag_name")
    private String tagName;

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


}
