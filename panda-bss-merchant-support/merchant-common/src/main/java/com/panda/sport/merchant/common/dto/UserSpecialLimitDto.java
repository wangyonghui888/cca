package com.panda.sport.merchant.common.dto;/**
 * @author Administrator
 * @date 2021/8/19
 * @TIME 14:12
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *@ClassName UserSpecialLimitDto
 *@Description TODO
 *@Author Administrator
 *@Date 2021/8/19 14:12
 */
@Data
public class UserSpecialLimitDto  implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long userId;
    private Integer specialLimitType;

    /**
     * 百分比限额数据
     */
    private BigDecimal percentage;
    private String remark;
    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 标签行情等级ID（赔率分组）
     */
    private String tagMarketLevelId;

    /**
     * 体育种类Id
     */
    private List<Long> sportIdList;

    /**
     * 	投注额外延时
     */
    private Integer betExtraDelay;
}
