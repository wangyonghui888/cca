package com.panda.sport.merchant.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDomainMerchantDTO {
    private Long Id;

    /**
     * 全量域名
     */
    private String videoAll;
    /**
     * 精彩剪辑
     */
    private String videoExcitingEditing;

    private List<String> merchantCodeList;
}
