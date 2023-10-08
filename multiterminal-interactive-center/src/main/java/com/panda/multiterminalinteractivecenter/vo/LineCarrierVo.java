package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

@Data
public class LineCarrierVo {

    private Long id;
    private Integer lineCarrierCode;
    private String lineCarrierName;
    private Integer lineCarrierStatus;

    private Integer domainGroupId;
    private String domainGroupName;
    private Long domainNum;

    private Integer h5Threshold;
    private Integer pcThreshold;
    private Integer apiThreshold;
    private Integer imgThreshold;

    private Integer domainId;
    private String domainName;

    private String createBy;
    private Long createTime;
    private String updateBy;
    private Long updateTime;
}
