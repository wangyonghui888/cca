package com.panda.sport.merchant.common.vo.merchant;

import com.panda.sport.merchant.common.constant.Constant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TicketOrderSettle implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 自动编号
         */
        private Long id;
        private Integer userLevel;

        /**
         * 订单单号
         */
        private String orderNo;

        private List<TicketOrderSettleDetail> orderDetailList;


        /**
         * 用户id
         */
        private String uid;
        /**
         * 商户
         */
        private String merchantCode;
        private Integer transferMode;
        private String merchantName;
        private String remark;

        /**
         * 用户名
         */
        private String userName;

        private String fakeName;

        /**
         * 订单状态(0:待处理，1:已处理，2:取消交易)
         */
        private Integer orderStatus;

        /**
         * 注单项数量
         */
        private Long betCount;

        /**
         * 串关类型(1：单关(默认) 、2：双式投注，例如1/2 、3：三式投注，例如1/2/3  、4：n串1，例如4串1  、5：n串f，例如5串26 )
         */
        private Integer seriesType;

        /**
         * 串关值(单关(默认) 、双式投注，例如1/2 、三式投注，例如1/2/3  、n串1，例如4串1  、n串f，例如5串26 )
         */
        private String seriesValue;

        /**
         * 注单总价
         */
        private BigDecimal productAmountTotal;

        /**
         * 结算金额
         */
        private BigDecimal settleAmount;


        private BigDecimal profitAmount;
        /**
         * 外汇有效注额
         */
        private BigDecimal orderValidBetMoney;
        /**
         * 本地有效注额
         */
        private BigDecimal localOrderValidBetMoney;
        private BigDecimal localProfitAmount;
        private BigDecimal localSettleAmount;

        /**
         * 实际付款金额
         */
        private BigDecimal orderAmountTotal;
        /**
         * 本地注单金额
         */
        private BigDecimal localBetAmount;

        /**
         * 结算时间
         */
        private Long settleTime;
        private Integer settleType;

        private String settleTimeStr;
        /**
         * vip升级时间
         */
        private Long vipUpdateTime;

        private String vipUpdateTimeStr;


        /**
         * 创建时间
         */
        private Long createTime;

        private String createTimeStr;

        private Integer managerCode;


        /**
         * 插入时间
         */

        private Long insertTime;

        /**
         * 修改时间
         */
        private Long updateTime;

        private String ip;
        private String ipArea;
        private String deviceType;
        private String deviceImei;
        private Integer outcome;
        private String currencyCode;
        private String currency;
        private String odds;
        private String acCode;

        @ApiModelProperty(value = "有效投注笔数")
        private Integer sumValidBetNo;

        /**
         * 是否进行过提前结算 1：是 0或者null：否
         */
        private Integer preSettle;

        /**
         * 结算本金金额
         */
        private BigDecimal preBetAmount;

        public void setSettleTime(Long settleTime) {
            this.settleTime = settleTime;
            if (settleTime != null) {
                this.settleTimeStr = DateFormatUtils.format(new Date(settleTime), "yyyy-MM-dd HH:mm:ss");
            }
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
            if (createTime != null) {
                this.createTimeStr = DateFormatUtils.format(new Date(createTime), "yyyy-MM-dd HH:mm:ss");
            }
        }

        /**
         * 本地币种(rmb)
         */
        private String localCurrencyCode = Constant.DEFAULT_CURRENCY_ID;
        private String localCurrency = Constant.DEFAULT_CURRENCY_CODE;

        /**
         * 提前结算顺序
         */
        private Integer preSettleSort;

}
