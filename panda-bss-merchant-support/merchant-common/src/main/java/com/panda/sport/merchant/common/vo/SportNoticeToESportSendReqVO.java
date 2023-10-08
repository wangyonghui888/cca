package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  体育写入电竞请求VO
 * @Date: 2022-03-08 17:18
 */
@Data
@Api(value = "体育写入电竞请求VO")
public class SportNoticeToESportSendReqVO implements Serializable {


    @ApiModelProperty(value = "体育本地公告表主键id", example = "123123")
    private Long sid;

    @ApiModelProperty(value = "商户ID(电竞平台提供)", example = "31433517168705439")
    private Long merchant;

    @ApiModelProperty(value = "体育游戏NBA2K，FIFA", example = "NBA2K")
    private String game_name;

    @ApiModelProperty(value = "是否置顶:0=否,1=是", example = "0")
    private Integer top;

    @ApiModelProperty(value = "对应语言的标题", example = "测试")
    private String title;

    @ApiModelProperty(value = "对应语言的正文内容",example = "测试一下")
    private String content;

    @ApiModelProperty(value = "对应的语言类型|1=简体中文,2=繁体中文,3=英文,4=越南语", example = "1")
    private Integer lang;

}
