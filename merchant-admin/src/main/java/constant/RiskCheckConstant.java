package constant;

/**
 *  风险用户协查变量
 * @author amos  2022/4/24
 */
public class RiskCheckConstant {

    /**
     * 未在我方场馆投注用户
     */
    public static final String NOT_EXIST_USER  = "1" ;

    /**
     * 投注总笔数不足20笔
     */
    public static final String TOTAL_BET_NO_LACK  = "2" ;

    /**
     * 投注金额小于等于1000
     */
    public static final String TOTAL_BET_AMOUNT_LACK_ONE_THOUSANDS  = "3" ;

    /**
     * 距上次查询不足3天
     */
    public static final String NOT_REACH_THREE_DAYS  = "4" ;



}
