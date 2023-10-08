package com.panda.sport.merchant.common.vo;

import lombok.Data;

/**
 * <p>
 * 公共栏
 * </p>
 *
 * @author Auto Generator
 * @since 2020-06-11
 */
@Data
public class TMatchNoticeVO {


    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告状态，0:草稿，1:已发布
     */
    private Integer status;

    /**
     * 公告类型，1、足球赛事  2、篮球赛事，3网球赛事，4羽毛球赛事，5乒乓球赛事，6斯诺克赛事,100 活动公告
     */
    private Integer noticeType;

    /**
     * 正文
     */
    private String context;

    /**
     * 浏览次数
     */
    private Long visitNumber;

    /**
     * 发送时间
     */
    private Long sendTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 发布范围,1:全体用户,2、商户，以英文逗号拼接
     */
    private String releaseRange;

    /**
     * 登录弹出 0否 1是
     */
    private Integer isPop;

    /**
     * 上传的附件
     */
    private String upload;

    /**
     * 重要标记 0否 1是
     */
    private Integer isTag;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 上传的附件的文件名称，多个以英文;号分隔'
     */
    private String uploadName;

    /**
     * 异常用户ids
     */
    private String abnormalUserIds;
    /**
     * s_match_info的赛事id
     */
    private String standardMatchId;
    /**
     * 开始时间
     */
    private Long beginTime;

    /**
     * 结束时间
     */
    private Long noticeEndTime;
    /**
     * 公告类型
     */
    private Integer noticeTypeId;
    /**
     * 是否轮播
     */
    private Integer isShuf;
    /**
     *是否置顶
     */
    private Integer isTop;

}
