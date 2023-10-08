package com.panda.multiterminalinteractivecenter.mapper;

import com.panda.multiterminalinteractivecenter.dto.DomainResDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO4DJ;
import com.panda.multiterminalinteractivecenter.entity.TMerchantGroup;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import com.panda.multiterminalinteractivecenter.vo.DomainProgramVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantGroupVO;
import com.panda.multiterminalinteractivecenter.vo.TMerchantGroupInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CpDjMerchantGroupMapper {

    List<MerchantGroupPO> selectMerchantGroup(MerchantGroupPO merchantGroupPO);

    MerchantGroupPO selectMerchantGroupById(@Param("id")Long id,@Param("tab")String tab);

    int updateMerchantGroup(MerchantGroupPO merchantGroupPO);

    int deleteMerchantGroup(MerchantGroupPO merchantGroupPO);

    DomainProgramVO findProgram(MerchantGroupVO merchantGroupVO);

    /**
     * 更新
     **/
    int update(TMerchantGroup tMerchantGroup);

    /**
     * 新增
     **/
    int insert(TMerchantGroup tMerchantGroup);

    TMerchantGroup loadIdByGroupName(@Param("groupName") String groupName,@Param("groupCode") Integer groupCode);

    List<DomainResDTO4DJ> getDomainIdsByTab(@Param("tab") String tab,@Param("account") String account);

    DomainResDTO getDomainDetails4AccountId(@Param("tab") String tab, @Param("type") int type,
                                            @Param("merchantAccount") String merchantAccount,
                                            @Param("ipArea") String ipArea,@Param("areaCode") String areaCode);

    List<TMerchantGroup> selectAll(@Param("groupCode")Integer groupCode);

    int getCountByProgramId(@Param("merchantGroupPO") MerchantGroupVO merchantGroupPO);
}
