package com.panda.center.controller;

import com.panda.center.result.Response;
import com.panda.center.service.IAcBonusLogService;
import com.panda.center.param.AcBonusLogParam;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/25 14:10:47
 */
@RestController
@RequestMapping("/manage/dj/acBonusLog")
@Slf4j
public class ESportAcBonusLogController {
    @Resource
    private IAcBonusLogService acBonusLogService;

    @PostMapping("/list")
    @AuthRequiredPermission("merchant:dj:manage:acBonusLog:list")
    public Response<?> list(@RequestBody AcBonusLogParam param) {
        return acBonusLogService.pageList(param);
    }

    @PostMapping("/export")
    @AuthRequiredPermission("merchant:dj:manage:acBonusLog:export")
    public Response<?> export(@RequestBody AcBonusLogParam param, HttpServletResponse response) {
        return acBonusLogService.export(param, response);
    }
}
