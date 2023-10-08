package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author YK
 * @Description:我的消息
 * @date 2020/3/12 16:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantNews implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
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
     * panda总商户是否以读，0未读  1以读
     */
    private String isRead;

    /**
     *  商户自己是否已读，0未读 1已读
     */
    private String selfIsRead;

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

}
