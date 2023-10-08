package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.TMerchantGroup;
import com.panda.multiterminalinteractivecenter.vo.*;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @param <T>
 */
@Repository
public interface MerchantGroupServiceTransfer<T> {
     public List<ThirdMerchantVo> getMerchantList();

     void sendCPMsg(String merchantCode, Integer domainType, String url,Boolean isVip ,String ipArea,int changeType);

     void sendDJMsg(String merchantCode, Integer domainType, String url,Integer changeType) ;

     List<MerchantVO> getMerchantList(Integer code);

     List<ThirdMerchantVo> getTblMerchantList(Integer code);

     APIResponse getMerchantGroup(Integer merchantGroupCode);

     List<MerchantGroupVO> selectMerchantGroup(MerchantGroupVO merchantGroupPO);

     APIResponse updateMerchantDomain(DomainVO domainVo, String userName, String ip);

     APIResponse getMerchantGroupDomainRelationDataList(MerchantDomainVO merchantDomainVO);

     APIResponse getDomainNameList(DomainGroupVO domainGroupVO);

     APIResponse<?> selectDomain(Integer page, Integer size, String domainName, Integer domainType,
                                 Long domainGroupId, String domainGroupName, Long lineCarrierId, Integer groupType,
                                 Boolean used, String tab,Long programId,String areaName);

     APIResponse updateMerchantGroup(MerchantGroupVO merchantGroupPO, HttpServletRequest request);

     APIResponse deleteMerchantGroup(MerchantGroupVO merchantGroupPO, HttpServletRequest request);

     APIResponse createMerchantGroup(TMerchantGroup tMerchantGroup, HttpServletRequest request);

     APIResponse delProgramRelationByDomainGroupId(MerchantGroupVO merchantGroupVO, HttpServletRequest request);

     APIResponse getMerchantGroupDomain(MerchantGroupDomainVO req);


     APIResponse editThreshold(MerchantGroupVO req, HttpServletRequest request);

     APIResponse getThreshold(MerchantGroupVO req);
}
