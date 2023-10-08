package com.panda.multiterminalinteractivecenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainReqDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO4DJ;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;

import java.util.List;


/**
 * <p>
 * 域名表 服务类
 * </p>
 *
 * @author ifan
 * @since 2022-07-11
 */
public interface IDomainService extends IService<TyDomain> {


    /**
     * 更新
     *
     * @param url
     * @return
     */
    APIResponse updateAnimation(String url);


    /**
     * 清除缓存
     *
     * @return
     */
    APIResponse deleteAniCache();

    /**
     * 查询
     *
     * @return
     */
    APIResponse queryAnimation();


    /**
     * 查询
     *
     * @return
     */
    APIResponse queryLiveH5Domain();


    /**
     * 清除缓存
     *
     * @return
     */
    APIResponse deleteH5Cache();


    /**
     * 更新
     *
     * @param url
     * @return
     */
    APIResponse updateLiveH5Domain(String url);


    /**
     * 查询
     *
     * @return
     */
    APIResponse queryLivePcDomain();


    /**
     * 更新
     *
     * @param url
     * @return
     */
    APIResponse updateLivePcDomain(String url);


    /**
     * 清除缓存
     *
     * @return
     */
    APIResponse deletePcCache();

    APIResponse getMerchantGroupInfoByThirdCode(String code, String account);

    DomainResDTO getCPDomainByMerchantGroupV2Test(DomainReqDTO domainReqDTO);

    DomainResDTO getCPDomainByMerchantGroupV2(DomainReqDTO domainReqDTO);

    List<DomainResDTO4DJ> getDJDomainByMerchantGroupV2(DomainReqDTO domainReqDTO);

    APIResponse queryChatroomDomain();

    APIResponse updateChatroomDomain(String url);

    APIResponse deleteChatRoomCache();

}
