package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.MerchantTreeVO;
import com.panda.sport.merchant.common.vo.OperationsBannerVO;
import com.panda.sport.merchant.common.vo.OperationsVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IOperationsBannerSetService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:03:22
 */
@RestController
@Slf4j
@RequestMapping("/manage/banner")
public class MerchantOperationsController {

    @Resource
    private IOperationsBannerSetService operationsBannerSetService;

    /**
     * 获取所有商户信息
     *
     * @param paramVO paramVO
     */
    @GetMapping(value = "/merchantList")
    @AuthRequiredPermission("merchant:manage:merchantList")
    public Response<?> getMerchantList(MerchantTreeVO paramVO) {
        try {
            return Response.returnSuccess(operationsBannerSetService.getMerchantList(paramVO));
        } catch (Exception e) {
            log.error("SportMenuControllerV1.loadMenuListPC FAIL", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @RequestMapping("/getBanners")
    @AuthRequiredPermission("merchant:manage:getBanners")
    public Response<?> list(@RequestBody OperationsVO operationsVO) {
        return Response.returnSuccess(operationsBannerSetService.getBannerList(operationsVO));
    }

    @RequestMapping("/saveBanner")
    @AuthRequiredPermission("merchant:manage:saveBanner")
    public Response<?> save(HttpServletRequest request, @RequestBody OperationsBannerVO operationsBannerVO) {
        if(operationsBannerVO.getTType() != null){
            if(operationsBannerVO.getTType() != 3 && operationsBannerVO.getTType() != 4 && operationsBannerVO.getTType() != 5){
                if(operationsBannerVO.getOrderNum() == null || operationsBannerVO.getOrderNum() <= 0){
                    return Response.returnFail(ApiResponseEnum.SORT_IS_NULL);
                }
            }
        }
        return operationsBannerSetService.save(request,operationsBannerVO);
    }

    @PostMapping("/updateBanner")
    @AuthRequiredPermission("merchant:manage:updateBanner")
    public Response<?> updateSetting(HttpServletRequest request,@RequestBody OperationsBannerVO operationsBannerVO) {
        if(operationsBannerVO.getTType() != null){
            if(operationsBannerVO.getTType() != 3 && operationsBannerVO.getTType() != 4 && operationsBannerVO.getTType() != 5){
                if(operationsBannerVO.getOrderNum() == null || operationsBannerVO.getOrderNum() <= 0){
                    return Response.returnFail(ApiResponseEnum.SORT_IS_NULL);
                }
            }
        }
        if (null == operationsBannerVO.getId()) {
            return Response.returnFail(ApiResponseEnum.ID_IS_NULL);
        }
        return operationsBannerSetService.update(request,operationsBannerVO);
    }

    @PostMapping("/deleteBanner")
    @AuthRequiredPermission("merchant:manage:deleteBanner")
    public Response<?> deleteSetting(@RequestBody OperationsVO parmaVO) {
        if (null == parmaVO.getId()) {
            return Response.returnFail(ApiResponseEnum.ID_IS_NULL);
        }
        return operationsBannerSetService.delete(parmaVO);
    }

    @GetMapping("/deleteKey")
    public Response<?> deleteKey(@Param("key") String key) {
        return operationsBannerSetService.deleteKey(key);
    }
}
