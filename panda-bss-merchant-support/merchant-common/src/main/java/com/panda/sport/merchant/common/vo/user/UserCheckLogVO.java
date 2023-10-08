package com.panda.sport.merchant.common.vo.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCheckLogVO implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 提交商户
     */
    private String merchantCode;
    private List<String> merchantCodeList;

    private String uid;

    private String userName;

    /**
     * 0是待协查 1是已完成
     */
    private Integer status;

    private Long startTime;
    private Long endTime;

    /**
     * 检查结果 1 未在我方场馆投注用户
     *   2 投注总笔数不足20 笔
     *   3 投注金额小于等于1000
     *   4 距上次查询不足3天
     */
    private String checkType;
    /**
     * 检查原因
     */
    private String checkReason;
    /**
     * 协查说明
     */
    private String checkExplain;

    private Integer pageSize;
    private Integer pageIndex;
    private String sort;
    private String orderBy;


}


