package com.panda.sport.merchant.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AricleStatisticsDto {
    private Long articleId;
    private String articleTittle;
    private Long tagId;
    private String keyWords;
    private Long startTime;
    private Long endTime;
    private int pageNum = 1;
    private int pageSize = 20;
}
