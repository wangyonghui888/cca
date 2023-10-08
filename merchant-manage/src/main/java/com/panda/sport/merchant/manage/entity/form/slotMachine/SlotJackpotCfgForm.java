package com.panda.sport.merchant.manage.entity.form.slotMachine;

import com.panda.sport.merchant.common.po.bss.SlotJackpotCfg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 02/18/22 20:37:09
 */
@Getter
@Setter
@ToString
public class SlotJackpotCfgForm {
    /**
     * 彩金配置
     */
    private List<SlotJackpotCfg> jackpotList;
    /**
     * 0：不限制次数
     * 大于0：每日可参与游戏次数
     */
    private Integer dailyGameNumber;
    /**
     * 老虎机ID
     */
    private Integer id;
}
