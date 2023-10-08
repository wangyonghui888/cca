package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.SlotMachineCfg;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.slotMachine.SlotMachineForm;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 老虎机游戏配置表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
public interface ISlotMachineCfgService extends IService<SlotMachineCfg> {

    /**
     * 新增游戏配置
     *
     * @param slotMachineForm params
     * @return com.panda.sport.merchant.common.vo.Response
     */
    Response<?> addSlotMachineCfg(SlotMachineForm slotMachineForm, HttpServletRequest request);

    /**
     * 拖拽排序
     *
     * @param id     id
     * @param sortNo sortNo
     * @return com.panda.sport.merchant.common.vo.Response
     */
    Response<?> draggableSort(HttpServletRequest request,Long id, Integer sortNo);
}
