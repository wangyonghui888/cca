package com.panda.sport.admin.controller;

import com.panda.sport.merchant.admin.service.ExternalMerchantConfigService;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingEditReqVO;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingQueryReqVO;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingQueryRespVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author :  ives
 * @Description :  对外商户配置服务类
 * @Date: 2022-01-24
 */
@RestController
@RequestMapping("/admin/config")
@Slf4j
public class ExternalMerchantConfigController {

    @Resource
    private ExternalMerchantConfigService merchantConfigService;


    @PostMapping(value = "/getQueryConditionSetting")
    @ApiOperation(value = "/admin/config/getQueryConditionSetting", notes = "账户中心/商户信息管理/查看直营商户基本资料/注单查询条件设置-获取查询条件设置")
    public Response getQueryConditionSetting(@RequestBody @Valid QueryConditionSettingQueryReqVO queryReqVO){

        QueryConditionSettingQueryRespVO queryRespVO = merchantConfigService.getQueryConditionSetting(queryReqVO.getMerchantCode());
        return Response.returnSuccess(queryRespVO);
    }

    @PostMapping("/editQueryConditionSetting")
    @ApiOperation(value = "/admin/config/editQueryConditionSetting", notes = "账户中心/商户信息管理/查看直营商户基本资料/注单查询条件设置-修改查询条件设置")
    public Response editQueryConditionSetting(@RequestBody @Valid QueryConditionSettingEditReqVO editReqVO){

        merchantConfigService.editQueryConditionSetting(editReqVO);
        return Response.returnSuccess();
    }
}
