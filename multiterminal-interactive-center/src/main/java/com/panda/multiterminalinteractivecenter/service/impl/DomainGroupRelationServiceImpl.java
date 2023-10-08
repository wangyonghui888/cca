package com.panda.multiterminalinteractivecenter.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.dto.DomainRelationDto;
import com.panda.multiterminalinteractivecenter.entity.DomainGroupRelation;
import com.panda.multiterminalinteractivecenter.mapper.DomainGroupRelationMapper;
import com.panda.multiterminalinteractivecenter.service.DomainGroupRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 域名组关系表 服务实现类
 * </p>
 *
 * @author amos
 * @since 2022-07-13
 */
@Service
public class DomainGroupRelationServiceImpl extends ServiceImpl<DomainGroupRelationMapper, DomainGroupRelation> implements DomainGroupRelationService {

    @Override
    public List<DomainRelationDto> getDomainList(String tab) {
        return baseMapper.getDomainList( tab );
    }
}
