package com.panda.sport.merchant.common.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @FieldExplain("uid")
    private Long uid;

    @FieldExplain("注单号")
    private String orderNo;

    @FieldExplain("订单状态(0:未结算,1:已结算,2:注单取消,3:确认中,4:投注失败)")
    private String orderStatus;

    @FieldExplain("投注总金额")
    private BigDecimal orderAmountTotal;

    private BigDecimal preBetAmount;

    @FieldExplain("最高可盈总金额")
    private BigDecimal maxWinAmount;

    @FieldExplain("净盈利")
    private BigDecimal profitAmount;

    @FieldExplain("返还金额")
    private BigDecimal backAmount;

    private BigDecimal settledAmount;

    @FieldExplain("负盘附加金额")
    private BigDecimal addition;

    @FieldExplain("串关值")
    private String seriesValue;

    @FieldExplain("盘口类型")
    private String marketType;

    @FieldExplain("串关类型")
    private String seriesType;

    @FieldExplain("串关注单数量")
    private Integer seriesSum;

    @FieldExplain("结算 (1赢，2输，3赢半，4输半，5走盘，6赛事取消，7赛事延期)")
    private Integer outcome;

    @FieldExplain("投注时间")
    private Long betTime;

    private String betTimeStr;

    /**
     * 赛事开始时间
     */
    private Long beginTime;

    /**
     * 赛事开始时间字符串
     */
    private String beginTimeStr;

    private Long settleTime;

    private Long modifyTime;

    /**
     * 修改时间
     */
    private String modifyTimeStr;

    private Integer settleType;

    private Integer managerCode;

    private String langCode;
    /**
     * 活动编码
     **/
    private String acCode;

    private Integer preSettle;

    private Integer preOrder;

    private String id;

    private List<OrderDetailVO> detailList;

    private List<PreOrderVo> preOrderVoList;

    private Integer preOrderStatus;

    private Integer matchType;

}
