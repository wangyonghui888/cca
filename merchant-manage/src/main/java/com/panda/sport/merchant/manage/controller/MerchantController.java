package com.panda.sport.merchant.manage.controller;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.dto.DomainChangeDTO;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.permission.AuthSeaMoonKey;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.manage.entity.vo.MerchantGroupDomainVO;
import com.panda.sport.merchant.manage.service.MerchantGroupService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.MerchantService;
import com.panda.sport.merchant.manage.util.JWTUtil;
import com.panda.sport.merchant.manage.util.RegularUtil;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@RestController
@RequestMapping("/manage/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private MerchantGroupService merchantGroupService;

    private static final String decoty = "/opt/merchant/upload/";

    @Value("${merchant.code:622285}")
    private String merchantCode;

    @PostMapping("/create")
    @AuthRequiredPermission("Merchant:Manage:add")
    public Response create(HttpServletRequest request, @RequestBody @Validated MerchantVO merchantVO) {
        log.info("manage/merchant/create:" + merchantVO);
        if (merchantVO == null) {
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.createMerchant(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.create,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/update")
    @AuthRequiredPermission("Merchant:Manage:update")
    public Response update(HttpServletRequest request, @RequestBody @Validated MerchantVO merchantVO) {
        log.info("manage/merchant/update:" + merchantVO);
        if (merchantVO == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        try {
            merchantVO.setAdminPassword(null);
            merchantVO.setMerchantAdmin(null);
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.updateMerchant(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request),null ==merchantVO.getAgentLevel()? "" : merchantVO.getAgentLevel().toString());
        } catch (Exception e) {
            log.error("MerchantController.update,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 更新商户预留Ip字段
     *
     * @param request
     * @param merchantVO
     * @return
     */
    @AuthSeaMoonKey("商户管理/设置IP白名单")
    @PostMapping("/update/ip")
    @AuthRequiredPermission("Merchant:Manage:update:ip")
    public Response updateIp(String tokenCode, HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("/manage/merchant/update/ip:" + merchantVO);
        if (merchantVO == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        // 验证必传参数
        String whiteIp = merchantVO.getWhiteIp();
        if (StringUtils.isBlank(whiteIp) || StringUtils.isBlank(merchantVO.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String[] ips = whiteIp.split(",");
        if (!RegularUtil.isIpv4(ips)) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        // 验证通过处理业务
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.updateMerchantWhiteIp(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.update,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 更新商户虚拟玩法
     *
     * @param request
     * @param merchantVO
     * @return
     */
    @PostMapping("/update/openVrSport")
    //@AuthRequiredPermission("Merchant:Manage:update:openVrSport")
    public Response updateVrSport(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("/manage/merchant/update/openVrSport:" + merchantVO);
        if (merchantVO == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        // 验证必传参数
        Integer openVrSport = merchantVO.getOpenVrSport();
        if (openVrSport == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.updateMerchantVrSport(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.update,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/update/openESport")
    //@AuthRequiredPermission("Merchant:Manage:update:openESport")
    public Response openESport(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("/manage/merchant/update/openESport:" + merchantVO);
        if (merchantVO == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        // 验证必传参数
        Integer openESport = merchantVO.getOpenEsport();
        if (openESport == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.updateMerchantESport(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.openESport,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/update/openVideo")
    //@AuthRequiredPermission("Merchant:Manage:update:openESport")
    public Response openVideo(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("/manage/merchant/update/openVideo:" + merchantVO);
        if (merchantVO == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        // 验证必传参数
        Integer openVideo = merchantVO.getOpenVideo();//视频开关
        if (openVideo == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.updateMerchantOpenVideo(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.openVideo,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 更新商户提前结算开关
     */
    @PostMapping("/update/settleSwitchAdvance")
    public Response<ResponseEnum> settleSwitchAdvance(HttpServletRequest request, @RequestBody MerchantSettleVO merchantSettleVO) {
        log.info("/manage/merchant/update/settleSwitchAdvance:" + merchantSettleVO);
        Assert.notNull(merchantSettleVO.getId(), "ID参数不能为空");
        //Assert.notNull(merchantSettleVO.getSettleSwitchAdvance(), "提前结算开关参数settleSwitchAdvance不能为空");
        try {
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setId(merchantSettleVO.getId());
            String sportId = merchantSettleVO.getSportId();
            if("1".equals(sportId)) {
                merchantVO.setSettleSwitchAdvance(merchantSettleVO.getSettleSwitchAdvance());
            }else {
                merchantVO.setSettleSwitchBasket(merchantSettleVO.getSettleSwitchBasket());
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.updateMerchantSettleSwitchAdvance(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request),sportId);
        } catch (Exception e) {
            log.error("MerchantController.settleSwitchAdvance,exception:", e);
            return Response.returnFail(e.getMessage());
        }
    }

    /**
     * 更新商户提前结算开关
     */
    @PostMapping("/update/isTestOrExternal")
    public Response<ResponseEnum> isTestOrExternal(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("/manage/merchant/update/settleSwitchAdvance:" + merchantVO);
        Assert.notNull(merchantVO.getId(), "ID参数不能为空");
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.isTestOrExternal(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.settleSwitchAdvance,exception:", e);
            return Response.returnFail(e.getMessage());
        }
    }

    /**
     * 更新商户ISAPP
     */
    @PostMapping("/update/isApp")
    public Response<ResponseEnum> isApp(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("/manage/merchant/update/isApp:" + merchantVO);
        Assert.notNull(merchantVO.getId(), "ID参数不能为空");
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.isApp(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.settleSwitchAdvance,exception:", e);
            return Response.returnFail(e.getMessage());
        }
    }

    @GetMapping(value = "/list")
    @AuthRequiredPermission("Merchant:Manage:list")
    public Response list(HttpServletRequest request, @RequestParam(value = "merchantName", required = false) String merchantName,
                         @RequestParam(value = "status", required = false) Integer status,
                         @RequestParam(value = "merchantCode", required = false) String merchantCode,
                         @RequestParam(value = "merchantCodes", required = false) String merchantCodes,
                         @RequestParam(value = "agentLevel", required = false) Integer agentLevel,
                         @RequestParam(value = "parentName", required = false) String parentName,
                         @RequestParam(value = "merchantTag", required = false) Integer merchantTag,
                         @RequestParam(value = "merchantGroupId", required = false) String merchantGroupId,
                         @RequestParam(value = "domainGroupCode", required = false) String domainGroupCode,
                         @RequestParam(value = "transferMode", required = false) Integer transferMode,
                         @RequestParam(value = "isApp", required = false) Integer isApp,
                         @RequestParam(value = "isTest", required = false) Integer isTest,
                         @RequestParam(value = "backendSwitch", required = false) Integer backendSwitch,
                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                         @RequestParam(value = "pageNum") Integer pageNum,
                         @RequestParam(value = "sort", required = false) String sort,
                         @RequestParam(value = "orderBy", required = false) String orderBy) {
        log.info("/manage/merchant/list:merchantName:" + merchantName + ",merchantCode:" + merchantCode + ",merchantCodes:" + merchantCodes + ",status:" + status +
                ",pageSize:" + pageSize + ",pageIndex:" + pageNum + ",sort:" + sort + ",orderBy:" + orderBy);
        MerchantVO merchantVO = new MerchantVO();
        try {
            merchantVO.setMerchantName(merchantName);
            merchantVO.setMerchantCode(merchantCode);
            merchantVO.setStatus(status);
            if (merchantGroupId != null) {
                merchantVO.setMerchantGroupId(Long.valueOf(merchantGroupId));
            }
            if (isTest != null) {
                merchantVO.setIsTest(isTest);
            }
            if (backendSwitch != null) {
                merchantVO.setBackendSwitch(backendSwitch);
            }
            if (domainGroupCode != null) {
                merchantVO.setDomainGroupCode(domainGroupCode);
            }
            if (transferMode != null) {
                merchantVO.setTransferMode(transferMode);
            }
            if (isApp != null) {
                merchantVO.setIsApp(isApp);
            }
            merchantVO.setAgentLevel(agentLevel);
            merchantVO.setParentName(parentName);
            merchantVO.setPageSize(pageSize);
            merchantVO.setMerchantTag(merchantTag);
            merchantVO.setPageIndex(pageNum);
            merchantVO.setSort(sort);
            merchantVO.setOrderBy(orderBy);
            if (!StringUtil.isBlankOrNull(merchantCodes)) {
                try {
                    List<String> list = Arrays.asList(merchantCodes.split(","));
                    merchantVO.setMerchantCodes(list);
                } catch (Exception ex) {
                    log.error("MerchantController.list,merchantCodes change fail exception:" + merchantVO, ex);
                    return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
                }
            }
            return pageNum == null ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    merchantService.selectList(merchantVO);
        } catch (Exception e) {
            log.error("MerchantController.list,exception:" + merchantVO, e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/queryMerchantListByParams")
    List<MerchantVO> queryMerchantListByParams(@RequestBody JSONObject param){
        return merchantService.queryMerchantListByParams(param);
    }

    @PostMapping("/queryMerchantSimpleListByParams")
    List<MerchantSimpleVO> queryMerchantSimpleListByParams(@RequestBody JSONObject param){
        log.info("queryMerchantSimpleListByParams param = {}", JSON.toJSONString(param));
        return merchantService.queryMerchantSimpleListByParams(param);
    }

    /**
     * 商户信息导出
     */
    @RequestMapping(value = "/merchantInfoExport")
    public Response merchantInfoExport(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("merchantInfoExport param = {}", JSON.toJSONString(merchantVO));
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            if (merchantVO.getAgentLevel() == null) {
                throw new NullPointerException("agentLevel不能为空");
            }
            merchantService.merchantInfoExport(request, merchantVO, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("MerchantController.merchantInfoExport,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }



    /**
     * 商户信息导出
     */
    @RequestMapping(value = "/merchantInfoKanaCodeExport")
    public Response merchantInfoKanaCodeExport(HttpServletRequest request, @RequestBody MerchantVO merchantVO) {
        log.info("merchantInfoKanaCodeExport param = {}", JSON.toJSONString(merchantVO));
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            if (merchantVO.getAgentLevel() == null) {
                throw new NullPointerException("agentLevel不能为空");
            }
            merchantService.merchantInfoKanaCodeExport(request, merchantVO, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("MerchantController.merchantInfoKanaCodeExport,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }

    @GetMapping(value = "/{id:.*}")
    @AuthRequiredPermission("Merchant:Manage:detail")
    public Response detail(HttpServletRequest request, @PathVariable(value = "id") String id) {
        try {
            log.info("/manage/merchant/id:" + id);
            return merchantService.merchantDetail(id);
        } catch (Exception e) {
            log.error("MerchantController.detail,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/updateMerchantStatus")
    @AuthRequiredPermission("Merchant:Manage:updateStatus")
    public Response<Object> updateMerchantStatus(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                                 @RequestParam(value = "status") String status) {
        log.info("/manage/merchant/updateMerchantStatus:" + merchantCode + "," + status);
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return StringUtils.isAnyEmpty(merchantCode, status) || (!"0".equals(status) && !"1".equals(status)) ?
                    Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    merchantService.updateMerchantStatus(merchantCode, status, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantStatus,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/updateMerchantBackendStatus")
    public Response<Object> updateMerchantBackendStatus(HttpServletRequest request,
                                                        @RequestParam(value = "merchantCode") String merchantCode,
                                                 @RequestParam(value = "status") String status) {
        log.info("/manage/merchant/updateMerchantBackendStatus:" + merchantCode + "," + status);
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return StringUtils.isAnyEmpty(merchantCode, status) || (!"0".equals(status) && !"1".equals(status)) ?
                    Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    merchantService.updateMerchantBackendStatus(merchantCode, status, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantBackendStatus,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 导入禁用商户
     * @param request
     * @param merchantInfoVo
     * @return
     */
    @PostMapping("/importMerchantStatus")
    @AuthRequiredPermission("Merchant:Manage:updateDisabled")
    public Response<Object> importMerchantStatus(HttpServletRequest request,  @RequestBody MerchantInfoVo merchantInfoVo) {
        log.info("/manage/merchant/importMerchantStatus:" + merchantInfoVo);
        try {

            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.importMerchantStatus(merchantInfoVo,SsoUtil.getUserId(request),language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantStatus,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/findMerchantInfo")
    // @AuthRequiredPermission("Merchant:Manage:updateStatus")
    public Response<Object> findMerchantInfo(HttpServletRequest request,  @RequestBody MerchantInfoVo merchantInfoVo) {
        log.info("/manage/merchant/findMerchantInfo:" + merchantInfoVo);
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.findMerchantInfo(merchantInfoVo,SsoUtil.getUserId(request),language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.findMerchantInfo,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }
    @GetMapping("/deleteSubAgent")
    //@AuthRequiredPermission("Merchant:Manage:updateMerchantStatus")
    public Response<Object> deleteSubAgent(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                           @RequestParam(value = "parentId") String parentId) {
        log.info("/manage/merchant/deleteSubAgent:" + merchantCode + "," + parentId);
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return StringUtils.isAnyEmpty(merchantCode, parentId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    merchantService.deleteSubAgent(merchantCode, parentId, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.deleteSubAgent,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/getMerchantLanguageList")
    public Response getMerchantLanguageList(HttpServletRequest request) {
        log.info("/manage/merchant/getMerchantLanguageList:");
        try {
            return merchantService.getMerchantLanguageList();
        } catch (Exception e) {
            log.error("MerchantController.getMerchantLanguageList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @AuthSeaMoonKey("代理商管理/设置管理员")
    @PostMapping("/admin/create")
    @AuthRequiredPermission("Merchant:Manage:createAdmin")
    @ApiOperation(value = "/manage/merchant/admin/create", notes = "对内商户后台-商户中心-商户管理-设置超级管理员")
    public Response createAdmin(String tokenCode, HttpServletRequest request, @RequestParam(value = "id") String id,
                                @RequestParam(value = "adminName") String adminName) {
        log.info("/manage/merchant/admin/create:" + id + "," + adminName);
        if (StringUtils.isAnyEmpty(id, adminName)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantPO merchantPO = merchantService.getMerchantById(id);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return merchantService.createAdmin(id, adminName, adminName + merchantPO.getMerchantCode(), SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request), merchantPO.getAgentLevel().toString());
    }

    @GetMapping("/updateMerchantFTPInfo")
    public Response updateMerchantFTPInfo(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                          @RequestParam(value = "ftpUser") String ftpUser,
                                          @RequestParam(value = "ftpPassword") String ftpPassword) {
        log.info("/manage/merchant/updateMerchantFTPInfo:" + merchantCode + "," + ftpUser);
        return StringUtils.isAnyEmpty(merchantCode, ftpUser, ftpPassword) ? Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY) :
                Response.returnSuccess(merchantService.updateMerchantFTPInfo(merchantCode, ftpUser, ftpPassword));
    }

    @AuthSeaMoonKey("商户管理/重置密码")
    @PostMapping("/password/update")
    @AuthRequiredPermission("Merchant:Manage:updateAdmin")
    public Response updateAdminPassword(String tokenCode, HttpServletRequest request, @RequestParam(value = "id") String id) {
        log.info("/manage/merchant/password/update:" + id);
        if (StringUtils.isAnyEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        try {
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setId(id);
            MerchantPO merchantPO = merchantService.getMerchantById(id);
            if (merchantPO == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            merchantVO.setMerchantCode(merchantPO.getMerchantCode());
            merchantVO.setMerchantAdmin(merchantPO.getMerchantAdmin());

            //2058需求，增加密码复杂程度，随机生成至少12位且必须含特殊字符+大小字母+数字
//            merchantVO.setAdminPassword(merchantPO.getMerchantAdmin() + merchantPO.getMerchantCode());
            String psw= CreatPswUtil.getPsw(12);//生成随机密码
            merchantVO.setAdminPassword(psw);//保存MD5加密码
            merchantVO.setPswCode(psw);//保存明码
            merchantVO.setResetPasswordSwitch(0);//重置密码0关

            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return  merchantService.updateMerchant(merchantVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request),"");
        } catch (Exception e) {
            log.error("MerchantController.update,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/getPassword")
    @AuthRequiredPermission("Merchant:Manage:getPassword")
    public Response getPassword(HttpServletRequest request) {
        log.info("/manage/merchant/getPassword");
        try {
            return Response.returnSuccess(CreateAdminPassword.getPassword());
        } catch (Exception e) {
            log.error("MerchantController.getPassword,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/queryKeyList", produces = "application/json;charset=utf-8")
    @AuthRequiredPermission("Merchant:Manage:key:list")
    public Response<PageInfo<TMerchantKey>> queryKeyList(HttpServletRequest request,
                                                         @RequestParam(value = "merchantName", required = false) String merchantName,
                                                         @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                                         @RequestParam(value = "parentId", required = false) String parentId,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        log.info("/manage/merchant/queryKeyList:" + merchantCode);
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantService.queryKeyList(merchantName, merchantCode, parentId, pageSize, pageNum, language);
        } catch (Exception e) {
            log.error("MerchantController.queryKeyList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/generateKey", produces = "application/json;charset=utf-8")
    @AuthRequiredPermission("Merchant:Manage:key:get")
    public Response<Object> generateKey(HttpServletRequest request) {
        log.info("/manage/merchant/generateKey:");
        try {
            return merchantService.generateKey();
        } catch (Exception e) {
            log.error("MerchantController.generateKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @AuthSeaMoonKey("商户管理/更新证书")
    @GetMapping(value = "/updateKey")
    @AuthRequiredPermission("Merchant:Manage:key:update")
    public Response<Object> updateKey(String tokenCode, HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                      @RequestParam(value = "key") String key, @RequestParam(value = "keyLabel") String keyLabel,
                                      @RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "endTime") String endTime) {
        //keyLabel 密钥标记,用于区分修改的是哪一个密钥(0为merchantKey,1为secondMerchantKey)
        log.info("/manage/merchant/updateKey:" + merchantCode + "," + key + "," + keyLabel);
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return StringUtils.isAnyEmpty(merchantCode, key, keyLabel, startTime, endTime) ?
                    Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    merchantService.updateKey(merchantCode, key, keyLabel, startTime, endTime, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/queryAgentCount")
    @AuthRequiredPermission("Merchant:Manage:home:queryAgentCount")
    public Response queryAgentCount(HttpServletRequest request) {
        log.info("merchant/queryAgentCount");
        try {
            return merchantService.queryAgentCount();
        } catch (Exception e) {
            log.error("MerchantController.agentLevel,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/admin/list")
    public Response adminList(HttpServletRequest request) {
        log.info("manage/merchant/admin/list");
        try {
            return merchantService.adminList();
        } catch (Exception e) {
            log.error("MerchantController.manage/merchant/admin/list,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/month/list")
    @AuthRequiredPermission("Merchant:Manage:home:list")
    public Response merchantMonth(HttpServletRequest request, @RequestParam(value = "agentLevel", required = false) Integer agentLevel) {
        log.info("merchant/month/list" + ",agentLevel:" + agentLevel);
        try {
            return merchantService.selectMonthList(agentLevel);
        } catch (Exception e) {
            log.error("MerchantController.list,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/img/upload")
    public Response<Object> uploadImg(@RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty() || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String contentType = multipartFile.getContentType();
        String root_fileName = multipartFile.getOriginalFilename();
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String res = encoder.encodeBuffer(multipartFile.getBytes()).trim();
        log.info("上传图片:name={},type={}", root_fileName, contentType);
        return Response.returnSuccess(res);
    }

    @PostMapping(value = "/file/upload")
    public Response<Object> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            Map<String, String> map = Maps.newHashMap();
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            map.put("fileName", fileName);
            String firstName = fileName.substring(0, fileName.lastIndexOf("."));
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            String path = decoty + firstName + CreateAdminPassword.getStr() + suffixName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            map.put("filePath", path);
            return Response.returnSuccess(map);
        } catch (IllegalStateException | IOException e) {
            log.error("上传的文件异常：", e);
        }
        return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
    }

    @GetMapping("/file/download")
    public File downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "fileName") String fileName,
                             @RequestParam(value = "filePath") String filePath) {
        if (fileName != null) {
            //设置文件路径
            File file = new File(filePath);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                    outputStream = response.getOutputStream();
                    response.setContentType("application/x-download");
                    String name = fileName.replace(decoty, "");
                    log.info("/file/download fileName=" + name);
                    name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
                    response.addHeader("Content-Disposition", "attachment;filename=" + name);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.flush();
                } catch (Exception e) {
                    log.error("下载文件异常：", e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            log.error("下载文件异常：", e);
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error("下载文件异常：", e);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 商户联想查询列表
     */
    @PostMapping(value = "/queryMerchantList")
    public Response queryMerchantList(HttpServletRequest request) {
        log.info("/manage/merchant/queryMerchantList");
        return merchantService.queryMerchantListByParam();
    }

    /**
     * 商户联想查询列表
     */
    @PostMapping(value = "/queryMerchantListTree")
    public Response queryMerchantListTree(HttpServletRequest request) {
        log.info("/manage/merchant/queryMerchantListTree");
        return merchantService.queryMerchantTree();
    }

    /**
     * 商户联想查询列表
     */
    @GetMapping(value = "/getMerchantListTree")
    public Response getMerchantListTree(HttpServletRequest request) {
        log.info("/manage/merchant/getMerchantListTree");
        return merchantService.getMerchantListTree();
    }

    @GetMapping(value = "/getMerchantCodeListTree")
    public Response getMerchantCodeListTree(HttpServletRequest request) {
        log.info("/manage/merchant/getMerchantCodeListTree");
        return merchantService.getMerchantCodeListTree();
    }

    @GetMapping(value = "/getMerchantNameListTree")
    public Response getMerchantNameListTree(HttpServletRequest request) {
        log.info("/manage/merchant/getMerchantNameListTree");
        return merchantService.getMerchantNameListTree();
    }

    /**
     * 测试使用
     *
     * @param request
     * @param merchantCode
     * @param userName
     * @param transferType
     * @param amount
     * @return
     */
    @PostMapping("/transfer")
    public Response<Object> transfer(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode
            , @RequestParam(value = "userName") String userName,
                                     @RequestParam(value = "transferType") Integer transferType,
                                     @RequestParam(value = "amount") Double amount) {
        log.info("/manage/merchant/transfer " + "merchantCode" + ":" + merchantCode + "username" + ":" + userName +
                ",transferType:" + transferType + ",amount:" + amount);
        try {
            return StringUtils.isAnyEmpty(merchantCode, userName) || transferType == null || amount == null ?
                    Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    merchantService.transfer(merchantCode, userName, transferType, amount);
        } catch (Exception e) {
            log.error("MerchantController.transfer,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 测试使用
     *
     * @param request
     * @param merchantCode
     * @param userName
     * @return
     */
    @PostMapping("/queryTransferList")
    public Response<Object> queryTransferList(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                              @RequestParam(value = "merchantCode") String merchantCode,
                                              @RequestParam(value = "pageSize") Integer pageSize,
                                              @RequestParam(value = "pageIndex") Integer pageIndex) {
        log.info("/manage/merchant/queryTransferList " + "username" + ":" + userName + ",merchantCode:" + merchantCode);
        try {
            return StringUtils.isAnyEmpty(userName, merchantCode) ?
                    Response.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                    merchantService.queryTransferList(userName, merchantCode, pageSize, pageIndex);
        } catch (Exception e) {
            log.error("MerchantController.queryTransferList,exception:", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/merchantDomainList")
    @AuthRequiredPermission("Merchant:Manage:domain:merchantDomainList")
    public Response merchantDomainList(HttpServletRequest request,
                                       @RequestParam(value = "merchantTag") Integer merchantTag,
                                       @RequestParam(value = "containsType") Integer containsType,
                                       @RequestParam(value = "containsStr", required = false) String containsStr,
                                       @RequestParam(value = "parentCode", required = false) String parentCode,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestParam(value = "pageNum") Integer pageNum) {
        log.info("/manage/merchant/merchantDomainList" + ",merchantTag:"
                + merchantTag + ",containsType:"
                + parentCode + ",parentCode:"
                + pageSize + ",pageSize:"
                + pageNum + ",pageNum:"
                + containsType + ",containsStr:" + containsStr);
        try {
            return merchantService.merchantDomainList(merchantTag, containsType, containsStr, parentCode, pageSize, pageNum);
        } catch (Exception e) {
            log.error("MerchantController.list,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/updateDomainList")
    @AuthRequiredPermission("Merchant:Manage:domain:updateDomainList")
    public Response updateDomainList(HttpServletRequest request,
                                     @RequestParam(value = "merchantTag", required = false) Integer merchantTag,
                                     @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                     @RequestParam(value = "parentCode", required = false) String parentCode,
                                     @RequestParam(value = "containsType", required = false) Integer containsType,
                                     @RequestParam(value = "containsStr", required = false) String containsStr,
                                     @RequestParam(value = "newDomainStr") String newDomainStr) {
        log.info("/manage/merchant/updateDomainList" + ",merchantTag:"
                + merchantTag + ",containsType:"
                + containsType + ",merchantCode:"
                + merchantCode + ",parentCode:"
                + parentCode + ",newDomainStr:"
                + newDomainStr + ",containsStr:" + containsStr);
        try {
            if (merchantCode == null && parentCode == null && (merchantTag == null || containsType == null) || newDomainStr == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            //记录日志
            return merchantService.updateDomainList(SsoUtil.getUserId(request), merchantTag, containsType, containsStr, newDomainStr, merchantCode, parentCode, language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.list,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 修改商户流水号
     *
     * @param request
     * @param merchantCode
     * @param kanaCode
     * @return
     */
    @PostMapping(value = "/updateKanaCode")
    @AuthRequiredPermission("Merchant:Manage:updateKanaCode")
    public Response updateKanaCode(HttpServletRequest request,
                                   @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                   @RequestParam(value = "kanaCode", required = false) String kanaCode) {
        log.info("/manage/merchant/updateKanaCode merchantCode:" + merchantCode + ",KanaCode:" + kanaCode);
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            //记录日志
            return merchantService.updateKanaCode(SsoUtil.getUserId(request), kanaCode, merchantCode, language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateKanaCode,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 修改商户流水号
     *
     * @param request
     * @param merchantCode
     * @param serialNumber
     * @return
     */
    @PostMapping(value = "/updateSerialNumber")
    @AuthRequiredPermission("Merchant:Manage:updateKanaCode")
    public Response updateSerialNumber(HttpServletRequest request,
                                       @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                       @RequestParam(value = "serialNumber", required = false) Integer serialNumber) {
        log.info("/manage/merchant/updateSerialNumber merchantCode:" + merchantCode + ",serialNumber:" + serialNumber);
        try {
            if (merchantCode == null && serialNumber == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            //记录日志
            return merchantService.updateSerialNumber(SsoUtil.getUserId(request), serialNumber, merchantCode, language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateSerialNumber,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @AuthSeaMoonKey("域名管理/批量刷新缓存")
    @PostMapping(value = "/domain/cleanMerchant")
    @AuthRequiredPermission("Merchant:Manage:domain:cleanMerchant")
    public Response cleanMerchant(String tokenCode, HttpServletRequest request,
                                  @RequestParam(value = "merchantTag", required = false) Integer merchantTag,
                                  @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                  @RequestParam(value = "parentCode", required = false) String parentCode,
                                  @RequestParam(value = "containsType", required = false) Integer containsType,
                                  @RequestParam(value = "containsStr", required = false) String containsStr) {
        log.info("/manage/merchant/cleanMerchant" + ",merchantTag:"
                + merchantTag + ",containsType:"
                + containsType + ",merchantCode:"
                + merchantCode + ",parentCode:"
                + parentCode + ",containsStr:" + containsStr);
        try {
            if (merchantCode == null && parentCode == null && (merchantTag == null || containsType == null)) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return merchantService.cleanMerchant(merchantTag, containsType, containsStr, merchantCode, parentCode);
        } catch (Exception e) {
            log.error("MerchantController.cleanMerchant,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping(value = "/domain/merchantAppDomainList")
    @AuthRequiredPermission("Merchant:Manage:domain:merchantDomainList")
    public Response merchantAppDomainList(HttpServletRequest request,
                                          @RequestParam(value = "merchantTag") Integer merchantTag,
                                          @RequestParam(value = "containsType") Integer containsType,
                                          @RequestParam(value = "containsStr", required = false) String containsStr,
                                          @RequestParam(value = "parentCode", required = false) String parentCode,
                                          @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                          @RequestParam(value = "pageNum") Integer pageNum) {
        log.info("/manage/merchant/merchantDomainList" + ",merchantTag:"
                + merchantTag + ",containsType:"
                + pageSize + ",pageSize:"
                + merchantCode + ",merchantCode:"
                + pageNum + ",pageNum:"
                + containsType + ",containsStr:" + containsStr);
        try {
            return merchantService.merchantAppDomainList(merchantTag, containsType, containsStr, parentCode,merchantCode, pageSize, pageNum);
        } catch (Exception e) {
            log.error("MerchantController.merchantAppDomainList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping(value = "/domain/updateAppDomainList")
    @AuthRequiredPermission("Merchant:Manage:domain:updateDomainList")
    public Response updateAppDomainList(HttpServletRequest request,
                                        @RequestParam(value = "merchantTag", required = false) Integer merchantTag,
                                        @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                        @RequestParam(value = "containsType", required = false) Integer containsType,
                                        @RequestParam(value = "parentCode", required = false) String parentCode,
                                        @RequestParam(value = "containsStr", required = false) String containsStr,
                                        @RequestParam(value = "newDomainStr") String newDomainStr) {
        log.info("/manage/merchant/updateDomainList" + ",merchantTag:"
                + merchantTag + ",containsType:"
                + containsType + ",merchantCode:"
                + merchantCode + ",newDomainStr:"
                + newDomainStr + ",containsStr:" + containsStr);
        try {
            if (merchantCode == null && (merchantTag == null || containsType == null) || newDomainStr == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            //记录日志
            return merchantService.updateAppDomainList(SsoUtil.getUserId(request), merchantTag, containsType, containsStr, newDomainStr, merchantCode, parentCode, language, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("MerchantController.updateAppDomainList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping(value = "/domain/createMerchantGroup")
    @AuthRequiredPermission("Merchant:Manage:domain:createMerchantGroup")
    public Response createMerchantGroup(HttpServletRequest request,
                                        @RequestBody MerchantGroupVO merchantGroupPO) {
        log.info("/manage/merchant/createMerchantGroup" + merchantGroupPO);
        try {
            if (merchantGroupPO == null || merchantGroupPO.getMerchantCodes() == null
                    || merchantGroupPO.getTimes() < 0 || merchantGroupPO.getTimeType() < 0
                    || merchantGroupPO.getGroupType() == null
            ) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            Response response = merchantGroupService.createMerchantGroup(merchantGroupPO);
            /**
             *  添加系统日志
             * */
            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_44;
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.SAVE_INFO;
            if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
                // 三端入口来的
                pageEnum = MerchantLogPageEnum.API_DOMAIN_SET;
                typeEnum = MerchantLogTypeEnum.SAVE;
                username = JWTUtil.getUsername(request.getHeader("token"));
            }
            String prefix = "";
            /*时间类型  1为分钟 2为小时 3为日  4为月*/
            if (merchantGroupPO.getTimeType() == 1) {
                prefix = "分钟";
            }else if (merchantGroupPO.getTimeType() == 2){
                prefix = "小时";
            }else if (merchantGroupPO.getTimeType() == 3){
                prefix = "日";
            }else if (merchantGroupPO.getTimeType() == 4){
                prefix = "月";
            }
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().addAll(Arrays.asList("域名防护商户组","商户分组库","详细商户","切换频率","域名提醒设置","启用/禁用"));
            vo.getAfterValues().addAll(Arrays.asList(merchantGroupPO.getGroupName(),merchantGroupPO.getGroupCode(),merchantGroupPO.getMerchantCodes().toString(),
                    merchantGroupPO.getTimes() + prefix, "低于" + merchantGroupPO.getAlarmNum(), "禁用"));
            merchantLogService.saveLog(pageEnum, typeEnum, vo,
                    MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username, null, merchantGroupPO.getGroupName(), merchantGroupPO.getGroupName(), request.getHeader("language"), IPUtils.getIpAddr(request));
            return response;
        } catch (Exception e) {
            log.error("MerchantController.createMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/updateMerchantGroup")
    @AuthRequiredPermission("Merchant:Manage:domain:updateMerchantGroup")
    public Response updateMerchantGroup(HttpServletRequest request,
                                        @RequestBody MerchantGroupVO merchantGroupPO) {
        log.info("/manage/merchant/updateMerchantGroup" + merchantGroupPO);
        try {
            if (merchantGroupPO == null || merchantGroupPO.getId() == null || merchantGroupPO.getGroupType() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return merchantGroupService.updateMerchantGroupNew(request,merchantGroupPO);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/deleteMerchantGroup")
    @AuthRequiredPermission("Merchant:Manage:domain:deleteMerchantGroup")
    public Response deleteMerchantGroup(HttpServletRequest request,
                                        @RequestBody MerchantGroupVO merchantGroupPO) {
        log.info("/manage/merchant/deleteMerchantGroup" + merchantGroupPO);
        try {
            if (merchantGroupPO == null || merchantGroupPO.getId() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return merchantGroupService.deleteMerchantGroup(merchantGroupPO);
        } catch (Exception e) {
            log.error("MerchantController.deleteMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/selectMerchantGroup")
    @AuthRequiredPermission("Merchant:Manage:domain:selectMerchantGroup")
    public Response selectMerchantGroup(HttpServletRequest request,
                                        @RequestBody MerchantGroupVO merchantGroupPO) {
        log.info("/manage/merchant/selectMerchantGroup" + merchantGroupPO);
        try {
            return Response.returnSuccess(merchantGroupService.selectMerchantGroup(merchantGroupPO));
        } catch (Exception e) {
            log.error("MerchantController.selectMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/updateMerchantGroupNum")
    public Response updateMerchantGroupNum(HttpServletRequest request,
                                           @RequestBody MerchantGroupPO merchantGroupPO) {
        log.info("/manage/merchant/selectMerchantGroup" + merchantGroupPO);
        try {
            merchantGroupService.updateGroupNum(request,merchantGroupPO);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantController.selectMerchantGroup,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "/domain/updateThirdEnable")
    public Response updateThirdEnable(HttpServletRequest request,
                                      @RequestBody MerchantGroupPO merchantGroupPO) {
        log.info("/manage/merchant/updateThirdEnable" + merchantGroupPO);
        try {
            if (merchantGroupPO.getThirdStatus() == 1) {
                merchantGroupService.updateThirdEnable("true");
            } else {
                merchantGroupService.updateThirdEnable("false");
            }

            /**
             *  添加系统日志
             * */
            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_44;
            if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
                // 三端入口来的
                pageEnum = MerchantLogPageEnum.API_DOMAIN_SET;
                try {
                    username = JWTUtil.getUsername(request.getHeader("token"));
                }catch (Exception e){
                    log.error("三端服务启用&禁用开关，获取用户名失败！，{}",e.getMessage(),e);
                }
            }
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("serverSwitch"));
            vo.getBeforeValues().add(merchantGroupPO.getThirdStatus() == 1 ? "关" : "开");
            vo.getAfterValues().add(merchantGroupPO.getThirdStatus() == 1 ? "开" : "关");
            merchantLogService.saveLog(pageEnum, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username, null, null, userId, language, ip);

            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantController.updateThirdEnable,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/domain/getThirdEnable")
    public Response getThirdEnable(HttpServletRequest request) {
        try {
            return Response.returnSuccess(merchantGroupService.getThirdEnable());
        } catch (Exception e) {
            log.error("MerchantController.getThirdEnable,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 获取业务组和公用组的分组域名
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping(value = "/domain/selectGroupDomain")
    public Response selectGroupDomain(HttpServletRequest request) {
        try {
            return merchantGroupService.selectGroupDomain();
        } catch (Exception e) {
            log.error("MerchantController.selectGroupDomain,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @AuthSeaMoonKey("商户管理/设置管理员")
    @PostMapping("/updateAdminUserName")
    @ApiOperation(value = "/manage/merchant/updateAdminUserName", notes = "对内商户后台-商户中心-商户管理-修改超级管理员名称")
    public Response updateAdminUserName(String tokenCode, HttpServletRequest request, @RequestBody @Valid AdminUserNameUpdateReqVO adminUserNameUpdateReqVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        String ip = IPUtils.getIpAddr(request);
        return merchantService.updateAdminUserName(request, adminUserNameUpdateReqVO, language, ip, adminUserNameUpdateReqVO.getAgentLevel());
    }


    @PostMapping("/updateOpenBill")
    @ApiOperation(value = "/manage/merchant/updateOpenBill", notes = "对内商户后台-商户中心-商户管理-修改是否生成帐单")
    public Response updateOpenBill(HttpServletRequest request, @RequestBody @Valid MerchantVO merchantVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        String ip = IPUtils.getIpAddr(request);
        return merchantService.updateOpenBill(merchantVO, language, ip);
    }

    /**
     * 获取风险商户列表
     *
     * @param request
     * @param
     * @return
     */
    @GetMapping(value = "/getMerchantRiskList")
    public Response getMerchantRiskList(HttpServletRequest request) {
        log.info("/manage/merchant/getMerchantRiskList:");
        try {
            return merchantService.getMerchantRiskList();
        } catch (Exception e) {
            log.error("MerchantController.getMerchantRiskList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 更新风险商户状态
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/updateMerchantRiskStatus")
    public Response<Object> updateMerchantRiskStatus(HttpServletRequest request, @RequestBody List<MerchantConfig> configList) {
        log.info("/manage/merchant/updateMerchantRiskStatus:" + configList);
        try {
            String userId = request.getHeader("user-id");
            String ip = IPUtils.getIpAddr(request);
            return merchantService.updateMerchantRiskStatus(configList, userId, ip);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantRiskStatus,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/updateApiDomainByMerchantCode")
    public int updateApiDomainByMerchantCode(HttpServletRequest request,
                                             @RequestParam(value = "newDomain") String newDomain,
                                             @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("/manage/merchant/updateApiDomainByMerchantCode" + ",newDomain:"
                + newDomain + ",merchantCode:"
                + merchantCode);
        try {
            return merchantService.updateApiDomainByMerchantCode(newDomain, merchantCode);
        } catch (Exception e) {
            log.error("MerchantController.updateApiDomainByMerchantCode,exception:", e);
            return 0;
        }
    }

    @GetMapping(value ="/updateMerchantDomainByMerchantCode")
    public int updateMerchantDomainByMerchantCode(HttpServletRequest request,
                                                     @RequestParam(value = "domainType") Integer domainType,
                                                     @RequestParam(value = "newDomain") String newDomain,
                                                     @RequestParam(value = "oldDomain") String oldDomain,
                                                     @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("/manage/merchant/updateMerchantDomainByMerchantCode"+ ",domainType:"
                + domainType  + ",newDomain:"
                + newDomain + ",merchantCode:"
                + merchantCode + ",oldDomain:"
                + oldDomain);
        return merchantService.updateMerchantDomainByMerchantCode(domainType,newDomain,oldDomain, merchantCode);
    }

    @PostMapping(value ="/updateMerchantDomainByMerchantCodes")
    public int updateMerchantDomainByMerchantCodes(HttpServletRequest request, @RequestBody JSONObject param) {
        log.info("/manage/merchant/updateMerchantDomainByMerchantCodes" +
                ""+ ",merchantGroupId:" + param.getString("merchantGroupId")
                + ",newDomain:" + param.getString("domainVOS")
        );
        JSONArray domainVos = param.getJSONArray("domainVOS");
        try {
            return merchantService.updateMerchantDomainByMerchantCodes(param.getString("merchantGroupId"),domainVos);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantDomainByMerchantCodes,exception:", e);
            return 0;
        }
    }

    @GetMapping(value ="/getMerchantByDomains")
    public List<?> getMerchantByDomains(HttpServletRequest request, @RequestParam(value = "domain") String domain) {
        log.info("/manage/merchant/getMerchantByDomains" +
                ""+ ",domain:" + domain
        );
        try {
            return merchantService.getMerchantByDomains(domain);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantDomainByMerchantCodes,exception:", e);
            return Lists.newArrayList();
        }
    }


    @GetMapping("/queryMerchantListByGroup")
    public List<?> queryMerchantListByGroup(HttpServletRequest request,
                                            @RequestParam(value = "merchantGroupId") String merchantGroupId, @RequestParam(value = "status", required = false) Integer status) {
        log.info("/manage/merchant/queryMerchantListByGroup" + ",merchantGroupId:"
                + merchantGroupId + ",status:" + status);
        try {
            return merchantService.queryMerchantListByGroup(merchantGroupId, status);
        } catch (Exception e) {
            log.error("MerchantController.queryMerchantListByGroup,exception:", e);
            return null;
        }
    }

    @GetMapping("/queryMerchantCountByGroup")
    public int queryMerchantCountByGroup(HttpServletRequest request,
                                            @RequestParam(value = "merchantGroupId") String merchantGroupId,
                                             @RequestParam(value = "status", required = false) Integer status) {
        log.info("/manage/merchant/queryMerchantCountByGroup" + ",merchantGroupId:"
                + merchantGroupId + ",status:" + status);
        try {
            return merchantService.queryMerchantCountByGroup(merchantGroupId, status);
        } catch (Exception e) {
            log.error("MerchantController.queryMerchantCountByGroup,exception:", e);
            return 0;
        }
    }

    @GetMapping("/updateMerchantGroupIdDefult")
    public void updateMerchantGroupIdDefult(HttpServletRequest request,
                                            @RequestParam(value = "id") String id) {
        log.info("/manage/merchant/updateMerchantGroupIdDefult" + ",merchantGroupId:"
                + id);
        try {
            merchantService.updateMerchantGroupIdDefult(id);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantGroupIdDefult,exception:", e);
        }
    }

    @GetMapping("/updateMerchantGroupId")
    public int updateMerchantGroupId(HttpServletRequest request,
                                     @RequestParam(value = "id") String id, @RequestParam(value = "groupCode") String groupCode, @RequestParam(value = "merchantCodes") List<String> merchantCodes) {
        log.info("/manage/merchant/updateMerchantGroupId" + ",id:"
                + id + ",groupCode:" + groupCode);
        try {
            return merchantService.updateMerchantGroupId(id, groupCode, merchantCodes);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantGroupId,exception:", e);
            return 0;
        }
    }


    @PostMapping(value = "/selectList")
    public List<?> list(@RequestBody MerchantVO merchantVO) {
        try {
            return merchantService.list(merchantVO);
        } catch (Exception e) {
            log.error("MerchantController.selectList,exception:" + merchantVO, e);
            return null;
        }

    }

    @GetMapping(value = "/merchantGroupInfoByGroupId")
    APIResponse merchantGroupInfoByGroupId(@RequestParam(value = "merchantGroupId") Long merchantGroupId){
        try {
            return merchantService.merchantGroupInfoByGroupId(merchantGroupId);
        } catch (Exception e) {
            log.error("MerchantController.merchantGroupInfoByGroupId,exception:" + merchantGroupId, e);
            return null;
        }
    }

    @GetMapping(value = "/load")
    APIResponse load(@RequestParam(value = "id")  Integer id){
        try {
            return merchantService.load(id);
        } catch (Exception e) {
            log.error("MerchantController.load,exception:" + id, e);
            return null;
        }
    }

    @GetMapping(value = "/checkMerchantDomainExistByDomainType")
    int checkMerchantDomainExistByDomainType(@RequestParam(value = "domain") String domain,@RequestParam(value = "domainType") Integer domainType){
        try {
            return merchantService.checkMerchantDomainExistByDomainType(domain,domainType);
        } catch (Exception e) {
            log.error("MerchantController.checkMerchantDomainExistByDomainType,exception:" + domain, e);
            return 0;
        }
    }

    @GetMapping("/getMerchantList")
    public List<?> getMerchantList() {
        return merchantService.getMerchantList();
    }

    @GetMapping("/getMerchantByCodes")
    public List<?> getMerchantByCodes(@RequestParam(value = "merchantCode") List<String> merchantCode) {
        return merchantService.getMerchantByCodes(merchantCode);
    }

    @GetMapping("/getMerchantByName")
    public List<?> getMerchantByName(@RequestParam(value = "merchantName") String merchantName) {
        return merchantService.getMerchantByName(merchantName);
    }

    @GetMapping("/getTblMerchantList")
    public List<?> getTblMerchantList() {
        return merchantService.getTblMerchantList();
    }

    @GetMapping("/getTblMerchantByCodes")
    public List<?> getTblMerchantByCodes(@RequestParam(value = "merchantCode") List<String> merchantCode) {
        return merchantService.getTblMerchantByCodes(merchantCode);
    }

    /**
     * 修改多语言
     * @return
     */
    @PostMapping(value = "/updateLanguage")
    APIResponse updateLanguage(HttpServletRequest request, @RequestBody MerchantVO merchantVO){

        return merchantService.updateLanguage(merchantVO);
    }


    /**
     * 获取多语言
     * @return
     */
    @GetMapping(value = "/getLanguageById")
    APIResponse getLanguageById(@RequestParam(value = "id") String id){

        return merchantService.getLanguageById(id);
    }

    @PostMapping(value = "/getMerchantGroupDomain")
    APIResponse getMerchantGroupDomain(@RequestBody MerchantGroupDomainVO req){
        return merchantService.getMerchantGroupDomain(req);
    }

    @PostMapping(value = "/queryAnimation")
    APIResponse queryAnimation(){
        return merchantService.queryAnimation();
    }



    @GetMapping(value ="/getAnimationURL")
    public String getAnimationURL() {
        log.info("/manage/merchant/getAnimationURL");
        try {
            return merchantService.getAnimationURL();
        } catch (Exception e) {
            log.error("MerchantController.getAnimationURL,exception:", e);
            return null;
        }
    }

    @GetMapping(value ="/updateAnimationURL")
    public int updateAnimationURL(HttpServletRequest request,
                                     @RequestParam(value = "url") String url) {
        log.info("/manage/merchant/updateAnimationURL");
        try {
            return merchantService.updateAnimationURL(url);
        } catch (Exception e) {
            log.error("MerchantController.getAnimationURL,exception:", e);
            return 0;
        }
    }

    @PostMapping("/updateDefaultVideoDomain")
    public APIResponse updateDefaultVideoDomain (HttpServletRequest request, @RequestBody MerchantConfig merchantConfig){
        return merchantService.updateDefaultVideoDomain(request, merchantConfig);
    }

    @PostMapping("/queryDefaultVideoDomain")
    public APIResponse queryDefaultVideoDomain(){
        return merchantService.queryDefaultVideoDomain();
    }

    @PostMapping("/deleteVideoDomainCache")
    public APIResponse deleteVideoDomainCache(){
        return merchantService.deleteVideoDomainCache();
    }

    @PostMapping("/saveOperationLog")
    public APIResponse saveOperationLog(@RequestBody OperationLogVO operationLogVO){
        log.info("/manage/merchant/saveOperationLog");
        try {
            return merchantService.saveOperationLog(operationLogVO);
        }catch (Exception e){
            log.error("MerchantController.saveOperationLog,exception:", e);
            return null;
        }
    }


    @PostMapping(value = "/file/changMerchantName")
    public Response<Object> changMerchantName(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws IOException {
            if (file.isEmpty()) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
        return Response.returnSuccess(merchantService.changeMerchantName(file,request));
    }

    @PostMapping(value ="/changeMerchantDomain")
    public APIResponse changeMerchantDomain(@RequestBody DomainChangeDTO domainChangeDto) {
        log.info("manage/merchant/changeMerchantDomain");
        try {
            return merchantService.changeMerchantDomain(domainChangeDto);
        } catch (Exception e) {
            log.error("merchantController.changeMerchantDomain,exception:",e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryMerchantDomain")
    public APIResponse queryMerchantDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain){
        log.info("manage/merchant/queryMerchantDomain");
        if(null == merchantDomain.getPageIndex()){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.queryMerchantDomain(merchantDomain);
        }catch (Exception e){
            log.error("merchantController.queryMerchantDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value ="/createFrontendMerchantGroup")
    public APIResponse createFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroup merchantGroup) {
        log.info("/manage/merchant/createFrontendMerchantGroup", "merchantGroup:" + merchantGroup);
        try {
            return merchantService.createFrontendMerchantGroup(merchantGroup);
        } catch (Exception e) {
            log.error("MerchantController.createFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value ="/updateFrontendMerchantGroup")
    public APIResponse updateFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroup merchantGroup) {
        log.info("/manage/merchant/updateFrontendMerchantGroup", "merchantGroup:" + merchantGroup);
        try {
            return merchantService.updateFrontendMerchantGroup(request, merchantGroup);
        } catch (Exception e) {
            log.error("MerchantController.updateFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value ="/queryFrontendMerchantGroup")
    public APIResponse queryFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroupDomainPO groupDomainPo) {
        log.info("/manage/merchant/queryFrontendMerchantGroup", "merchantGroup:" + groupDomainPo);
        try {
            return merchantService.queryFrontendMerchantGroup(groupDomainPo);
        } catch (Exception e) {
            log.error("MerchantController.queryFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value ="/createFrontendDomain")
    public APIResponse createFrontendDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain) {
        log.info("/manage/merchant/createFrontendDomain", "merchantGroup:" + merchantDomain);
        try {
            return merchantService.createFrontendDomain(merchantDomain);
        } catch (Exception e) {
            log.error("MerchantController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value ="/delFrontendDomain")
    public APIResponse delFrontendDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain) {
        log.info("/manage/merchant/delFrontendDomain", "merchantGroup:" + merchantDomain);
        try {
            return merchantService.delFrontendDomain(merchantDomain);
        } catch (Exception e) {
            log.error("MerchantController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }


    @PostMapping("/updateEvent")
    @AuthRequiredPermission("Merchant:Manage:update")
    public Response updateEvent(HttpServletRequest request, @RequestBody @Validated MerchantEventVO merchantEventVO) {

        log.info("/manage/merchant/updateMerchantEvent:" + merchantEventVO);
        try {
            return merchantService.updateMerchantEvent(merchantEventVO);
        } catch (Exception e) {
            log.error("MerchantController.createFrontendDomain,exception:", e);
            return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }


    @PostMapping("/update/updateMerchantEventSwitch")
    public Response updateMerchantEventSwitch(HttpServletRequest request, @RequestBody MerchantEventVO merchantEventVO) {
        log.info("/manage/merchant/update/updateMerchantEventSwitch:" + merchantEventVO);
        if (merchantEventVO == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        // 验证必传参数
        Integer eventSwitch = merchantEventVO.getEventSwitch();//精彩回放
        if (eventSwitch == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.updateMerchantEventSwitch(merchantEventVO);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantEventSwitch,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/getMerchantEventSwitch")
    public Response<MerchantEventVO> getMerchantEventSwitch(@RequestBody MerchantEventVO merchantEventVO){
        if(StringUtils.isEmpty(merchantEventVO.getMerchantCode())){
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            return Response.returnSuccess(merchantService.getMerchantEventSwitch(merchantEventVO));
        } catch (Exception e) {
            log.error("MerchantController.getMerchantEventSwitch,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/exportMerchantKey")
    public Response exportMerchantKey(){
        log.info("/manage/merchant/exportMerchantKey");
        try {
            List<String> codeList = Arrays.asList(merchantCode.split(","));
            merchantService.exportMerchantKey(codeList);
            return Response.returnSuccess();
        }catch (Exception e){
            log.error("MerchantController.exportMerchantKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 新增商户证书
     * @param request
     * @param merchantKeyVO
     * @return
     */
    @PostMapping("/addMerchantKey")
    public Response addMerchantKey(HttpServletRequest request, @RequestBody MerchantKeyVO merchantKeyVO){
        log.info("/manage/merchant/addMerchantKey:" + merchantKeyVO);
        try {
            if (StringUtils.isEmpty(merchantKeyVO.getMerchantCode())){
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return Response.returnSuccess(merchantService.addMerchantKey(request, merchantKeyVO));
        }catch (Exception e){
            log.error("MerchantController.addMerchantKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 商户密钥启用/禁用
     * @param request
     * @param merchantKeyVO
     * @return
     */
    @PostMapping("/enableMerchantKey")
    public Response enableMerchantKey(HttpServletRequest request, @RequestBody MerchantKeyVO merchantKeyVO){
        log.info("/manage/merchant/enableMerchantKey:" + merchantKeyVO);
        try {
            if (StringUtils.isEmpty(merchantKeyVO.getMerchantCode()) && (null == merchantKeyVO.getKeyStatus() || null == merchantKeyVO.getSecondStatus())){
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return Response.returnSuccess(merchantService.enableMerchantKey(request, merchantKeyVO));
        }catch (Exception e){
            log.error("MerchantController.enableMerchantKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

}

