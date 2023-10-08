package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  TODO
 * @Date: 2020-09-04 15:43
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MerchantLogTypeVo {

    /**
     * 操作类型id
     */
    private Integer id;

    /**
     * 操作名称
     */
    private String name;

    /**
     * 操作名称
     */
    private String nameEn;
}
