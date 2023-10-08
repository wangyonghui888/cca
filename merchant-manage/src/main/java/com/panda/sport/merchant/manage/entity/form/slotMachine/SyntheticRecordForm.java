package com.panda.sport.merchant.manage.entity.form.slotMachine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author baylee
 * @version 1.0
 * @date 02/22/22 18:55:42
 */
@Getter
@Setter
@ToString
public class SyntheticRecordForm {
    /**
     * 商户ID
     */
    private String merchantCode;
    /**
     * 用户ID
     */
    private String uid;

    /**
     * 消耗奖券类型
     */
    private Integer sourceTokenId;

    /**
     * 合成奖券类型
     */
    private Integer targetTokenId;

    /**
     * 返还奖券类型
     */
    private Integer returnTokenId;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 每页显示条数，默认 20
     */
    private Integer size = 20;
    /**
     * 当前页
     */
    private Integer current = 1;
}
