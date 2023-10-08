package com.panda.sport.merchant.common.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MerchantConfigReqVO {

    @NotEmpty(message = "列表不能为空")
    @Size(min = 1,max = 10000, message = "请求参数不能为空")
    private List<Long> idList;

    private static final long serialVersionUID = 1L;

    @NotNull(message = "商户编码不能为空！")
    private String merchantCode;

    private String url;

    /**
     * 开关 1： 关  0：开
     */
    @NotNull(message = "开关不能为空！")
    private Integer tag;

    /**
     * 1: 球类 2：联赛
     */
    @NotNull(message = "类型不能为空！")
    private Integer type;

    /**
     * 0:直营 1:渠道 2:二级
     */
    @NotNull(message = "商户类型不能为空！")
    private String agentLevel;

}
