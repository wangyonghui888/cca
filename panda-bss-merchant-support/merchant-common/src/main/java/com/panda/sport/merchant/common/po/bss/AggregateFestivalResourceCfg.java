package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.panda.sport.merchant.common.constant.AggregateFestivalResourceCfgConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 三端节庆资源配置表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-03-06
 * @see AggregateFestivalResourceCfgConstants
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("t_aggregate_festival_resource_cfg")
public class AggregateFestivalResourceCfg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台类型：	TY：体育	DJ：电竞	CP：彩票
     */
    @TableField("platform")
    private String platform;

    /**
     * zh：中文简体	tw：中文繁体	en：英文	vi：越南语
     */
    @TableField("language")
    private String language;

    /**
     * DJ：PC节日资源图	CP：PC节日资源图	TY：PC顶部左侧（日间版）
     */
    @TableField("img_1")
    private String img1;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_1_type")
    private String img1Type;

    /**
     * img1跳转连接
     */
    @TableField("img_1_url")
    private String img1Url;

    /**
     * DJ：PC推荐位	CP：PC节日资源图	TY：PC顶部左侧（夜间版）
     */
    @TableField("img_2")
    private String img2;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_2_type")
    private String img2Type;

    /**
     * img2跳转连接
     */
    @TableField("img_2_url")
    private String img2Url;

    /**
     * DJ：PC弹窗资源图	CP：H5节日资源图	TY：H5节日资源图（日间版）
     */
    @TableField("img_3")
    private String img3;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_3_type")
    private String img3Type;

    /**
     * img3跳转连接
     */
    @TableField("img_3_url")
    private String img3Url;

    /**
     * DJ：H5节日资源图	CP：H5节日资源图	TY：H5节日资源图（日间版）
     */
    @TableField("img_4")
    private String img4;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_4_type")
    private String img4Type;

    /**
     * img4跳转连接
     */
    @TableField("img_4_url")
    private String img4Url;

    /**
     * DJ：H5推荐位	CP：H5节日资源图	TY：PC轮播1（日间版）
     */
    @TableField("img_5")
    private String img5;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_5_type")
    private String img5Type;

    /**
     * img5跳转连接
     */
    @TableField("img_5_url")
    private String img5Url;

    /**
     * DJ：H5弹窗资源图	CP：H5节日资源图	TY：PC轮播2（日间版）
     */
    @TableField("img_6")
    private String img6;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_6_type")
    private String img6Type;

    /**
     * img6跳转连接
     */
    @TableField("img_6_url")
    private String img6Url;

    /**
     * CP：PC节日资源图  TY：PC轮播3（日间版）
     */
    @TableField("img_7")
    private String img7;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_7_type")
    private String img7Type;

    /**
     * img7跳转连接
     */
    @TableField("img_7_url")
    private String img7Url;

    /**
     * TY：PC轮播1（夜间版）
     * @see AggregateFestivalResourceCfgConstants
     */
    @TableField("img_8")
    private String img8;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_8_type")
    private String img8Type;

    /**
     * img8跳转连接
     */
    @TableField("img_8_url")
    private String img8Url;

    /**
     *  TY：PC轮播2（夜间版）
     * @see AggregateFestivalResourceCfgConstants
     */
    @TableField("img_9")
    private String img9;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_9_type")
    private String img9Type;

    /**
     * img9跳转连接
     */
    @TableField("img_9_url")
    private String img9Url;

    /**
     *  TY：PC轮播3（夜间版）
     * @see AggregateFestivalResourceCfgConstants
     */
    @TableField("img_10")
    private String img10;

    /**
     * 0：无连接	1：内部导航	2：弹窗连接
     */
    @TableField("img_10_type")
    private String img10Type;

    /**
     * img10跳转连接
     */
    @TableField("img_10_url")
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
    @TableField("start_time")
    private Long startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Long endTime;

    /**
     * 资源状态：	0：关闭	1：开启
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
