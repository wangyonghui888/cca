package com.panda.sport.merchant.common.dto;/**
 * @author Administrator
 * @date 2021/8/19
 * @TIME 14:12
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *@ClassName UserSpecialLimitDto
 *@Description TODO
 *@Author Administrator
 *@Date 2021/8/19 14:12
 */
@Data
public class UserTradeRestrictDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long userId;
}
