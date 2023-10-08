package com.panda.sport.merchant.manage.controller;

import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentPageVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentVO;
import com.panda.sport.merchant.manage.service.MerchantAgentService;
import com.panda.sport.merchant.manage.service.impl.LoginUserService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * @author javier
 * @date 2020/12/15
 * @apiNote 代理商设置
 */
@RestController
@RequestMapping("/manage/merchantAgent")
@Slf4j
public class MerchantAgentController {

    @Resource
    private MerchantAgentService merchantAgentService;
    @Resource
    private LoginUserService loginUserService;

    /**
     * todo 根据代理商id查询渠道商户
     */
    @GetMapping(value = "/merchantList")
    @AuthRequiredPermission("Merchant:Manage:merchantAgent:query:list")
    public Response<PageInfo<MerchantPO>> merchantList(MerchantAgentPageVO merchantAgentPageVO) {
        log.info("url:manage/merchantAgent/merchantList;param:{}", merchantAgentPageVO.toString());
        return merchantAgentService.listMerchant(merchantAgentPageVO);
    }

    /**
     * todo 根据代理商id批量添加渠道商户
     */
    @GetMapping(value = "/batchAddMerchant")
    @AuthRequiredPermission("Merchant:Manage:merchantAgent:batchAdd")
    public Response<ResponseEnum> batchAddMerchant(HttpServletRequest request,
                                                   @RequestParam(value = "agentId") String agentId,
                                                   @RequestParam(value = "merchantIds") String merchantIds) {
        log.info("manage/merchantAgent/batchAddMerchant?agentId={}&merchantIds={}",
                agentId, merchantIds);
        Assert.hasLength(agentId, "代理商(agentId)必填");
        Assert.hasLength(merchantIds, "渠道商户(merchantIds)必填");
        Integer userId = SsoUtil.getUserId(request);
        String username = loginUserService.getLoginUser(userId);
        MerchantAgentVO merchantAgentVO = new MerchantAgentVO()
                .setUserId(userId.toString())
                .setUpdateUser(username)
                .setUpdateTime(new Date())
                .setMerchantIds(new HashSet<>(Arrays.asList(merchantIds.split(","))))
                .setMerchantAgentId(agentId)
                .setMerchantOutIn(MerchantLogConstants.MERCHANT_OUT);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return merchantAgentService.bindMerchantByAgent(merchantAgentVO, language, IPUtils.getIpAddr(request));
    }
}
