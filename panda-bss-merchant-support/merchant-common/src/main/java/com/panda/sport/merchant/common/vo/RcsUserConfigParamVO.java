package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author javier
 * @date 2021/2/9
 *  TODO 用户限额接收参数信息
 */
@Data
public class RcsUserConfigParamVO {
    private RcsUserConfigVO rcsUserConfigVo;
    private List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList;
}
