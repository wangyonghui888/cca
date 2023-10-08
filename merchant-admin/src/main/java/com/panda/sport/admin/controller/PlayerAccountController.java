package com.panda.sport.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.panda.sport.admin.feign.BssBackendClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.PlayerTransferService;
import com.panda.sport.admin.service.RcsUserConfigLimitService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.manage.service.UserAccountService;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.controller
 * @Description :  账户交易查询
 * @Date: 2020-09-11 16:19
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/admin/account")
public class PlayerAccountController {


    @Autowired
    private UserAccountService userAccountService;


    @Autowired
    private PlayerTransferService playerTransferService;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private BssBackendClient bssBackendClient;

    @Autowired
    private RcsUserConfigLimitService rcsUserConfigService;

    /**
     * 查询交易记录
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findRecord")
    public Response findRecord(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        JwtUser user = SecurityUtils.getUser();
        List<String> codes = new ArrayList<>();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel()) || AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(user.getAgentLevel())) {
            if (CollectionUtils.isEmpty(findVO.getMerchantCodeList())) {
                codes = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
            } else {
                codes = findVO.getMerchantCodeList();
            }
        } else {
            codes.add(user.getMerchantCode());
        }
        findVO.setMerchantCodes(codes);
        return Response.returnSuccess(userAccountService.queryTransferRecord(findVO));
    }


    /**
     * 查询失败交易记录
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findReTryRecord")
    public Response findReTryRecord(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        JwtUser user = SecurityUtils.getUser();
        List<String> codes = new ArrayList<>();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel()) || AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(user.getAgentLevel())) {
            if (CollectionUtils.isEmpty(findVO.getMerchantCodeList())) {
                codes = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
            } else {
                codes = findVO.getMerchantCodeList();
            }
        } else {
            codes.add(user.getMerchantCode());
        }
        findVO.setMerchantCodes(codes);
        return Response.returnSuccess(userAccountService.queryRetryTransferRecord(findVO));
    }


    /**
     * 重新调用交易接口
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/retryTransfer")
    public Response retryTransfer(HttpServletRequest request, @RequestBody UserRetryTransferVO vo) {
        log.info("admin/account/retryTransfer:" + vo);
        JwtUser user = SecurityUtils.getUser();
        vo.setUserName(user.getUsername());
        vo.setTransferIdList(vo.getTransferId());
        //bssBackendClient.retryTransfer(vo);
        playerTransferService.retryTransfer(vo);
        return Response.returnSuccess();
    }

    /**
     * 交易记录导出
     */
    @RequestMapping(value = "/transferRecordExport")
    public Response transferRecordExport(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        try {
            //导出时间范围增加一天内的限制
            long time  = findVO.getEndTime() - findVO.getStartTime();
            long dayMillisecond = 86400000L;
            if (time > dayMillisecond) {
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("code", "0002");
                resultMap.put("msg", "请查询时间范围一天内的数据");
                return Response.returnSuccess(resultMap);
            }
            List<String> codes = new ArrayList<>();
            String merchantCode;
            String username;
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            String merchantId = user.getMerchantId();
            username = user.getUsername();
            Integer agentLevel = user.getAgentLevel();
            if (CollectionUtil.isEmpty(findVO.getMerchantCodeList())) {
                if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(agentLevel) || AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(agentLevel)) {
                    codes = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                } else {
                    codes.add(merchantCode);
                }
                findVO.setMerchantCodes(codes);
            }
            if (StringUtils.isNotEmpty(findVO.getBizTypeListStr())) {
                String[] str = findVO.getBizTypeListStr().split(",");
                List<Integer> list = new ArrayList<>(str.length);
                for (String s : str) {
                    list.add(Integer.valueOf(s));
                }
                findVO.setBizTypeList(list);
            }
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return Response.returnSuccess(userAccountService.transferRecordExport(username, merchantCode, findVO, language));
        } catch (Exception e) {
            log.error("UserController.export error!", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }

    /**
     * 查询账变记录
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findAccountHistory")
    public Response findAccountHistory(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        JwtUser user = SecurityUtils.getUser();
        List<String> codes = new ArrayList<>();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel()) || AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(user.getAgentLevel())) {
            codes = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
        } else {
            codes.add(user.getMerchantCode());
        }
        findVO.setMerchantCodes(codes);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return Response.returnSuccess(userAccountService.queryAccountHistory(findVO, language));
    }

    /**
     * 账变记录导出
     */
    @RequestMapping(value = "/accountHistoryExport")
    public Response accountHistoryExport(HttpServletResponse response, HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        try {
            //导出时间范围增加一天内的限制
            long time = findVO.getEndTime() - findVO.getStartTime();
            long dayMillisecond = 86400000L;
            if (time > dayMillisecond) {
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("code", "0002");
                resultMap.put("msg", "请查询时间范围一天内的数据");
                return Response.returnSuccess(resultMap);
            }
            String merchantCode;
            String username;
            List<String> codes = new ArrayList<>();
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            String merchantId = user.getMerchantId();
            username = user.getUsername();
            Integer agentLevel = user.getAgentLevel();
            if (CollectionUtil.isEmpty(findVO.getMerchantCodeList())) {
                if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(agentLevel) || AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(agentLevel)) {
                    codes = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                } else {
                    codes.add(merchantCode);
                }
                findVO.setMerchantCodes(codes);
            }
            if (StringUtils.isNotEmpty(findVO.getBizTypeListStr())) {
                String[] str = findVO.getBizTypeListStr().split(",");
                List<Integer> list = new ArrayList<>(str.length);
                for (String s : str) {
                    list.add(Integer.valueOf(s));
                }
                findVO.setBizTypeList(list);
            }
            return Response.returnSuccess(userAccountService.accountHistoryExport(username, merchantCode, findVO));
        } catch (Exception e) {
            log.error("UserController.export error!", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }

    /**
     * 不用了
     * 商户对外增加帐变记录信息（数据中心==》帐变记录）
     *
     * @param request
     * @param findVO
     * @return
     */
    //@PostMapping("/addChangeRecordHistory")
    public Response addChangeRecordHistory(HttpServletRequest request, @RequestBody AccountChangeHistoryFindVO findVO) {
        return Response.returnSuccess(rcsUserConfigService.addChangeRecordHistory(findVO,request));
    }
}
