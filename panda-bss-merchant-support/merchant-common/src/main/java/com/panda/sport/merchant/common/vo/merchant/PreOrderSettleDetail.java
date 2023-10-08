package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PreOrderSettleDetail implements Serializable {
    private static final long serialVersionUID = 2819257632275334581L;

    //表ID
    private  Long  id;

    // 订单编号
    private  String orderNo;

    //注单编号
    private  String   betNo;

    //用户id
    private  String uid;

    //投注类型ID(对应上游的投注项ID),传给风控的
    private  Long playOptionsId;

    //运动种类编号
    private  Integer sportId;

    //运动种类名称
    private  String sportName;

    //玩法ID
    private  Integer playId;

    //赛事编号
    private  Long matchId;

    //赛事名称
    private  String matchName;

    //赛事类型：1 ：早盘赛事 ，2： 滚球盘赛事，3： 冠军盘赛事，5：活动赛事
    private  Integer matchType;

    //下注时间
    private String betTime;

    // 盘口id
    private  Integer marketId;

    //盘口类型(EU:欧盘 HK:香港盘 US:美式盘 ID:印尼盘 MY:马来盘 GB:英式盘）
    private  String  marketType;

    //盘口值
    private String marketValue;

    //对阵信息
    private String  matchInfo;

    //注单金额，指的是下注本金2位小数，投注时x10000
    private BigDecimal betAmount;

    //注单赔率,固定2位小数 【欧洲赔率】
    private Double oddsValue;

    //最终盘口类型(EU:欧盘 HK:香港盘 US:美式盘 ID:印尼盘 MY:马来盘 GB:英式盘）
    private String marketTypeFinally;

    //最终赔率,可能是1/20
    private String oddFinally;

    //投注赔率接受类型(1：自动接收更好的赔率 2：自动接受任何赔率变动 3：不自动接受赔率变动)
    private String acceptBetOdds;

    //最高可赢金额(注单金额*注单赔率)
    private String maxWinAmount;

    //注单状态(0未结算 1已结算 2结算异常 3手动注单取消[不可逆] 4消息注单取消[可逆] 5拒单[PA手动拒单，PA自动拒单，业务拒单，MTS拒单] 6冻结)
    private String betStatus;

    //基准比分(下注时已产生的比分)
    private String scoreBenchmark;

    //投注类型(投注时下注的投注类型 比如1 X 2)，规则引擎用
    private String playOptions;

    //0:未删除，1 已删除
    private String delFlag;

    //备注 （订单为什么无效？)
    private String remark;

    //创建时间
    private String createTime;

    private String beginTime;

    private String beginTimeStr;

    //创建用户
    private String createUser;

    //修改人
    private String modifyUser;

    //修改时间
    private String modifyTime;

    //联赛id
    private String tournamentId;

    //注项结算结果 0-无结果 2-走水 3-输 4-赢 5-赢一半 6-输一半 7-赛事取消 8-赛事延期 11-比赛延迟 12-比赛中断 13-未知 15-比赛放弃 16-异常盘口 17未知赛事状态 18比赛取消 19比赛延期 20SR-其他 21SR-无进球球员 22SR-正确比分丢失 23SR-无法确认的赛果 24SR-格式变更 25SR-进球球员丢失 26SR-主动弃赛 27SR-并列获胜 28SR-中途??赛 29SR-赔率错误 30SR-统计错误 31SR-投手变更
    private String betResult;

    //附加金额(实际付款金额-投注金额)
    private String addition;

    //注单所有可能结算结果json格式
    private String betAllResult;

    //投注项结算比分
    private String settleScore;

    //联赛级别
    private Long tournamentLevel;

    //主副盘标识 0、主盘 1、副盘
    private Integer marketMain;

    //赛前操盘源
    private String preDataSourse;

    //滚球操盘源
    private String liveDataSourse;

    //取消类型 0未取消，1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，10联赛错误，11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16时间错误，17赛事提前，18自定义原因，19数据源取消，20比赛延迟，21操盘手取消，2主动弃赛，23并列获胜，24中途弃赛，25统计错误， 40PA手动拒单，41PA自动拒单，42业务拒单，43MTS拒单，44虚拟自动拒单，45商户扣款失败
    private Integer cancelType;

    //坑位(盘口位置) 1：表示主盘，2：表示第一副盘
    private Integer placeNum;

    //(原始币种)注单金额，单注本金*100
    private BigDecimal originalBetAmount;

    //(原始币种)最高可赢金额(注单金额*注单赔率)
    private BigDecimal originalMaxWinAmount;

   //预投注订单状态(0预约中 ;1预约成功;2.风控预约失败;3.)风控取消预约注单.4.用户手动取消预约投注
    private Integer preOrderDetailStatus;

    private String playOptionName;

    private String playName;

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
        if (StringUtils.isNotEmpty(beginTime)) {
            this.beginTimeStr = DateFormatUtils.format(new Date(Long.valueOf(beginTime)), "yyyy-MM-dd HH:mm:ss");
        }
    }
}
