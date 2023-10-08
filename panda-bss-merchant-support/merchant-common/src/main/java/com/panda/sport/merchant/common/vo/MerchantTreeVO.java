package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:16:47
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantTreeVO implements Serializable {

    private static final long serialVersionUID = 441782737269944104L;

    @FieldExplain("商户名称")
    private String merchantName;
    @FieldExplain("商户编码")
    private String merchantCode;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;
    /**
     * 子节点集合
     */
    private List<MerchantTreeVO> subList;

    /**
     * 是否信网商户
     */
    private Integer merchantTag;
}
