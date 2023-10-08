package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamTYDTO;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.util.List;

public interface MerchantDomainService {

    APIResponse<?> pageList(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId,
                                   String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab) ;


    void create(TyDomain tyDomain);

    void edit(TyDomain tyDomain, HttpServletRequest request) ;

    void delete(Long id,String tab, HttpServletRequest request) ;

    void off(List<Long> id, String tab, HttpServletRequest request, String dataId);

    void importDomains(DomainImportReqDTO domainDTO) ;

    void replace(Long oldDomainId, Long domainId,String tab, HttpServletRequest request) ;

    void switchStatus(TyDomain tyDomain) ;

    void switchSelfTestTag(TyDomain tyDomain);

    List<TyDomain> getDomainByDomainByParam(DomainSqlParamDto domainSqlParamDto, TyDomain tyDomain);

    TyDomain selectById(Long id);

    List<?> getDomainByMerchantAndArea(Long merchantGroupId, String domainGroupCode) ;


    List<TyDomain> getNewDomainByGroupId(DomainSqlParamTYDTO domainSqlParamTYDTO);

    List<TyDomain> getDomainListByLineId(Long id, String tab);

    List<TyDomain> getDomainListByNames(List<String> domainNameList,String tab);

    List<TyDomain> getDomainListByIds(List<Long> domainIdList,String tab);

}
