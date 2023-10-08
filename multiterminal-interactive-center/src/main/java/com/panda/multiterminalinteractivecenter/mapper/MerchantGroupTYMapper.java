package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.MerchantGroupTY;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;

/**
 * <p>
 * B端商户分组表 Mapper 接口
 * </p>
 *
 * @author amos
 * @since 2022-07-11
 */
public interface MerchantGroupTYMapper extends BaseMapper<MerchantGroupTY> {

    TyDomain selectCombineDomainList();

}
