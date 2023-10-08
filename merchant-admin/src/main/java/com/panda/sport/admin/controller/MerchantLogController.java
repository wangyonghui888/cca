package com.panda.sport.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.vo.MerchantLogFindVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.admin.controller
 * @Description :  商户端日志接口
 * @Date: 2020-09-04 15:34
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/admin/log")
public class MerchantLogController {


    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private OutMerchantService merchantService;

    /**
     * 获取日志页面
     *
     * @return
     */
    @GetMapping("/getLogPages")
    public Response getLogPages(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        JwtUser user = SecurityUtils.getUser();
        if (!AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel())) {
            return Response.returnSuccess(merchantLogService.loadLogOutPages(MerchantLogConstants.MERCHANT_OUT, AgentLevelEnum.AGENT_LEVEL_1.getCode(), language));
        } else {

            return Response.returnSuccess(merchantLogService.loadLogPages(MerchantLogConstants.MERCHANT_OUT, language));
        }
    }

    /**
     * 获取日志操作类型
     *
     * @return
     */
    @GetMapping("/getLogTypes")
    public Response getLogTypes() {
        return Response.returnSuccess(merchantLogService.loadLogType());
    }

    /**
     * 查询日志
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findLog")
    public Response queryLog(HttpServletRequest request, @RequestBody MerchantLogFindVO findVO) {
        JwtUser user = SecurityUtils.getUser();
        List<String> codes = new ArrayList<>();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel())) {
            codes = merchantService.queryMerchantList();
            if (CollectionUtil.isNotEmpty(codes)) {
                codes.add(user.getMerchantCode());
            } else {
                codes.add(user.getMerchantCode());
            }
        } else {
            codes.add(user.getMerchantCode());
        }
        findVO.setMerchantCodes(codes);
        findVO.setTag(MerchantLogConstants.MERCHANT_OUT);
        return Response.returnSuccess(merchantLogService.queryLog(findVO));
    }
}
