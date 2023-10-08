package com.panda.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.center.entity.Merchant;
import com.panda.center.mapper.trader.MerchantMapper;
import com.panda.center.param.MerchantTreeForm;
import com.panda.center.service.IMerchantService;
import com.panda.center.vo.MerchantTree;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-27
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements IMerchantService {

    @Resource
    private MerchantMapper merchantMapper;

    @Override
    public List<MerchantTree> getMerchantTree(MerchantTreeForm merchantTreeForm) {
        List<MerchantTree> merchantTrees = merchantMapper.getMerchantTree(merchantTreeForm.getTag(),
                merchantTreeForm.getName());
        //获取根节点
        List<MerchantTree> rootTree =
                merchantTrees.stream().filter(v -> "0".equals(v.getParentId())).collect(Collectors.toList());
        //递归获取子节点
        for (MerchantTree rTree : rootTree) {
            List<MerchantTree> childTree = getChildTree(merchantTrees, rTree);
            rTree.setTrees(childTree);
        }
        return rootTree;
    }

    @Override
    public List<MerchantTree> getMerchantList(MerchantTreeForm merchantTreeForm) {
        return merchantMapper.getMerchantTree(merchantTreeForm.getTag(), merchantTreeForm.getName());
    }

    private List<MerchantTree> getChildTree(List<MerchantTree> merchantTrees, MerchantTree rTree) {
        List<MerchantTree> childTree =
                merchantTrees.stream().filter(v -> rTree.getId().equals(v.getParentId())).collect(Collectors.toList());
        if (childTree.size() == 0) {
            return Collections.emptyList();
        }
        for (MerchantTree merchantTree : childTree) {
            merchantTree.setTrees(getChildTree(merchantTrees, merchantTree));
        }
        return childTree;
    }
}
