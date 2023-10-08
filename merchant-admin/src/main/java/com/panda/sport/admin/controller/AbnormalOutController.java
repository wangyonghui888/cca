package com.panda.sport.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.AbnormalOutService;
import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import com.panda.sport.merchant.common.vo.user.MerchantComboVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@RestController
@RequestMapping("/admin/abnormal")
public class AbnormalOutController {

    @Autowired
    private AbnormalOutService abnormalOutService;


    @Autowired
    private OutMerchantService merchantService;


    /**
     * 异常会员名单查询
     */
    @PostMapping(value = "/queryAbnormalList")
    //@AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryAbnormalList(HttpServletRequest request, @RequestBody AbnormalVo abnormalVo) {
        log.info("admin/abnormal/queryAbnormalList:" + abnormalVo);
        String language = request.getHeader("language");
        List<String> codes = new ArrayList<>();
        JwtUser user = SecurityUtils.getUser();
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
        abnormalVo.setMerchantCodes(codes);
        //abnormalVo.setMerchantCode(user.getMerchantCode());
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        abnormalVo.setLanguage(language);
        return abnormalOutService.queryAbnormalList(abnormalVo, language);
    }


    /**
     * 异常会员名单icon统计查询
     */
    @PostMapping(value = "/queryAbnormalCount")
    public Response queryAbnormalCount(HttpServletRequest request, @RequestBody AbnormalVo abnormalVo) {
        log.info("admin/abnormal/queryAbnormalCount:" + abnormalVo);
        String language = request.getHeader("language");
        List<String> codes = new ArrayList<>();
        JwtUser user = SecurityUtils.getUser();
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
        abnormalVo.setMerchantCodes(codes);
        abnormalVo.setMerchantCode(user.getMerchantCode());
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        abnormalVo.setLanguage(language);
        return abnormalOutService.queryAbnormalCount(abnormalVo, language);
    }

    /**
     * 异常会员名单icon点击更新时间
     */
    @PostMapping(value = "/updateAbnormalClickTime")
    public Response updateAbnormalClickTime(HttpServletRequest request, @RequestParam(value = "abnormalClickTime") Long abnormalClickTime) {
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        return abnormalOutService.updateAbnormalClickTime(merchantCode,abnormalClickTime);
    }




    /**
     * 异常会员名单导出
     */
    @RequestMapping("/exportAbnormalStatistic")
    //@AuthRequiredPermission("Merchant:Report:match:export")
    public Response exportAbnormalStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody AbnormalVo vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        log.info("/order/abnormal/exportAbnormalStatistic:" + vo);
        try {
            List<String> codes = new ArrayList<>();
            JwtUser user = SecurityUtils.getUser();
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
            vo.setMerchantCodes(codes);
            vo.setLanguage(language);
            abnormalOutService.exportAbnormalStatistic(response, request, vo, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("AbnormalController.exportAbnormalStatistic,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 异常用户查询
     */
    @PostMapping(value = "/queryAbnormalUserList")
    //@AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryAbnormalUserList(HttpServletRequest request, @RequestBody AbnormalUserVo abnormalUserVo) {
        log.info("/order/abnormal/queryAbnormalUserList:" + abnormalUserVo);
        if(StringUtils.isEmpty(abnormalUserVo.getMerchantCode())){
            List<String> codes = new ArrayList<>();
            JwtUser user = SecurityUtils.getUser();
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
            abnormalUserVo.setMerchantCodes(codes);
        }
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        abnormalUserVo.setLanguage(language);
        abnormalUserVo.setPageNum(abnormalUserVo.getPageNum()-1);
        return abnormalOutService.queryAbnormalUserList(abnormalUserVo, language);
    }

    /**
     * 异常用户导出
     */
    @RequestMapping("/exportAbnormalUserStatistic")
    //@AuthRequiredPermission("Merchant:Report:match:export")
    public Response exportAbnormalUserStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody AbnormalUserVo vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        log.info("/order/abnormal/exportAbnormalUserStatistic:" + vo);
        try {
            if(StringUtils.isEmpty(vo.getMerchantCode())) {
                List<String> codes = new ArrayList<>();
                JwtUser user = SecurityUtils.getUser();
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
                vo.setMerchantCodes(codes);
            }
            vo.setLanguage(language);
            abnormalOutService.exportAbnormalUserStatistic(response, request, vo, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("AbnormalController.exportAbnormalUserStatistic,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 商户对外下拉查询
     */
    @PostMapping(value = "/queryUserComboList")
    //@AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryUserComboList(HttpServletRequest request) {
        log.info("/order/abnormal/queryUserComboList:" );
        Map<String, Object> map = new HashMap<String, Object>(2);
        JwtUser user = SecurityUtils.getUser();
        List<MerchantComboVO> codes = new ArrayList<>();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel())) {
            codes = abnormalOutService.findUserComboList();

        } else {
            MerchantComboVO vo = new MerchantComboVO();
            vo.setMerchantCode(user.getMerchantCode());
            vo.setMerchantName(user.getMerchantName());
            codes.add(vo);
        }
        map.put("level",user.getAgentLevel());
        map.put("merchantList",codes);
        return Response.returnSuccess(map);
    }
}
