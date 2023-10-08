package com.panda.sport.merchant.manage.entity.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxUpdateForm {

    /**
     * ID
     */
    private Long id;

    /**
     * 必中日期
     */
    private String mustHitDate;

    /**
     * 必中概率
     */
    private Long mustHitRate;

    /**
     * 必中次数
     */
    private Integer mustHitNumber;

    /**
     * 出现次数
     */
    private Integer visitNumber;
    /**
     * 盲盒类型 
     */
    private int boxType;

    /**是否上架*/
    private int isUp;
    /**
     * 分
     */
    private Long award;

    /**
     * 名称
     */
    private String name ;

    /**
     *  是否派发奖品   1 ：派发 ， 0：不派发
     */
    private int isAllocate;
}
