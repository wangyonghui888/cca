package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 踢用户日志表
 * @author duwan
 * @date 2022-03-18
 */
@Data
@TableName("m_kick_user_log")
public class KickUserLog {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 数据类型
     */
    private String dataCode;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * (按商户踢用户，按设备踢用户等)
     */
    private int operationType;

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

    public KickUserLog() {}
}
