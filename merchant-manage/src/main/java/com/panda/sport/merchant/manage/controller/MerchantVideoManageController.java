package com.panda.sport.merchant.manage.controller;


import com.panda.sport.merchant.common.vo.MerchantVideoManageVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IMerchantVideoManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/merchantVideoManage")
public class MerchantVideoManageController {

    @Autowired
    private IMerchantVideoManageService videoManageService;

    @PostMapping("/saveMerchantVideoManage")
    public Response saveMerchantVideoManage(@RequestBody MerchantVideoManageVo videoManageVo){
        videoManageService.saveMerchantVideoManage(videoManageVo);
        return Response.returnSuccess();
    }

    @GetMapping("/getMerchantVideoManage")
    public Response<MerchantVideoManageVo> getMerchantVideoManage(@Param("merchantCode") String merchantCode){
        if(StringUtils.isEmpty(merchantCode)){
            return null;
        }
        return Response.returnSuccess(videoManageService.getMerchantVideoManage(merchantCode));
    }

    @PostMapping("/getVideoManageList")
    public Response<Object> getVideoManageList(@RequestBody MerchantVideoManageVo videoManageVo){
        return videoManageService.getVideoManageList(videoManageVo);
    }

    @PostMapping("/batchUpdateMerchantVideoManage")
    public Response batchUpdateMerchantVideoManage(@RequestBody MerchantVideoManageVo videoManageVo){
        videoManageService.batchUpdateMerchantVideoManage(videoManageVo);
        return Response.returnSuccess();
    }

    @PostMapping("/getMerchantVideoManageList")
    public Response<Object> getMerchantVideoManageList(){
        return videoManageService.getMerchantVideoManageList();
    }

    @PostMapping("/updateMerchantVideoManage")
    public Response updateMerchantVideoManage(HttpServletRequest request, @RequestBody MerchantVideoManageVo videoManageVo){
        videoManageService.updateMerchantVideoManage(request,videoManageVo);
        return Response.returnSuccess();
    }
}
