package com.panda.sport.merchant.common.vo.merchant;

import com.panda.sport.merchant.common.po.bss.MerchantDomainPO;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author hobart 2023-04-30
 */
@Data
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantEventVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * goalEvent   进球事件(0关,1开)
     */
    private Integer goalEvent;
    /**
     * cornerEvent  角球事件(0关,1开)
     */
    private Integer cornerEvent;
    /**
     * penaltyEvent 罚牌事件(0关,1开)
     */
    private Integer penaltyEvent;
    /**
     * videoTraffic   视频流量消耗
     */
    private Integer videoTraffic;

    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 精彩回放开关
     */
    private Integer eventSwitch;

}
