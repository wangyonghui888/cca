package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserSportOrderDayPO extends OrderPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sport_id
     */
    private boolean sportId;

    /**
     * 格式20200110
     */
    private Integer time;
    /**
     * 活跃天数
     */
    private Integer activeDays = 1;

}
