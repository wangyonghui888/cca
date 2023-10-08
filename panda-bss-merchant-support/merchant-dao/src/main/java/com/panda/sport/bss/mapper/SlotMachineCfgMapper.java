package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SlotMachineCfg;

/**
 * <p>
 * 老虎机游戏配置表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
public interface SlotMachineCfgMapper extends BaseMapper<SlotMachineCfg> {
    /**
     * 查询排序最大值
     *
     * @return java.lang.Integer
     */
    Integer selectMaxSortNo();
}
