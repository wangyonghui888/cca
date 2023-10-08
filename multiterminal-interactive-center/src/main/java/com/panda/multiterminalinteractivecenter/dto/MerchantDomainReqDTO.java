package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantDomainReqDTO {
    private Integer merchantTag;
    private String merchantCode;
    private String parentCode;
    private Integer containsType;
    private String containsStr;
}
