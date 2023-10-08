package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;

/**
 * <p>
 * 赛事文章表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("t_article")
public class TArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 赛种ID
     */
    @TableField("sport_id")
    private Long sportId;

    /**
     * 赛种名称
     */
    @TableField("sport_name")
    private String sportName;

    /**
     * 赛事ID，如果是冠军则是盘口ID
     */
    @TableField("match_id")
    private Long matchId;

    /**
     * 赛事详情
     */
    @TableField("match_detail")
    private String matchDetail;

    /**
     * 主队名称
     */
    @TableField("home_name")
    private String homeName;

    /**
     * 客队名称
     */
    @TableField("away_name")
    private String awayName;

    /**
     * 玩法ID
     */
    @TableField("play_id")
    private Long playId;

    /**
     * 联赛ID
     */
    @TableField("league_id")
    private Long leagueId;

    /**
     * 联赛名称
     */
    @TableField("league_name")
    private String leagueName;

    /**
     * 联赛排序
     */
    @TableField("league_sort")
    private Long leagueSort;

    /**
     * 栏目ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 栏目名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 作者ID
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 作者名称
     */
    @TableField("author_name")
    private String authorName;

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
     * 封面图，数组，使用;分割
     */
    @TableField("thumbnails")
    private String thumbnails;

    /**
     * 摘要
     */
    @TableField("summary")
    private String summary;

    /**
     * 展示状态	0：下架	1：上架
     */
    @TableField("is_show")
    private Integer isShow;

    /**
     * 是否置顶	0：否	1：是
     */
    @TableField("is_top")
    private Integer isTop;

    /**
     * 是否冠军	0：否	1：是
     */
    @TableField("is_champion")
    private Integer isChampion;

    /**
     * 置顶上线时间
     */
    @TableField(value = "top_start_time",updateStrategy =FieldStrategy.IGNORED)
    private Long topStartTime;

    /**
     * 置顶下线时间
     */
    @TableField(value = "top_end_time",updateStrategy =FieldStrategy.IGNORED)
    private Long topEndTime;

    /**
     * 上线时间
     */
    @TableField(value = "online_time",updateStrategy= FieldStrategy.IGNORED)
    private Long onlineTime;

    /**
     * 真实的上线时间
     */
    @TableField("show_time")
    private Long showTime;

    /**
     * 权重（阅读量=真实点击数*权重）
     */
    @TableField("factor")
    private Integer factor;

    /**
     * 永久置顶	0：否	1：是
     */
    @TableField("permanent_top")
    private Integer permanentTop;

    /**
     * 标签ID
     */
    @TableField("tag_id")
    private Long tagId;

    /**
     * 标签名称
     */
    @TableField("tag_name")
    private String tagName;

    /**
     * 标签名称
     */
    @TableField("tag_color")
    private String tagColor;

    /**
     * 文章内容
     */
    @TableField("article_content")
    private String articleContent;

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

    /**
     * 排序优先级0：普通文章1：置顶文章2：永久置顶文章
     */
    @TableField("sort_priority")
    private Integer sortPriority;

    /**
     * 关键字
     */
    @TableField("key_words")
    private String keyWords;
}
