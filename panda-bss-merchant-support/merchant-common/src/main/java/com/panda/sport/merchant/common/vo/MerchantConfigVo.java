package com.panda.sport.merchant.common.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  TODO
 * @Date: 2021-02-04 13:48:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MerchantConfigVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "商户编码不能为空！")
    private String merchantCode;

    @NotNull(message = "配置ID不能为空！")
    private Long id;

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
}
