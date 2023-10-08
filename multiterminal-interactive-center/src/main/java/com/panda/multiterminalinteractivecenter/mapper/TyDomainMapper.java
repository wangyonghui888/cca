package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.dto.DomainRelationDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamTYDTO;
import com.panda.multiterminalinteractivecenter.entity.TDomain;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.api.DomainApiVO;
import com.panda.multiterminalinteractivecenter.vo.api.DomainGroupApiVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TyDomainMapper extends BaseMapper<TyDomain> {
    List<TyDomain> pageList(@Param("page") Integer page, @Param("size") Integer size, @Param("domainName") String domainName,
                            @Param("domainType") Integer domainType, @Param("domainGroupId") Long domainGroupId,
                            @Param("domainGroupName") String domainGroupName, @Param("lineCarrierId") Long lineCarrierId,
                            @Param("groupType") Integer groupType, @Param("used") Boolean used, @Param("tab") String tab,
                            @Param("domainTypes") List<Integer> domainTypes);

    int countByName(@Param("domainName") String domainName,@Param("groupType") Integer groupType, @Param("id") Long id);

    int closeDomain(List<Long> domainIds);

    void switchStatus(@Param("id") long id, @Param("status") int status, @Param("enable") long enable, @Param("enableTime") long enableTime, @Param("updateUser") String updateUser);

    void switchSelfTestTag(@Param("tyDomain") TyDomain tyDomain);

    List<DomainGroupApiVO> getDomainGroupByMerchant(@Param("merchantgroupid") Long merchantgroupid, @Param("groupType") Integer groupType,@Param("tab") String tab);

    List<DomainApiVO> getDomainByDomainGroupId(@Param("domainGroupId") Long domainGroupId, @Param("groupType") Integer groupType);

    List<TDomain> selectAll(DomainVO domainVO);

    List<TyDomain> getDomainByDomainByParam(@Param("sqlDTO") DomainSqlParamDto domainSqlParamDto, @Param("domain") TyDomain tyDomain);


    List<DomainRelationDto> list();


    void deleteByDomainId(@Param("id") Long id);

    @Select("select domain_name from t_domain_ty where domain_type in(1,2) and  enable in(1,2) and status = 1")
    List<String> getNewH5PcDomain();

    List<TyDomain> getNewDomainByGroupId(@Param("param") DomainSqlParamTYDTO domainSqlParamTYDTO);

    int closeDomainByLineId(@Param("id") Long id,@Param("tab")String tab,@Param("enable")Integer enable);

    List<TyDomain> getDomainListByLineId(@Param("id") Long id, @Param("tab") String tab);

    List<TyDomain> getDomainListByNames(@Param("domainNameList")List<String> domainNameList,@Param("tab") String tab);

    List<TyDomain> getDomainListByIds(@Param("domainIdList")List<Long> domainIdList,@Param("tab") String tab);

    void offDomain(@Param("ids") List<Long> ids);

}
