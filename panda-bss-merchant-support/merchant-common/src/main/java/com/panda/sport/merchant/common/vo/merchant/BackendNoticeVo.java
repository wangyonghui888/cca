package com.panda.sport.merchant.common.vo.merchant;

import com.panda.sport.merchant.common.po.merchant.NoticeLangContextPo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class BackendNoticeVo implements Serializable {

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
     * 正文
     */
    private String context;

    /**
     * 浏览次数
     */
    private Long  visitNumber;

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
     * 发布范围,1:直营商户，2:渠道商户,3:二级商户,4:投注用户,5:panada内部用户, 6:所有商户投注用户，7部分商户投注用户 ==》以英文逗号拼接
     */
    private String releaseRange;


    /**
     * 商户codeS,以逗号拼接一起
     */
    private String[] merchantCodes;

    /**
     * 是否选中全部商户 0 为全选  1 全选
     */
    private Integer isFull;

    /**
     * 登录弹出 0否 1是
     */
    private Integer isPop;

    /**
     * 上传的附件
     */
    private String upload;

    /**
     * 上传的附件的名称
     */
    private String uploadName;

    /**
     * 重要标记 0否 1是
     */
    private Integer isTag;

    /**
     * 公告类型
     */
    private Integer noticeTypeId;

    /**
     * s_match_info的赛事id
     */
    private String standardMatchId;

    /**
     *赛事id的赛种
     */
    private Long sportId;

    /**
     * 玩法ID
     */
    private Long playId;

    /**
     * 公告来源
     */
    private Integer isFrom;

    /**
     * tybss_new数据库种的t_match_noitce的主键ID
     */
    private Long tMatchNoticeId;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 最后修改人
     */
    private String updatedBy;

    /**
     * 国际化公告
     */
    private List<NoticeLangContextPo> list;

    /**
     * 开始时间
     */
    private Long beginTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     *是否置顶
     */
    private Integer isTop;


    /**
     * 是否轮播
     */
    private Integer isShuf;

    /**
     * 异常用户ids
     */
    private String abnormalUserIds;
}
