package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.bss.BettingPlayPO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.vo.BettingPlayVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface IBettingPlayService extends IService<BettingPlayPO> {

    List<BettingPlayVO> getListByIds(List<Integer> acIds);
}
