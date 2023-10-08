package com.panda.center.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.panda.center.entity.AcTask;
import com.panda.center.entity.MerchantLog;
import com.panda.center.param.AcTaskVO;
import com.panda.center.result.Response;
import com.panda.center.service.IAcTaskService;
import com.panda.center.service.IMerchantLogService;
import com.panda.center.vo.SportAndPlayTreeVO;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/24 13:36:11
 */
@RequestMapping("/manage/dj/activity")
@RestController
@Slf4j
public class ESportActivityController {
    @Resource
    private IAcTaskService acTaskService;
    @Resource
    private IMerchantLogService merchantLogService;

    @GetMapping("/getSportAndPlayTree")
    public Response<List<SportAndPlayTreeVO>> getSportAndPlayTree() {
        return Response.returnSuccess(acTaskService.getSportAndPlayTree());
    }

    @PostMapping("/list")
    @AuthRequiredPermission("merchant:dj:manage:acTask:list")
    public Response<?> list(@RequestParam(name = "actId") Integer actId,
							@RequestParam(name = "invalidation", required = false) Integer invalidation,
							@RequestParam(name = "page", defaultValue = "1") Integer page,
							@RequestParam(name = "size", defaultValue = "20") Integer size) {
        PageHelper.startPage(page, size);
        QueryWrapper<AcTask> wrapper = new QueryWrapper<AcTask>().eq("act_id", actId).orderByDesc("order_no");
        if (!Objects.isNull(invalidation)) {
            wrapper.eq("invalidation", invalidation);
        }
        List<AcTask> list = acTaskService.list(wrapper);
        PageInfo<AcTask> pageInfo = new PageInfo<>(list);
        return Response.returnSuccess(pageInfo);
    }

    @PostMapping("/add")
    @AuthRequiredPermission("merchant:dj:manage:acTask:add")
    public Response<?> add(@RequestBody AcTaskVO acTaskVO, HttpServletRequest request) {
        String actName = "";
        if (acTaskVO.getActId() == 1) {
            actName = "每日任务";
        }
        if (acTaskVO.getActId() == 2) {
            actName = "成长任务";
        }
        acTaskVO.setActName(actName);
        AcTask acTaskPO = convertObj(acTaskVO);
        long curTime = System.currentTimeMillis();
        acTaskPO.setCreateTime(curTime);
        acTaskPO.setUpdateTime(curTime);
        //查询当前排序最大值
        Integer curMaxNo = acTaskService.getCurMaxNo(acTaskVO.getActId());
        acTaskPO.setOrderNo(curMaxNo + 1);
        acTaskService.save(acTaskPO);
        //写入日志
        try {
            MerchantLog logPO = new MerchantLog();
            Map<String, String> userInfo = extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(Long.valueOf(userId));
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(99);
            logPO.setTypeName("新增任务");
            logPO.setPageName("电竞运营管理—" + actName);
            logPO.setPageCode("");
            logPO.setMerchantCode("");
            logPO.setMerchantName("新增" + actName);
            logPO.setDataId("1100001");
            logPO.setOperatField(JSONUtil.toJsonStr(Collections.singletonList("新增任务")));
            logPO.setAfterValues(JSONUtil.toJsonStr(Collections.singletonList(acTaskPO)));
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            merchantLogService.save(logPO);
        } catch (Exception e) {
            log.error("add log error:", e);
        }
        return Response.returnSuccess();
    }

    @PostMapping("/save")
    @AuthRequiredPermission("merchant:dj:manage:acTask:save")
    public Response<?> save(@RequestBody AcTaskVO acTaskVO, HttpServletRequest request) {
        AcTask acTaskPO = convertObj(acTaskVO);
        acTaskPO.setUpdateTime(System.currentTimeMillis());
        AcTask beforeValue = acTaskService.getById(acTaskVO.getId());
        acTaskService.updateById(acTaskPO);
        //写入日志
        try {
            MerchantLog logPO = new MerchantLog();
            Map<String, String> userInfo = extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(Long.valueOf(userId));
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(99);
            logPO.setTypeName("任务编辑");
            logPO.setPageName("电竞运营管理—" + acTaskVO.getActName());
            logPO.setPageCode("");
            logPO.setMerchantCode("");
            logPO.setMerchantName("编辑" + acTaskVO.getActName());
            logPO.setDataId("1100001");
            logPO.setOperatField(JSONUtil.toJsonStr(Collections.singletonList("编辑任务")));
            logPO.setBeforeValues(JSONUtil.toJsonStr(Collections.singletonList(beforeValue)));
            logPO.setAfterValues(JSONUtil.toJsonStr(Collections.singletonList(acTaskPO)));
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            merchantLogService.save(logPO);
        } catch (Exception e) {
            log.error("save log error:", e);
        }
        return Response.returnSuccess();
    }

