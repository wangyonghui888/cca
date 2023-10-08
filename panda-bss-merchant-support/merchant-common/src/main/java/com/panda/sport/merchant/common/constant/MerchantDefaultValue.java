package com.panda.sport.merchant.common.constant;

/**
 * @author :  ives
 * @Description :  商户相关默认值类
 * @Date: 2022-01-24 17:59
 */
public class MerchantDefaultValue {

    /**
     * 对外商户查询条件设置
     */
    public static class MerchantQueryConditionSetting{
        /**
         * 默认时间类型|1 投注时间|2 开赛时间 |3 结算时间，默认为2 开赛时间
         */
        public static final int defaultTimeType = 2;

        /**
         * 默认是否勾选自然日|0 不勾选 |1 勾选，默认为1 勾选
         */
        public static final int isNatureDay = 1;
    }
}
