package com.panda.sport.merchant.common.vo.activity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelIgnoreUnannotated

public class LuckyboxRecordsVO implements Serializable {

    public  final static  String boxName1 = "白银盲盒";
    public  final static  String boxName2 = "黄金盲盒";
    public  final static  String boxName3 = "钻石盲盒";

    @JsonIgnore
    private String id;
    @ColumnWidth(20)
    @ExcelProperty(value = "商户名称", index = 1)
    private String merchantName;
    @ColumnWidth(20)
    @ExcelProperty(value = "商户ID", index = 2)
    private String merchantId;
    @ExcelProperty(value = "用户名称", index = 3)
    @ColumnWidth(20)
    private String userName;
    //private String merchantCode;
    @ColumnWidth(20)
    @ExcelProperty(value = "用户ID", index = 4)
    private String uid;
    @JsonIgnore
    private Integer boxType;
    @ColumnWidth(15)
    @ExcelProperty(value = "盲盒类型", index =  5)
    private String boxName;
    @ColumnWidth(15)
    @ExcelProperty(value = "奖品名称", index = 6)
    private String awardStr;

    @ExcelProperty(value = "金额", index = 7)
    private Long award;

    @ColumnWidth(15)
    @ExcelProperty(value = "消耗奖卷数", index = 8)
    private Integer useToken;

    @ColumnWidth(15)
    @ExcelProperty(value = "领取状态", index = 9)
    private String status;
    @ColumnWidth(20)
    @ExcelProperty(value = "领取时间", index = 10)
    private String createTime;
    //开始
    @JSONField(serialize = false)
    private Long startTime;
    //结束
    @JSONField(serialize = false)
    private Long endTime;
    //不需要给到客户端
    @JSONField(serialize = false)
    private Page<LuckyboxRecordsVO> page;

    public String getBoxName() {
        if (boxType == null) {
            return null;
        }
        if (boxType == 1) {
            boxName = LuckyboxRecordsVO.boxName1;
        } else if (boxType == 2) {
            boxName = LuckyboxRecordsVO.boxName2;
        } else if (boxType == 3) {
            boxName = LuckyboxRecordsVO.boxName3;
        }
        return boxName;
    }

}