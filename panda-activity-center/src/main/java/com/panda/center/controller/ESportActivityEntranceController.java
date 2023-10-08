package com.panda.center.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.center.constant.Constant;
import com.panda.center.entity.ActivityConfig;
import com.panda.center.entity.ActivityMerchant;
import com.panda.center.entity.MerchantLog;
import com.panda.center.mq.producer.PubMQProducer;
import com.panda.center.param.AcMaintainParam;
import com.panda.center.param.AcMerParam;
import com.panda.center.param.EventDateParam;
import com.panda.center.param.MerchantTreeForm;
import com.panda.center.result.Response;
import com.panda.center.service.IActivityConfigService;
import com.panda.center.service.IActivityMerchantService;
import com.panda.center.service.IMerchantLogService;
import com.panda.center.service.IMerchantService;
import com.panda.center.service.impl.AsyncScheduleImpl;
import com.panda.center.utils.RedisTemp;
import com.panda.center.vo.MerchantTree;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/27 20:48:24
 */
@RestController
@RequestMapping("/manage/dj/activityEntrance")
@Slf4j
public class ESportActivityEntranceController {
    @Resource
    private IActivityMerchantService activityMerchantService;
    @Resource
    private IMerchantService merchantService;
    @Resource
    private IActivityConfigService activityConfigService;
    @Resource
    private IMerchantLogService merchantLogService;
    @Resource
    private AsyncScheduleImpl asyncSchedule;
    @Resource
    private PubMQProducer pubMQProducer;
    @Value("${e_sports_maintenance_announcement_topic:e_sports_maintenance_announcement_topic}")
    private String E_SPORTS_MAINTENANCE_ANNOUNCEMENT;

    @GetMapping("/getActivityList")
    public Response<?> getActivityList() {
        List<ActivityConfig> configList = activityConfigService.list(new QueryWrapper<ActivityConfig>()
                .select("id", "name")
                .eq("status", 1));
        return Response.returnSuccess(configList);
    }

