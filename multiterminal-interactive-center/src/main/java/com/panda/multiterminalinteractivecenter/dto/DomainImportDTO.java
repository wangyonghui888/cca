package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainImportDTO {

    private Long domainGroupId;

    // 1全量，2增量
    private Integer importType;

    private List<String> domainList;

    private String tab;

}
