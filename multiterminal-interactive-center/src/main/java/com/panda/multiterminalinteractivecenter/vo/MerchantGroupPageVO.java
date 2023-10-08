package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantGroupPageVO implements Serializable {

    private String merchantName;

    private Integer status;

    private String merchantCode;

    private String merchantCodes;

    private Integer agentLevel;

    private String parentName;

    private Integer merchantTag;

    private String merchantGroupId;

    private String domainGroupCode;

    private Integer transferMode;

    private Integer isApp;

    private Integer pageSize;

    private Integer pageNum;

    private String sort;

    private String orderBy;

    private Integer groupType;

    //商户组code : common,y,s,b
    private String groupCode;

    private String tab;
}
