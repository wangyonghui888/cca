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
 * 
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("t_article_statistics")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TArticleStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    @TableId(value = "article_id",type = IdType.INPUT)
    private Long articleId;
    
    /**
     * 阅读次数（客户端展示）
     */
    @TableField("read_counts")
    private Long readCounts;

    /**
     * 真实阅读次数
     */
    @TableField("real_read_counts")
    private Long realReadCounts;

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
