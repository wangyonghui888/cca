package com.panda.center.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class SOlympicLuckyboxDictVo implements Serializable {
 
	private static final long serialVersionUID = 1L;

	// 主键ID
    private Long id;

    //盲盒类型，1：白银盲盒 2：黄金盲盒 3：钻石盲盒
    private String boxTypeName;
    
    //盲盒类型，1：白银盲盒 2：黄金盲盒 3：钻石盲盒
    private int boxType;

    //出现次数
    private int visitNumber;

    // 单次奖金(单位 分)
    private long award;

    // 必中概率 * 100, 比如 20.24% 存值 2024
    private Long mustHitRate;

    // 必中日期格式 yyyy-MM-dd 如：2021-07-28
    private String mustHitDate;

    // 必中次数
    private int mustHitNumber;

    // 是否派发奖品
    private int isAllocate;
    // 是否上架
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
