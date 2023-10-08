package com.panda.multiterminalinteractivecenter.controller;


import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.base.BaseEnum;
import com.panda.multiterminalinteractivecenter.dto.LineCarrierDTO;
import com.panda.multiterminalinteractivecenter.entity.LineCarrier;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import com.panda.multiterminalinteractivecenter.service.impl.LineCarrierServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lineCarrier")
@Slf4j
public class LineCarrierController {

    @Autowired
    private LineCarrierServiceImpl lineCarrierService;

    @PostMapping("/saveLineCarrier")
    public APIResponse<Object> saveLineCarrier(HttpServletRequest request, @RequestBody LineCarrier lineCarrier){
        if(StringUtils.isEmpty(lineCarrier.getLineCarrierName())){
            return APIResponse.returnFail("参数异常!");
        }
        if(StringUtils.isEmpty(lineCarrier.getTab())) lineCarrier.setTab(TabEnum.TY.getName());
        String currentUser = JWTUtil.getUsername(request.getHeader("token"));
        lineCarrier.setCreateBy(currentUser);
        lineCarrier.setUpdateBy(currentUser);
        return APIResponse.returnSuccess(lineCarrierService.saveLineCarrier(lineCarrier, request));
    }

    @PostMapping("/getLineCarrierList")
    public APIResponse<Object> getLineCarrierList(HttpServletRequest request, @RequestBody LineCarrier lineCarrier){
        return lineCarrierService.getLineCarrierList(lineCarrier);
    }

    @PostMapping("/updateLineCarrier")
    public APIResponse<Object> updateLineCarrier(HttpServletRequest request, @RequestBody LineCarrier lineCarrier){
        if(lineCarrier.getId() == null || StringUtils.isEmpty(lineCarrier.getLineCarrierName())){
            return APIResponse.returnFail("参数异常!");
        }
        String currentUser = JWTUtil.getUsername(request.getHeader("token"));
        lineCarrier.setUpdateBy(currentUser);
        lineCarrierService.updateLineCarrier(lineCarrier, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping("/updateLineCarrierStatus")
    public APIResponse<Object> updateLineCarrierStatus(HttpServletRequest request, @RequestBody LineCarrier lineCarrier){
        if(lineCarrier.getId() == null || lineCarrier.getLineCarrierStatus() == null){
            return APIResponse.returnFail("参数异常!");
        }
        String currentUser = JWTUtil.getUsername(request.getHeader("token"));
        lineCarrier.setUpdateBy(currentUser);
        return APIResponse.returnSuccess(lineCarrierService.updateLineCarrierStatus(lineCarrier, request));
    }

    @PostMapping("/queryLineCarrier")
    public APIResponse<Object> queryLineCarrier(HttpServletRequest request, @RequestBody LineCarrier lineCarrier){
        if(lineCarrier.getId() == null){
            return APIResponse.returnFail("参数异常!");
        }
        return lineCarrierService.queryLineCarrier(lineCarrier);
    }

    @PostMapping("/delLineCarrierById/{id}")
    public APIResponse<Object> delLineCarrierById(HttpServletRequest request, @PathVariable Long id){
        if(id == null){
            return APIResponse.returnFail("参数异常!");
        }
        String msg = lineCarrierService.delLineCarrierById(id, request);
        if(StringUtils.isNotBlank(msg)){
            return APIResponse.returnFail(msg);
        }
        return APIResponse.returnSuccess();
    }

    @GetMapping("/queryLineCarrierList")
    public APIResponse<Object> queryLineCarrierList(@RequestParam(required = false) String tab,
                                                    @RequestParam(required = false) String name
                                                    ){
        return lineCarrierService.queryLineCarrierList(tab,name);
    }

    @PostMapping("/validate")
    public APIResponse<Boolean> validate(@RequestBody LineCarrierDTO lineCarrierDTO){
        if(lineCarrierDTO.getStep() == 1){
            return lineCarrierService.validateThreshold(lineCarrierDTO);
        }
        return lineCarrierService.validateSwitch(lineCarrierDTO);
    }

    @PostMapping("/ASwitch")
    public APIResponse<Boolean> ASwitch(HttpServletRequest request, @RequestBody LineCarrierDTO lineCarrierDTO){
        lineCarrierDTO.setStep(2);
        APIResponse apiResponse = lineCarrierService.validateSwitch(lineCarrierDTO);
        if(apiResponse.getData().equals(false)){
            return apiResponse;
        }
        if(lineCarrierDTO.getId()==null||
                lineCarrierDTO.getTab()==null||
                lineCarrierDTO.getId().equals(lineCarrierDTO.getTargetId())){
            return APIResponse.returnSuccess(ApiResponseEnum.PARAMETER_INVALID);
        }
        return lineCarrierService.ASwitch(lineCarrierDTO, request);
    }

}
