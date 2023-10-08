package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import com.panda.sport.merchant.common.base.BasePO;
import lombok.Data;
import lombok.ToString;

/**
 * @author :  arlo
 * @Project Name : panda-bss-order
 * @Package Name : com.panda.sports.bss.order.po
 * @Description : 注单详情PO
 * @Date: 2019-09-20 22:30
 **/
@Data
@ToString
@TableName(value = "t_pre_order_detail")
public class TPreOrderDetailPO extends BasePO<TPreOrderDetailPO> {

    @TableId(value = "ID",type = IdType.AUTO)
    @FieldExplain("id")
    private Long id;
    @TableField("bet_no")
    @FieldExplain("注单编号")
    private String betNo;
    @TableField("order_no")
    @FieldExplain("订单编号")
    private String orderNo;
    @TableField("odd_finally")
    @FieldExplain("提前结算赔率")
    private String oddFinally;
    @TableField("bet_status")
    @FieldExplain("注单状态(0未结算 1已结算 2结算异常 3手动注单取消[不可逆] 4消息注单取消[可逆] 5拒单[PA手动拒单，PA自动拒单，业务拒单，MTS拒单，商户扣款失败拒单]))")
    private Integer betStatus;
    @TableField("remark")
    @FieldExplain("备注")
    private String remark;
    @TableField("del_flag")
    @FieldExplain("逻辑删除标识 0:未删除，1已删除")
    private Integer delFlag;
    @TableField("bet_result")
    @FieldExplain("注项结算结果0-无结果  2-走水  3-输 4-赢 4-赢一半 5-输一半")
    private Integer betResult;
    @TableField("pre_settle_score")
    @FieldExplain("投注项提前结算比分")
    private String preSettleScore;
    @TableField("cancel_type")
    @FieldExplain("取消类型 0未取消，1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，10联赛错误，11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16时间错误，17赛事提前，18自定义原因，19数据源取消，20PA手动拒单，21PA自动拒单，22业务拒单，23MTS拒单")
    private Integer cancelType;
    @TableField("cancel_type")
    @FieldExplain("取消时间")
    private Long cancelTime;
    @TableField("risk_event")
    @FieldExplain("风控事件")
    private String riskEvent;
    @FieldExplain("提前结算订单号")
    private String preOrderNo;
}
