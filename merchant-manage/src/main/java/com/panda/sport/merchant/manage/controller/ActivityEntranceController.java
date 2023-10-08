package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.activity.ActivityMaintainVO;
import com.panda.sport.merchant.manage.entity.form.MerchantTreeForm;
import com.panda.sport.merchant.manage.service.ActivityEntranceService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/manage/activityEntrance")
@Slf4j
public class ActivityEntranceController {

    @Autowired
    private ActivityEntranceService activityEntranceService;

    /**
     * 活动入口设置列表
     */
    @PostMapping("/queryList")
    public Response queryList(@RequestBody ActivityEntranceVO pageVO) {
        try {
            log.info("活动入口设置列表==>{}", JSON.toJSONString(pageVO));
            //Page<Object> page = PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
            return activityEntranceService.queryList(pageVO);
            //return Response.returnSuccess(new PageVO(page, activityEntranceService.queryList(pageVO)));
        } catch (Exception e) {
            log.error("ActivityEntranceController.queryList error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 活动入口设置列表
     */
    @GetMapping("/queryActivityEntranceList")
    public Response queryActivityEntranceList() {
        try {
            return activityEntranceService.queryActivityEntranceList();
        } catch (Exception e) {
            log.error("ActivityEntranceController.queryList error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 活动更新设置
     * status 1开启，0关闭
     */
    @GetMapping("/update")
    public Response update(HttpServletRequest request,
                           @RequestParam(value = "id", required = true) Long id,
                           @RequestParam(value = "status") Integer status) {
        try {
            //log.info("活动更新设置==>{}==>{}", activityId,status);
            if (status == 1) {
                Boolean flag = activityEntranceService.queryActivityConfigPoById(id);
                if (!flag) {
                    return Response.returnFail(ResponseEnum.NOT_ALLOW_UPDATE);
                }
            }
            activityEntranceService.update(request, id, status);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("ActivityEntranceController.update info: error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 活动入口配置保存
     */
    @PostMapping("/save")
    @AuthRequiredPermission("activity:entrance:config")
    public Response save(HttpServletRequest request, @RequestBody ActivityConfigVO activityConfigVO) {
        try {
            log.info("活动入口配置保存==>{}", JSON.toJSONString(activityConfigVO));
            if (StringUtil.isNotBlank(activityConfigVO.getActivityMerchants()) && "all".equals(activityConfigVO.getActivityMerchants())) {
                activityConfigVO.setActivityMerchant(1);
            }
            Response save = activityEntranceService.save(request, activityConfigVO);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("ActivityEntranceController.update info: error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 活动配置查询
     */
    @GetMapping("/detail")
    public Response<ActivityMerchantVO> detail(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "merchantCode", required = true) String merchantCode) {
        try {
            return Response.returnSuccess(activityEntranceService.detail(id, merchantCode));
        } catch (Exception e) {
            log.error("ActivityEntranceController detail.detail error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 活动类型检查
     * status 1开启，2关闭
     */
    @GetMapping("/activityCheck")
    public Response activityCheck(@RequestParam(value = "activityId", required = true) Long activityId) {
        try {
            log.info("活动类型检查==>{}", JSON.toJSONString(activityId));
            Boolean flag = activityEntranceService.activityCheckById(activityId);
            //true证明该活动处于活动中状态（即活动时间未结束），false 证明可以编辑活动已结束
            if (flag) {
                return Response.returnFail(ResponseEnum.FAIL);
            }
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("ActivityEntranceController.activityCheck info: error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 活动配置保存
     */
    @PostMapping("/activityConfigUpdate")
    @AuthRequiredPermission("activity:config")
    public Response activityConfigUpdate(HttpServletRequest request, @RequestBody ActivityMerchantVO activityMerchantVO) {
        try {
            log.info("activityConfigUpdate活动配置保存==>{}", JSON.toJSONString(activityMerchantVO));
            activityEntranceService.activityMechantUpdate(activityMerchantVO, request);
        } catch (Exception e) {
            log.error("ActivityEntranceController.activityConfigUpdate info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 商户联级查询列表
     */
    @PostMapping("/getActivityMerchantTree")
    public Response getActivityMerchantTree(@RequestBody MerchantTreeForm merchantTreeForm, BindingResult bindingResult) {
        return activityEntranceService.getMerchantListTree(merchantTreeForm);
    }

    /**
     * 获取所有活动商户的code  ****无效
     */
    @GetMapping("/getActivityMerchantCode")
    public Response getActivityMerchantCode() {
        try {
            log.info("获取所有使用的活动商户的code");
            return activityEntranceService.getActivityMerchantCode();
        } catch (Exception e) {
            log.error("获取商户树失败!", e);
            return Response.returnFail("获取商户树失败");
        }
    }

    /**
     * 查询活动时间
     */
    @GetMapping("/queryActivityTime")
    public Response queryActivityTime() {
        try {
            return Response.returnSuccess(activityEntranceService.queryActivityTime());
        } catch (Exception e) {
            log.error("ActivityEntranceController.queryActivityTime info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 活动时间设置
     */
    @PostMapping("/activityTimeUpdate")
    public Response activityTimeUpdate(HttpServletRequest request, @RequestBody ActivityTimeVO activityTimeVO) {
        try {
            log.info("活动时间设置==>{}", JSON.toJSONString(activityTimeVO));
            return activityEntranceService.activityTimeUpdate(activityTimeVO, request);
        } catch (Exception e) {
            log.error("ActivityEntranceController.activityTimeUpdate info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 一键开启/关闭商户入口
     */
    @PostMapping("/onceOperationMerchantEntrance")
    @AuthRequiredPermission("activity:update:all:merchant:entrance")
    public Response onceOperationMerchantEntrance(HttpServletRequest request, @RequestBody ActivityStatusVO activityStatusVO) {
        try {
            if (StringUtil.isBlankOrNull(activityStatusVO.getActivityMerchants())) {
                return Response.returnFail(ResponseEnum.FAIL);
            }
            log.info("一键开启/关闭商户入口==>{}", JSON.toJSONString(activityStatusVO));
            activityEntranceService.onceOperationMerchantUpdate(activityStatusVO, request);
        } catch (Exception e) {
            log.error("ActivityEntranceController.onceOpenMerchant info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 一键开启/关闭活动入口
     */
    @PostMapping("/onceOperationMerchantActivity")
    @AuthRequiredPermission("activity:update:all:merchant:activity")
    public Response onceOperationMerchantActivity(HttpServletRequest request, @RequestBody ActivityStatusVO activityStatusVO) {
        try {
            if (StringUtil.isBlankOrNull(activityStatusVO.getActivityMerchants())) {
                return Response.returnFail(ResponseEnum.FAIL);
            }
            log.info("一键开启/关闭活动入口==>{}", JSON.toJSONString(activityStatusVO));
            activityEntranceService.onceOperationMerchantActivity(activityStatusVO, request);
        } catch (Exception e) {
            log.error("ActivityEntranceController.onceOperationMerchantActivity info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 一键delete
     */
    @PostMapping("/onceOperationMerchantDelete")
    @AuthRequiredPermission("activity:delete:all:merchant:activity")
    public Response onceOperationMerchantDelete(HttpServletRequest request, @RequestBody ActivityStatusVO activityStatusVO) {
        try {
            if (StringUtil.isBlankOrNull(activityStatusVO.getActivityMerchants())) {
                return Response.returnFail(ResponseEnum.FAIL);
            }
            log.info("一键delete==>{}", JSON.toJSONString(activityStatusVO));
            activityEntranceService.onceOperationMerchantDelete(activityStatusVO, request);
        } catch (Exception e) {
            log.error("ActivityEntranceController.onceOperationMerchantDelete info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 活动维护时间设置
     */
    @PostMapping("/activityMaintain")
    public Response activityMaintain(HttpServletRequest request, @RequestBody ActivityMaintainVO activityMaintainVO) {
        try {
            log.info("activityMaintain活动时间设置==>" + activityMaintainVO);
            activityEntranceService.activityMaintain(activityMaintainVO,request);
        } catch (Exception e) {
            log.error("ActivityEntranceController.activityMaintain info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 查询活动维护时间
     */
    @GetMapping("/getActivityMaintain")
    public Response getActivityMaintain(HttpServletRequest request) {
        try {
            log.info("getActivityMaintain活动时间设置==>");
            return Response.returnSuccess(activityEntranceService.getActivityMaintain());
        } catch (Exception e) {
            log.error("ActivityEntranceController.getActivityMaintain info: error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }
}
