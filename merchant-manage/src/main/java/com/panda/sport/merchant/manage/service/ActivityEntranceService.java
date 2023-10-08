package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.enums.BaseEnum;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.activity.ActivityMaintainVO;
import com.panda.sport.merchant.manage.entity.form.MerchantTreeForm;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : dorf
 * @Date: 2021-08-20
 * @Description :活动入口设置
 */
public interface ActivityEntranceService {

    /**
     * 查询活动入口列表
     */
    Response queryList(ActivityEntranceVO activityEntranceVO);

    /**
     * 活动设置
     * @param request
     * @param id
     * @param status
     */
    void update(HttpServletRequest request, Long id, Integer status);

    /**
     * 核查是否是活动时间范围内
     * @param activityId
     * @return
     */
    Boolean queryActivityConfigPoById(Long activityId);

    Response save(HttpServletRequest request, ActivityConfigVO activityConfigVO);

    /**
     * 活动配置查询
     * @param id
     * @param merchantCode
     * @return
     */
    ActivityMerchantVO detail(Long id, String merchantCode);

    /**
     * 核查活动是否在该时间内
     * @param activityId
     * @return
     */
    Boolean activityCheckById(Long activityId);

    void activityMechantUpdate(ActivityMerchantVO activityMerchantVO, HttpServletRequest request);

    /**
     * 活动时间设置
     * @param activityTimeVO
     * @param request
     */
    Response<?> activityTimeUpdate(ActivityTimeVO activityTimeVO, HttpServletRequest request);

    ActivityTimeVO queryActivityTime();

    Response getMerchantListTree(MerchantTreeForm merchantTreeForm);

    Response getActivityMerchantCode();

    Response queryActivityEntranceList();

    void onceOperationMerchantUpdate(ActivityStatusVO activityStatusVO, HttpServletRequest request);

    void onceOperationMerchantActivity(ActivityStatusVO activityStatusVO, HttpServletRequest request);

    void onceOperationMerchantDelete(ActivityStatusVO activityStatusVO, HttpServletRequest request);

    void activityMaintain(ActivityMaintainVO activityMaintainVO,HttpServletRequest request) throws Exception;

    Object getActivityMaintain();
}
