package com.panda.multiterminalinteractivecenter.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.entity.DomainProgramRelation;
import com.panda.multiterminalinteractivecenter.service.IDomainProgramService;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.DomainProgramVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 域名切换方案管理
 *
 * @author ifan
 * @date 2022/7/2 17:34:05
 */
@RestController
@RequestMapping("/domainProgram/program")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainProgramController {

    private final IDomainProgramService programService;


    /**
     * 域名切换方案查询
     *
     * @param request
     * @param domainProgramVO
     * @return
     */
    @PostMapping("/list")
    public APIResponse queryList(HttpServletRequest request, @RequestBody DomainProgramVO domainProgramVO) {
        if (StringUtils.isBlank(domainProgramVO.getTab())) domainProgramVO.setTab("ty");
        return programService.queryList(domainProgramVO);
    }

    /**
     * 新建域名切换方案
     *
     * @param request
     * @param domainProgramVO
     * @return
     */
    @PostMapping(value = "/save")
    public APIResponse save(HttpServletRequest request, @RequestBody DomainProgramVO domainProgramVO) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        if (StringUtils.isBlank(domainProgramVO.getTab())) domainProgramVO.setTab("ty");
        if(!"ty".equals(domainProgramVO.getTab()) && StringUtils.isBlank(domainProgramVO.getMerchantGroupId())) {
            return APIResponse.returnFail("商户组不能为空！");
        }
        return programService.save(domainProgramVO, userName, request);
    }

    /**
     * 修改域名切换方案
     *
     * @param request
     * @param domainProgramVO
     * @return
     */
    @PostMapping(value = "/update")
    public APIResponse update(HttpServletRequest request, @RequestBody DomainProgramVO domainProgramVO) {
        if (domainProgramVO.getId() == null || domainProgramVO.getGroupType()==null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        if (StringUtils.isBlank(domainProgramVO.getTab())) domainProgramVO.setTab("ty");
        if(!"ty".equals(domainProgramVO.getTab()) && StringUtils.isBlank(domainProgramVO.getMerchantGroupId())) {
            return APIResponse.returnFail("商户组不能为空！");
        }
        return programService.update(domainProgramVO, userName, request);
    }

    /**
     * 删除域名切换方案
     *
     * @param domainProgramVO
     * @return
     */
    @PostMapping(value = "/deleteById")
    public APIResponse deleteById(HttpServletRequest request, @RequestBody DomainProgramVO domainProgramVO) {
        if (domainProgramVO.getId() == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        return programService.deleteById(domainProgramVO, request);
    }

    /**
     * 查询方案集合
     * @return
     */
    @PostMapping("/findProgramList")
    public APIResponse findProgramList(@RequestBody DomainProgramVO domainProgramVO) {
        return programService.findProgramList(domainProgramVO);
    }


    /**
     * 批量保存方案域名组关系数据
     *
     * @param request
     * @param domainProgramRelation
     * @return
     */
    @PostMapping(value = "/batchSaveDomainProgramRelation")
    public APIResponse batchSaveDomainProgramRelation(HttpServletRequest request, @RequestBody DomainProgramRelation domainProgramRelation) {
        if (Objects.isNull(domainProgramRelation)) {
            APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        if (CollectionUtil.isEmpty(domainProgramRelation.getConfig())) {
            APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        return programService.batchSaveDomainProgramRelation(domainProgramRelation, userName, request);
    }

    /**
     * 查询方案详情
     * @return
     */
    @PostMapping("/findProgramDetail")
    public APIResponse findProgramDetail(@RequestBody DomainProgramVO domainProgramVO) {
        log.info("方案详情查询/domainProgram/program/findProgramDetail.param:{}",domainProgramVO);
        if (StrUtil.isBlank(domainProgramVO.getProgramIds())) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        return programService.findProgramDetail(domainProgramVO);
    }
}
