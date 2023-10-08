package com.panda.center.entity;


import com.panda.center.vo.BaseVO;
import lombok.Data;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.order.po
 * @Description: TODO
 * @Date: 2019/10/15 13:26
 * @Version: 1.0
 */
@Data
public class OrderPO extends BaseVO {

    private Long id;

    private String orderNo;

    private Long uid;

    private Integer orderStatus;

    private Integer billStatus;

    private Integer productCount;

    private Integer seriesType;

    private Double productAmountTotal;

    private Double orderAmountTotal;

    private Integer deviceType;

    private String ip = "127.0.0.1";

    private String remark;

    private Long tenantId;


    private String currencyCode;

    private String ipArea;

    private String seriesValue;

    private String deviceImei;

    private Long maxWinAmount;

    private Integer managerCode;
}
