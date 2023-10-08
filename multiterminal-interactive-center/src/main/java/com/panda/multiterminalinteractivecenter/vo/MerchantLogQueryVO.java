package com.panda.multiterminalinteractivecenter.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


/**
 * @author Z9-velpro
 */
@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantLogQueryVO implements Serializable {

    private static final long serialVersionUID = 2819257632275334581L;

    private String merchantName;

    private String merchantCode;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 查询商户集合
     */
    private List<String> merchantCodes;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作人
     */
    private String operatBy;

    /**
     * 页面编码
     */
    private String pageCode;

    private List<String> pageCodes;

    /**
     * 操作类型(切换源选择，1自检2手动，默认所有)
     */
    private Integer operatSourceType;

    /**
     * 操作类型
     */
    private Integer operatType;

    /**
     * 操作类型(数据库使用)
     */
    private List<Integer> operatTypes;

    /**
     * 标识
     */
    private Integer tag;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 操作字段
     */
    private String fieldName;
    /**
     * 操作字段
     */
    private String operatField;

    /**
     * 页码
     */
    Integer pageNum;

    /**
     * 页面大小
     */
    Integer pageSize;

    /**
     * 开始
     */
    Integer start;

    Integer domainType;

    /**
     * ip地址
     */
    String ip;

    /**
     * 日志来源
     * 1 三端操作日志
     * 2 域名切换日志
     * 3 维护日志
     * 4 踢用户日志
     */
    String logSource;

    /**
     * 查询维护日志
     * 0 不查询
     * 默认查询
     */
    Integer isQueryMaintenanceLog;

    /**
     * 查询踢用户日志
     *  0 不查询
     *  默认查询
     */
    Integer isQueryKickUserLog;



}
