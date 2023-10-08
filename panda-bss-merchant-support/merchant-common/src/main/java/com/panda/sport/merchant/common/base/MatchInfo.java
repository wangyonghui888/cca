package com.panda.sport.merchant.common.base;

import lombok.Data;

@Data
public class MatchInfo {

    private String sportName;
    private String matchName;
    private Long beginTime;
    private Integer matchStatus;

}
