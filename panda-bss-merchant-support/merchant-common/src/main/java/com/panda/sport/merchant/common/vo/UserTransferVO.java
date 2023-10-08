package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserTransferVO implements Serializable {
    /**
     * 用户id
     */
    private String userId;
    private Long uid;
    /**
     * 用户名
     */
    private String userName;

    private String orderNo;

    /**
     * 查询账变数据为负数
     */
    private Boolean bizStatus;

    private List<Integer> bizTypeList;


    /**
     * 投注开始时间
     */
    private Long startTimeL;
    /**
     * 投注开始时间(yyyyMMdd HH:mm:ss)
     */
    private String startTime;

    /**
     * 投注结束时间(yyyyMMdd HH:mm:ss)
     */
    private String endTime;

    /**
     * 投注结束时间
     */
    private Long endTimeL;


    /**
     * 页码
     */
    Integer page;

    /**
     * 页面大小
     */
    Integer size;

    /**
     * 开始
     */
    private Integer start;

    /**
     * 结束
     */
    private Integer end;

    private Integer pageSize;

    private Integer pageNum;

    private Integer pageNo;

    private String sort;

    private String orderBy;

    private String merchantCode;

    private List<String> merchantCodeList;

    public void setUserId(String userId) {
        this.userId = userId;
        if (StringUtils.isNotEmpty(userId)) {
            this.uid = Long.parseLong(userId);
        }
    }
}


