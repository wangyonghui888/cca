package com.panda.multiterminalinteractivecenter.service;


import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.vo.*;

import java.util.List;

/**
 * 商户组接口类
 * @author ifan
 * @date 2022/7/11
 */
public interface MerchantGroupService {


    /**
     * 查询商户组信息
     * @param merchantGroupVO
     * @return
     */
    List<MerchantGroupVO> selectMerchantGroup(MerchantGroupVO merchantGroupVO);

    /**
     * 清除商户缓存
     * @param merchantTag
     * @param containsType
     * @param containsStr
     * @param merchantCode
     * @param parentCode
     * @return
     */
    APIResponse cleanMerchant(Integer merchantTag, Integer containsType, String containsStr, String merchantCode, String parentCode);


    /**
     * 手动切换域名
     * @param domainVo
     * @param userName
     * @param ip
     * @return
     */
    APIResponse updateMerchantDomain(DomainVO domainVo, String userName,  String ip);

    /**
     * 查询商户组下的域名
     * @param merchantDomainVO
     * @return
     */
    APIResponse getMerchantGroupDomainRelationDataList(MerchantDomainVO merchantDomainVO);

    /**
     * 根据商户分组类型 域名类型查询域名名称
     * @param domainGroupVO
     * @return
     */
    APIResponse getDomainNameList(DomainGroupVO domainGroupVO);

    /**
     * 通过商户分组类型 域名类型 区域选择域名名称
     * @return
     */
    APIResponse<?> selectDomain(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId,
                             String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab);

    /**
     * 修改商户组信息
     * @param merchantGroupPO
     * @return
     */
    APIResponse updateMerchantGroup(MerchantGroupVO merchantGroupPO);

    /**
     * 删除商户组信息
     * @param merchantGroupPO
     * @return
     */
    APIResponse deleteMerchantGroup(MerchantGroupVO merchantGroupPO);

    /**
     * 根据商户分组类型查询商户信息
     * @param merchantVO
     * @return
     */
    APIResponse selectList(MerchantVO merchantVO);

    /**
     * 创建商户组信息
     * @param merchantGroupPO
     * @return
     */
    APIResponse createMerchantGroup(MerchantGroupVO merchantGroupPO);

    /**
     * 删除方案关系表
     */
    APIResponse delProgramRelationByDomainGroupId(MerchantGroupVO merchantGroupVO);
}
