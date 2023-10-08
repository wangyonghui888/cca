package com.panda.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.center.entity.Merchant;
import com.panda.center.param.MerchantTreeForm;
import com.panda.center.result.Response;
import com.panda.center.vo.MerchantTree;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-27
 */
public interface IMerchantService extends IService<Merchant> {

    List<MerchantTree> getMerchantTree(MerchantTreeForm merchantTreeForm);

    List<MerchantTree> getMerchantList(MerchantTreeForm merchantTreeForm);
}
