package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.Domain2DomainGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Domain2DomainGroupMapper extends BaseMapper<Domain2DomainGroup> {
    List<Domain2DomainGroup> selectByDomainId(@Param("domainId") Long domainId,@Param("tab") String tab);

    void replaceByDomainId(@Param("oldDomainId") Long oldDomainId, @Param("domainId") Long domainId,@Param("tab") String tab);

    int countByGroupId(@Param("domainGroupId") Long id,@Param("tab") String tab);

    List<Long> getDomainIdsByDomainGroupId(@Param("domainGroupId") Long domainGroupId,@Param("tab") String tab);

    List<Long> selectIdsByDomainIdAndDomainType(@Param("domainGroupId") Long domainGroupId, @Param("domainType") Integer domainType,@Param("tab") String tab);

    int insertList(@Param("list") List<Domain2DomainGroup> list);
}
