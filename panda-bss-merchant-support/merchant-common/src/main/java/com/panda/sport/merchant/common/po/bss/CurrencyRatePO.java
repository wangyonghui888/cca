package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @author :  valar
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.order.po
 * @Description :  币种汇率表
 * @Creation Date:  2019-10-09 10:28
 * --------  ---------  --------------------------
 */
@Data
public class CurrencyRatePO extends BaseVO {
    /**
     * 序列号
     */
    private static final long serialVersionUID = -8429533972818011019L;
    /**
     * 表ID，自增
     */
    private Long id;
    /**
     * 国家名称【英文】
     */
    private String countryZh;

    /**
     * 国家名称【中文】
     */
    private String countryCn;
    /**
     * 汇率值,固定4位小数(以美金为基准)
     */
    private Double rate;

    /**
     * 币种编码
     */
    private String currencyCode;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 修改人
     */
    private String modifyUser;

}