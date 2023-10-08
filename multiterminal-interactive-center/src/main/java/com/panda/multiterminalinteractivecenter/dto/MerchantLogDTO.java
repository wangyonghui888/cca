package com.panda.multiterminalinteractivecenter.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author Z9-velpro
 */
@Data
@Accessors(chain = true)
public class MerchantLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 操作人
     */
    private String operatBy;

    /**
     * 操作类型 参考代码枚举
     */
    private Integer operatType;

    /**
     * 操作时间
     */
    private Long operatTime;


    /**
     * 数据id
     */
    private String dataId;

    /**
     * 操作字段
     */
    private String operatField;

    /**
     * 操作之前的值
     */
    private String beforeValues;

    /**
     * 操作之后的值
     */
    private String afterValues;

    /**
     * 页面名称
     */
    private String pageName;


    private Integer domainType;

    /**
     * ip外网
     */
    private String ip ;
    /**
     * 日志来源
     * 1 三端操作日志
     * 2 域名切换日志
     * 3 维护日志
     * 4 踢用户日志
     */
    private Integer logSource ;

    /**
     * 日志标识 0 商户端 1后端 2 三端
     */
    private Integer logTag;

    /**
     * 页面编码 参考josn配置
     */
    private String pageCode;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    private String domainSelfResult;

    private String domainThirdResult;

    /**
     * 类型名称
     */
    private String typeName;

}