    private AcTask convertObj(AcTaskVO acTaskVO) {
        AcTask acTask = new AcTask();
        BeanUtils.copyProperties(acTaskVO, acTask);
        return acTask;
    }

    @PostMapping("/changeOrder")
    @AuthRequiredPermission("merchant:dj:manage:acTask:changeOrder")
    public Response<?> changeOrder(@RequestParam(name = "id") Integer id,
                                   @RequestParam(name = "sort") Integer sort) {
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(sort)) {
            return Response.returnFail("参数错误");
        }
        return acTaskService.changeOrder(id, sort);
    }

    @PostMapping("/changeStatus")
    @AuthRequiredPermission("merchant:dj:manage:acTask:changeStatus")
    public Response<?> changeStatus(@RequestParam(name = "id") Integer id,
                                    @RequestParam(name = "status", required = false) Integer status,
                                    @RequestParam(name = "invalidation", required = false) Integer invalidation,
                                    HttpServletRequest request) {
        if (ObjectUtil.isNull(id)) {
            return Response.returnFail("id can not be empty");
        }
        boolean checkStatus = ObjectUtil.isNotNull(status);
        boolean checkInvalidation = ObjectUtil.isNotNull(invalidation);
        if (checkStatus && checkInvalidation) {
            return Response.returnFail("参数错误");
        }
        AcTask taskPO = acTaskService.getById(id);
        String typeName = "";
        String merchantName = "";
        if (checkInvalidation) {
            if (taskPO.getStatus() == 1 && invalidation == 0) {
                return Response.returnFail("请先把任务设置为隐藏状态");
            }
            taskPO.setInvalidation(invalidation);
            if (invalidation == 0) {
                typeName = "任务失效";
                merchantName = "失效";
            }
            if (invalidation == 1) {
                typeName = "任务生效";
                merchantName = "生效";
            }
        }
        if (checkStatus) {
            if (taskPO.getInvalidation() == 0 && status == 1) {
                return Response.returnFail("请先把任务设置为生效状态");
            }
            taskPO.setStatus(status);
            if (status == 0) {
                typeName = "任务隐藏";
            }
            if (status == 1) {
                typeName = "任务显示";
            }
        }
        AcTask beforeValue = acTaskService.getById(id);
        taskPO.setUpdateTime(System.currentTimeMillis());
        acTaskService.updateById(taskPO);
        //写入日志
        try {
            MerchantLog logPO = new MerchantLog();
            Map<String, String> userInfo = extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(Long.valueOf(userId));
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(99);
            logPO.setTypeName(typeName);
            logPO.setPageName("运营管理—" + beforeValue.getActName());
            logPO.setPageCode("");
            logPO.setMerchantCode("");
            logPO.setMerchantName(beforeValue.getActName() + merchantName);
            logPO.setDataId("1100001");
            logPO.setOperatField(JSONUtil.toJsonStr(Collections.singletonList(typeName)));
            if (checkInvalidation) {
                if (beforeValue.getInvalidation() == 0) {
                    logPO.setBeforeValues(JSONUtil.toJsonStr(Collections.singletonList("任务失效")));
                }
                if (beforeValue.getInvalidation() == 1) {
                    logPO.setBeforeValues(JSONUtil.toJsonStr(Collections.singletonList("任务生效")));
                }
            }
            if (checkStatus) {
                if (beforeValue.getStatus() == 0) {
                    logPO.setBeforeValues(JSONUtil.toJsonStr(Collections.singletonList("任务隐藏")));
                }
                if (beforeValue.getStatus() == 1) {
                    logPO.setBeforeValues(JSONUtil.toJsonStr(Collections.singletonList("任务显示")));
                }
            }
            logPO.setAfterValues(JSONUtil.toJsonStr(Collections.singletonList(typeName)));
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            merchantLogService.save(logPO);
        } catch (Exception e) {
            log.error("changeStatus log error:", e);
        }
        return Response.returnSuccess();
    }

    private Map<String, String> extractUserInfo(HttpServletRequest request) {
        Map<String, String> userMap = Maps.newHashMap();
        String userId = request.getHeader("user-id");
        if (StringUtils.isBlank(userId)) {
            userId = "999";
        }
        String userName = request.getHeader("merchantName");
        if (StringUtils.isBlank(userId)) {
            userName = "system";
        }
        userMap.put("userId", userId);
        userMap.put("userName", userName);
        return userMap;
    }

}
