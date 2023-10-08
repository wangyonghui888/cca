package com.panda.sport.merchant.manage.entity.form;

import com.panda.sport.merchant.common.po.merchant.NoticeLangContextPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author YK
 * @Description:发布公告
 * @date 2020/3/13 14:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantNoticeForm {

    private Long id;

    /**
     * 公告标题
     */
//    @NotEmpty(message = "公告标题不能为空")
//    private String title;

    /**
     * 重要标记 0否 1是
     */
    @NotNull(message = "重要标记不能为空")
    private Integer isTag;

    /**
     * 发布范围,1:直营商户，2:渠道商户,3:二级商户,4:投注用户,5:panada内部用户,6:所有商户投注用户，7部分商户投注用户,8信用网==》以英文逗号拼接,
     */
    @NotEmpty(message = "发布范围")
    private String releaseRange;

    /**
     * 发布的商户codes,以逗号隔开  releaseRange：6 =》商户的codes 为 空字符串 ， 7  =商户的codes为商户的codes,用逗号凭接一起的字符串
     */
    private String mids;


    /**
     * false 为未全部选中：  true 是全部选中，
     */
    private Boolean isFull;


    /**
     * 登录弹出 0否 1是
     */
    //@NotNull(message = "登录弹出不能为空")
    private Integer isPop;

    /**
     * 正文
     */
//    @NotEmpty(message = "正文不能为空")
//    private String context;

    /**
     * 上传的附件
     */
    private String upload;

    /**
     * 上传附件的名称
     */
    private String uploadName;

    /**
     * 公告类型
     */
    private Integer noticeTypeId;

    /**
     * 赛事ID
     */
    private String standardMatchId;

    /**
     * 球种ID
     */
    private Long sportId;


    /**
     * 是否发布公告
     */
    @NotNull(message = "发布公告状态位不能为空")
    private Boolean isPublish;


    @NotEmpty(message = "创建人不能为空")
    private String createdBy;


    /**
     * 国际化公告
     */
    @NotNull(message = "类型不能为空")
    private List<NoticeLangContextPo> list;


    /**
     * 开始时间
     */
    //@NotNull(message = "生效开始时间不能为空")
    private Long beginTime;

    /**
     * 结束时间
     */
    //@NotNull(message = "生效结束时间不能为空")
    private Long endTime;

    /**
     *是否置顶
     */
    @NotNull(message = "置顶不能为空")
    private Integer isTop;


    /**
     * 是否轮播
     */
    @NotNull(message = "轮播不能为空")
    private Integer isShuf;

    /**
     * 异常用户id
     * merchantCode_username
     */
    private String abnormalUserIds;

}
