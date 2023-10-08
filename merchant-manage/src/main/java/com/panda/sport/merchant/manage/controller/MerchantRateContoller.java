package com.panda.sport.merchant.manage.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.RequestPageVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantRateVO;
import com.panda.sport.merchant.manage.service.MerchantRateService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/manage/merchantRate")
@Slf4j
public class MerchantRateContoller {
    @Autowired
    private MerchantRateService merchantRateService;

    /**
     * 查询费率列表
     */
    @PostMapping("/queryList")
    @AuthRequiredPermission("Merchant:Manage:rate:list")
    public Response<PageVO<List>> queryList(@RequestBody RequestPageVO<MerchantRateVO> pageVO) {
        try {
            Page<Object> page = PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
            return Response.returnSuccess(new PageVO(page, merchantRateService.queryList(pageVO.getParam())));
        } catch (Exception e) {
            log.error("MerchantRateContoller.queryList error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    @GetMapping("/queryRateList")
    @AuthRequiredPermission("Merchant:Manage:rate:list")
    public Response<Object> queryRateList() {
        try {
            return Response.returnSuccess(merchantRateService.queryRateList());
        } catch (Exception e) {
            log.error("MerchantRateContoller.queryRateList error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 新增修改
     */
    @PostMapping("/add")
    @AuthRequiredPermission("Merchant:Manage:rate:add")
    public Response add(HttpServletRequest request, @RequestBody MerchantRateVO merchantRateVO) {
        try {
            if (merchantRateVO.getComputingStandard() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            if (merchantRateVO.getRangeAmountBegin() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            if (merchantRateVO.getRangeAmountEnd() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            if (merchantRateVO.getTerraceRate() == null) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return Response.returnSuccess(merchantRateService.add(merchantRateVO,request));
        } catch (Exception e) {
            log.error("MerchantRateContoller.add error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 新增修改
     */
    @PostMapping("/update")
    @AuthRequiredPermission("Merchant:Manage:level:update")
    public Response update(HttpServletRequest request, @RequestBody MerchantRateVO merchantRateVO) {
        try {
            if (merchantRateVO.getId() == null) {
                return Response.returnFail(ResponseEnum.INTERNAL_ERROR, "更新ID不能为空");
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return Response.returnSuccess(ResponseEnum.SUCCESS.getId(), "已处理", merchantRateService.update(merchantRateVO, SsoUtil.getUserId(request), language,IPUtils.getIpAddr(request)));
        } catch (Exception e) {
            log.error("MerchantRateContoller.update error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }
}
