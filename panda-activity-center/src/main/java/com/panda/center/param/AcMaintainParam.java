package com.panda.center.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/29 21:16:20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AcMaintainParam {
    /**
     * 维护 h5 banner
     */
    private String h5MaintainUrl;
    /**
     * 维护 pc banner
     */
    private String pcMaintainUrl;
    /**
     * 0关闭维护,开启活动;1开启维护
     */
    private Integer maintainStatus;
    /**
     * 维护结束时间
     */
    private Long maintainEndTime;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告内容
     */
    private String content;
}
