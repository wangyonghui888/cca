package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @author javier
 * @date 2021/1/9
 */
@Data
public class PullDownParamPO {
    Integer pageNum;
    Integer pageSize;
    Long startTime;
    Long endTime;
    Integer sportId;
    String language;
}
