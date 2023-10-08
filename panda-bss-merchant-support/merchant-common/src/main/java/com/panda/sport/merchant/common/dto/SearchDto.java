package com.panda.sport.merchant.common.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchDto {
    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 球种类型
     */
    @NotNull(message = "请选择比赛类型")
    private Long sportId;
    /**
     * 联赛id
     */
    private Long leagueId;
    /**
     * 赛事类型1：普通赛事2：冠军
     */
    private Integer type = 1;

}