    @PostMapping("/getMerchantActivityList")
    public Response<?> getMerchantActivityList(@RequestParam(value = "merchantAccount", required = false) String merchantAccount,
                                               @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                               @RequestParam(value = "entranceStatus", required = false) Integer entranceStatus,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        QueryWrapper<ActivityMerchant> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            queryWrapper.eq("merchant_account", merchantAccount);
        }
        if (entranceStatus != null) {
            queryWrapper.eq("entrance_status", entranceStatus);
        }
        if (StringUtils.isNotBlank(merchantCode)) {
            queryWrapper.eq("merchant_code", merchantCode);
        }
        queryWrapper.orderByDesc("id");
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityMerchant> list = activityMerchantService.list(queryWrapper);
        PageInfo<ActivityMerchant> pageInfo = new PageInfo<>(list);
        Page<ActivityMerchant> pageList = new Page<>(pageNum, pageSize);
        pageList.setTotal(pageInfo.getTotal());
        pageList.setRecords(pageInfo.getList());
        return Response.returnSuccess(pageList);
    }

    @PostMapping("/getMerchantTree")
    public Response<?> getMerchantTree(@RequestBody MerchantTreeForm merchantTreeForm) {
        List<MerchantTree> merchantTree = merchantService.getMerchantList(merchantTreeForm);
        //查询已配置商户
        List<ActivityMerchant> list = activityMerchantService.list();
        if (list.isEmpty()) {
            return Response.returnSuccess(merchantTree);
        }
        List<String> merchantIds = list.stream().map(ActivityMerchant::getMerchantCode).collect(Collectors.toList());
        merchantTree.removeIf(v -> merchantIds.contains(v.getMerchantCode()));
        return Response.returnSuccess(merchantTree);
    }

    @PostMapping("/getMerchantSearchTree")
    public Response<?> getMerchantSearchTree(@RequestBody MerchantTreeForm merchantTreeForm) {
        List<MerchantTree> merchantTree = merchantService.getMerchantList(merchantTreeForm);
        return Response.returnSuccess(merchantTree);
    }

    @PostMapping("/updateStatus")
    @AuthRequiredPermission("merchant:dj:manage:entrance:update")
    public Response<?> updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "entranceStatus") Long entranceStatus,
                                    HttpServletRequest request) {
        if (id == null) {
            return Response.returnFail("id can not be null");
        }
        if (entranceStatus == null) {
            return Response.returnFail("entranceStatus can not be null");
        }
        if (entranceStatus != 0 && entranceStatus != 1) {
            return Response.returnFail("parameter entranceStatus error");
        }
        ActivityMerchant activityMerchant = activityMerchantService.getById(id);
        if (activityMerchant == null) {
            return Response.returnFail("商户配置不存在，请联系管理员!");
        }
        Long oldEnSta = activityMerchant.getEntranceStatus();
        if (entranceStatus == 1) {
            boolean isBlank = StringUtils.isBlank(activityMerchant.getActivityId());
            if (isBlank) {
                return Response.returnFail("商户 " + activityMerchant.getMerchantAccount() + " 活动入口开启失败，请先配置商户活动");
            }
        }
        activityMerchantService.update(new UpdateWrapper<ActivityMerchant>().set("entrance_status", entranceStatus).eq("id", id));
        saveOperationLog(request, 38, "入口开关", "运营活动设置-入口开关",
                "入口开关", "38", JSONUtil.toJsonStr(Collections.singletonList("entrance_status")),
                JSONUtil.toJsonStr(Collections.singletonList(oldEnSta == 0L ? "关闭" : "开启")),
                JSONUtil.toJsonStr(Collections.singletonList(entranceStatus == 0L ? "关闭" : "开启")));
        return Response.returnSuccess();
    }

    /**
     * @param request      request
     * @param operateType  operateType
     * @param typeName     操作类型
     * @param pageName     页面名称
     * @param merchantName 主数据
     * @param dataId       ID
     * @param operateField 操作字段 []
     * @param beforeValues 操作前数据 []
     * @param afterValues  操作后数据[]
     */
    private void saveOperationLog(HttpServletRequest request, Integer operateType, String typeName, String pageName,
                                  String merchantName, String dataId, String operateField, String beforeValues,
                                  String afterValues) {
        String userId = request.getHeader("user-id");
        String userName = request.getHeader("merchantName");
        MerchantLog merchantLog = new MerchantLog();
        merchantLog.setUserId(Long.valueOf(userId));
        merchantLog.setUserName(userName);
        merchantLog.setOperatType(operateType);
        merchantLog.setTypeName(typeName);
        merchantLog.setPageName(pageName);
        merchantLog.setMerchantName(merchantName);
        merchantLog.setDataId(dataId);
        merchantLog.setOperatField(operateField);
        merchantLog.setBeforeValues(beforeValues);
        merchantLog.setAfterValues(afterValues);
        merchantLog.setLogTag(1);
        merchantLog.setOperatTime(System.currentTimeMillis());
        merchantLogService.save(merchantLog);
    }

    @PostMapping("/delMerchant")
    @AuthRequiredPermission("merchant:dj:manage:entrance:del")
    public Response<?> delMerchant(@RequestParam(value = "id") Long id, HttpServletRequest request) {
        if (id == null) {
            return Response.returnFail("id can not be null");
        }
        ActivityMerchant activityMerchant = activityMerchantService.getById(id);
        if (activityMerchant == null) {
            return Response.returnFail("商户配置不存在，请联系管理员!");
        }
        activityMerchantService.removeById(id);
        saveOperationLog(request, 51, "删除配置", "运营活动设置-删除配置",
                "删除配置", "51", JSONUtil.toJsonStr(Collections.singletonList("delete")),
                JSONUtil.toJsonStr(Collections.singletonList(activityMerchant)),
                JSONUtil.toJsonStr(Collections.singletonList("")));
        return Response.returnSuccess();
    }

    @GetMapping("/getAcMerById")
    public Response<?> getAcMerById(@RequestParam(value = "id") Long id) {
        ActivityMerchant activityMerchant = activityMerchantService.getById(id);
        return Response.returnSuccess(activityMerchant);
    }

    @PostMapping("/updateAcMerById")
    @AuthRequiredPermission("merchant:dj:manage:entrance:upAcMer")
    public Response<?> updateAcMerById(@RequestParam(value = "id") Long id,
                                       @RequestParam(value = "activityId") String activityId,
                                       HttpServletRequest request) {
        if (id == null) {
            return Response.returnFail("id can not be null");
        }
        ActivityMerchant activityMerchant = activityMerchantService.getById(id);
        if (activityMerchant == null) {
            return Response.returnFail("商户配置不存在，请联系管理员!");
        }
        if (activityId == null) {
            activityId = "";
        }
        activityMerchantService.update(new UpdateWrapper<ActivityMerchant>().eq("id", id).set("activity_id",
                activityId));
        saveOperationLog(request, 52, "活动配置", "运营活动设置-活动配置",
                "活动配置", "52", JSONUtil.toJsonStr(Collections.singletonList("activity_id")),
                JSONUtil.toJsonStr(Collections.singletonList(activityMerchant.getActivityId())),
                JSONUtil.toJsonStr(Collections.singletonList(activityId)));
        return Response.returnSuccess();
    }

    @PostMapping("/addAcMer")
    @AuthRequiredPermission("merchant:dj:manage:entrance:addAcMer")
    public Response<?> addAcMer(@RequestBody List<AcMerParam> acMerParamList, HttpServletRequest request) {
        boolean validActivity =
                acMerParamList.stream().anyMatch(v -> StringUtils.isBlank(v.getActivityId()) && v.getEntranceStatus() == 1);
        if (validActivity) {
            return Response.returnFail("未选择活动,入口状态不能开启");
        }

        List<ActivityMerchant> merchantList = Lists.newArrayList();
        for (AcMerParam acMerParam : acMerParamList) {
            ActivityMerchant merchant = new ActivityMerchant();
            BeanUtils.copyProperties(acMerParam, merchant);
            merchantList.add(merchant);
        }
        activityMerchantService.saveBatch(merchantList);
        saveOperationLog(request, 53, "新增活动商户", "运营活动设置-新增活动商户",
                "新增活动商户", "53", JSONUtil.toJsonStr(Collections.singletonList("insert")),
                JSONUtil.toJsonStr(Collections.singletonList("")),
                JSONUtil.toJsonStr(acMerParamList));
        return Response.returnSuccess();
    }

    @GetMapping("/getEntranceBanner")
    public Response<?> getEntranceBanner() {
        ActivityConfig activityConfig = activityConfigService.getOne(new QueryWrapper<ActivityConfig>().select("pc_url",
                "h5_url").eq("id", 10007));
        return Response.returnSuccess(activityConfig);
    }

    @PostMapping("/updateEntranceBanner")
    @AuthRequiredPermission("merchant:dj:manage:entrance:upBanner")
    public Response<?> updateEntranceBanner(@RequestParam(value = "pcUrl") String pcUrl,
                                            @RequestParam(value = "h5Url") String h5Url,
                                            HttpServletRequest request) {
        if (StringUtils.isBlank(pcUrl) || StringUtils.isBlank(h5Url)) {
            return Response.returnFail("banner can not be blank");
        }
        ActivityConfig activityConfig = activityConfigService.list().get(0);

        activityConfigService.update(new UpdateWrapper<ActivityConfig>().set("pc_url",
                pcUrl).set("h5_Url", h5Url));
        saveOperationLog(request, 54, "修改活动入口", "运营活动设置-修改活动入口",
                "修改活动入口", "54", JSONUtil.toJsonStr(Arrays.asList("pc_url", "h5url")),
                JSONUtil.toJsonStr(Arrays.asList(activityConfig.getPcUrl(), activityConfig.getH5Url())),
                JSONUtil.toJsonStr(Arrays.asList(pcUrl, h5Url)));
        return Response.returnSuccess();
    }

    @GetMapping("/getMaintainConfig")
    public Response<?> getMaintainConfig() {
        ActivityConfig activityConfig = activityConfigService.getOne(new QueryWrapper<ActivityConfig>().select(
                "content",
                "title", "h5_maintain_url", "pc_maintain_url", "maintain_status", "maintain_end_time").eq("id", 10007));
        return Response.returnSuccess(activityConfig);
    }

    @PostMapping("/updateMaintainConfig")
    @AuthRequiredPermission("merchant:dj:manage:entrance:upMaCfg")
    public Response<?> updateMaintainConfig(@RequestBody AcMaintainParam acMaintainParam, HttpServletRequest request) {
        ActivityConfig activityConfig = activityConfigService.list().get(0);
        AcMaintainParam old = new AcMaintainParam()
                .setH5MaintainUrl(activityConfig.getH5MaintainUrl())
                .setPcMaintainUrl(activityConfig.getPcMaintainUrl())
                .setMaintainStatus(activityConfig.getMaintainStatus())
                .setMaintainEndTime(activityConfig.getMaintainEndTime())
                .setTitle(activityConfig.getTitle())
                .setContent(activityConfig.getContent());
        activityConfigService.update(new UpdateWrapper<ActivityConfig>()
                .set("h5_maintain_url", acMaintainParam.getH5MaintainUrl())
                .set("pc_maintain_url", acMaintainParam.getPcMaintainUrl())
                .set("maintain_status", acMaintainParam.getMaintainStatus())
                .set("maintain_end_time", acMaintainParam.getMaintainEndTime())
                .set("title", acMaintainParam.getTitle())
                .set("content", acMaintainParam.getContent()));
        saveOperationLog(request, 55, "修改维护配置", "运营活动设置-修改维护配置",
                "修改维护配置", "55", JSONUtil.toJsonStr(Collections.singletonList("update")),
                JSONUtil.toJsonStr(Collections.singletonList(old)),
                JSONUtil.toJsonStr(Collections.singletonList(acMaintainParam)));
        //推送MQ消息给DJ
        pubMQProducer.send(JSONUtil.toJsonStr(acMaintainParam), E_SPORTS_MAINTENANCE_ANNOUNCEMENT,
                E_SPORTS_MAINTENANCE_ANNOUNCEMENT, E_SPORTS_MAINTENANCE_ANNOUNCEMENT);
        return Response.returnSuccess();
    }

    @GetMapping("/getEventDateConfig")
    public Response<?> eventDateConfig() {
        List<ActivityConfig> configList = activityConfigService.list(new QueryWrapper<ActivityConfig>()
                .select("id", "name", "type", "in_start_time", "in_end_time").eq("status", 1));
        return Response.returnSuccess(configList);
    }

    @PostMapping("/updateEventDateConfig")
    @AuthRequiredPermission("merchant:dj:manage:entrance:upEvDaCfg")
    public Response<?> updateEventDateConfig(@RequestBody List<EventDateParam> eventDateParamList,
                                             HttpServletRequest request) {
        String isExecuting = RedisTemp.get(Constant.DJ_MANAGE_GROWTH_TASK_RESET);
        if (StringUtils.isNotBlank(isExecuting)) {
            return Response.returnFail("任务重置中，请稍后再试");
        }
        List<ActivityConfig> old = activityConfigService.list();
        //TODO 成长任务重置清空数据
        ActivityConfig oldGrowth = old.stream().filter(v -> v.getId().equals(10008L)).findAny().orElse(null);
        EventDateParam newGrowth =
                eventDateParamList.stream().filter(v -> v.getId().equals(10008L)).findAny().orElse(null);
        if (oldGrowth == null || newGrowth == null) {
            return Response.returnFail("数据话初始化失败!");
        }
        List<EventDateParam> oldList = Lists.newArrayList();
        for (ActivityConfig config : old) {
            EventDateParam edp = new EventDateParam();
            BeanUtils.copyProperties(config, edp);
            oldList.add(edp);
        }
        List<ActivityConfig> configs = Lists.newArrayList();
        for (EventDateParam param : eventDateParamList) {
            ActivityConfig ac = new ActivityConfig();
            BeanUtils.copyProperties(param, ac);
            configs.add(ac);
        }
        boolean isSuc = activityConfigService.updateBatchById(configs);
        List<String> beforeValue = new ArrayList<>();
        oldList.forEach(e -> {
            beforeValue.add(this.getTimeStr(e.getId(), e.getInStartTime(), e.getInEndTime()));
        });
        List<String> afterValue = new ArrayList<>();
        eventDateParamList.forEach(e -> {
            afterValue.add(this.getTimeStr(e.getId(), e.getInStartTime(), e.getInEndTime()));
        });
        saveOperationLog(request, 56, "修改活动日期", "运营活动设置-修改活动日期",
                "修改活动日期", "56", JSONUtil.toJsonStr(Collections.singletonList("活动时间更新")),
                JSONUtil.toJsonStr(beforeValue),
                JSONUtil.toJsonStr(afterValue));
        if (isSuc) {
            log.info("old growth type:{}", oldGrowth);
            log.info("new growth type:{}", newGrowth);
            //成长任务修改时间重置任务状态
            Integer oT = oldGrowth.getType();
            Long oSt = oldGrowth.getInStartTime();
            Long oEn = oldGrowth.getInEndTime();
            Integer nT = newGrowth.getType();
            Long nSt = newGrowth.getInStartTime();
            Long nEd = newGrowth.getInEndTime();
            //常驻活动关闭 不更新
            if (Constant.INT_2.equals(nT) && nEd != 0) {
                return Response.returnSuccess();
            }
            //延长活动时间不更新
            if (Constant.INT_1.equals(nT) && oSt.equals(nSt)) {
                return Response.returnSuccess();
            }
            if (!oSt.equals(nSt) || !oEn.equals(nEd) || !oT.equals(nT)) {
                log.info("execute clear growth task data");
                RedisTemp.set(Constant.DJ_MANAGE_GROWTH_TASK_RESET, "execute");
                asyncSchedule.clearGrowthTaskData();
            }
        }
        return Response.returnSuccess();
    }

    private String dataConvert(List<EventDateParam> oldList) {
        return null;
    }

    private String getTimeStr(Long id, Long inStartTime, Long inEndTime) {
        StringBuilder sbf = new StringBuilder();
        if (id == 10007L) {
            sbf.append("每日任务");
        } else if (id == 10008L) {
            sbf.append("成长任务");
        } else if (id == 10009L) {
            sbf.append("幸运盲盒");
        } else if (id == 10010L) {
            sbf.append("老虎机活动");
        }
        if (StringUtils.isNotBlank(sbf.toString())) {
            sbf.append(":{时间:").
                    append(this.getStr(inStartTime)).
                    append("-").
                    append(this.getStr(inEndTime)).
                    append("}");
        }
        return sbf.toString();
    }

    private String getStr(Long time) {
        return time == null ? "未设置" : time == 0 ? "活动长期有效" : DateUtil.format(new Date(time), DatePattern.NORM_DATETIME_PATTERN);
    }
}
