package com.panda.sport.merchant.common.po.merchant;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 公告国际化插入
 */
@Data
public class NoticeLangContextPo implements Serializable {

    /**
     * 国际化类别ID
     */

    @NotNull(message = "语言类型不能为空")
    private Integer langType;

    /**
     * 标题
     */
    @NotEmpty(message = "公告标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotEmpty(message = "正文不能为空")
    private String context;
}
