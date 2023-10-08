package com.panda.sport.merchant.common.po.bss;


import lombok.Data;

import java.util.Date;


/**
 * 赛种操盘手排班
 * @author javier
 */
@Data
public class SportDutyTraderPO {

    private Long id;

    private Integer shift;

    private Long sportId;

    private String userCode;

    private Integer userId;

    private Date createTime;

    private Date updateTime;
}
