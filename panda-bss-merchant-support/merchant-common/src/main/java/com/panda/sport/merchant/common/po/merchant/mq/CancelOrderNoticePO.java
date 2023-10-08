package com.panda.sport.merchant.common.po.merchant.mq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  二次结算&取消注单自动公告请求PO
 * @Date: 2022-04-11
 */
@Data
@Api(value = "二次结算&取消注单自动公告请求PO")
public class CancelOrderNoticePO implements Serializable {

    private static final long serialVersionUID = -5946829607949571827L;

    @ApiModelProperty(value = "赛事ID", example = "123123")
    @NotNull(message = "赛事ID不允许为空！")
    private Long matchId;

    @ApiModelProperty(value = "球种ID", example = "1")
    @NotNull(message = "球种ID不允许为空！")
    private Integer sportId;

    @ApiModelProperty(value = "公告类型0赛果1事件", example = "0,1")
    @NotNull(message = "赛事ID不允许为空！")
    private Integer noticeResultType;

    @ApiModelProperty(value = "中文简体的标题及内容", example = "测试")
    private String zs;

    @ApiModelProperty(value = "英文的标题及内容", example = "test")
    private String en;

    @ApiModelProperty(value = "中文繁体的标题及内容", example = "測試")
    private String zh;

    @ApiModelProperty(value = "越文的标题及内容", example = "kiểm tra")
    private String yn;

    @ApiModelProperty(value = "泰语的标题及内容", example = "泰语")
    private String th;

    @ApiModelProperty(value = "马来语的标题及内容", example = "马来语")
    private String bm;

    @ApiModelProperty(value = "印尼语的标题及内容", example = "印尼语")
    private String bi;

    @ApiModelProperty(value = "发布人，默认为Auto",example = "Auto")
    private String operatorName;

    @ApiModelProperty(value = "是否追加赛果信息",example = "0,1")
    private Integer isFix;
}
