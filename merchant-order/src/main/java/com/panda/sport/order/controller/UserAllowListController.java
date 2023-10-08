package com.panda.sport.order.controller;

import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.dto.UserAllowListReq;
import com.panda.sport.merchant.common.enums.UserAllowListSourceEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.order.service.UserAllowListService;
import com.panda.sports.auth.util.SsoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/order/allowlist/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAllowListController {

    private final UserAllowListService userAllowListService;

    @GetMapping(value = "/source/list")
    public Response sourceList() {
        return Response.returnSuccess(UserAllowListSourceEnum.getUseSource());
    }

    @GetMapping(value = "list")
    public Response list(HttpServletRequest request, @SpringQueryMap UserAllowListReq req) {
        log.info("/order/allowlist/user/list:" + req);
        String merchantName = request.getHeader("merchantName");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        req.setDisabled(req.getAllowListSource());
        return Response.returnSuccess(userAllowListService.listAll(merchantName, req, language));
    }

    @PostMapping(value = "del")
    public Response del(HttpServletRequest request, @RequestBody UserAllowListReq req) {
        log.info("/order/allowlist/user/del:req {} ; operator {}" , req, SsoUtil.getUserId(request));
        if(StringUtils.isBlank(req.getUserId()) ){
            return Response.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        return Response.returnSuccess(userAllowListService.del(req));
    }

    @PostMapping(value = "delAll")
    public Response delAll(HttpServletRequest request) {
        log.info("/order/allowlist/user/delAll: operator {}" , SsoUtil.getUserId(request));
        return Response.returnSuccess(userAllowListService.delAll());
    }

    @PostMapping(value = "import")
    public Response importUser(HttpServletRequest request,@RequestBody UserAllowListReq req) {
        log.info("/order/allowlist/user/delAll:req {} operator {}" ,req, SsoUtil.getUserId(request));
        if(CollectionUtils.isEmpty(req.getUserIdList()) || req.getAllowListSource() == null ){
            return Response.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        req.setDisabled(req.getAllowListSource());
        return Response.returnSuccess(userAllowListService.importUser(req));
    }

    @PostMapping(value = "export")
    public Response export(HttpServletRequest request,@RequestBody(required = false) UserAllowListReq req) {
        log.info("/order/allowlist/user/export:req {} operator {}" ,req, SsoUtil.getUserId(request));
        if(req == null){
            req = new UserAllowListReq();
        }
        req.setDisabled(req.getAllowListSource());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            try {
                resultMap.put("code", "0000");
                resultMap.put("msg", "投注用户白名单导出,请在文件列表等候下载！");
                String language = request.getHeader("language");
                if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
                req.setLanguage(language);
                userAllowListService.exportAllowListUserList(request.getHeader("merchantName"), request, req);
            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
        } catch (Exception e) {
            log.error("/order/allowlist/user/export.export error!", e);
        }
        return Response.returnSuccess(resultMap);
    }



}
