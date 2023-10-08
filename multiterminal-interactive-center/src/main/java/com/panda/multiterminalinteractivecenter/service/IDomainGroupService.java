package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainImportDTO;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :  ifan
 * @Description :  域名组接口
 * @Date: 2022-07-02
 * --------  ---------  --------------------------
 */
public interface IDomainGroupService {

    /**
     * 域名组查询
     * @param domainGroupVO
     * @return
     */
    APIResponse queryList(DomainGroupVO domainGroupVO);

    /**
     * 新建域名组
     * @param domainGroupVO
     * @param userName
     * @return
     */
    APIResponse save(DomainGroupVO domainGroupVO, String userName, HttpServletRequest request);

    /**
     * 修改域名组
     * @param domainGroupVO
     * @return
     */
    APIResponse update(DomainGroupVO domainGroupVO,String userName, HttpServletRequest request);

    /**
     * 删除域名组
     * @param id
     */
    APIResponse deleteById(Long id, HttpServletRequest request);

    /**
     * 方案下域名组查询
     * @param programId
     * @param groupType
     * @return
     */
    APIResponse findProgramDownDomainGroupList( Long programId, Integer groupType, String tab);

    /**
     * 获取域名数据
     * @param domainVO
     * @return
     */
    APIResponse getDomainTree( DomainVO domainVO);

    /**
     * 校验区域是否属于同分组类型
     * @param areaId
     * @param groupType
     * @return
     */
    APIResponse checkAreaDomainGroup( Long areaId, Integer groupType,String tab);

    APIResponse importDomains(DomainImportDTO domainImportDTO, String userName);

    APIResponse getDomainTreeV2(DomainVO domainVO);

    APIResponse findDomainExist(Long domainGroupId, String tab);
}
