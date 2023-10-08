package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  多端公告本地记录表
 * @Date: 2022-03-09 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiTerminalNotice implements Serializable {

    private static final long serialVersionUID = -622772445934393516L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "电竞公告类型|1 DOTA2 |2 CS:GO 具体根据代码定义")
    private Integer noticeType;

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

    @ApiModelProperty(value = "是否置顶|0 否 |1 是")
    private Integer isTop;

    @ApiModelProperty(value = "是否重要|0 否 |1 是，默认为否")
    private Integer isImportant;

    @ApiModelProperty(value = "是否登录弹出|0 否 |1 是，默认为否")
    private Integer isLoginPopup;

    @ApiModelProperty(value = "是否轮播|0 否 |1 是，默认为是")
    private Integer isRotation;

    @ApiModelProperty(value = "生效开始时间时间戳，默认为调用接口时间")
    private Long effectiveTime;

    @ApiModelProperty(value = "生效结束时间时间戳，默认为开始时间3天后的相同时间点")
    private Long effectiveEndTime;

    @ApiModelProperty(value = "消息来源|1 电竞 | 2 彩票")
    private Integer source;

    @ApiModelProperty(value = "发布人，默认为Auto")
    private String publishedBy;

    @ApiModelProperty(value = "其他端的公告表对应的ID主键，用于联动修改和删除用")
    private Long multiTerminalNoticeId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private Long updateTime;

    @ApiModelProperty(value = "赛事ID，默认为0",example = "0")
    private String matchId;

}
