package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainDTO2 {
    private Long domainId;
    private Integer existOtherGroup;
    private String groupIdStr;
    private String groupNameStr;
}
