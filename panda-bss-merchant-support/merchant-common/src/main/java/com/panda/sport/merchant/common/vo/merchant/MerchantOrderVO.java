package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 体育种类（0所有赛种）
     */
    private Integer sportId;

    /**
     * 商户名称
     */
    private String merchantName;
    private Integer agentLevel;
    private List<Integer> agentLevelList;
    /**
     * 商户
     */
    private String merchantCode;
    private List<String> merchantCodeList;
    private String currency;


    private String sort;

    private String orderBy;

    /**
     * 查询类型(1-天 2-周 3-月 4-年)
     */
    private String dateType;

    private String startDate;

    private String endDate;

    private String startTime;

    private String endTime;

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

    private String createTime;
    /**
     * 日期类型 UTC8,EZ
     */
    private String timeZone;
    private String language;

}
