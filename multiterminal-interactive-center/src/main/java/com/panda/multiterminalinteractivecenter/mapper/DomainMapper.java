package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO4DJ;
import com.panda.multiterminalinteractivecenter.entity.TDomain;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantDomainVO;
import com.panda.multiterminalinteractivecenter.vo.TDomainVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface DomainMapper extends BaseMapper<TDomain> {
    List<TDomain> selectAll(DomainVO domainVO);

    int resetDomain(Long id);
    int reset2Domain(Long id);


    /**
     * 保存
     *
     * @param domain
     * @return
     */
    Integer saveDomian(TDomain domain);


    /**
     * 更新启用时间
     *
     * @param id
     * @param enableTime
     * @return
     */
    Integer updateDomainEnableTimeById(@Param("id") Long id, @Param("enableTime") long enableTime);

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
                                     @Param("groupType")Integer groupType, @Param("used")Boolean used, @Param("tab") String tab);

    /**
     * 修改域名方案
     *
     * @param merchantDomainVO
     * @return
     */
    Integer updateDomainProgram(MerchantDomainVO merchantDomainVO);

    /**
     * 修改域名表中商户组ID
     * @param merchantGroupId
     * @return
     */
    Integer updateDomainByMerchantGroupId(@Param("merchantGroupId") String merchantGroupId);

    /**
     * 刪除
     *
     * @author duwan
     * @date 2021/08/19
     **/
    int deleteAll(DomainVO domainVo);


    void updateDomainEnable(@Param("enable") Integer enable, @Param("domainName") String domainName);

    Map<String,String> getAvailableDomainByGroup(@Param("domainName") String domain);

    TDomainVo getDomainByTypeAndGroupId(@Param("domainType") Integer domainType, @Param("merchantGroupId") Long merchantGroupId);

    int updateByDomainTypeAndName(@Param("newEnable")Integer newEnable,@Param("type") Integer type,@Param("domainName") String domainName,@Param("merchantGroupId") Long merchantGroupId,@Param("enableTime") Long enableTime);


    TDomainVo getTypeByDomainName(@Param("domainName") String domainName,@Param("merchantGroupId") Long merchantGroupId);

    List<TyDomain> findDomainByGroupType();

    Long getLineCarrierOne();

    List<MerchantDomainVO> getDomainGroupByProgramId(@Param("programId") String programId, @Param("tab") String tab);

    List<Map<String,Object>> getDomainGroupListByMerchantCode(@Param("merchantDomainVO") MerchantDomainVO merchantDomainVO,
                                                              @Param("domainGroupIds") List<Long> domainGroupIds);
}
