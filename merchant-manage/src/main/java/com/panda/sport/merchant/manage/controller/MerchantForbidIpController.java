package com.panda.sport.merchant.manage.controller;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IDomainService;
import com.panda.sport.merchant.manage.service.IForbidIpService;
import com.panda.sport.merchant.manage.service.IOssDomainService;
import com.panda.sport.merchant.manage.service.impl.LoginUserService;
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

/**
 * @author baylee
 * @version 1.0
 * @date 2021/5/27 17:34:05
 */
@RestController
@RequestMapping("/manage/forbidip")
@Slf4j
@Validated
public class MerchantForbidIpController {

    @Resource
    private IForbidIpService forbidIpService;

    @Autowired
    private LoginUserService loginUserService;


    @PostMapping("/delete")
    //@AuthRequiredPermission("Domain:Manage:deleteDomain")
    public Response delete(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        log.info("删除 /deleteDomain param = {}", id);
        forbidIpService.deleteIp(request,id);
        return Response.returnSuccess("删除成功");
    }


    @PostMapping("/deletebyname")
    //@AuthRequiredPermission("Domain:Manage:deleteDomain")
    public Response deleteByName(HttpServletRequest request, @RequestParam(value = "ipName") String ipName) {
        log.info("删除 /deleteDomain param = {}", ipName);
        forbidIpService.deleteIpByName(request,ipName);
        return Response.returnSuccess("删除成功");
    }

    @PostMapping("/queryIp")
    //@AuthRequiredPermission("Domain:Manage:queryDomain")
    public Response queryIp(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("域名查询 /queryDomain param = {}", JSON.toJSONString(domainVo));
        Response response = forbidIpService.queryList(domainVo);
        return response;
    }

    @PostMapping(value = "/save")
    public Response save(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        Integer userId = SsoUtil.getUserId(request);
        String username = loginUserService.getLoginUser(userId);
        if (StringUtils.isNotEmpty(domainVo.getUsername())){
            username = domainVo.getUsername();
        }
        return forbidIpService.saveIp(request,domainVo,username);
    }

}
