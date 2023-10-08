package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  电竞公告修改请求VO
 * @Date: 2022-03-08 17:18
 */
@Data
@Api(value = "电竞公告修改请求VO")
public class ESportsNoticeEditReqVO implements Serializable {

    private static final long serialVersionUID = 4613455939329852157L;

    @ApiModelProperty(value = "电竞公告表对应的ID主键，用于修改和删除用", example = "11111")
    @NotNull(message = "电竞公告表对应的ID主键不允许为空！")
    private Long multiTerminalNoticeId;

    @ApiModelProperty(value = "中文简体的标题", example = "测试")
    private String title;

    @ApiModelProperty(value = "中文简体的正文内容",example = "测试一下")
    private String context;

    @ApiModelProperty(value = "英文标题", example = "test")
    private String enTitle;

    @ApiModelProperty(value = "英文正文", example = "test")
    private String enContext;

    @ApiModelProperty(value = "中文繁体的标题", example = "測試")
    private String zhTitle;

    @ApiModelProperty(value = "中文繁体的正文", example = "測試")
    private String zhContext;

    @ApiModelProperty(value = "日文的标题", example = "テスト")
    private String jpTitle;

    @ApiModelProperty(value = "日文的正文", example = "テスト")
    private String jpContext;

    @ApiModelProperty(value = "越文的标题", example = "kiểm tra")
    private String viTitle;

    @ApiModelProperty(value = "越文的正文", example = "kiểm tra")
    private String viContext;

    @ApiModelProperty(value = "泰文的标题", example = "kiểm tra")
    private String thTitle;

    @ApiModelProperty(value = "泰文的正文", example = "kiểm tra")
    private String thContext;

    @ApiModelProperty(value = "马来西亚文的标题", example = "kiểm tra")
    private String maTitle;

    @ApiModelProperty(value = "马来西亚文的正文", example = "kiểm tra")
    private String maContext;

    @ApiModelProperty(value = "电竞公告类型|31 LOL |32 DOTA2 |33 KoG |34 CS_GO", example = "31")
    @Range(min = 31, max = 34,message = "电竞公告类型默认为31-34")
    private Integer noticeType;

    @ApiModelProperty(value = "是否置顶|0 否 |1 是，置顶和轮播为互斥条件，两者须选一个开启，此处仅做说明，添加时不做判断处理", example = "1")
    @Range(min = 0, max = 1,message = "是否置顶默认为0-1")
    private Integer isTop;

    @ApiModelProperty(value = "公告状态  0:草稿，1:已发布，2:取消发布,添加时默认已发布", example = "1")
    @Range(min = 0, max = 2,message = "公告状态默认为0-2")
    private Integer status;

    @ApiModelProperty(value = "是否轮播|0 否 |1 是，默认为是", example = "1")
    @Range(min = 0, max = 1,message = "是否轮播选择范围为0-1")
    private Integer isRotation;

    @ApiModelProperty(value = "是否重要|0 否 |1 是，默认为否", example = "0")
    @Range(min = 0, max = 1,message = "是否重要选择范围为0-1")
    private Integer isImportant;

    @ApiModelProperty(value = "是否登录弹出|0 否 |1 是，默认为否", example = "0")
    @Range(min = 0, max = 1,message = "是否登录弹出选择范围为0-1")
    private Integer isLoginPopup;

    @ApiModelProperty(value = "生效开始时间时间戳(毫秒值)，默认为调用接口时间")
    private Long effectiveTime;

    @ApiModelProperty(value = "生效结束时间时间戳(毫秒值)，默认为开始时间3天后的相同时间点")
    private Long effectiveEndTime;

    @ApiModelProperty(value = "发布人，默认为Auto",example = "Auto")
    private String publishedBy;

    @ApiModelProperty(value = "赛事ID，默认为0",example = "0")
    private String matchId;
}
