package com.panda.multiterminalinteractivecenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.multiterminalinteractivecenter.dto.DomainRelationDto;
import com.panda.multiterminalinteractivecenter.entity.DomainGroupRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 域名组关系表 服务类
 * </p>
 *
 * @author amos
 * @since 2022-07-13
 */
public interface DomainGroupRelationService extends IService<DomainGroupRelation> {

    List<DomainRelationDto> getDomainList(@Param("tab") String tab);

}
