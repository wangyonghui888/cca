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
public class MerchantPagesTree {

    /**
     * 树id
     */
    private Integer id;

    /**
     * 父节点id
     */
    private Integer pid;

    /**
     * 页面编码
     */
    private String code;

    /**
     * 页面名称
     */
    private String name;

    private String en;

    /**
     * 子节点
     */
    List<MerchantPagesTree> nodes;
}
