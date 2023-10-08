package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.bss.SportPO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.vo.SportVO;

import java.util.List;

/**
 * <p>
 * 标准球类表. 【数据来自融合表：standard_sport_type】 服务类
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface ISportService extends IService<SportPO> {

    List<SportVO> getListByIds(List<Integer> acIds);
}
