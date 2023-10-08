package com.panda.sport.merchant.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;

import java.io.Serializable;

@Data
public class InfomationOf2TimesOrderDTO implements Serializable {

    @ColumnWidth(20)
    @FieldExplain("用户ID")
    @ExcelProperty(value = "用户ID", index = 0)
    private String uid;

    @ColumnWidth(20)
    @FieldExplain("用户名")
    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    @ColumnWidth(20)
    @FieldExplain("商户名")
    @ExcelProperty(value = "商户名", index = 2)
    private String merchantCode;

    @ColumnWidth(20)
    @FieldExplain("商户名")
    @ExcelProperty(value = "商户名", index = 3)
    private String merchantName;

    @ColumnWidth(20)
    @FieldExplain("订单号")
    @ExcelProperty(value = "订单号", index = 4)
    private String orderNo;

    @ColumnWidth(20)
    @FieldExplain("负余额")
    @ExcelProperty(value = "负余额", index = 5)
    private String diffNegativeAmount;

    @ColumnWidth(20)
    @FieldExplain("当前余额")
    @ExcelProperty(value = "当前余额", index = 6)
    private String amount;

    @ColumnWidth(20)
    @FieldExplain("赛事ID")
    @ExcelProperty(value = "赛事ID", index = 7)
    private String matchId;

    @ColumnWidth(20)
    @FieldExplain("对阵")
    @ExcelProperty(value = "对阵", index = 8)
    private String matchInfo;

    @ColumnWidth(20)
    @FieldExplain("联赛名称")
    @ExcelProperty(value = "联赛名称", index = 9)
    private String matchName;

    @ColumnWidth(20)
    @FieldExplain("玩法名称")
    @ExcelProperty(value = "玩法名称", index = 10)
    private String playName;

    @ColumnWidth(20)
    @FieldExplain("投注项名称")
    @ExcelProperty(value = "投注项名称", index = 11)
    private String playOptionName;

    @ColumnWidth(20)
    @FieldExplain("变动原因")
    @ExcelProperty(value = "变动原因", index = 12)
    private String changeReason;

    @ColumnWidth(20)
    @FieldExplain("比赛时间")
    @ExcelProperty(value = "比赛时间", index = 13)
    private String beginTime;

    @ColumnWidth(20)
    @FieldExplain("最后一次账变金额")
    @ExcelProperty(value = "最后一次账变金额", index = 14)
    private String lastChangeAmount;

    @ColumnWidth(20)
    @FieldExplain("最后一次账变时间")
    @ExcelProperty(value = "最后一次账变时间", index = 15)
    private String lastChangeTime;

    @ColumnWidth(20)
    @FieldExplain("最后一次账变后账户余额")
    @ExcelProperty(value = "最后一次账变后账户余额", index = 16)
    private String lastAfterChangeAmount;

    @ColumnWidth(20)
    @FieldExplain("第一次账变金额")
    @ExcelProperty(value = "第一次账变金额", index = 17)
    private String firstChangeAmount;

    @ColumnWidth(20)
    @FieldExplain("第一次账变时间")
    @ExcelProperty(value = "第一次账变时间", index = 18)
    private String firstChangeTime;

    @ColumnWidth(20)
    @FieldExplain("第一次账变前账户余额")
    @ExcelProperty(value = "第一次账变前账户余额", index = 19)
    private String firstChangeBeforeAmount;
}