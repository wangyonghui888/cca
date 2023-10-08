package com.panda.sport.merchant.manage.controller;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.dto.ApiMerchantGroupVO;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.manage.service.IDomainService;
import com.panda.sport.merchant.manage.service.IOssDomainService;
import com.panda.sport.merchant.manage.service.impl.OssDomainServiceImpl;
import com.panda.sport.merchant.manage.service.impl.WebSocketService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/5/27 17:34:05
 */
@RestController
@RequestMapping("/manage/merchantDomain")
@Slf4j
@Validated
public class MerchantDomainController {
    @Resource
    private IOssDomainService ossDomainService;

    @Autowired
    private IDomainService domainService;
    @Autowired
    private WebSocketService webSocketService;

    private String keys = "panda1234_1234ob";

    @PostMapping("/ossUpload")
    @AuthRequiredPermission("Domain:Manage:ossUpload")
    public Response<Object> ossUpload(@RequestBody JSONObject obj) {
        JSONObject gbb = obj.get("GAB", JSONObject.class);
        List<String> gbbApi = gbb.get("api", List.class);
        for (String str : gbbApi) {
            String api = OssDomainServiceImpl.decryptAES(str, keys);
            boolean bool = domainService.checkDomainByGroup(api, "b");
            if (!bool) {
                return Response.returnFail(api + "不存在商户组GAB中");
            }
        }

        JSONObject gas = obj.get("GAS", JSONObject.class);
        List<String> gasApi = gas.get("api", List.class);
        for (String str : gasApi) {
            String api = OssDomainServiceImpl.decryptAES(str, keys);
            boolean bool = domainService.checkDomainByGroup(api, "s");
            if (!bool) {
                return Response.returnFail(api + "不存在商户组GAS中");
            }
        }

        JSONObject gay = obj.get("GAY", JSONObject.class);
        List<String> gayApi = gay.get("api", List.class);
        for (String str : gayApi) {
            String api = OssDomainServiceImpl.decryptAES(str, keys);
            boolean bool = domainService.checkDomainByGroup(api, "y");
            if (!bool) {
                return Response.returnFail(api + "不存在商户组GAY中");
            }
        }

        JSONObject gaCommon = obj.get("GACOMMON", JSONObject.class);
        List<String> gaCommonApi = gaCommon.get("api", List.class);
        for (String str : gaCommonApi) {
            String api = OssDomainServiceImpl.decryptAES(str, keys);
            boolean bool = domainService.checkDomainByGroup(api, "common");
            if (!bool) {
                return Response.returnFail(api + "不存在商户组GACOMMON中");
            }
        }
        boolean success = ossDomainService.ossUpload(obj);
        if (success) {
            return Response.returnSuccess("域名更新成功");
        }
        return Response.returnFail("域名更新失败");
    }

    @GetMapping("/getDomainConfig")
    public void getDomainConfig(HttpServletResponse response) {
        log.info("/manage/merchantDomain/getDomainConfig");
        ossDomainService.getDomainConfig(response);
    }

    @PostMapping("/deleteDomain")
    //@AuthRequiredPermission("Domain:Manage:deleteDomain")
    public Response deleteDomain(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        log.info("删除 /deleteDomain param = {}", id);
        Integer userId = SsoUtil.getUserId(request);
        domainService.deleteDomain(userId, id, IPUtils.getIpAddr(request), request);
        return Response.returnSuccess("删除成功");
    }

    @PostMapping("/resetDomain")
    //@AuthRequiredPermission("Domain:Manage:deleteDomain")
    public Response resetDomain(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        log.info("恢复成功 /deleteDomain param = {}", id);
        Integer userId = SsoUtil.getUserId(request);
        domainService.resetDomain(userId, id);
        return Response.returnSuccess("恢复成功");
    }

    @PostMapping("/deleteDomainAll")
    //@AuthRequiredPermission("Domain:Manage:deleteDomain")
    public Response deleteDomain(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("域名查询 /deleteDomain param = {}", JSON.toJSONString(domainVo));
        Integer userId = SsoUtil.getUserId(request);
        domainService.deleteDomainAll(userId, domainVo, IPUtils.getIpAddr(request), request);
        return Response.returnSuccess("删除成功");
    }

    @PostMapping("/queryDomain")
    //@AuthRequiredPermission("Domain:Manage:queryDomain")
    public Response queryDomain(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("域名查询 /queryDomain param = {}", JSON.toJSONString(domainVo));
        Response response = domainService.queryList(domainVo);
        return response;
    }

    @PostMapping(value = "/importDomain")
    public Response importEmp(HttpServletRequest request, MultipartFile file) {
        Long starTime = System.currentTimeMillis();
        log.info("域名导入开始  /importDomain-------------{}", starTime);
        Integer userId = SsoUtil.getUserId(request);
        Response response = domainService.importDomain(userId, file);
        log.info("域名导入结束  /importDomain-------------耗时{}", System.currentTimeMillis() - starTime);
        return response;
    }


