package com.panda.multiterminalinteractivecenter.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.entity.MerchantGroupTY;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.mapper.MerchantGroupTYMapper;
import com.panda.multiterminalinteractivecenter.service.MerchantGroupTYService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * B端商户分组表 服务实现类
 * </p>
 *
 * @author amos
 * @since 2022-07-11
 */
@Service
public class MerchantGroupTYServiceImpl extends ServiceImpl<MerchantGroupTYMapper, MerchantGroupTY> implements MerchantGroupTYService {

    @Override
    public TyDomain selectCombineDomainList() {

        return baseMapper.selectCombineDomainList() ;
    }
}
