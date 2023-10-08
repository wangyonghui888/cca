package com.panda.sport.merchant.common.po.merchant.mq;

import lombok.Data;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/6/28 15:31
 */
@Data
public class MarketOptionsResultPO {

    /**
     * 投注项ID
     */
    private Long optionsId;

    /**
     * 投注项赛果 0-无结果  2-走水  3-输 4-赢 5-赢一半 6-输一半
     */
    private Integer result;
}
