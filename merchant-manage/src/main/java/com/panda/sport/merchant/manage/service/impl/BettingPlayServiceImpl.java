package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.BettingPlayPOMapper;
import com.panda.sport.merchant.common.po.bss.BettingPlayPO;
import com.panda.sport.merchant.common.vo.BettingPlayVO;
import com.panda.sport.merchant.manage.service.IBettingPlayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
@Service
public class BettingPlayServiceImpl extends ServiceImpl<BettingPlayPOMapper, BettingPlayPO> implements IBettingPlayService {
    @Resource
    private BettingPlayPOMapper bettingPlayPOMapper;

    @Override
    public List<BettingPlayVO> getListByIds(List<Integer> acIds) {
        return bettingPlayPOMapper.getListByIds(acIds);
    }
}
