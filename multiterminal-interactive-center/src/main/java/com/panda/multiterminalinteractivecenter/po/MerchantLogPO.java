package com.panda.multiterminalinteractivecenter.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author :  ifan
 * @Description :  TODO
 * @Date: 2022-07-11
 */
@Data
@Accessors(chain = true)
public class MerchantLogPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作类型 参考代码枚举
     */
    private Integer operatType;

    /**
     * 操作类型名称
     */
    private String typeName;

    /**
     * 页面名称
     */
    private String pageName;

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
     * 日志标识 0 商户端 1后端 2 三端
     */
    private Integer logTag;

    /**
     * 操作时间
     */
    private Long operatTime;

    private String domainSelfResult;

    private String domainThirdResult;

    private Integer domainType;

    /**
     * ip外网
     */
    private String ip ;

}
