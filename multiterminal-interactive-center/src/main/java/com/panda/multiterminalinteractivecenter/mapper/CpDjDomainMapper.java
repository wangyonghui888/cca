package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.TDomain;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantDomainVO;
import com.panda.multiterminalinteractivecenter.vo.TDomainVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CpDjDomainMapper extends BaseMapper<TDomain> {
    List<TDomain> selectAll(DomainVO domainVO);

    int resetDomain(@Param("id") Long id, @Param("tab") String tab);
    int reset2Domain(@Param("id") Long id, @Param("tab") String tab);



    /**
     * 更新启用时间
     *
     * @param id
     * @param enableTime
     * @return
     */
    Integer updateDomainEnableTimeById(@Param("id") Long id, @Param("enableTime") long enableTime, @Param("tab") String tab);

    /**
     * 获取商户组下的域名
     * @param merchantDomainVO
     * @return
     */
    List<MerchantDomainVO>  getMerchantGroupDomainRelationDataList(MerchantDomainVO merchantDomainVO);


    List<DomainVO>  getDomainNameList(DomainGroupVO domainGroupVO);

    /**
     * 选择域名名称
     * @return
     */
    List<TyDomain>  selectDomainList(@Param("page")Integer page, @Param("size") Integer size, @Param("domainName") String domainName,
                                     @Param("domainType")Integer domainType, @Param("domainGroupId") Long domainGroupId,
                                     @Param("domainGroupName")String domainGroupName, @Param("lineCarrierId")Long lineCarrierId,
                                     @Param("groupType")Integer groupType, @Param("used")Boolean used, @Param("tab") String tab,
                                     @Param("programId")Long programId);


    /**
     * 修改域名表中商户组ID
     * @param merchantGroupId
     * @return
     */
    Integer updateDomainByMerchantGroupId(@Param("merchantGroupId") String merchantGroupId,@Param("tab") String tab);

    List<Long> selectDomainGroupIds(@Param("id") Long id,@Param("tab") String tab);

    List<MerchantDomainVO> getDomainGroupByProgramId(@Param("programId") String programId,@Param("tab") String tab);

    List<Map<String,Object>> getDomainGroupListByMerchantCode(@Param("merchantDomainVO") MerchantDomainVO merchantDomainVO,
                                                @Param("domainGroupIds") List<Long> domainGroupIds);

    Long getVIPDomainGroupIdByProgramId(@Param("programId") Long programId);

    Long getDefaultDomainGroupIdByProgramId(@Param("programId")Long programId);

    Long getDomainGroupIdByProgramId(@Param("programId")Long programId,@Param("areaName") String areaName);
}
