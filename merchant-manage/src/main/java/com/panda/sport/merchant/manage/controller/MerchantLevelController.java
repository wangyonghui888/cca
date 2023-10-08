package com.panda.sport.merchant.manage.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantLevelPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.RequestPageVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantLevelVO;
import com.panda.sport.merchant.manage.service.MerchantLevelService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/manage/merchantLevel")
@Slf4j
public class MerchantLevelController {

    @Autowired
    private MerchantLevelService merchantLevelService;

    /**
     * 商户等级表-查询费率列表
     */
    @PostMapping("/queryList")
    @AuthRequiredPermission("Merchant:Manage:level:query:list")
    public Response<PageVO<MerchantLevelVO>> queryList(@RequestBody RequestPageVO<MerchantLevelVO> pageVO) {
        try {
            Page<Object> page = PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
            List<MerchantLevelVO> returnData = merchantLevelService.queryList(pageVO.getParam());
            return Response.returnSuccess(new PageVO(page, returnData));
        } catch (Exception e) {
            log.error("MerchantLevelController.queryList error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 商户等级表-查询费率列表
     */
    @PostMapping("/{id}")
    @AuthRequiredPermission("Merchant:Manage:level:detail")
    public Response<MerchantLevelVO> detail(HttpServletRequest request, @PathVariable(value = "id") String id) {
        try {
            return merchantLevelService.detail(id);
        } catch (Exception e) {
            log.error("MerchantLevelController.detail error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 商户等级表-查询费率列表
     */
    @GetMapping("/queryLevelList")
    @AuthRequiredPermission("Merchant:Manage:level:list")
    public Response<List<MerchantLevelPO>> queryLevelList() {
        try {
            return Response.returnSuccess(merchantLevelService.queryLevelList());
        } catch (Exception e) {
            log.error("MerchantLevelController.queryLevelList error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 商户等级表-查询费率
     */
    @GetMapping("/getMerchantLevel")
    @AuthRequiredPermission("Merchant:Manage:rate:detail")
    public Response<MerchantLevelPO> getMerchantLevel(@RequestParam(value = "level") Integer level) {
        try {
            return level == null ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    Response.returnSuccess(merchantLevelService.getMerchantLevel(level));
        } catch (Exception e) {
            log.error("MerchantLevelController.getMerchantLevel error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 商户等级表-新增
     */
    @PostMapping("/add")
    @AuthRequiredPermission("Merchant:Manage:level:add")
    public Response add(HttpServletRequest request, @RequestBody MerchantLevelVO merchantLevelVO) {
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return merchantLevelVO.getLevel() == null ?
                    Response.returnFail(ResponseEnum.PARAMETER_INVALID) : merchantLevelService.add(merchantLevelVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));

        } catch (Exception e) {
            log.error("MerchantLevelController.addUpdate error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 商户等级表-修改
     */
    @PostMapping("/update")
    @AuthRequiredPermission("Merchant:Manage:level:update")
    public Response update(HttpServletRequest request, @RequestBody MerchantLevelVO merchantLevelVO) {
        try {
            if (merchantLevelVO.getId() == null) {
                return Response.returnFail(ResponseEnum.INTERNAL_ERROR, "更新ID不能为空");
            }
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            log.info("/manage/merchantLevel/update info:" + merchantLevelVO.toString());
            return Response.returnSuccess(merchantLevelService.update(merchantLevelVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request)));
        } catch (Exception e) {
            log.error("MerchantLevelController.update info: error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 利率
     *
     * @return
     */
    @PostMapping("/currencyRate")
    public Response currencyRate() {
        try {
            log.info("query:manage/merchantLevel/currencyRate");
            return Response.returnSuccess(merchantLevelService.currencyRateList());
        } catch (Exception e) {
            log.error("MerchantLevelController.currencyRate: error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }
}
