package com.panda.sport.merchant.common.vo;


import lombok.Data;

/**
 * @author :  valar
 * @Project Name :panda-bss
 * @Package Name :com.panda.sports.bss.spi.usercenter.vo
 * @Description : 用户vo
 * @Date: 2019-10-10 21:29
 */
@Data
public class OrderOverLimitVO {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 注单号
     */
    private String orderNo;
    /**
     * 超限标注
     */
    private String remark;
}
