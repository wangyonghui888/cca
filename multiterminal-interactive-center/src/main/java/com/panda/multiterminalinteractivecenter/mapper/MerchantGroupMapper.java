package com.panda.multiterminalinteractivecenter.mapper;

import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import com.panda.multiterminalinteractivecenter.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MerchantGroupMapper{

    List<MerchantGroupPO> selectMerchantGroup(MerchantGroupPO merchantGroupPO);

    // 不知道谁的代码，没有mapper。xml方法，先注释了
    //List<MerchantPO> queryMerchantListByGroup(Map<String, Object> param);

    MerchantGroupPO selectMerchantGroupById(@Param("id")Long id, @Param("tab") String tab);

    int updateMerchantGroup(MerchantGroupPO merchantGroupPO);

    int deleteMerchantGroup(MerchantGroupPO merchantGroupPO);

    int createMerchantGroup(MerchantGroupPO merchantGroupPO);

    DomainProgramVO findProgram(MerchantGroupVO merchantGroupVO);

    /**
     * 修改商户组方案id
     *
     * @param programId
     * @param programId
     * @return
     */
    int updateMerchantGroupByProgramId(@Param("programId") Long programId,
                                       @Param("groupType") Integer groupType,
                                       @Param("tab") String tab);

    int findMerchantGroupCount(@Param("programId") Long programId
                                , @Param("groupType") Integer groupType
                                , @Param("tab") String tab);

    List<TMerchantGroupInfoVo> getMerchantGroupInfoByThirdCode(@Param("groupCode")Integer groupCode, @Param("account")String account);

    String getMerchantGroupNameByDomainId(@Param("domainId")Long id, @Param("tab")String tabStr);

    List<MerchantGroupDomainVO> getMerchantGroupDomain(MerchantGroupDomainVO req);

    List<Long> getMerchantNotInParamGroup(@Param("merchantGroupCodeList")List<Long> merchantGroupCodeList, @Param("groupType")Integer groupType);

    List<MerchantDomainVO> queryMerchantDomain();

    int changeMerchantDomain(@Param("newMerchantDomain") String newMerchantDomain, @Param("oldMerchantDomain") String oldMerchantDomain);

    int updateMerchantDomain(@Param("newMerchantDomain") String newMerchantDomain, @Param("oldMerchantDomain") String oldMerchantDomain, @Param("domainGroup") String domainGroup);
}
