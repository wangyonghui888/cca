package com.panda.sport.merchant.common.constant;

/**
 * @author :  ives
 * @Description :  常用默认值类
 * @Date: 2022-01-24 17:59
 */
public class CommonDefaultValue {

    /**
     * 常用数字
     */
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int TEN = 10;
    public static final int FIFTEEN = 15;
    public static final int SIXTEEN = 16;
    public static final int TWENTY = 20;
    public static final int THIRTY_SIX = 36;
    public static final int FIFTY = 50;
    public static final int ONE_HUNDRED = 100;
    public static final int ONE_THOUSAND = 1000;

    /**
     * 结果状态
     */
    public static class DifferentiateStatus{
        /**
         * 0：否
         */
        public static final int NO = 0;

        /**
         * 1：是
         */
        public static final int YES = 1;
    }

    /**
     * 结果状态
     */
    public static class ResultStatus{
        /**
         * 0：错误
         */
        public static final int ERROR = 0;

        /**
         * 1：正确
         */
        public static final int CORRECT = 1;
    }

    /**
     * 日期类型
     */
    public static class DateType{
        /**
         * 1：账务日（默认）
         */
        public static final int EZ = 1;

        /**
         * 2：自然日
         */
        public static final int UTC8 = 2;
    }

    /**
     * 默认最大最小串关数设置
     */
    public static class SeriesNumSetting{
        /**
         * 最小串关数,默认值为2
         */
        public static final int MIN = 2;

        /**
         * 最大串关数,默认值为10
         */
        public static final int MAX = 10;
    }

    /**
     * 商户类型/代理级别(0,直营;1:渠道;2:二级代理;10:代理商) 代理商跟直营平级，代理商下面有渠道，渠道下面有二级代理
     */
    public static class AgentLevel{
        /**
         * 0,直营
         */
        public static final int DIRECT_SALES = 0;

        /**
         * 1:渠道
         */
        public static final int CHANNEL = 1;

        /**
         * 2:二级代理
         */
        public static final int SECONDARY_AGENT = 2;
        /**
         * 10:代理商
         */
        public static final int AGENCY = 10;
    }

}
