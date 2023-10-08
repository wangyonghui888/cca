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

import java.util.List;

public interface ThirdDomainMapper extends BaseMapper<TyDomain> {
    List<TyDomain> pageList(@Param("page") Integer page, @Param("size") Integer size, @Param("domainName") String domainName,
                            @Param("domainType") Integer domainType, @Param("domainGroupId") Long domainGroupId,
                            @Param("domainGroupName") String domainGroupName, @Param("lineCarrierId") Long lineCarrierId,
                            @Param("groupType") Integer groupType, @Param("used") Boolean used, @Param("tab") String tab);

    int countByName(@Param("domainName") String domainName, @Param("id") Long id);

    int closeDomain(List<Integer> domainIds);

    void switchStatus(@Param("id") long id, @Param("status") int status, @Param("enable") long enable, @Param("enableTime") long enableTime,@Param("updateUser") String updateUser);

    void switchSelfTestTag(@Param("tyDomain") TyDomain tyDomain);

    List<DomainGroupApiVO> getDomainGroupByMerchant(@Param("merchantgroupid") Long merchantgroupid);

    List<DomainApiVO> getDomainByDomainGroupId(@Param("domainGroupId") Long domainGroupId, @Param("groupType") Integer groupType);

    List<TDomain> selectAll(DomainVO domainVO);

    List<TyDomain> getDomainByDomainByParam(@Param("sqlDTO") DomainSqlParamDto domainSqlParamDto, @Param("domain") TyDomain tyDomain);

    List<DomainRelationDto> list();

    void deleteByDomainId(@Param("id") Long id,@Param("tab") String tab);

    void insertIgnoreNull(TyDomain tyDomain);

    void updateIgnoreNull(TyDomain tyDomain);

    /**
     * 删除记录
     *
     * @param id 待删除的记录
     * @return 返回影响行数
     */
    int delById(@Param("id") Long id);

    void deleteByDomainId(@Param("id") Long id);

    List<String> getNewH5PcDomain();

    List<TyDomain> getList(@Param("enable") Integer enable,@Param("status")  Integer status,
                           @Param("selfTestTag") Integer selfTestTag,
                           @Param("domainTypes") List<Integer> domainTypes,
                           @Param("tab") String tab);

    TyDomain getById(@Param("id") Long id);

    List<TyDomain> getNewDomainByGroupId(@Param("param") DomainSqlParamTYDTO domainSqlParamTYDTO);

    List<TyDomain> getDomainListByLineId(@Param("id") Long id, @Param("tab") String tab);

    List<TyDomain> getDomainListByNames(@Param("domainNameList")List<String> domainNameList,@Param("tab") String tab);

    List<TyDomain> getDomainListByIds(@Param("domainIdList")List<Long> domainIdList,@Param("tab")String tab);

    void offDomain(@Param("ids") List<Long> ids, @Param("tab") String tab);

    /**
     * 获取 merchantGroupId
     * @param ids domainIds
     * @param tab cp、dj
     * @return list
     */
    List<Long> selectMerchantGroupId(@Param("ids") List<Long> ids, @Param("tab") String tab);
}
