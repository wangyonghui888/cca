package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description maintenance_platform
 * @author duwan
 * @date 2022-03-18
 */
@Data
@TableName("m_maintenance_platform")
public class MaintenancePlatform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 平台或服务名称
     */
    private String serverName;

    /**
     * 服务编码
     */
    private String serveCode;

    /**
     * 所属项目
     */
    private String dataCode;

    /**
     * 服务主路径
     */
    private String baseUrl;

    /**
     * 踢除用户类型1 2 3 4
     */
    private String kickUserType;

    /**
     * 踢用户用的url地址
     */
    private String kickUserUrl;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;


    public MaintenancePlatform() {}
}