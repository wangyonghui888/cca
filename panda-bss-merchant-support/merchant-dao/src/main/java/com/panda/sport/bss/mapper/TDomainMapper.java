package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.TDomain;
import com.panda.sport.merchant.common.po.bss.TMerchantDomainGroupPo;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.TDomainVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author duwan
 * @description t_domain
 * @date 2021-08-19
 */
@Mapper
@Repository
public interface TDomainMapper extends BaseMapper<TDomain> {


    /**
     * 刪除
     *
     * @author duwan
     * @date 2021/08/19
     **/
    int delete(Long id);

    int resetDomain(Long id);

    int checkDomainByGroup(@Param("api")String api,@Param("groupCode")String groupCode);

    /**
     * 刪除
     *
     * @author duwan
     * @date 2021/08/19
     **/
    int deleteAll(DomainVo domainVo);

    /**
     * 更新
     *
     * @author duwan
     * @date 2021/08/19
     **/
    int update(TDomain tDomain);

    /**
     * 查询 分页查询
     *
     * @author duwan
     * @date 2021/08/19
     **/
    List<TDomainVo> pageList(DomainVo domainVo);

    /**
     * 查询 根据主键 id 查询
     *
     * @author duwan
     * @date 2021/08/19
     **/
    TDomain load(int id);

    /**
     * 查询 分页查询 count
     *
     * @author duwan
     * @date 2021/08/19
     **/
    int pageListCount(DomainVo domainVo);

    /**
     * 查询 分页查询
     *
     * @author duwan
     * @date 2021/08/19
     **/
    List<TDomain> selectAll(DomainVo domainVo);

    List<TDomain> selectAllByEnable34();
    /**
     * 搜索
     *
     * @return
     */
    String selectAnimation3Url();

    /**
     * 更新动画域名
     *
     * @param url
     * @return
     */
    Integer updateAnimation(String url);

    /**
     * 保存
     *
     * @param domain
     * @return
     */
    Integer saveDomian(TDomain domain);

    /**
     * 检测状态
     *
     * @param
     * @return
     */
    Integer checkDomianByEnable(@Param("merchantGroupId") Long merchantGroupId);

    /**
     * 检测状态
     *
     * @param
     * @return
     */
    Integer updateDomianEnableById(@Param("enable") Integer enable, @Param("id") Long id);

    void updateDomainEnable(@Param("enable") Integer enable, @Param("domainName") String domainName);

    /**
     * 检测状态
     *
     * @param
     * @return
     */
    Long maxDomianIdByEnable(@Param("merchantGroupId") Long merchantGroupId);

    /**
     * 更新启用时间
     *
     * @param id
     * @param enableTime
     * @return
     */
    Integer updateDomianEnableTimeByid(@Param("id") Long id, @Param("enableTime") Date enableTime);

    Integer updateByName(@Param("domainName") String domainName);

    String getAvailableDomain();

    Map<String,String> getAvailableDomainByGroup(@Param("domainName") String domain);

    String get();

    @Select("SELECT DISTINCT ip_name FROM t_forbid_ip ")
    List<String> getIpPool();

    String getUnuseDomain();


    /**
     * 根据Id选择
     * @param groupCode
     * @return
     */
    List<TMerchantDomainGroupPo> selectTmerchantDomainGroup(@Param("groupCode") String groupCode);

    /**
     * api商户分组域名
     * @return
     */
    List<TMerchantDomainGroupPo>  tMerchantDomainGroupList();

    /**
     * 添加api商户分组
     * @param tMerchantDomainGroupPo
     * @return
     */
    Integer addApiMerchantGroup(TMerchantDomainGroupPo tMerchantDomainGroupPo);

    /**
     * 批量插入
     * @param tMerchantDomainGroupPoList
     * @return
     */
    Integer addBatchMerchantDomainGroup(List<TMerchantDomainGroupPo> tMerchantDomainGroupPoList);

    /**
     * 更新api商户分组
     * @param tMerchantDomainGroupPo
     * @return
     */
    Integer updateApiMerchantGroup(TMerchantDomainGroupPo tMerchantDomainGroupPo);

    /**
     *  删除api商户分组
     * @param tMerchantDomainGroupPo
     * @return
     */
    Integer deleteApiMerchantGroup(TMerchantDomainGroupPo tMerchantDomainGroupPo);


    /**
     * 根据IDs获取域名
     * @param merchantGroupIds
     * @return
     */
    List<TDomain> getApiDomainUrl(@Param("merchantGroupIds") List<Long> merchantGroupIds);



}