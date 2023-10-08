package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.dto.DomainRelationDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamDto;
import com.panda.multiterminalinteractivecenter.entity.DomainGroupRelation;
import com.panda.multiterminalinteractivecenter.entity.DomainProgramRelation;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 域名组关系表 Mapper 接口
 * </p>
 *
 * @author amos
 * @since 2022-07-13
 */
@Repository
public interface DomainGroupRelationMapper extends BaseMapper<DomainGroupRelation> {

    List<TyDomain> selectDomainSameGroup(DomainSqlParamDto domainSqlParamDto);

    List<DomainRelationDto> getDomainList(@Param("tab") String tab);

    List<Long> selectDomainGroupIds(@Param("id") Long id);

    int deleteByDomainProgramId(@Param("programRelationList") List<DomainProgramRelation> programRelationList, @Param("programId") Long programId );

    int delProgramRelationByDomainGroupId(@Param("programId") Long programId, @Param("domainGroupId") Long domainGroupId );

    void deleteByGroupId(@Param("id") Long id);
}
