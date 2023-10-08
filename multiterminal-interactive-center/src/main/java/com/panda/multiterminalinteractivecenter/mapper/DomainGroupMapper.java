package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.dto.DomainDTO2;
import com.panda.multiterminalinteractivecenter.dto.DomainGroupDTO;
import com.panda.multiterminalinteractivecenter.entity.DomainGroup;
import com.panda.multiterminalinteractivecenter.entity.DomainGroupRelation;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author :  ifan
 * @Description :  域名组mapper
 * @Date: 2021-07-02
 * --------  ---------  --------------------------
 */
@Mapper
@Repository
public interface DomainGroupMapper extends BaseMapper<DomainGroup> {

    /**
     * 新建域名组
     * @param domainGroup
     * @return
     */
    int insert(DomainGroup domainGroup);

    /**
     * 删除域名组
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 删除域名关系数据
     * @param id
     * @return
     */
    int deleteByGroupId(Long id);

    int deleteByDomainId(@Param("ids") List<String> ids,@Param("groupId") Long groupId ,@Param("tab") String tab);

    int deleteByDomainIds(@Param("groupId") Long groupId ,@Param("tab") String tab);

    /**
     * 修改域名组
     * @param DomainGroup
     * @return
     */
    int update(DomainGroup DomainGroup);

    /**
     * 批量保存域名组关系数据
     * @param relationList
     * @return
     */
    int batchSaveDomainGroupRelation(List<DomainGroupRelation> relationList);

    /**
     * 域名组分页查询
     * @param domainGroupVO
     * @return
     */
    List<DomainGroup> pageList(DomainGroupVO domainGroupVO);

    /**
     *
     * 域名组查询 count
     * @param domainGroupVO
     * @return
     */
    int pageListCount(DomainGroupVO domainGroupVO);

    /**
     * 域名组Id查询
     * @param id
     * @return
     */
    DomainGroupVO getDomainGroupById(@Param("id") Long id,@Param("tab") String tab);

    List<DomainGroupVO> getDomainGroupByIds(@Param("ids") List<Long> ids,@Param("tab") String tab);

    /**
     * 修改域名管理中的domain_group_id
     * @param ids
     * @return
     */
    int batchUpdateByDomainGroupId( @Param("ids") List<String> ids, @Param("domainGroupId") Long domainGroupId);

    /**
     * 查询域名组集合
     * @param id
     * @param groupType
     * @return
     */
    List<DomainGroup> findProgramDownDomainGroupList(@Param("id") Long id, @Param("groupType") Integer groupType, @Param("groupType") String tab);

    /**
     * 获取域名数据
     * @param domainVO
     * @return
     */
    List<DomainVO>  getDomainTree(@Param("domainVO") DomainVO domainVO);

    /**
     * 查找线路商名称
     * @param lineCarrierId
     * @return
     */
    String  getLineCarrierName(@Param("lineCarrierId") Long lineCarrierId);

    /**
     * 校验区域是否属于同分组类型
     * @param areaId
     * @param groupType
     * @return
     */
    int checkAreaDomainGroup(@Param("areaId") Long areaId,@Param("groupType") Integer groupType, @Param("tab") String tab);

    List<String> getDomainGroupType(@Param("domainId")Long id);

    List<DomainDTO2> findDomainExist(@Param("domainGroupId") Long domainGroupId, @Param("tab") String tab);

    List<DomainGroupDTO> selectSimpleAll( @Param("tab") String tab);
}
