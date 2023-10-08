package com.panda.center.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Collections;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantTree {

    private String id;

    private String merchantCode;

    private String merchantName;

    private Integer agentLevel;

    private String parentId;

    List<MerchantTree> trees = Collections.emptyList();
}
