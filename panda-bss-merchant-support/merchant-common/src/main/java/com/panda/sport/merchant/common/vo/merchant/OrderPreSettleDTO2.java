package com.panda.sport.merchant.common.vo.merchant;

import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Data
public class OrderPreSettleDTO2 {

    @FieldExplain("原始订单号")
    private String orderNo;

    @FieldExplain("提前结算订单号")
    private String preOrderNo;

    @FieldExplain("结算（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期)")
    private Integer outCome;

    @FieldExplain("结算本金金额")
    private String betAmount;

    @FieldExplain("结算本金金额(本地)")
    private String localBetAmount;

    @FieldExplain("输赢金额")
    private String profitAmount;

    @FieldExplain("输赢金额(本地)")
    private String localProfitAmount;

    @FieldExplain("派彩金额")
    private String settleAmount;

    @FieldExplain("派彩金额(本地)")
    private String localSettleAmount;

    @FieldExplain("结算类型(0:提前部分结算,1:提前全部结算,2:结算取消,3:剩余赛果结算)")
    private Integer settleType;

    @FieldExplain("取消原因")
    private String cancelReason;

    @FieldExplain("结算比分")
    private String settleScore;

    @FieldExplain("最终赔率")
    private String oddFinally;

    @FieldExplain("结算时间:时间戳")
    private Long settleTime;

    @FieldExplain("确认时间:时间戳")
    private Long confirmTime;

    @FieldExplain("等待时间:单位s")
    private Long waitTime;

    @FieldExplain("盘口类型")
    private String marketType;

    @FieldExplain("备注 （订单为什么无效？)")
    private String remark;

    @FieldExplain("订单状态(0:待处理,1:已处理,2:取消交易【自动，手动】,3:待确认,4:已拒绝【风控】)")
    private String orderStatus;

    @FieldExplain("币种")
    private String currency;

    @FieldExplain("ip地址")
    private String ip;

    @FieldExplain("设备")
    private Long deviceType;

    @FieldExplain("设备imei")
    private String deviceImei;

    @FieldExplain("地址")
    private String ipArea;

    @FieldExplain("取消类型 0未取消，1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，10联赛错误，11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16时间错误，17赛事提前，18自定义原因，19数据源取消，20PA手动拒单，21PA自动拒单，22业务拒单，23MTS拒单")
    private Integer cancelType;

    private String confirmTimeStr;

    public void setConfirmTime(Long confirmTime) {
        this.confirmTime = confirmTime;
        if (confirmTime != null) {
            this.confirmTimeStr = DateFormatUtils.format(new Date(confirmTime), "yyyy-MM-dd HH:mm:ss");
        }
    }

    private String settleTimeStr;

    public void setSettleTime(Long settleTime) {
        this.settleTime = settleTime;
        if (settleTime != null) {
            this.settleTimeStr = DateFormatUtils.format(new Date(settleTime), "yyyy-MM-dd HH:mm:ss");
        }
    }
    @FieldExplain("滚球：0-滚球，1-早盘'")
    private Integer matchType;

}
