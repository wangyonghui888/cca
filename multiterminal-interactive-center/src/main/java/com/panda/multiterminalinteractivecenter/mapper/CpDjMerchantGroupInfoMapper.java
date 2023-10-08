package com.panda.multiterminalinteractivecenter.mapper;

import com.panda.multiterminalinteractivecenter.dto.DomainReqDTO;
import com.panda.multiterminalinteractivecenter.entity.TMerchantGroup;
import com.panda.multiterminalinteractivecenter.entity.TMerchantGroupInfo;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CpDjMerchantGroupInfoMapper {

    /**
     * 新增
     **/
    int insert(TMerchantGroupInfo tMerchantGroupInfo);

    /**
     * 刪除
     **/
    int delete(int id);

    int deleteByGroupId(Long merchantGroupId);
    /**
     * 更新
     **/
    int update(TMerchantGroupInfo tMerchantGroupInfo);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/01/01
     **/
    List<TMerchantGroupInfo> pageList(int offset, int pagesize);

    List<TMerchantGroupInfo> tMerchantGroupInfoByGroupId(@Param("merchantGroupId")Long merchantGroupId);

    List<MerchantPO> getAllByCode(@Param("code")Integer  code);
    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/01/01
     **/
    int pageListCount(int offset,int pagesize);

    MerchantGroupPO selectMerchantGroupById(@Param("id")Long id, @Param("tab") String tab);

    MerchantPO getMerchantInfoByAccount(@Param("domainReqDTO") DomainReqDTO domainReqDTO);

}