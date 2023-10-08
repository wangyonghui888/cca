package com.panda.multiterminalinteractivecenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.multiterminalinteractivecenter.entity.MerchantGroupTY;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;

/**
 * <p>
 * B端商户分组表 服务类
 * </p>
 *
 * @author amos
 * @since 2022-07-11
 */
public interface MerchantGroupTYService extends IService<MerchantGroupTY> {

    TyDomain selectCombineDomainList();

}
