package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  TODO
 * @Date: 2020-09-18 11:02
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MerchantTree {

    private String id;

    private String merchantCode;

    private String merchantName;

    private Integer agentLevel;

    private String parentId;

    List<MerchantTree> trees;
}
