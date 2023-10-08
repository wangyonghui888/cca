package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 异常用户查询vo
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbnormalUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户ID
    private String user;

    //用于调用风控接口
    private String userId;

    //用于调用风控接口
    private String appId;

    //用户名
    private String userName;

    //商户id
    private String merchantCode;

    private List<String> merchantCodes;

    //商户
    private String merchantName;

    //日期
    private String startDate;

    private Long startTime ;

    private String endDate;

    private Long endTime;

    private String language;

    private Integer pageNum;

    private Integer pageSize;

    /**
     *查询类型 0 前端显示；1导出报表
     * */
    private Integer category;

}
