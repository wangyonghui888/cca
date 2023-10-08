package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class MerchantOrderRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 体育种类（0所有赛种）
     */
    private String sportId;

    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 商户
     */
    private String merchantCode;


    private String sort;

    private String orderBy;

    /**
     * 查询类型(1-天 2-周 3-月 4-年)
     */
    private String dateType;

    private String date;

    private Integer pageSize;

    private Integer pageNum;


    /**
     * 1:投注时间  2：开赛时间  3：结算时间
     */
    private String filter;

    /**
     * 时间
     */
    private Long time;
}