package com.panda.sport.merchant.manage.entity.form.aggregationPlatform;

import com.baomidou.mybatisplus.annotation.TableField;
import com.panda.sport.merchant.common.constant.AggregateFestivalResourceCfgConstants;
import com.panda.sport.merchant.manage.validation.AddGroup;
import com.panda.sport.merchant.manage.validation.UpdateGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


@Getter
@Setter
@ToString
public class FestiveCfgForm {
    @Null(message = "id should be empty", groups = AddGroup.class)
    @NotNull(message = "id can not be null", groups = UpdateGroup.class)
    private Long id;

    /**
     * 平台类型：	TY：体育	DJ：电竞	CP：彩票
     */
    @NotBlank(message = "platform can not be blank")
    private String platform;

    /**
     * zh：中文简体	tw：中文繁体	en：英文	vi：越南语
     */
    @NotBlank(message = "请勾选语言配置")
    private String language;

    /**
     * DJ：PC节日资源图	CP：PC节日资源图	TY：PC顶部左侧（日间版）
     */
    @NotNull(message = "img1 can not be null")
    private String img1;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_1_type can not be blank")
    private String img1Type;

    /**
     * img1跳转连接
     */
    @NotNull(message = "img1Url can not be null")
    private String img1Url;

    /**
     * DJ：PC推荐位	CP：PC节日资源图	TY: PC顶部左侧（夜间版）
     */
    @NotNull(message = "img2 can not be null")
    private String img2;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_2_type can not be blank")
    private String img2Type;

    /**
     * img2跳转连接
     */
    @NotNull(message = "img2Url can not be null")
    private String img2Url;

    /**
     * DJ：PC弹窗资源图	CP：H5节日资源图	TY：H5节日资源图（日间版）
     */
    @NotNull(message = "img3 can not be null")
    private String img3;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_3_type can not be blank")
    private String img3Type;

    /**
     * img3跳转连接
     */
    @NotNull(message = "img3Url can not be null")
    private String img3Url;

    /**
     * DJ：H5节日资源图	CP：H5节日资源图	TY：H5节日资源图（夜间版）
     */
    @NotNull(message = "img4 can not be null")
    private String img4;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_4_type can not be blank")
    private String img4Type;

    /**
     * img4跳转连接
     */
    @NotNull(message = "img4Url can not be null")
    private String img4Url;

    /**
     * DJ：H5推荐位	CP：H5节日资源图	TY：PC轮播1（日间版）
     */
    @NotNull(message = "img5 can not be null")
    private String img5;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_5_type can not be blank")
    private String img5Type;

    /**
     * img5跳转连接
     */
    @NotNull(message = "img5Url can not be null")
    private String img5Url;

    /**
     * DJ：H5弹窗资源图	CP：H5节日资源图	TY：PC轮播2（日间版）
     */
    @NotNull(message = "img6 can not be null")
    private String img6;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_6_type can not be blank")
    private String img6Type;

    /**
     * img6跳转连接
     */
    @NotNull(message = "img6Url can not be null")
    private String img6Url;

    /**
     * CP：PC节日资源图   TY: PC轮播3（日间版）
     */
    @NotNull(message = "img7 can not be null")
    private String img7;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img_7_type can not be blank")
    private String img7Type;

    /**
     * img7跳转连接
     */
    @NotNull(message = "img7Url can not be null")
    private String img7Url;

    /**
     * TY：PC轮播1（夜间版）
     * @see AggregateFestivalResourceCfgConstants
     */
    @NotNull(message = "img8 can not be null")
    private String img8;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img8_type can not be null")
    private String img8Type;

    /**
     * img8跳转连接
     */
    @NotNull(message = "img8_url can not be null")
    private String img8Url;

    /**
     *  TY：PC轮播2（夜间版）
     * @see AggregateFestivalResourceCfgConstants
     */
    @NotNull(message = "img9 can not be null")
    private String img9;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img9_type can not be null")
    private String img9Type;

    /**
     * img9跳转连接
     */
    @NotNull(message = "img9_url can not be null")
    private String img9Url;

    /**
     *  TY：PC轮播3（夜间版）
     * @see AggregateFestivalResourceCfgConstants
     */
    @NotNull(message = "img10 can not be null")
    private String img10;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @NotBlank(message = "img10_type can not be null")
    private String img10Type;

    /**
     * img10跳转连接
     */
    @NotNull(message = "img10_url can not be null")
    private String img10Url;

    /**
     *  TY：H5日间版UI挂件
     * @see AggregateFestivalResourceCfgConstants
     */
    @TableField("img_11")
    private String img11;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_11_type")
    private String img11Type;

    /**
     * img11跳转连接
     */
    @TableField("img_11_url")
    private String img11Url;

    /**
     *  TY：H5夜间版UI挂件
     * @see AggregateFestivalResourceCfgConstants
     */
    @TableField("img_12")
    private String img12;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_12_type")
    private String img12Type;

    /**
     * img12跳转连接
     */
    @TableField("img_12_url")
    private String img12Url;


    /**
     * 开始时间
     */
    @NotNull(message = "请设置生效时间")
    private Long startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "请设置生效时间")
    private Long endTime;
}
