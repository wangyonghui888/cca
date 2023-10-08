package com.panda.sport.merchant.manage.controller;


import com.panda.sport.merchant.common.vo.AcBonusLogParam;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IAcBonusLogService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 优惠券领取日志表 前端控制器
 * </p>
 *
 * @author baylee
 * @since 2021-08-26
 */
@RestController
@RequestMapping("/manage/acBonusLog")
@Slf4j
public class AcBonusLogController {

    @Resource
    private IAcBonusLogService acBonusLogService;

    @PostMapping("/list")
    @AuthRequiredPermission("merchant:manage:acBonusLog:list")
    public Response<?> list(@RequestBody AcBonusLogParam param) {
        return acBonusLogService.pageList(param);
    }

    @PostMapping("/export")
    @AuthRequiredPermission("merchant:manage:acBonusLog:export")
    public Response<?> export(@RequestBody AcBonusLogParam param, HttpServletResponse response) {
        return acBonusLogService.export(param, response);
    }
}
