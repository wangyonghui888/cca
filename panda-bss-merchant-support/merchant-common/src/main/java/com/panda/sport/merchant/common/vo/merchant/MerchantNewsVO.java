package com.panda.sport.merchant.common.vo.merchant;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author YK
 * @Description:
 * @date 2020/4/17 15:58
 */
@Data
@Accessors(chain = true)
public class MerchantNewsVO {

    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 正文
     */
    private String context;

    /**
     * 上传的附件
     */
    private String upload;

    /**
     * 如果是商户登录则默认是0，如果是panda运营为真实uid
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uid;

    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 是否以读，0未读  1以读
     */
    private String isRead;

    /**
     * 是否是商户账号 ，0不是  1是
     */
    private Integer isMerchant;

    /**
     * 发送时间
     */
    private Long sendTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 浏览次数
     */
    private Long  visitNumber;


    /**
     * 用户消息，1，商户key是否到期消息  2，商户新注册的消息
     */
    private Integer type;


    /**
     * 输出用
     * 对应的消息的包含不同的结构体字段
     */
    private Object children;
}
