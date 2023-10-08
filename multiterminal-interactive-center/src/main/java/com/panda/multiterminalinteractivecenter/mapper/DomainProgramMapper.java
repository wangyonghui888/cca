package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.DomainProgram;
import com.panda.multiterminalinteractivecenter.entity.DomainProgramRelation;
import com.panda.multiterminalinteractivecenter.vo.DomainProgramVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author :  ifan
 * @Description :  域名切换方案mapper
 * @Date: 2021-07-02
 * --------  ---------  --------------------------
 */
@Mapper
@Repository
public interface DomainProgramMapper extends BaseMapper<DomainProgram> {

    /**
     * 新建域名切换方案
     *
     * @param domainProgram
     * @return
     */
    int insert(DomainProgram domainProgram);

    /**
     * 删除域名切换方案
     *
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 修改域名切换方案
     *
     * @param domainProgram
     * @return
     */
    int update(DomainProgram domainProgram);


    /**
     * 删除域名切换方案关系表
     *
     * @param programId
     * @param programId
     * @return
     */
    int delDomainProgramRelation(@Param("programId") Long programId,
                                 @Param("groupType") Integer groupType,
                                 @Param("tab") String tab);

    /**
     * 域名切换方案分页查询
     *
     * @param domainProgramVO
     * @return
     */
    List<DomainProgram> pageList(DomainProgramVO domainProgramVO);

    /**
     * 域名切换方案查询 count
     *
     * @param domainProgramVO
     * @return
     */
    int pageListCount(DomainProgramVO domainProgramVO);

    int countByProgram(DomainProgramVO domainProgramVO);

    /**
     * 查询方案集合
     *
     * @return
     */
    List<DomainProgram> findProgramList(DomainProgramVO domainProgramVO);

    /**
     * 批量保存方案域名组关系数据
     * @param relationList
     * @return
     */
    int batchSaveDomainProgramRelation(List<DomainProgramRelation> relationList);

    /**
     * 查询方案详情
     * @param list
     * @param groupType
     * @return
     */
    List<DomainProgram> findProgramDetail(@Param("list") List<String> list,@Param("groupType") Integer groupType,@Param("tab") String tab);

    int hasDefaultDomainGroup(@Param("programId") Long programId,@Param("tab") String tab);

    List<Long> getIdByDomainId(@Param("domainId") Long domainId,@Param("tab") String tab);

    int getUseCountById(@Param("programId")Long id);
}
