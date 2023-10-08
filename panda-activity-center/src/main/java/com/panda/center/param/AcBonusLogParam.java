package com.panda.center.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/26 17:10:44
 */
@Getter
@Setter
@ToString
public class AcBonusLogParam implements Serializable {

    private String merchantSer;

    private String userSer;

    private Long strTime;

    private Long endTime;

    private Integer taskId;

    private Integer page;

    private Integer size;
}
