package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

@Data
public class MaintenanceLogPageVo {

    private Integer page;

    private Integer size;

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
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 操作人
     */
    private String operators;
}
