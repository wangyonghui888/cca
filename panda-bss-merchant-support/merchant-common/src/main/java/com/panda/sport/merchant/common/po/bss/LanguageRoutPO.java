package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @author :  arlo
 * @Project Name : panda-bss-job
 * @Package Name : com.panda.sports.bss.po
 * @Description : 路由指向PO
 * @Date: 2019-10-02 15:47
 */
@Data
public class LanguageRoutPO extends BaseVO {

    /** 自动编号 */
    private Long id;

    /** 语言类型 */
    private String languageType;

    /** 对应表 */
    private String mappingTable;

    /** 默认语言 1默认 */
    private Integer isDefault;


}
