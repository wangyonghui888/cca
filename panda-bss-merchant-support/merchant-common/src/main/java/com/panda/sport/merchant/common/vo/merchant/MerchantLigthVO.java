package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author YK
 * @Description:
 * @date 2020/4/17 20:22
 */
@Data
@Accessors(chain = true)
public class MerchantLigthVO {

    /**
     * id
     */
    private Long id;

    /**
     * 1：活动公告  2:赛事取消公告
     */
    private Integer headType;

    /**
     * 标题
     */
    private String title;

    /**
     * 重要标记 0否 1是
     */
    private Integer isTag;

    /**
     * 是否上传附件 0否  1是
     */
    private Integer isUpload;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 发布时间
     */
    private String sendTimeStr;

    /**
     * 1 公告  2 消息
     */
    private Integer type;
}
