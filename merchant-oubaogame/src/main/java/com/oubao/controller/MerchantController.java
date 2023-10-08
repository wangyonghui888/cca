package com.oubao.controller;

import com.oubao.po.MerchantPO;
import com.oubao.po.UserApiVo;
import com.oubao.service.MerchantService;
import com.oubao.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    private MerchantService merchantService;


    @PostMapping(value = "/createMerchant")
    public APIResponse<UserApiVo> createMerchant(HttpServletRequest request,
                                                 @RequestBody MerchantPO merchantPO) {
        try {
            String merchantCode = merchantPO.getMerchantCode();
            String merchantKey = merchantPO.getMerchantKey();
            Integer transferMode = merchantPO.getTransferMode();
            if (StringUtils.isAnyEmpty(merchantCode, merchantKey) || transferMode == null) {
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            log.info("merchantCode=" + merchantCode + ",merchantKey=" + merchantKey + ",transferMode=" + transferMode);
            merchantService.createMerchant(merchantCode, merchantKey, transferMode);
            return APIResponse.returnSuccess(ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.login,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping(value = "/updateMerchant")
    public APIResponse<Object> updateMerchant(HttpServletRequest request, @RequestBody MerchantPO merchantPO) {
        try {
            String merchantCode = merchantPO.getMerchantCode();
            String merchantKey = merchantPO.getMerchantKey();
            Integer transferMode = merchantPO.getTransferMode();
            log.info("updateMerchant:---" + merchantCode + ",---" + merchantKey + ",---" + transferMode);
            if (StringUtils.isEmpty(merchantCode)) {
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            merchantService.updateMerchant(merchantCode, merchantKey, transferMode);
            return APIResponse.returnSuccess(ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.login,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }
}
