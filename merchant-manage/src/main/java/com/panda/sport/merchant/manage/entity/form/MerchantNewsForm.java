package com.panda.sport.merchant.manage.entity.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @author YK
 * @Description: 消息发送的插入
 * @date 2020/3/14 11:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantNewsForm {

    @NotEmpty(message = "消息标题不能为空")
    private String title;

    @NotEmpty(message = "消息内容不能为空")
    private String context;

    private Long uid;

    private String merchantCode;

    private String merchantName;

    private String isMerchant;
}
