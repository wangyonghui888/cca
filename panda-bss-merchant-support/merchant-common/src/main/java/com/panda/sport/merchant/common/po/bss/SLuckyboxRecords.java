package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SLuckyboxRecords implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *id
     * */
    private Long id;
    /**
     *id 用户id
     * */
    private Long uid;
    
    /**
     *盲盒类型，1：白银盲盒  2：黄金盲盒  3：钻石盲盒
     * */
    private Integer boxType;
    /**
     *单次奖金(单位 分)
     * */
    private Long award;
    /**
     * 令牌消耗数
     */
    private int useToken;
    
    /**
     * 拆盒次数
     */
    private int openNumber;
    
    /**
     *创建时间
     * */
    private Long createTime;
    
    private String createdBy;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String awardStr;
}