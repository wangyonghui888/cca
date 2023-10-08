package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

@Data
public class MaintenanceRecordEditVo {
    /**
     * 维护记录id
     */
    private Long recordId;

    /**
     * 维护平台id
     */
    private Long platformId;

    /**
     * 所属项目
     */
    private String dataCode;
}
