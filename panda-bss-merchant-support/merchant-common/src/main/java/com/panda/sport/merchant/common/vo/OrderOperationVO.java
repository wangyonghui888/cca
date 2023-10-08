package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.bss.common.entity
 * @Description: 订单操作
 * @Date: 2020/6/24 16:41
 * @Version: v1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderOperationVO {

    @FieldExplain("订单号，多个使用，隔开，可以是单关或者串关的定单号")
    //@NotBlank(message = "定单号不能为空！")
    private String orderNos;
    @FieldExplain("注单号，多个使用，隔开，可以是单关或者串关的注单号")
    //@NotBlank(message = "注单号不能为空！")
    private String betNos;
    @FieldExplain("操作用户id")
    @NotBlank(message = "用户id不能为空！")
    private String userId;
    @FieldExplain("操作类型，1：订单结算，5：融合注单取消回滚消息，6：融合注单取消消息，11：注单结算回滚消息，9：风控订单异常，取消订单")
    @NotNull(message = "操作类型不能为空！")
    private Integer operateType;
    @FieldExplain("操作描述,如果是数字则是对应的取消类型，数字非规定的取消类型标识，则是自定义原因，1比赛取消，2比赛延期，" +
            "3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，10联赛错误，11比分错误，12电视裁判，" +
            "13主客场错误，14赛制错误，15赛程错误，16时间错误，17赛事提前，18自定义原因，19数据源取消，20比赛延迟，21操盘手取消，40PA手动拒单，41PA自动拒单，" +
            "42业务拒单，43MTS拒单，44虚拟自动拒单，45商户扣款失败")
    private String remark;

    @FieldExplain("操作人")
    private String operateUser;

    @FieldExplain("操作消息id")
    private String msgId;


}
