package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商户日志
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("merchant_log")
public class MerchantLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 操作类型 参考代码枚举
     */
    @TableField("operat_type")
    private Integer operatType;

    /**
     * 类型名称
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 页面名称
     */
    @TableField("page_name")
    private String pageName;

    /**
     * 页面编码 参考josn配置
     */
    @TableField("page_code")
    private String pageCode;

    /**
     * 商户编码
     */
    @TableField("merchant_code")
    private String merchantCode;

    /**
     * 商户名称
     */
    @TableField("merchant_name")
    private String merchantName;

    /**
     * 数据id
     */
    @TableField("data_id")
    private String dataId;

    /**
     * 操作字段
     */
    @TableField("operat_field")
    private String operatField;

    /**
     * 操作之前的值
     */
    @TableField("before_values")
    private String beforeValues;

    /**
     * 操作之后的值
     */
    @TableField("after_values")
    private String afterValues;

    /**
     * 日志标识 0 商户端 1后端
     */
    @TableField("log_tag")
    private Integer logTag;

    /**
     * 操作时间
     */
    @TableField("operat_time")
    private Long operatTime;

    /**
     * 域名自检结果
     */
    @TableField("domain_self_result")
    private String domainSelfResult;

    /**
     * 域名第三方检测结果
     */
    @TableField("domain_third_result")
    private String domainThirdResult;


}
