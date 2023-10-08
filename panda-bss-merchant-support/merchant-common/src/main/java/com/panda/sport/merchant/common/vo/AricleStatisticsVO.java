package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AricleStatisticsVO {
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 真实阅读次数
     */
    private Long realReadCounts;

    /**
     * 文章标题
     */
    private String articleTittle;

    /**
     * 真实的上线时间
     */
    private Long showTime;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 关键字
     */
    private String keyWords;
    /**
     * 标签ID
     */
    private Long tagId;
}
