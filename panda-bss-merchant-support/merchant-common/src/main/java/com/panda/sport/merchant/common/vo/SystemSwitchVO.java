package com.panda.sport.merchant.common.vo;

import lombok.Data;

@Data
public class SystemSwitchVO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 开关key值
     */
    private String configKey;

    /**
     * 开关value值(0关 1开)
     */
    private String configValue;

    /**
     * 聊天室消息拉取频率
     */
    private Integer pullMsgRate;

    private Long createTime;
    private String createBy;
    private Long updateTime;
    private String updateBy;
}
