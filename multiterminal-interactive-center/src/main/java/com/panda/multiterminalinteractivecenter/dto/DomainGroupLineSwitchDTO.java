package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainGroupLineSwitchDTO {

    private Long id;
    private String domainGroupName;
    private String areaName;
    private Integer h5Threshold;
    private Integer pcThreshold;
    private Integer apiThreshold;
    private Integer imgThreshold;
    private Integer h5Count;
    private Integer pcCount;
    private Integer apiCount;
    private Integer imgCount;

}
