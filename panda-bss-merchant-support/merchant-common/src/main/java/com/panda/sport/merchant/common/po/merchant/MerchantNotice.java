package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author YK
 * @Description: 公告栏
 * @date 2020/3/12 16:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantNotice implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 英文简体公告标题
     */
    private String enTitle;

    /**
     * 中文繁体公告标题
     */
    private String zhTitle;

    /**
     * 日文繁体公告标题
     */
    private String jpTitle;

    /**
     * 越语标题
     */
    private String viTitle;

    /**
     * 泰语标题
     */
    private String thTitle;

    /**
     * 马来语标题
     */
    private String maTitle;

    /**
     * 正文-马来语
     */
    private String biTitle;

    /**
     * 缅甸语标题
     */
    private String myaTitle;
    /**
     * 韩语标题
     */
    private String koTitle;
    /**
     * 葡萄牙语标题
     */
    private String ptTitle;
    /**
     * 马西班牙语标题
     */
    private String esTitle;

    /**
     * 公告状态，0:草稿，1:已发布
     */
    private Integer status;

    /**
     * 正文
     */
    private String context;

    /**
     * 正文-英文
     */
    private String enContext;

    /**
     * 正文-繁体
     */
    private String zhContext;

    /**
     * 正文-日本
     */
    private String jpContext;

    /**
     * 正文-越语
     */
    private String viContext;
    /**
     * 正文-泰语
     */
    private String thContext;
    /**
     * 正文-马来语
     */
    private String maContext;

    /**
     * 正文-印尼语
     */
    private String biContext;
    /**
     * 正文-缅甸语
     */
    private String myaContext;
    /**
     * 正文-韩语
     */
    private String koContext;
    /**
     * 正文-西班牙语
     */
    private String esContext;
    /**
     * 正文-葡萄牙语
     */
    private String ptContext;

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
     * 发布范围,1:直营商户，2:渠道商户,3:二级商户,4:投注用户,5:panada内部用户==》以英文逗号拼接, 6:所有商户投注用户，7部分商户投注用户
     */
    private String releaseRange;


    /**
     * 商户codeS,以逗号拼接一起
     */
    private String merchantCodes;

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
     * 公告类型0赛果1事件
     */
    private Integer noticeResultType;

    @ApiModelProperty(value = "其他端的公告表对应的ID主键，用于联动修改和删除用")
    private Long multiTerminalNoticeId;

    /**
     * 异常用户ids
     */
    private String abnormalUserIds;
}
