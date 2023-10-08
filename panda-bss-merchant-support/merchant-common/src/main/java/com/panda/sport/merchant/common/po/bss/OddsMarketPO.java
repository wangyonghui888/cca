package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author :  christion
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.schedule.po
 * @Description :  交易项赔率记录
 * @Date: 2019-09-07 11:05
 */
@Data
public class OddsMarketPO extends BaseVO {
    /**
     * 交易项id
     */
    private Long marketId ;
    /**
     * 上一次赔率值
     */
    private String oldValue ;
    /**
     * 最新赔率值
     */
    private String newValue ;

    /**
     * 创建时间
     */
    private Date createTime ;

    /**
     * 创建时间
     */
    private Date modifyTime ;

    /**
     * 赔率项map字符串
     */
    private Map<String ,String> marketMapValue;

    /**
     * 统计字段
     */
    private int count ;
}
