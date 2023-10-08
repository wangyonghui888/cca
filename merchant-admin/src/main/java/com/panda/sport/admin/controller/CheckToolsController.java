package com.panda.sport.admin.controller;

/**
 * @author :  ives
 * @Description :  对账工具服务类
 * @Date: 2022-02-06 21:30
 */

import com.panda.sport.admin.service.CheckToolsService;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author :  ives
 * @Description :  对外商户 财务中心/对账工具 服务类
 * @Date: 2022-02-10 14:25
 */
@Slf4j
@RestController
@RequestMapping("/admin/check")
public class CheckToolsController {

    @Resource
    private CheckToolsService checkToolsService;

    @PostMapping(value = "/checkFinance")
    @ApiOperation(value = "/admin/check/checkFinance", notes = "财务中心/对账工具-开始对账")
    public Response checkFinance(@RequestBody @Valid CheckToolsQueryReqVO queryReqVO){

        CheckToolsQueryRespVO queryRespVO = checkToolsService.checkFinance(queryReqVO);
        return Response.returnSuccess(queryRespVO);
    }

    @PostMapping(value = "/editFinance")
    @ApiOperation(value = "/admin/check/editFinance", notes = "财务中心/对账工具-修正对账")
    public Response editFinance(@RequestBody @Valid CheckToolsEditReqVO editReqVO){

        CheckToolsEditRespVO editFinance = checkToolsService.editFinance(editReqVO);
        return Response.returnSuccess(editFinance);
    }


    @PostMapping(value = "/checkUserFinance")
    @ApiOperation(value = "/admin/check/checkUserFinance", notes = "财务中心/用户级别对账工具-开始对账")
    public Response checkUserFinance(@RequestBody @Valid CheckToolsQueryReqVO queryReqVO){

        CheckToolsQueryRespVO queryRespVO = null;
        try {
            if(StringUtil.isBlankOrNull(queryReqVO.getDateTimeType())){
                return Response.returnFail("日期类型不能为空");
            }
            if (queryReqVO.getUserId() == null  && StringUtil.isBlankOrNull(queryReqVO.getUserName())){
                return Response.returnFail("用户Id和用户名不能都为空");
            }
            queryRespVO = checkToolsService.checkUserFinance(queryReqVO);
        } catch (Exception e) {
            return Response.returnFail(e.getMessage());
        }
        return Response.returnSuccess(queryRespVO);
    }

    @PostMapping(value = "/editUserFinance")
    @ApiOperation(value = "/admin/check/editUserFinance", notes = "财务中心/用户级别对账工具-修正对账")
    public Response editUserFinance(@RequestBody @Valid CheckToolsEditReqVO editReqVO){

        CheckToolsEditRespVO editFinance = checkToolsService.editUserFinance(editReqVO);
        return Response.returnSuccess(editFinance);
    }

}