    @PostMapping(value = "/saveDomain")
    public Response saveDomain(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        Integer userId = SsoUtil.getUserId(request);
        return domainService.saveDomain(userId, domainVo, request);
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    @PostMapping("/queryAnimation")
    public Response queryAnimation(HttpServletRequest request) {
        Response response = domainService.queryAnimation();
        return response;
    }

    /**
     * 更新
     *
     * @param request
     * @param url
     * @return
     */
    @PostMapping("/updateAnimation")
    public Response updateAnimation(HttpServletRequest request, @RequestParam(value = "url") String url) {
        Response response = domainService.updateAnimation(request,url);
        return response;
    }

    /**
     * 清除动画缓存
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/deleteAniCache")
    public Response deleteAniCache(HttpServletRequest request) {
        Response response = domainService.deleteAniCache(request);
        return response;
    }

    @GetMapping("/checkSingleDomain")
    public Response checkSingleDomain(HttpServletRequest request, @RequestParam(value = "domain") String domain) {
        webSocketService.checkSingleDomain(domain);
        return Response.returnSuccess();
    }

    /**
     * /manage/merchantDomain/updateMerchantDomain
     * 手动切换域名核心逻辑
     */
    @PostMapping("/updateMerchantDomain")
    public Response updateMerchantDomain(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        if (domainVo.getMerchantGroupId() == null || StringUtils.isEmpty(domainVo.getDomainName())) {
            return Response.returnFail("参数 groupId 或 domain 不能为空");
        }
        Integer userId = null;
        try {
            userId = SsoUtil.getUserId(request);
        } catch (Exception e) {
            log.warn("获取用户异常！");
        }

        return domainService.updateMerchantDomain(userId, domainVo.getOldDomain(), domainVo.getDomainName(), domainVo.getMerchantGroupId(), IPUtils.getIpAddr(request),domainVo.getDomainType());
    }


    /**
     * api商户域名分组
     *
     * @return
     */
    @PostMapping("/apiMerchantDomainList")
    //@AuthRequiredPermission("Merchant:Manage:merchantDomain:apiMerchantDomainList")
    public Response apiMerchantDomainList() {

        try {
            List<ApiMerchantGroupVO> apiMerchantGroupVOList = domainService.apiMerchantGroupList();
            return Response.returnSuccess(apiMerchantGroupVOList);
        } catch (Exception e) {
            log.error("/manage/merchantDomain/apiMerchantDomainList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 添加Api商户分组
     *
     * @return
     */
    @PostMapping("/addApiMerchantGroup")
    public Response addApiMerchantGroup(HttpServletRequest request,
                                        @RequestBody ApiMerchantGroupVO apiMerchantGroupVO) {

        log.info("/manage/merchantDomain/addApiMerchantGroup" + apiMerchantGroupVO);
        try {
            if (apiMerchantGroupVO == null || apiMerchantGroupVO.getGroupName() == null ||
                    apiMerchantGroupVO.getGroupCode() == null || apiMerchantGroupVO.getDomainName() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return domainService.addApiMerchantGroup(apiMerchantGroupVO);
        } catch (Exception e) {
            log.error("/manage/merchantDomain/addApiMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 更新Api商户分组
     *
     * @param request
     * @param apiMerchantGroupVO
     * @return
     */
    @PostMapping(value = "/updateApiMerchantGroup")
    public Response updateApiMerchantGroup(HttpServletRequest request,
                                           @RequestBody ApiMerchantGroupVO apiMerchantGroupVO) {
        log.info("/manage/merchantDomain/updateApiMerchantGroup" + apiMerchantGroupVO);
        try {
            if (apiMerchantGroupVO == null || apiMerchantGroupVO.getId() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return domainService.updateApiMerchantGroup(apiMerchantGroupVO);
        } catch (Exception e) {
            log.error("/manage/merchantDomain.updateApiMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 删除Api商户分组
     *
     * @param request
     * @param apiMerchantGroupVO
     * @return
     */
    @PostMapping(value = "/deleteApiMerchantGroup")
    public Response deleteApiMerchantGroup(HttpServletRequest request,
                                           @RequestBody ApiMerchantGroupVO apiMerchantGroupVO) {
        log.info("/manage/merchantDomain/deleteApiMerchantGroup" + apiMerchantGroupVO);
        try {
            if (apiMerchantGroupVO == null || apiMerchantGroupVO.getGroupCode() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return domainService.deleteApiMerchantGroup(apiMerchantGroupVO);
        } catch (Exception e) {
            log.error("/manage/merchantDomain.deleteApiMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/queryFrontendMerchantDomain")
    public APIResponse<Object> queryFrontendMerchantDomain(@RequestBody FrontendMerchantGroupDomainPO frontendMerchantGroup){
        try {
            return domainService.queryFrontendMerchantDomain(frontendMerchantGroup);
        }catch (Exception e){
            log.error("FrontendDomainController.queryFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryVideoMerchantDomain")
    public APIResponse<Object> queryVideoMerchantDomain(@RequestBody VideoMerchantGroupDomainPO frontendMerchantGroup){
        try {
            return domainService.queryVideoMerchantDomain(frontendMerchantGroup);
        }catch (Exception e){
            log.error("FrontendDomainController.queryFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

}
