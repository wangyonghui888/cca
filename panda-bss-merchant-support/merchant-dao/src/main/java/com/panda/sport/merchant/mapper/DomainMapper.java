package com.panda.sport.merchant.mapper;

import com.panda.sport.merchant.common.po.merchant.TDomain;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.TDomainVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description 域名池表
 * @author duwan
 * @date 2022-01-01
 */
@Mapper
@Repository
public interface DomainMapper {

    /**
     * 新增
     * @author duwan
     * @date 2022/01/01
     **/
    int insert(TDomain tDomain);

    /**
     * 刪除
     * @author duwan
     * @date 2022/01/01
     **/
    int delete(int id);

    int deleteByGroupId(int merchantGroupId);


    /**
     * 更新
     * @author duwan
     * @date 2022/01/01
     **/
    int update(TDomain tDomain);

    int updateByDomainTypeAndName(@Param("newEnable")Integer newEnable,@Param("type") Integer type,@Param("domainName") String domainName,@Param("merchantGroupId") Long merchantGroupId,@Param("enableTime") Long enableTime);

    int updateEnableByType(@Param("type") Integer type,@Param("newEnable")Integer newEnable,@Param("oldEnable")Integer oldEnable,@Param("merchantGroupId")Long merchantGroupId);

    /**
     * 查询 根据主键 id 查询
     * @author duwan
     * @date 2022/01/01
     **/
    TDomain load(int id);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/01/01
     **/
    List<TDomainVo> pageList(DomainVo domainVo);

    int resetDomain(Long id);

    List<TDomain> selectAll(DomainVo domainVo);

    /**
     * 更新启用时间
     *
     * @param id
     * @param enableTime
     * @return
     */
    Integer updateDomianEnableTimeByid(@Param("id") Long id, @Param("enableTime") Long enableTime);
    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/01/01
     **/
    int pageListCount(DomainVo domainVo);

    TDomainVo getDomainByTypeAndGroupId(@Param("domainType") Integer domainType,@Param("merchantGroupId") Long merchantGroupId);

    List<Integer> getTypeByGroupId(Long merchantGroupId);

    TDomainVo getTypeByDomainName(@Param("domainName") String domainName,@Param("merchantGroupId") Long merchantGroupId);

    List<TDomainVo> getTypeByEnable(@Param("enable") Integer enable);

    Integer checkDomianByEnable(@Param("merchantGroupId") Long merchantGroupId,@Param("domainType") Integer domainType);

    /**
     * 检测状态
     *
     * @param
     * @return
     */
    Long maxDomianIdByEnable(@Param("merchantGroupId") Long merchantGroupId,@Param("domainType") Integer domainType);

    /**
     * 检测状态
     *
     * @param
     * @return
     */
    Integer updateDomainEnableById(@Param("enable") Integer enable, @Param("id") Long id);

}
