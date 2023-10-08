package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 盲盒设置的次数
 */
@Setter
@Getter
@TableName("s_olympic_luckybox_dict")
public class SOlympicLuckyboxDictPo {

    // 主键ID
	@JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    //盲盒类型，1：白银盲盒 2：黄金盲盒 3：钻石盲盒
    private int boxType;

    //盲盒类型，1：白银盲盒 2：黄金盲盒 3：钻石盲盒
    @TableField(exist = false)
    private String boxTypeName;

    //1表示删除
    @TableField(exist = false)
    private int delete;

    public String getBoxTypeName() {
    	return boxType == 1 ? "白银盲盒" : boxType == 2 ? "黄金盲盒" : "钻石盲盒";
    }

    //出现次数
    private int visitNumber;

    private int orderNum;

    // 单次奖金(单位 分)
    private long award;

    // 必中概率 * 100, 比如 20.24% 存值 2024
    private Long mustHitRate;

    // 必中日期格式 yyyy-MM-dd 如：2021-07-28
    private String mustHitDate;

    // 必中次数
    private Integer mustHitNumber;

    // 是否派发奖品
    private int isAllocate;
    //是否上架
    private int isUp;

    //名称比如：万国IWC 38003 工程师腕表
    private String name ;

    //限制用户次数，-1表示不限制
    private int userLimit;

    //是否top10用户特有,1 是，0 不是
    private int top10User;

    // 创建时间
    private Long createTime;

    // 修改时间
    private Long modifyTime;

}
