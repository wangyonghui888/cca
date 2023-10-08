package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description 维护日志表
 * @author duwan
 * @date 2022-03-18
 */
@Data
@TableName("m_maintenance_log")
@Accessors(chain = true)
public class MaintenanceLog  {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 数据类型
     */
    private String dataCode;

    /**
     * 数据类型
     */
    @TableField(exist = false)
    private String dataCodeName;
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

    /**
     * 创建时间
     */
    private Long createTime;


    public MaintenanceLog() {
    }


}
