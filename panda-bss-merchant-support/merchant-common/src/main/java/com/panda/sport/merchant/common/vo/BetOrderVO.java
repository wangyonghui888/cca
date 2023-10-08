package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Project Name :panda-bss
 * @Package Name :com.panda.sports.bss.spi.usercenter.vo
 * @Description : 用户vo
 * @Date: 2019-10-10 21:29
 */
@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BetOrderVO implements Serializable {

    private static final long serialVersionUID = 2819257632275334581L;

    private String merchantName;

    private String merchantCode;
    private Long merchantId;

    private Integer transferMode;

    private Integer merchantTag;

    private List<String> merchantCodeList;

    private List<Long> merchantIdList;
    /**
     * 盘口
     */
    private String marketType;
    /**
     * 运动种类ID
     */
    private Integer sportId;

    private Integer playId;

    private List<Integer> playIdList;
    /**
     * 体育类型
     */
    private String sportName;
    /**
     * 时间类型(1:今天 2:昨日 3:七日内 4:一月内)
     */
    private Integer timeType;
    /**
     * 投注开始时间
     */
    private Long startTimeL;
    private Long betStartTimeL;
    /**
     * 投注开始时间(yyyyMMdd HH:mm:ss)
     */
    private String startTime;

    /**
     * 投注结束时间(yyyyMMdd HH:mm:ss)
     */
    private String endTime;

    /**
     * 投注结束时间
     */
    private Long endTimeL;

    /**
     * 投注最小金额
     */
    private BigDecimal minBetAmount;

    /**
     * 投注最大金额
     */
    private BigDecimal maxBetAmount;
    /**
     * 投注最小金额
     */
    private BigDecimal minProfit;

    /**
     * 投注最大金额
     */
    private BigDecimal maxProfit;
    /**
     * 投注最大金额
     */
    private BigDecimal minOdds;
    /**
     * 投注最大金额
     */
    private BigDecimal maxOdds;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;
    private String fakeName;

    /**
     * 赛事ID
     */
    private Long matchId;

    /**
     * 注单号
     */
    private String betNo;
    /**
     * 注单号
     */
    private String orderNo;

    private List<String> orderNoList;
    /**
     * 注单类型
     */
    private Integer matchType;
    /**
     * 结算状态
     */
    private Integer settleStatus;

    /**
     * 结算类型(0:手工结算,1:自动结算,2:结算回滚,3:结算回滚之后再次结算（格式：3X，比如：31，再次结算第1次）)
     */
    private Integer settleType;

    private Integer settleTimes;
    /*
    二此结算取消(1)
     */
    private Integer settleCancle;

    /**
     * 页码
     */
    private Integer pageNum;
    private Integer pageNo;

    /**
     * 页面大小
     */
    private Integer pageSize;
    private Integer size;

    /**
     * 开始
     */
    private Integer start;

    /**
     * 结束
     */
    private Integer end;
    private String sortby;

    private String sqlOrder;
    /**
     * 串关类型 0：串关 其他表示单关
     */
    private Integer seriesType;

    private Integer managerCode;

    private String oddsDataSource;

    /**
     * 订单状态(0:未结算,1:已结算,2:注单取消,3:确认中,4:投注失败)
     */
    private Integer orderStatus;


    private List<Integer> orderStatusList;
    /**
     * 结算结果（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期）
     */
    private List<Integer> outComeList;


    private String filter;
    private String language;

    private Long tournamentId;

    /**
     * 不传，查所有。。0 普通用户， 1 VIP 用户
     **/
    private Integer userVip;

    /**
     * 注单币种，1:人民币 2:美元 3:欧元 4:新元
     */
    private String currency;

    /**
     * 账号tag 1为账变负数
     */
    private Integer accountTag;

    private List<Long> userIdList;

    /**
     * 仅作为查询条件使用
     * 是否开启提前结算
     */
    private Boolean enablePreSettle;


    /**
     * 查询t_xxx表
     */
    private String tXXXOrder;

    /**
     * 查询t_xxx_detail表
     */
    private String tXXXDetail;

    /**
     * 查询t_xxx_settle表
     */
    private String tXXXSettle;

    /**
     * 查询t_xxx_settle表
     */
    private String tXXXInternational;

    /**
     * 是否查询8天前的数据
     */
    private Boolean beforeEightDayData;

    private String databaseSwitch;

    private Integer exportType;

    /**
     * IP
     */
    private String ip;

    /**
     * 来自预约投注标识
     */
    private Integer fromAppointment;

    /**
     * 预约投注状态
     */
    private Integer preOrderStatus;

    /**
     * 设备信息
     */
    private Integer deviceType;

    private String searchAfterId;
    private Long  timeId;
    public static String toJson(BetOrderVO userVO) {
        Gson gson = new Gson();
        return gson.toJson(userVO);
    }
}
