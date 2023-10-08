package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.merchant.common.po.bss.SportPO;
import com.panda.sport.match.mapper.SportPOMapper;
import com.panda.sport.merchant.common.vo.SportVO;
import com.panda.sport.merchant.manage.service.ISportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标准球类表. 【数据来自融合表：standard_sport_type】 服务实现类
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
@Service
public class SportServiceImpl extends ServiceImpl<SportPOMapper, SportPO> implements ISportService {
    @Resource
    private SportPOMapper sportPOMapper;
    @Override
    public List<SportVO> getListByIds(List<Integer> acIds) {
        return sportPOMapper.getListByIds(acIds);
    }
}
