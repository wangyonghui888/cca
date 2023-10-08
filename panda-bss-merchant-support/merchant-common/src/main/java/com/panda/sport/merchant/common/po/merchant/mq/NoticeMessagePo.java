package com.panda.sport.merchant.common.po.merchant.mq;

import lombok.Data;

/**
 * @auth: YK
 * @Description:消息推送的结构体
 * @Date:2020/6/24 19:42
 */
@Data
public class NoticeMessagePo {

    /**
     * 公告类型
     */
    private Integer typeId;

    /**
     * 球种
     */
    private Integer sportId;

    /**
     * 基础赛事ID
     */
    private String standardMatchId;

    /**
     * 赛程管理ID
     */
    private String matchMmanageId;

    /**
     * 标题
     */
    private String title;


    /**
     *内容
     */
    private String content;
}
