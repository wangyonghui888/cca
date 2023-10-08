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
public class FrontDomainMerchantDTO {
    private Long Id;

    private String pcDomain;
    private String h5Domain;
    private List<String> merchantCodeList;
}
