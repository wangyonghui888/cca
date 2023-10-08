package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  用户账变查询
 * @Date: 2020-09-01 16:57
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountFindVO implements Serializable {

    private static final long serialVersionUID = 2819257632275334582L;

    /**
     * 交易号
     */
    private String transferId;

    private List<String> transferIdList;

    private String userId;
    /**
     * 注单号
     */
    private String orderNo;
    private Long uid;
    private List<String> userIdList;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 用户名
     */
    private String username;

    private String fakeName;

    /**
     * 状态
     */
    private Integer status;

    private Integer retryCount;

    private Integer minRetryCount;

    /**
     * 交易模式  1 免转   2转帐
     */
    private Integer transferMode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 查询商户集合
     */
    private List<String> merchantCodes;

    /**
     * 接收参数使用
     */
    private List<String> merchantCodeList;

    /**
     * 业务类型
     */
    private List<Integer> bizTypeList;

    /**
     * 业务类型字符串
     */
    private String bizTypeListStr;

    /**
     * 金额标识 0 为负数 1为正数
     */
    private Integer amountTag;

    /**
     * 开始时间
     */
    private Long startTime;


    /**
     * 结束时间
     */
    private Long endTime;

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

    /**
     * 排序
     */
    String sort;

    /**
     * 二次结算 1勾选
     */
    Integer orderTimes;

    /**
     * 请求对象
     */
    String token;

    /**
     * 排序参数
     */
    String orderBy;
    String language;

    /**
     * 币种编码
     */
    private String currency;
}
