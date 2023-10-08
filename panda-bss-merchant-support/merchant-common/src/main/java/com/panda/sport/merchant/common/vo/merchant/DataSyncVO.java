package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

/**
 * 数据同步
 */
@Data
public class DataSyncVO {

    private String timeZone;

    private String filter;

    private String startDate;

    private String endDate;
}
