package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @author :  arlo
 * @Project Name :  panda-bss-job
 * @Package Name :  com.panda.sports.bss.po
 * @Description :  语言表PO
 * @Date: 2019-09-27 17:23
 */
@Data
public class I18nItemPO {

    /**
     * 当前语言编码
     */
    private Long nameCode;

    /**
     * 语言类型. zh jp en 等
     */
    private String languageType;

    /**
     * 语言内容
     */
    private String text;

    /**
     * 备注
     */
    private String remark;


}
