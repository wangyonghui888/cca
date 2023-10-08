package com.panda.sport.admin.controller;

import com.github.pagehelper.PageInfo;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentPageVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentVO;
import com.panda.sport.merchant.manage.service.MerchantAgentService;
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
 * todo 代理商设置
 * @date 2020/12/15
 */
@RestController
@RequestMapping("/admin/merchantAgent")
@Slf4j
public class MerchantAgentController {
    @Resource
    private MerchantAgentService merchantAgentService;

    /**
     * todo 根据代理商id查询渠道商户
     */
    @GetMapping(value = "/merchantList")
    public Response<PageInfo<MerchantPO>> merchantList(MerchantAgentPageVO merchantAgentPageVO) {
        log.info("url：admin/merchantAgent/merchantList;param:{}", merchantAgentPageVO.toString());
        return merchantAgentService.listMerchant(merchantAgentPageVO);
    }

    /**
     * todo  根据代理商id批量添加渠道商户
     */
    @GetMapping(value = "/batchAddMerchant")
    public Response<ResponseEnum> batchAddMerchant(HttpServletRequest request, @RequestParam(value = "agentId") String agentId,
                                                   @RequestParam(value = "merchantIds") String merchantIds) {
        log.info("admin/merchantAgent/addOrRemoveMerchant?agentId={}&merchantIds={}",
                agentId, merchantIds);
        Assert.hasLength(agentId, "代理商(agentId)必填");
        Assert.hasLength(merchantIds, "渠道商户(merchantIds)必填");
        JwtUser user = SecurityUtils.getUser();
        MerchantAgentVO merchantAgentVO = new MerchantAgentVO()
                .setUserId(user.getId().toString())
                .setUpdateUser(user.getUsername())
                .setUpdateTime(new Date())
                .setMerchantIds(new HashSet<>(Arrays.asList(merchantIds.split(","))))
                .setMerchantAgentId(agentId)
                .setMerchantOutIn(MerchantLogConstants.MERCHANT_IN);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return merchantAgentService.bindMerchantByAgent(merchantAgentVO,language, IPUtils.getIpAddr(request));
    }
}
