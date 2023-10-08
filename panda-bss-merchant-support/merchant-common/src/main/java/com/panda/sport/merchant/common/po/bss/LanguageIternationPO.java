package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @author :  christion
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.schedule.po
 * @Description :  TODO
 * @Date: 2019-09-20 19:49
 */
@Data
public class LanguageIternationPO {
    /**
     * 语言编码
     */
    private  Long nameCode;
    /**
     * 多语言类型
     */
    private  String  languageType;
    /**
     * 显示文本
     */
    private  String text;
    /**
     * 是否有效
     */
    private  Integer active;
    /**
     * 备注
     */
    private  String  remark;
    public LanguageIternationPO() {

    }

    public LanguageIternationPO(Long nameCode , String languageType){
        this.nameCode = nameCode;
        this.languageType = languageType;
    }



}
