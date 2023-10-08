package com.panda.sport.merchant.common.po.bss;


import lombok.Data;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: 盘口表对象
 * @Date: 2019/10/24 17:20
 * @Version: 1.0
 */
@Data
@ToString
public class MarketPO {


    private Long id;

    private Long matchInfoId;

    private Long playId;

    private Integer marketType;

    private String marketValue;


    private String marketName;


    private String orderType;


    private String addition1;

    private String addition2;


    private String addition3;


    private String addition4;


    private String addition5;


    private String dataSourceCode;


    private Integer status;


    private Integer matchProcessId;


    private String thirdMarketSourceId;


    private String remark;


    private Long modifyTime;

}
