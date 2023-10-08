package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  日志查询
 * @Date: 2020-09-01 16:57
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantLogFindVO implements Serializable {

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
     * 页面编码
     */
    private String pageCode;

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
     * 标识
     */
    private List<Integer> tags;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 操作字段
     */
    private String fieldName;

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
    String ip ;


    public static String toJson(MerchantLogFindVO userVO) {
        Gson gson = new Gson();
        return gson.toJson(userVO);
    }
}
