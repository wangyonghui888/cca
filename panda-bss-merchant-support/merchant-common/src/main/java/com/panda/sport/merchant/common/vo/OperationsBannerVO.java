package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:24:47
 */
@Getter
@Setter
@ToString
public class OperationsBannerVO implements Serializable {

    private static final long serialVersionUID = -655564019986043319L;
    /**
     * 主键
     */
    private String id;
    /**
     * 类型：1-,首页banner  2-列表banner  3-浮层图标   4-列表页弹窗   5-pc端首页弹窗
     */
    private Integer tType;

    /**
     * 发布范围：商户json格式List<String>,如果为全部商户则留空
     */
    private String merchantList;

    /**
     * 有效开始时间
     */
    private Long beginTime;

    /**
     * 有效结束时间
     */
    private Long endTime;

    /**
     * 文案名称
     */
    private String bannerName;

    /**
     * 域名地址
     */
    private String hostUrl;

    /**
     * 排列顺序
     */
    private Integer orderNum;

    /**
     * 状态：0,-关，1-开启-预览, 2-开启-线上
     */
    private Integer status;

    /**
     * 语言类型，越南语：vi,英语：en,台湾：zh 简体：zs  等
     */
    private String langType;

    /**
     * 图片URL
     */
    private String imgUrl;

    /**
     * 二次确认弹框内容
     */
    private String secondComfirm;

    /**
     * 确认跳转提示文案
     */
    private String comfirmTxt;

    /**
     * 拒绝调转按钮文案
     */
    private String rejectTxt;

    /**
     * 使用商户
     */
    private String merchant;

    /**
     * 测试用户
     */
    private String testUser;

    /**
     * 图片名称
     */
    private String imgName;
    /**
     * 展示次数
     */
    private Integer showNum;

    /**
     *1是登录展示 2活动时间内弹出
     */
    private Integer showLoginTag;

    /**
     * 连接类型(0:无连接,1:内部导航,2:弹窗连接)
     */
    private String urlType;
}
