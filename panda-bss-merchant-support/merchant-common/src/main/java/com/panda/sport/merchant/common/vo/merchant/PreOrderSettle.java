package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.constant.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PreOrderSettle implements Serializable {
    private static final long serialVersionUID = 2819257632275334581L;

    //表ID
    private Long id;

    //原始订单号
    private String orderNo;

    //用户id
    private String uid;

    //用户名
    private String userName;

    //用户别名
    private String fakeName;

    //提前结算订单号
    private String preOrderNo;

    //订单状态(0:待处理,1:已处理,2:取消交易【自动，手动】,3:待确认,4:已拒绝【风控】)
    private Integer orderStatus;

    //结算状态(0:未结算 1:已结算 2:结算异常)
    private Integer billStatus;

    //注单项数量
    private Long productCount;

    //串关类型(1：单关(默认) 、N00F：串关投注)
    private Integer seriesType;

    //串关值(单关(默认) 、N串F：串关投注)
    private String seriesValue;

    /**
     * 本地币种(rmb)
     */
    private String localCurrencyCode = Constant.DEFAULT_CURRENCY_ID;
    private String localCurrency = Constant.DEFAULT_CURRENCY_CODE;

    //注单总价
    private BigDecimal productAmountTotal;

    //实际付款金额 orderAmountTotal 注额(本地)
    private BigDecimal localBetAmount;

    //1:H5，2：PC,3:Android,4:IOS
    private Integer deviceType;

    //ip地址
    private String ip ;


    //备注 （订单为什么无效？)
    private String remark;

    //商户id
    private Long merchantId;

    //0:未删除，1已删除（活动使用描述：2-统计中 3-统计完成 4-结算统计完成）
    private Integer delFlag;

    //创建时间
    private String  createTime;

    private String createTimeStr;
    //创建人
    private String  createUser;

    //修改人
    private String  modifyUser;

    //修改时间
    private String modifyTime;

     //币种编码
    private String currencyCode;

    //ip区域
    private String ipArea;

    //设备imei码，只有手机有，没有不添加
    private String  deviceImei;

     //最高可赢金额(注单金额*
    private BigDecimal maxWinAmount;

    //定单确认时间
    private String confirmTime;

    //是否满额投注 1：是，0：否
    private Integer fullBet;

    //预计盈利金额
    private BigDecimal expectProfit;

    //用户投注该单时的级别
    private Integer userLevel;

    //VIP等级： 0 非VIP，1 VIP用户
    private Integer vipLevel;

    //语言编码 对应 t_language 表中的name_code
    private String langCode;

    //(原始币种)最高可赢金额(注单金额*注单赔率)
    private BigDecimal originalMaxWinAmount;

    //(原始币种)实际付款金额 originalOrderAmountTotal 注额(外汇)
    private BigDecimal orderAmountTotal;

    //(原始币种)注单总价
    private BigDecimal originalProductAmountTotal;

    //(人名币兑换投注时币种)汇率
    private BigDecimal rmbExchangeRate;

    //活动编码 为空则非活动订单
   private Integer acCode;

    //订单来源 ty：从体育跳转；zr：从真?跳转；qp：从棋牌跳转；dy：从电游（捕?）跳转；lhj：从电游（??机）；cp：从彩票跳转；dj：从电竞跳转表示C端?持跳转的游戏
    private String jumpFrom;

   // 预投注订单状态(0预约中 ;1预约成功;2.风控预约失败;3.)风控取消预约注单.4.用户手动取消预约投注
   private Integer preOrderStatus;

   private String  preOrderStatusName;

    private List<PreOrderSettleDetail> preOrderSettleDetail;

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
        if (StringUtils.isNotEmpty(createTime)) {
            this.createTimeStr = DateFormatUtils.format(new Date(Long.valueOf(createTime)), "yyyy-MM-dd HH:mm:ss");
        }
    }
}
