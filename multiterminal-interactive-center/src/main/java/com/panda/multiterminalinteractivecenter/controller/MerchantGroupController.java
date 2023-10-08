package com.panda.multiterminalinteractivecenter.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.entity.TMerchantGroup;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.service.IDomainService;
import com.panda.multiterminalinteractivecenter.service.MerchantGroupService;
import com.panda.multiterminalinteractivecenter.service.MerchantGroupServiceTransfer;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.impl.CpDjMerchantGroupServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.MerchantGroupServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.IdempotentUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.*;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ifan
 * @date 2022/7/8
 */
@RestController
@RequestMapping("/merchant/group")
@Slf4j
@Validated
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantGroupController {

    private final IDomainService domainService;

    private final MerchantLogService merchantLogService;

    private final MerchantGroupServiceImpl merchantGroupService;

    private final CpDjMerchantGroupServiceImpl cpDJMerchantGroupService;

    private final Map<String, MerchantGroupServiceTransfer> merchantGroupServiceTransfer;

    /**
     * 查询聊天室全局域名
     *
     * @param request
     * @return
     */
    @PostMapping("/queryChatroomDomain")

    public APIResponse queryChatroomDomain(HttpServletRequest request) {
        return domainService.queryChatroomDomain();
    }

    /**
     * 更新聊天室域名设置
     *
     * @param request
     * @param domainVO
     * @return
     */
    @PostMapping("/updateChatroomDomain")
    public APIResponse updateChatroomDomain(HttpServletRequest request, @RequestBody DomainVO domainVO) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：更新聊天室域名设置，操作人：{}，参数:{}", userName, JSON.toJSONString(domainVO));
        return domainService.updateChatroomDomain(domainVO.getUrl());
    }

    /**
     * 清除聊天室域名设置缓存
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/deleteChatRoomCache")
    public APIResponse deleteChatRoomCache(HttpServletRequest request) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：清除聊天室域名设置缓存，操作人：{}", userName);
        return domainService.deleteChatRoomCache();
    }


    /**
     * 查询H5全局域名
     *
     * @param request
     * @return
     */
    @PostMapping("/queryLiveH5Domain")
    public APIResponse queryLiveH5Domain(HttpServletRequest request) {
        APIResponse response = domainService.queryLiveH5Domain();
        return response;
    }

    /**
     * 更新H5域名设置
     *
     * @param request
     * @param domainVO
     * @return
     */
    @PostMapping("/updateLiveH5Domain")
    public APIResponse updateLiveH5Domain(HttpServletRequest request, @RequestBody DomainVO domainVO) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：更新H5域名设置，操作人：{}，参数:{}", userName, JSON.toJSONString(domainVO));
        return domainService.updateLiveH5Domain(domainVO.getUrl());
    }

    /**
     * 清除H5域名设置缓存
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/deleteH5Cache")
    public APIResponse deleteH5Cache(HttpServletRequest request) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：清除H5域名设置缓存，操作人：{}", userName);
        return domainService.deleteH5Cache();
    }

    /**
     * 查询PC全局域名
     *
     * @param request
     * @return
     */
    @PostMapping("/queryLivePcDomain")
    public APIResponse queryLivePcDomain(HttpServletRequest request) {
        return domainService.queryLivePcDomain();
    }

    /**
     * 更新PC域名设置
     *
     * @param request
     * @param domainVO
     * @return
     */
    @PostMapping("/updateLivePcDomain")
    public APIResponse updateLivePcDomain(HttpServletRequest request, @RequestBody DomainVO domainVO) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：更新PC域名设置，操作人：{}，参数:{}", userName, JSON.toJSONString(domainVO));
        return domainService.updateLivePcDomain(domainVO.getUrl());
    }

    /**
     * 清除PC域名设置缓存
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/deletePcCache")
    public APIResponse deletePcCache(HttpServletRequest request) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：清除PC域名设置缓存，操作人：{}", userName);
        return domainService.deletePcCache();
    }

    /**
     * 查询数据商动画域名
     *
     * @param request
     * @return
     */
    @PostMapping("/queryAnimation")
    public APIResponse queryAnimation(HttpServletRequest request) {
        return domainService.queryAnimation();
    }

    /**
     * 更新数据商动画域名设置
     *
     * @param request
     * @param domainVO
     * @return
     */
    @PostMapping("/updateAnimation")
    public APIResponse updateAnimation(HttpServletRequest request, @RequestBody DomainVO domainVO) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：更新数据商动画域名设置，操作人：{}，参数:{}", userName, JSON.toJSONString(domainVO));
        return domainService.updateAnimation(domainVO.getUrl());
    }


    /**
     * 清除数据商动画域名设置缓存
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/deleteAniCache")
    public APIResponse deleteAniCache(HttpServletRequest request) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("操作记录：清除数据商动画域名设置缓存，操作人：{}", userName);
        return domainService.deleteAniCache();
    }

    /**
     * 查询商户组信息
     *
     * @param request
     * @param merchantGroupVO
     * @return
     */
    @PostMapping(value = "/selectMerchantGroup")
    public APIResponse selectMerchantGroup(HttpServletRequest request,
                                           @RequestBody MerchantGroupVO merchantGroupVO) {
        MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantGroupVO.getTab()));
        try {
            return APIResponse.returnSuccess(merchantGroupTransfer.selectMerchantGroup(merchantGroupVO));
        } catch (Exception e) {
            log.error("MerchantGroupController.selectMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 清除商户缓存
     *
     * @param cleanMerchantVO
     * @return
     */
    @PostMapping(value = "/cleanMerchant")
    public APIResponse cleanMerchant(HttpServletRequest request, @RequestBody CleanMerchantVO cleanMerchantVO) {
        log.info("/merchant/group/cleanMerchant" + ",merchantTag:"
                + cleanMerchantVO.getMerchantTag() + ",containsType:"
                + cleanMerchantVO.getContainsType() + ",merchantCode:"
                + cleanMerchantVO.getMerchantCode() + ",parentCode:"
                + cleanMerchantVO.getParentCode() + ",containsStr:"
                + cleanMerchantVO.getContainsStr() + ",groupId:"
                + cleanMerchantVO.getGroupId() + ",tab:"
                + cleanMerchantVO.getTab());
        try {
            String tab = StringUtils.isBlank(cleanMerchantVO.getTab()) ? TabEnum.TY.getName() : cleanMerchantVO.getTab();
            MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("cleanMerchantCache"), "-", "-");
            MerchantGroupPO merchantGroup = merchantGroupService.selectMerchantGroupById(cleanMerchantVO.getGroupId(), cleanMerchantVO.getTab());
            filedVO.setMerchantName(merchantGroup.getGroupName());
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, MerchantLogTypeEnum.CLEAR_CACHE, filedVO,  null, merchantGroup.getGroupName() + StringPool.AMPERSAND + cleanMerchantVO.getGroupId(), request);
            if (tab.equalsIgnoreCase(TabEnum.TY.getName())) {
                return merchantGroupService.cleanMerchant(cleanMerchantVO.getMerchantTag(), cleanMerchantVO.getContainsType(),
                        cleanMerchantVO.getContainsStr(), cleanMerchantVO.getMerchantCode(), cleanMerchantVO.getParentCode());
            } else if (tab.equalsIgnoreCase(TabEnum.DJ.getName())) {
                cpDJMerchantGroupService.sendDJMsg(cleanMerchantVO.getMerchantCode(), 0, "", DomainConstants.DOMAIN_CHANGE_MANUAL);
            } else {
                cpDJMerchantGroupService.clearCPCache(cleanMerchantVO.getGroupId(), null);
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantGroupController.cleanMerchant,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 手动切换域名
     *
     * @param request
     * @param domainVo
     * @return
     */
    @PostMapping("/updateMerchantDomain")
    public APIResponse updateMerchantDomain(HttpServletRequest request, @RequestBody DomainVO domainVo) {
        log.info("手动切换域名：tab：{}，param：{}", domainVo.getTab(), JSON.toJSONString(domainVo));
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(domainVo.getTab()));
        if (domainVo.getMerchantGroupId() == null || CollectionUtil.isEmpty(domainVo.getConfig())) {
            return APIResponse.returnFail("参数 groupId 或 domain 不能为空");
        }

        return merchantGroupTransfer.updateMerchantDomain(domainVo, userName, IPUtils.getIpAddr(request));
    }

    /**
     * 查询商户组下的域名
     * 手动切换域名列表
     *
     * @param request
     * @param merchantDomainVO
     * @return
     */
    @PostMapping("/getMerchantGroupDomainRelationData")
    public APIResponse getDomainRelationDataList(HttpServletRequest request, @RequestBody MerchantDomainVO merchantDomainVO) {

        MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantDomainVO.getTab()));
        return merchantGroupTransfer.getMerchantGroupDomainRelationDataList(merchantDomainVO);
    }


    /**
     * 根据商户分组类型 域名类型查询域名名称
     *
     * @return
     */
    @PostMapping("/getDomainName")
    public APIResponse getDomainNameList(@RequestBody DomainGroupVO domainGroupVO) {
        MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(domainGroupVO.getTab()));
        return merchantGroupTransfer.getDomainNameList(domainGroupVO);
    }

    /**
     * 修改商户组信息
     *
     * @param request
     * @param merchantGroupPO
     * @return
     */
    @PostMapping(value = "/updateMerchantGroup")
    public APIResponse updateMerchantGroup(HttpServletRequest request,
                                           @RequestBody MerchantGroupVO merchantGroupPO) {
        log.info("/manage/group/updateMerchantGroup" + merchantGroupPO);
        try {
            if (merchantGroupPO == null || merchantGroupPO.getId() == null || merchantGroupPO.getGroupType() == null) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            if (StringUtils.isBlank(merchantGroupPO.getTab())) merchantGroupPO.setTab(TabEnum.TY.getName());
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantGroupPO.getTab()));
            return merchantGroupTransfer.updateMerchantGroup(merchantGroupPO, request);
        } catch (Exception e) {
            log.error("MerchantGroupController.updateMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 删除商户组信息
     *
     * @param request
     * @param merchantGroupPO
     * @return
     */
    @PostMapping(value = "/deleteMerchantGroup")
    public APIResponse deleteMerchantGroup(HttpServletRequest request,
                                           @RequestBody MerchantGroupVO merchantGroupPO) {
        log.info("/manage/group/deleteMerchantGroup" + merchantGroupPO);
        try {
            if (merchantGroupPO == null || merchantGroupPO.getId() == null) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantGroupPO.getTab()));
            return merchantGroupTransfer.deleteMerchantGroup(merchantGroupPO, request);
        } catch (Exception e) {
            log.error("MerchantGroupController.deleteMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 根据商户分组类型查询商户信息
     *
     * @param pageVO
     * @return
     */
    @PostMapping(value = "/list")
//    @AuthRequiredPermission("Merchant:Group:list")
    public APIResponse list(@RequestBody MerchantGroupPageVO pageVO) {
        log.info("/manage/merchant/list:merchantName:" + pageVO.getMerchantName() + ",merchantCode:" + pageVO.getMerchantCode() + ",merchantCodes:" + pageVO.getMerchantCodes() + ",status:" + pageVO.getStatus() +
                ",pageSize:" + pageVO.getPageSize() + ",pageIndex:" + pageVO.getPageNum() + ",sort:" + pageVO.getSort() + ",orderBy:" + pageVO.getOrderBy());
        MerchantVO merchantVO = new MerchantVO();
        try {
            merchantVO.setMerchantName(pageVO.getMerchantName());
            merchantVO.setMerchantCode(pageVO.getMerchantCode());
            merchantVO.setStatus(pageVO.getStatus());
            if (pageVO.getMerchantGroupId() != null) {
                merchantVO.setMerchantGroupId(Long.valueOf(pageVO.getMerchantGroupId()));
            }
            if (pageVO.getGroupCode() != null) {
                merchantVO.setDomainGroupCode(pageVO.getGroupCode());
            }
            if (pageVO.getTransferMode() != null) {
                merchantVO.setTransferMode(pageVO.getTransferMode());
            }
            if (pageVO.getIsApp() != null) {
                merchantVO.setIsApp(pageVO.getIsApp());
            }
            merchantVO.setAgentLevel(pageVO.getAgentLevel());
            merchantVO.setParentName(pageVO.getParentName());
            merchantVO.setPageSize(pageVO.getPageSize());
            merchantVO.setMerchantTag(pageVO.getMerchantTag());
            merchantVO.setPageIndex(pageVO.getPageNum());
            merchantVO.setSort(pageVO.getSort());
            merchantVO.setOrderBy(pageVO.getOrderBy());
            merchantVO.setTab(pageVO.getTab());
            if (!StrUtil.isBlank(pageVO.getMerchantCodes())) {
                try {
                    List<String> list = Arrays.asList(pageVO.getMerchantCodes().split(","));
                    merchantVO.setMerchantCodes(list);
                } catch (Exception ex) {
                    log.error("MerchantController.list,merchantCodes change fail exception:" + merchantVO, ex);
                    return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
                }
            }

            if (null == pageVO.getPageNum()) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantVO.getTab()));
            if (Objects.equals(TransferEnum.CP.getCode(), merchantVO.getTab())) {

                return APIResponse.returnSuccess(merchantGroupTransfer.getMerchantList(2));

            }
            if (Objects.equals(TransferEnum.DJ.getCode(), merchantVO.getTab())) {

                return APIResponse.returnSuccess(merchantGroupTransfer.getTblMerchantList(1));
            }
            return merchantGroupService.selectList(merchantVO);
        } catch (Exception e) {
            log.error("MerchantGroupController.list,exception:" + merchantVO, e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 创建商户组信息
     *
     * @param request
     * @param merchantGroupPO
     * @return
     */
    @PostMapping(value = "/createMerchantGroup")
    public APIResponse createMerchantGroup(HttpServletRequest request,
                                           @RequestBody TMerchantGroup merchantGroupPO) {
        log.info("/manage/Group/createMerchantGroup" + merchantGroupPO);
        try {
            if (merchantGroupPO == null
                    || merchantGroupPO.getTimes() < 0 || merchantGroupPO.getTimeType() < 0
                    || merchantGroupPO.getGroupType() == null
            ) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }

            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantGroupPO.getTab()));
            merchantGroupPO.setGroupCode(TransferEnum.getByType(merchantGroupPO.getTab()));
            return merchantGroupTransfer.createMerchantGroup(merchantGroupPO, request);
        } catch (Exception e) {
            log.error("MerchantGroupController.createMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 通过商户分组类型 域名类型 区域选择域名名称
     *
     * @return
     */
    @GetMapping("/selectDomain")
    public APIResponse<?> selectDomain(@RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "size", required = false) Integer size,
                                       @RequestParam(value = "domainName", required = false) String domainName,
                                       @RequestParam(value = "domainType", required = false) Integer domainType,
                                       @RequestParam(value = "domainGroupId", required = false) Long domainGroupId,
                                       @RequestParam(value = "domainGroupName", required = false) String domainGroupName,
                                       @RequestParam(value = "lineCarrierId", required = false) Long lineCarrierId,
                                       @RequestParam(value = "groupType", required = false) Integer groupType,
                                       @RequestParam(value = "used", required = false) Boolean used,
                                       @RequestParam(value = "tab", required = false) String tab,
                                       @RequestParam(value = "areaName", required = false) String areaName,
                                       @RequestParam(value = "programId", required = false) Long programId) {
        String requestUrl = "/merchant/group/selectDomain";
        log.info(requestUrl);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(requestUrl).append(page).append(size).append(domainName)
                    .append(domainType).append(domainGroupId).append(domainGroupName)
                    .append(lineCarrierId).append(groupType).append(used).append(tab).append(areaName).append(programId);
            if (IdempotentUtils.repeat(sb.toString(), this.getClass())) {
                return APIResponse.returnFail("请勿重复提交!");
            }
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(tab));
            if (areaName.contains("%")) {
                try {
                    areaName = URLEncoder.encode(areaName, "UTF-8");
                } catch (Exception e) {
                    return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
                }
            }
            return merchantGroupTransfer.selectDomain(page, size, domainName, domainType, domainGroupId, domainGroupName, lineCarrierId, groupType, used, tab, programId, areaName);
        } catch (Exception e) {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        } finally {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        }
        return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
    }




    /**
     * 删除方案关系表
     *
     * @param request
     * @param merchantGroupVO
     * @return
     */
    @PostMapping(value = "/delProgramRelationByDomainGroupId")
    public APIResponse delProgramRelationByDomainGroupId(HttpServletRequest request,
                                           @RequestBody MerchantGroupVO merchantGroupVO) {
        log.info("/manage/group/delProgramRelationByDomainGroupId" + merchantGroupVO);
        try {
            if (merchantGroupVO == null || merchantGroupVO.getProgramId() == null || merchantGroupVO.getDomainGroupId() == null) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(merchantGroupVO.getTab()));
            return merchantGroupTransfer.delProgramRelationByDomainGroupId(merchantGroupVO, request);
        } catch (Exception e) {
            log.error("MerchantGroupController.delProgramRelationByDomainGroupId,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/getMerchantList")
    public APIResponse getMerchantList(HttpServletRequest request , @RequestParam(value = "tab") String tab ) {
        MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(tab));
        return APIResponse.returnSuccess(merchantGroupTransfer.getMerchantList(tab == "dj" ? 1 : 2 ));
    }

    @PostMapping("/getMerchantGroupDomain")
    public APIResponse getMerchantGroupDomain(HttpServletRequest request, @RequestBody MerchantGroupDomainVO req){

        MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(req.getTab()));

        return merchantGroupTransfer.getMerchantGroupDomain(req);

    }

    @PostMapping(value = "editThreshold")
    public APIResponse editThreshold(HttpServletRequest request,@RequestBody MerchantGroupVO req) {
        try {
            String userName = JWTUtil.getUsername(request.getHeader("token"));
            log.info("操作记录：编辑阈值，操作人：{}，参数:{}",userName,JSON.toJSONString(req));
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(req.getTab()));

            return merchantGroupTransfer.editThreshold(req, request);
        } catch (Exception e) {
            log.error("MerchantGroupController.editThreshold,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "getThreshold")
    public APIResponse getThreshold(HttpServletRequest request, @RequestBody MerchantGroupVO req) {

        try {
            MerchantGroupServiceTransfer merchantGroupTransfer = merchantGroupServiceTransfer.get(TransferEnum.getByCode(req.getTab()));

            return merchantGroupTransfer.getThreshold(req);
        } catch (Exception e) {
            log.error("MerchantGroupController.getThreshold,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

}
