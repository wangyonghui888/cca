package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  电竞公告删除请求VO
 * @Date: 2022-03-08 17:18
 */
@Data
@Api(value = "电竞公告删除请求VO")
public class ESportsNoticeDelReqVO implements Serializable {

    /**
     * 必填字段
     */
    @ApiModelProperty(value = "电竞公告表对应的ID主键，用于修改和删除用", example = "11111")
    @NotNull(message = "电竞公告表对应的ID主键不允许为空！")
    private Long multiTerminalNoticeId;

}
