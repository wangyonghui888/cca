package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

/**
 * 添加公告语言
 */
@Data
public class NoticeLangVo {

    /**
     * 类型ID
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String type;


    /**
     * 英文
     */
    private String enType;
}
