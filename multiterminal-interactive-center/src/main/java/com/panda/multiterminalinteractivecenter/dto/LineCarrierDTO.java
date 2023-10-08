package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineCarrierDTO {

    private Long id;

    /** 目标线路商id */
    private Long targetId;

    private String lineCarrierName;

    private String tab;

    private Integer lineCarrierStatus;
    /**1禁用2删除*/
    private Integer operationType;
    /**校验步数：1校验域名组阈值， 2校验域名组是否有足够域名切换 3后端自己调用的*/
    private Integer step;
}
