package com.oubao.vo;

/**
 * @author :  hooli
 * @version v1.0.0
 * @Project Name : panda-bss
 * @Package Name :
 * @Description :  定义业务逻辑系统，业务接口code
 * 业务编码code定义生成规则：BSS+模块编码+业务操作编码
 * <p>
 * 模块名称	         模块编码区间（50000）	业务操作编码区间（100）
 * 赛程基础服务   	50000	             100区间
 * 订单服务    	    50100	             200区间
 * 结算服务           50300	             300区间
 * 派彩服务            50400                400区间
 * 用户中心服务         50500                500区间
 * @Since: 2019-08-16 15:08
 */
public class BizCode {

    /**************************************
     * 1.业务逻辑-用户中心基础服务模块业务code码定义 *
     *************************************/

    /**
     * 业务逻辑-用户登录-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_USER_LONG = 50501;
    /**
     * 业务逻辑-添加用户收藏记录-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_USER_ADD_RECORD = 50502;
    /**
     * 业务逻辑-删除用户收藏记录-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_USER_DELETE_RECORD = 50503;
    /**
     * 业务逻辑-用户开户-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_USER_ACCOUNT = 50504;



    /**************************************
     * 2.业务逻辑-赛程基础服务模块业务code码定义 *
     *************************************/

    /**
     * 业务逻辑-加载栏目数据
     */
    public static final int BSS__SCHEDULE_QUERY_MENU_INFO = 50104;

    /**
     * 业务逻辑-加载栏目下赛事数据
     */
    public static final int BSS__SCHEDULE_QUERY_MENUMATCH_INFO = 50105;

    /**
     * 业务逻辑-实时接口-盘口投注项实时变更-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_MARKET_FIELD = 50106;

    /**
     * 业务逻辑-实时接口-赛事数据变化-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_MATCH_DATA = 50107;

    /**
     * 业务逻辑-实时接口-盘口投注项实时变更数据-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_MARKET_DATA = 50108;

    /**
     * 业务逻辑-实时接口-盘中事件变更数据-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_MARKET_EVENT_DATA = 50109;

    /**
     * 业务逻辑-实时接口-盘口赛事赛果变更数据-kiu
     */
    public static final int BSS_SCHEDULE_QUERY_MARKET_RESULT_DATA = 50110;


    /**
     * 统计所有体育玩法总数-kiu
     **/
    public static final int BSS_SCHEDULE_QUERY_MATCH_COUNT = 50111;
    /**
     *
     *  统计比赛体育玩法总数-kiu
     **/
    public static final int BSS_SCHEDULE_QUERY_MATCH_COUNT_ID = 50112;
    /**
     * 查询比赛玩法类型接口-kiu
     **/
    public static final int BSS_SCHEDULE_QUERY_MATCH_TYPE = 50113;
    /**
     * 根据体育类型查询联赛级别以及赛事状态-kiu
     **/
    public static final int BSS_SCHEDULE_QUERY_MATCH_STATUS = 50114;
    /**
     * 查询比赛球队信息-kiu
     **/
    public static final int BSS_SCHEDULE_QUERY_MATCH_TEAM = 50115;

    /**************************************
     * 3.业务逻辑-订单基础服务模块业务code码定义 *
     *************************************/

    /**************************************
     * 4.业务逻辑-结算基础服务模块业务code码定义 *
     *************************************/

    /**************************************
     * 5.业务逻辑-派彩基础服务模块业务code码定义 *
     *************************************/

}
