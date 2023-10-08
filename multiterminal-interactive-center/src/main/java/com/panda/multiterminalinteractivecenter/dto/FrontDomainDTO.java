package com.panda.multiterminalinteractivecenter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontDomainDTO {
    private String oldDomain;
    private String newDomain;
    private String userName;
    private String ip;
}
