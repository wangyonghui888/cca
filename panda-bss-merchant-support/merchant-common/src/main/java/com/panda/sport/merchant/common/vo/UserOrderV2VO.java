package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrderV2VO implements Serializable {
    /**
     *  1:投注 2开赛 3结算
     */
    private String filter;
    /**
     * 投注开始时间(yyyyMMdd HH:mm:ss)
     */
    private String startTime;
    /**
     * 投注结束时间(yyyyMMdd HH:mm:ss)
     */
    private String endTime;
    /**
     * 注单类型
     * 1赛前 2滚球盘 3冠军盘 5活动
     */
    private Integer matchType;
    /**
     * 注单状态
     */
    private List<Integer> orderStatusList;
    /**
     * 赛事ID
     */
    private String matchId;
    /**
     * 注单号
     */
    private String orderNo;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户id （集合）
     */
    private List<Long> userIdList;
    /**
     * 串关类型 1单关  2串关
     */
    private Integer seriesType;
    /**
     * 运动种类ID
     */
    private Integer sportId;
    /**
     * 商户CODE
     */
    private List<String> merchantCodeList;

    private String merchantCode;
    private Long merchantId;

    private Integer transferMode;

    private Integer merchantTag;

    /**
     * 盘口
     */
    private String marketType;


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
     * 注单号
     */
    private String betNo;


    private List<String> orderNoList;

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

    private String sortBy;

    private String sqlOrder;


    private String oddsDataSource;

    /**
     * 订单状态(0:未结算,1:已结算,2:注单取消,3:确认中,4:投注失败)
     */
    private Integer orderStatus;


    /**
     * 结算结果（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期）
     */
    private List<Integer> outComeList;


    private String language;

    private Long tournamentId;


    /**
     * 注单币种，1:人民币 2:美元 3:欧元 4:新元
     */
    private String currency;

    /**
     * 账号tag 1为账变负数
     */
    private Integer accountTag;


    /**
     * 仅作为查询条件使用
     * 是否开启提前结算
     */
    private Boolean enablePreSettle;


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
     * 开始时间
     */
    private Long startTimeL;
    /**
     * 结束时间
     */
    private Long endTimeL;
    /**
     *  tranceId
     */
    private String traceId;
    /**
     *  1:order 2 :admin
     */
    private Integer serviceType;
    /**
     *  商户ID（集合）
     */
    private List<Long> merchantIdList;
    private Long betStartTimeL;

}


