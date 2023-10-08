package com.panda.sport.merchant.manage.controller;

/**
 * @author :  ives
 * @Description :  对内商户 对账工具服务类
 * @Date: 2022-02-06 21:30
 */

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.CheckToolsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author :  ives
 * @Description :  对内商户 财务中心/对账工具 服务类
 * @Date: 2022-02-10 14:25
 */
@Slf4j
@RestController
@RequestMapping("/manage/check")
public class CheckToolsController {

    @Resource
    private CheckToolsService checkToolsService;

    @PostMapping(value = "/checkFinance")
    @ApiOperation(value = "/manage/check/checkFinance", notes = "财务中心/对账工具-开始对账")
    public Response checkFinance(@RequestBody @Valid CheckToolsQueryReqVO queryReqVO){

        CheckToolsQueryRespVO queryRespVO = checkToolsService.checkFinance(queryReqVO);
        return Response.returnSuccess(queryRespVO);
    }

    @PostMapping(value = "/editFinance")
    @ApiOperation(value = "/manage/check/editFinance", notes = "财务中心/对账工具-修正对账")
    public Response editFinance(@RequestBody @Valid CheckToolsEditReqVO editReqVO){

        CheckToolsEditRespVO editFinance = checkToolsService.editFinance(editReqVO);
        return Response.returnSuccess(editFinance);
    }

    @PostMapping(value = "/checkUserFinance")
    @ApiOperation(value = "/manage/check/checkUserFinance", notes = "用户级别财务中心/对账工具-开始对账")
    public Response checkUserFinance(@RequestBody @Valid CheckToolsQueryReqVO queryReqVO){

        log.info("用户级别对账工具:{}", JSON.toJSONString(queryReqVO));

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
            throw new RuntimeException(e);
        }
        return Response.returnSuccess(queryRespVO);
    }

    @PostMapping(value = "/editUserFinance")
    @ApiOperation(value = "/manage/check/editUserFinance", notes = "用户级别财务中心/对账工具-修正对账")
    public Response editUserFinance(@RequestBody @Valid CheckToolsEditReqVO editReqVO){

        CheckToolsEditRespVO editFinance = checkToolsService.editUserFinance(editReqVO);
        return Response.returnSuccess(editFinance);
    }

}
