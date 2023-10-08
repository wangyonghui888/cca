package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

@Data
public class MaintenanceLogVo {

    /**
     * 数据类型
     */
    private String dataCode;

    /**
     * 操作类型(设置维护，踢用户等)
     */
    private int operationType;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 操作人
     */
    private String operators;
    /**
     * 操作内容
     */
    private String operationContent;
    /**
     * 操作IP
     */
    private String operationIp;
}
