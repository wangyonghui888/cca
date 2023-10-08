package com.panda.sport.admin.controller;

/**
 * @author :  ives
 * @Description :  平台用户风控服务类
 * @Date: 2022-04-09
 */

import com.panda.sport.admin.service.UserRiskControlService;
import com.panda.sport.merchant.common.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author :  ives
 * @Description :  对外商户 账户中心/平台用户风控服务类
 * @Date: 2022-02-10 14:25
 */
@Slf4j
@RestController
@RequestMapping("/admin/userRiskControl")
public class UserRiskControlController {

    @Resource
    private UserRiskControlService userRiskControlService;

    @PostMapping(value = "/queryPageUserRiskControlList")
    @ApiOperation(value = "/admin/userRiskControl/queryPageUserRiskControlList", notes = "账户中心/平台用户风控-查询用户风控分页列表")
    public Response queryPageUserRiskControlList(@RequestBody @Valid UserRiskControlPageQueryReqVO queryReqVO){

        return userRiskControlService.queryPageUserRiskControlList(queryReqVO);
    }

    @PostMapping(value = "/updateUserRiskControlStatus")
    @ApiOperation(value = "/admin/userRiskControl/updateUserRiskControlStatus", notes = "账户中心/平台用户风控-修改用户风控状态")
    public Response updateUserRiskControlStatus(@RequestBody @Valid UserRiskControlStatusEditReqVO editReqVO){

        return userRiskControlService.updateUserRiskControlStatus(editReqVO);
    }

    @PostMapping("/exportUserRiskControlList")
    @ApiOperation(value = "/admin/userRiskControl/exportUserRiskControlList", notes = "商户管控记录表导出")
    public void exportUserRiskControlList(@RequestBody UserRiskControlQueryAllReqVO queryAllReqVO,
                                          HttpServletResponse httpServletResponse){

        userRiskControlService.exportUserRiskControlList(queryAllReqVO,httpServletResponse);
    }

    @PostMapping("/importUserRiskControlList")
    @ApiOperation(value = "/admin/userRiskControl/importUserRiskControlList", notes = "商户管控记录表批量导入")
    public Response importUserRiskControlList(HttpServletRequest httpServletRequest, @RequestPart(value = "multipartFile") MultipartFile multipartFile){

        return userRiskControlService.importUserRiskControlList(httpServletRequest,multipartFile);
    }

    @GetMapping("/pendingCount")
    @ApiOperation(value = "/admin/userRiskControl/pendingCount", notes = "获取待处理状态的风控消息数量")
    public Response pendingCount() {
        return userRiskControlService.pendingCount();
    }
}
