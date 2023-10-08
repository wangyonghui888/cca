package com.panda.sport.merchant.common.vo.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ActivityMaintainVO implements Serializable {

    private static final long serialVersionUID = 5241526151768786394L;

    /**
     * h5图片地址
     */
    private String h5MaintainUrl;

    /**
     * pc图片地址
     */
    private String pcMaintainUrl;
    private String title;
    private String content;
    /**
     * 活动参与商户 1全部商户 ，2部分商户
     */
    private Long maintainEndTime;
    /**
     * 部分商户 列表
     */
    private Integer maintainStatus;

}
