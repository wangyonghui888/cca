package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-bss-report
 * @Package Name :  com.panda.sports.report.common.vo
 * @Description :  TODO
 * @Date: 2022-06-13 15:48:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbnormalVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private String startDate;

    private Long endTime;

    private String language;

    private String merchantCode;

    private List<String> merchantCodes;

    private Integer pageNum;

    private Integer pageSize;

    private String endDate;

    private Long startTime ;

    private String userName;

    private String userId;

    private Integer userType;

    /**
     * 异常用户名单点击时间
     */
    private Long abnormalClickTime;

    /**
     * 异常用户名是否点击 1是0否
     */
    private Integer isClick;

}
