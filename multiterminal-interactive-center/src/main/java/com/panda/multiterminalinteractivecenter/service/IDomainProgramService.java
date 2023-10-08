package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.DomainProgramRelation;
import com.panda.multiterminalinteractivecenter.vo.DomainProgramVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :  ifan
 * @Description :域名切换方案接口
 * @Date: 2022-07-02
 * --------  ---------  --------------------------
 */
public interface IDomainProgramService {

    /**
     * 域名切换方案查询
     * @param domainProgramVO
     * @return
     */
    APIResponse queryList(DomainProgramVO domainProgramVO);

    /**
     * 新建域名切换方案
     * @param domainProgramVO
     * @param userName
     * @return
     */
    APIResponse save(DomainProgramVO domainProgramVO, String userName, HttpServletRequest request);

    /**
     * 修改域名切换方案
     * @param domainProgramVO
     * @return
     */
    APIResponse update(DomainProgramVO domainProgramVO,String userName, HttpServletRequest request);

    /**
     * 删除域名切换方案
     * @param domainProgramVO
     */
    APIResponse deleteById(DomainProgramVO domainProgramVO, HttpServletRequest request);

    /**
     * 查询方案集合
     * @return
     */
    APIResponse findProgramList(DomainProgramVO domainProgramVO);

    /**
     * 批量保存方案域名组关系数据
     * @param domainProgramRelation
     * @param userName
     * @return
     */
    APIResponse batchSaveDomainProgramRelation(DomainProgramRelation domainProgramRelation, String userName, HttpServletRequest request);

    /**
     * 查询方案详情
     * @return
     */
    APIResponse findProgramDetail(DomainProgramVO domainProgramVO);
}
