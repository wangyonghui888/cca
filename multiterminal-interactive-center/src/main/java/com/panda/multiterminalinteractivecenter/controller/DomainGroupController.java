package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.dto.DomainImportDTO;
import com.panda.multiterminalinteractivecenter.enums.ResponseEnum;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import com.panda.multiterminalinteractivecenter.service.IDomainGroupService;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 域名组管理
 * @author ifan
 * @date 2022/7/2 17:34:05
 */
@RestController
@RequestMapping("/domainGroup/group")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainGroupController {

    private final IDomainGroupService domainGroupService;

    /**
     * 域名组查询
     * @param request
     * @param domainGroupVO
     * @return
     */
    @PostMapping("/list")
    public APIResponse queryList(HttpServletRequest request, @RequestBody DomainGroupVO domainGroupVO) {
        if (StringUtils.isBlank(domainGroupVO.getTab())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return domainGroupService.queryList(domainGroupVO);
    }

    /**
     * 新建域名组
     * @param request
     * @param domainGroupVO
     * @return
     */
    @PostMapping(value = "/save")
    public APIResponse save(HttpServletRequest request, @RequestBody DomainGroupVO domainGroupVO){

        if (StringUtils.isBlank(domainGroupVO.getTab())) {
            domainGroupVO.setTab("ty");
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));

        return domainGroupService.save(domainGroupVO,userName, request);
    }

    /**
     * 修改域名组
     * @param request
     * @param domainGroupVO
     * @return
     */
    @PostMapping(value = "/update")
    public APIResponse update(HttpServletRequest request, @RequestBody DomainGroupVO domainGroupVO){
        if (domainGroupVO.getId() == null || StringUtils.isBlank(domainGroupVO.getTab())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));

        return domainGroupService.update(domainGroupVO,userName, request);
    }

    /**
     * 删除域名组
     * @param id
     * @return
     */
    @PostMapping(value = "/deleteById/{id}")
    public APIResponse deleteById(HttpServletRequest request, @PathVariable Long id){
        if (id == null){
            return APIResponse.returnFail("删除域名组：参数异常！");
        }
        return domainGroupService.deleteById(id, request);
    }


    /**
     * 方案下域名组查询
     * @param request
     * @param programId
     * @param groupType
     * @return
     */
    @GetMapping("/findProgramDownDomainGroupList")
    public APIResponse findProgramDownDomainGroupList(HttpServletRequest request,
                                                      @RequestParam(value = "programId") Long programId,
                                                      @RequestParam(value = "groupType",required = false) Integer groupType,
                                                      @RequestParam(value = "tab",required = false) String tab) {
        log.info("方案下域名组查询/domainGroup/group/findProgramDownDomainGroupList");
        return domainGroupService.findProgramDownDomainGroupList(programId,groupType,tab);
    }

    /**
     * 获取域名数据
     * @param domainVO
     * @return
     */
    @PostMapping("/getDomainTree")
    public APIResponse getDomainTree(@RequestBody DomainVO domainVO) {
        if(domainVO==null || StringUtils.isBlank(domainVO.getTab())){
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        return domainGroupService.getDomainTree(domainVO);
    }

    /**
     * 获取域名数据
     * 这里会将已经排除的商户组数据返回
     * @param domainVO
     * @return
     */
    @PostMapping("V2/getDomainTree")
    public APIResponse getDomainTreeV2(@RequestBody DomainVO domainVO) {
        if(domainVO==null || StringUtils.isBlank(domainVO.getTab())){
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        return domainGroupService.getDomainTreeV2(domainVO);
    }

    /**
     * 校验区域是否属于同分组类型
     * @param request
     * @param areaId
     * @param groupType
     * @return
     */
    @GetMapping("/checkAreaDomainGroup")
    public APIResponse checkAreaDomainGroup(HttpServletRequest request,
                                            @RequestParam(value = "areaId") Long areaId,
                                            @RequestParam(value = "groupType") Integer groupType,
                                            @RequestParam(value = "tab",required = false) String tab
                                            ) {
        log.info("校验区域是否属于同分组类型/domainGroup/group/checkAreaDomainGroup");
        if(StringUtils.isBlank(tab)) tab = "ty";
        return domainGroupService.checkAreaDomainGroup(areaId,groupType,tab);
    }


    /**
     * 域名组导入域名
     */
    @PostMapping(value = "/import/domain")
    public APIResponse importDomains(HttpServletRequest request, @RequestBody DomainImportDTO domainImportDTO){

        if (StringUtils.isBlank(domainImportDTO.getTab())) {
            domainImportDTO.setTab("ty");
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));

        return domainGroupService.importDomains(domainImportDTO,userName);
    }

    /**
     * 域名组查询所有域名 是否存在其它组
     */
    @GetMapping(value = "/find/domain/exist")
    public APIResponse findDomainExist(HttpServletRequest request, @RequestParam(required = false) Long domainGroupId,@RequestParam String tab){

        if (StringUtils.isBlank(tab)) {
            tab = TabEnum.TY.getName();
        }
        return domainGroupService.findDomainExist(domainGroupId,tab);
    }

}
