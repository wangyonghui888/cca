package com.panda.sport.merchant.common.po.match;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <p>
 * 公共栏
 * </p>
 *
 * @author Auto Generator
 * @since 2020-06-11
 * 注意 添加新语言 必须补充 下方transferNoticePerfix 方法
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TMatchNotice implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String NOTICE_PREFIX = "$P$M$";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 英文标题
     */
    private String enTitle;
    /**
     * 繁体标题
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
     * 印尼语标题
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
     * 公告类型，1、足球赛事  2、篮球赛事，3网球赛事，4羽毛球赛事，5乒乓球赛事，6斯诺克赛事,100 活动公告
     */
    private Integer noticeType;

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
     * 发布范围,1:直营商户，2:渠道商户,3:二级商户,4:投注用户,5:panada内部用户,6:所有商户投注用户，7部分商户投注用户
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

    public void transferNoticePerfix(){
        if(StringUtils.isNotBlank(this.getContext())){
            this.setContext(this.getContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getEnContext())){
            this.setEnContext(this.getEnContext().replace(NOTICE_PREFIX,""));
            this.setBiContext(this.getEnContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getZhContext())){
            this.setZhContext(this.getZhContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getJpContext())){
            this.setJpContext(this.getJpContext().replace(NOTICE_PREFIX,""));
        }
      /*  if(StringUtils.isNotBlank(this.getContext())){
            this.setBiContext(this.getEnContext().replace(NOTICE_PREFIX,""));
        }*/
        if(StringUtils.isNotBlank(this.getMaContext())){
            this.setMaContext(this.getMaContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getThContext())){
            this.setThContext(this.getThContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getViContext())){
            this.setViContext(this.getViContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getKoContext())){
            this.setKoContext(this.getKoContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getPtContext())){
            this.setPtContext(this.getPtContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getEsContext())){
            this.setEsContext(this.getEsContext().replace(NOTICE_PREFIX,""));
        }
        if(StringUtils.isNotBlank(this.getMyaContext())){
            this.setMyaContext(this.getMyaContext().replace(NOTICE_PREFIX,""));
        }

    }


}
