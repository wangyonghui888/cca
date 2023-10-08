package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.po.bss.SportDutyTraderPO;
import lombok.Data;

import java.util.List;

/**
 * @author javier
 * @date 2021/2/20
 */
@Data
public class SportDutyTraderMqVO {
    private String linkId;
    private List<SportDutyTraderPO> data;
}
