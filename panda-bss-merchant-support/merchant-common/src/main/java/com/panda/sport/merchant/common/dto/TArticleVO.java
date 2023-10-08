package com.panda.sport.merchant.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TArticleVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 赛种ID
     */
    private Long sportId;

    /**
     * 赛种名称
     */
    private String sportName;

    /**
     * 赛事ID，如果是冠军则是盘口ID
     */
    private Long matchId;

    /**
     * 赛事详情
     */
    private String matchDetail;

    /**
     * 主队名称
     */
    private String homeName;

    /**
     * 客队名称
     */
    private String awayName;

    /**
     * 玩法ID
     */
    private Long playId;

    /**
     * 联赛ID
     */
    private Long leagueId;

    /**
     * 联赛名称
     */
    private String leagueName;

    /**
     * 联赛排序
     */
    private Long leagueSort;

    /**
     * 栏目ID
     */
    private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 文章标题
     */
    private String articleTittle;

    /**
     * 封面图，数组，使用;分割
     */
    private String thumbnails;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 展示状态	0：下架	1：上架
     */
    private Integer isShow;

    /**
     * 是否置顶	0：否	1：是
     */
    private Integer isTop;

    /**
     * 是否冠军	0：否	1：是
     */
    private Integer isChampion;

    /**
     * 置顶上线时间
     */
    private Long topStartTime;

    /**
     * 置顶下线时间
     */
    private Long topEndTime;

    /**
     * 上线时间
     */
    private Long onlineTime;

    /**
     * 真实的上线时间
     */
    private Long showTime;

    /**
     * 权重（阅读量=真实点击数*权重）
     */
    private Integer factor;

    /**
     * 永久置顶	0：否	1：是
     */
    private Integer permanentTop;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 排序优先级0：普通文章1：置顶文章2：永久置顶文章
     */
    private Integer sortPriority;

    /**
     * 关键字
     */
    private String keyWords;
    /**
     * 关联赛事0：不关联1：关联
     */
    private Integer isHaveMatch;

    /**
     * 虚假阅读数
     */
    private Long readCounts;
}
