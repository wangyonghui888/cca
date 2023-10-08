package com.panda.multiterminalinteractivecenter.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainGroupDTO {
    private Long id ;
    private String domainGroupName;
}
