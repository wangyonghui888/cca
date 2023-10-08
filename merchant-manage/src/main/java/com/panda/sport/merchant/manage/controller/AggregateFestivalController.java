package com.panda.sport.merchant.manage.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.panda.sport.merchant.common.enums.AggregateFestivalEnum;
import com.panda.sport.merchant.common.enums.AggregatePlatformEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.AggregateFestivalResourceCfg;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.aggregationPlatform.FestiveCfgForm;
import com.panda.sport.merchant.manage.service.IAggregateFestivalResourceCfgService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.validation.AddGroup;
import com.panda.sport.merchant.manage.validation.UpdateGroup;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manage/aggregateFestival")
@Slf4j
@Validated
@RefreshScope
public class AggregateFestivalController {

    @Resource
    private IAggregateFestivalResourceCfgService resourceCfgService;
    @Resource
    private MerchantLogService merchantLogService;
    @Value("${dj_festival_url:http://www.phiqui.com}")
    private String djFestivalUrl;
    @Value("${cp_festival_url:http://test-mk-sabang-api.k8s-dev.com}")
    private String cpFestivalUrl;

    @GetMapping("/getPlatform")
    public Response<?> getPlatform() {
        return Response.returnSuccess(AggregatePlatformEnum.list);
    }

    @GetMapping("/getLanguage")
    public Response<?> getLanguage() {
        return Response.returnSuccess(AggregateFestivalEnum.list);
    }

    @PostMapping("/list")
    public Response<?> list(@RequestParam(required = false) String platform,
                            @RequestParam(required = false) Integer state) {
        QueryWrapper<AggregateFestivalResourceCfg> wrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(platform)) {
            wrapper.eq("platform", platform);
        }
        if (state != null) {
            wrapper.eq("state", state);
        }
        wrapper.orderByDesc("create_time", "id");
        List<AggregateFestivalResourceCfg> resourceCfg = resourceCfgService.list(wrapper);
        return Response.returnSuccess(resourceCfg);

    }

    @AuthRequiredPermission("aggregateFestival:add")
    @PostMapping("/add")
    public Response<?> add(@RequestBody @Validated({AddGroup.class, Default.class}) FestiveCfgForm cfgForm,
                           HttpServletRequest request) {
        String[] languages = cfgForm.getLanguage().split(",");
        List<AggregateFestivalResourceCfg> list = Lists.newArrayList();
        long currentTime = System.currentTimeMillis();
        for (String language : languages) {
            AggregateFestivalResourceCfg resourceCfg = new AggregateFestivalResourceCfg();
            BeanUtils.copyProperties(cfgForm, resourceCfg);
            resourceCfg.setLanguage(language);
            resourceCfg.setState(0);
            resourceCfg.setCreateTime(currentTime);
            list.add(resourceCfg);
        }
        resourceCfgService.saveBatch(list);
        for (AggregateFestivalResourceCfg resourceCfg : list) {
            merchantLogService.saveOperationLog(MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getCode(), "新增资源配置"
                    , "运营管理—资源配置",
                    MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(0), "", "新增资源配置", "1100001",
                    "新增资源配置", convertFestivalLog(null), convertFestivalLog(resourceCfg), request);

        }
        if (cfgForm.getLanguage().equals(AggregatePlatformEnum.DJ.getCode())) {
            callDJFestivalAPI(request);
        }
        if (cfgForm.getLanguage().equals(AggregatePlatformEnum.CP.getCode())) {
            callCPFestivalAPI(request);
        }
        return Response.returnSuccess();
    }

    @AuthRequiredPermission("aggregateFestival:update")
    @PostMapping("/update")
    public Response<?> update(@RequestBody @Validated({UpdateGroup.class, Default.class}) FestiveCfgForm cfgForm,
                              HttpServletRequest request) {

        Long cfgFormId = cfgForm.getId();
        AggregateFestivalResourceCfg resourceCfg = resourceCfgService.getById(cfgFormId);
        if (resourceCfg == null) {
            return Response.returnFail("can not find cfg");
        }
        List<String> beforeValue = convertFestivalLog(resourceCfg);
        BeanUtils.copyProperties(cfgForm, resourceCfg);
        resourceCfgService.updateById(resourceCfg);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getCode(), "编辑资源配置",
                "运营管理—资源配置",
                MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(1), "", "编辑资源配置", "1100001",
                "编辑资源配置", beforeValue, convertFestivalLog(resourceCfg), request);
        if (resourceCfg.getPlatform().equals(AggregatePlatformEnum.DJ.getCode())) {
            callDJFestivalAPI(request);
        }
        if (resourceCfg.getPlatform().equals(AggregatePlatformEnum.CP.getCode())) {
            callCPFestivalAPI(request);
        }
        return Response.returnSuccess();
    }

    private List<String> convertFestivalLog(AggregateFestivalResourceCfg resourceCfg) {
        List<String> list = Lists.newArrayList();
        if (resourceCfg == null) {
            list.add("--");
            return list;
        }
        String language = resourceCfg.getLanguage();
        if(language.equalsIgnoreCase("ma")){
            language = "ms";
        }else if(language.equalsIgnoreCase("id")){
            language = "ad";
        }
        list.add("平台类型：" + AggregatePlatformEnum.platformEnumMap.get(resourceCfg.getPlatform()).getValue());
        list.add("生效时间：" + DateUtil.formatDateTime(new DateTime(resourceCfg.getStartTime())) + "-" + DateUtil.formatDateTime(new DateTime(resourceCfg.getEndTime())));
        list.add("语言环境：" + AggregateFestivalEnum.festivalEnumMap.get(language).getValue());
        list.add("img1：" + resourceCfg.getImg1());
        list.add("img1Type：" + resourceCfg.getImg1Type());
        list.add("img1Url：" + resourceCfg.getImg1Url());
        list.add("img2：" + resourceCfg.getImg2());
        list.add("img2Type：" + resourceCfg.getImg2Type());
        list.add("img2Url：" + resourceCfg.getImg2Url());
        list.add("img3：" + resourceCfg.getImg3());
        list.add("img3Type：" + resourceCfg.getImg3Type());
        list.add("img3Url：" + resourceCfg.getImg3Url());
        list.add("img4：" + resourceCfg.getImg4());
        list.add("img4Type：" + resourceCfg.getImg4Type());
        list.add("img4Url：" + resourceCfg.getImg4Url());
        list.add("img5：" + resourceCfg.getImg5());
        list.add("img5Type：" + resourceCfg.getImg5Type());
        list.add("img5Url：" + resourceCfg.getImg5Url());
        list.add("img6：" + resourceCfg.getImg6());
        list.add("img6Type：" + resourceCfg.getImg6Type());
        list.add("img6Url：" + resourceCfg.getImg6Url());
        list.add("img7：" + resourceCfg.getImg7());
        list.add("img7Type：" + resourceCfg.getImg7Type());
        list.add("img7Url：" + resourceCfg.getImg7Url());
        list.add("img8：" + resourceCfg.getImg8());
        list.add("img8Type：" + resourceCfg.getImg8Type());
        list.add("img8Url：" + resourceCfg.getImg8Url());
        list.add("img9：" + resourceCfg.getImg9());
        list.add("img9Type：" + resourceCfg.getImg9Type());
        list.add("img9Url：" + resourceCfg.getImg9Url());
        list.add("img10：" + resourceCfg.getImg10());
        list.add("img10Type：" + resourceCfg.getImg10Type());
        list.add("img10Url：" + resourceCfg.getImg10Url());
        list.add("img11：" + resourceCfg.getImg11());
        list.add("img11Type：" + resourceCfg.getImg11Type());
        list.add("img11Url：" + resourceCfg.getImg11Url());
        list.add("img12：" + resourceCfg.getImg12());
        list.add("img12Type：" + resourceCfg.getImg12Type());
        list.add("img12Url：" + resourceCfg.getImg12Url());
        return list;
    }

    @AuthRequiredPermission("aggregateFestival:del")
    @PostMapping("/del")
    public Response<?> del(@RequestParam Long id, HttpServletRequest request) {
        AggregateFestivalResourceCfg beforeValue = resourceCfgService.getById(id);
        resourceCfgService.removeById(id);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getCode(), "删除资源配置",
                "运营管理—资源配置",
                MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(1), "", "删除资源配置", "1100001",
                "删除资源配置", convertFestivalLog(beforeValue), convertFestivalLog(null), request);
        if (beforeValue.getPlatform().equals(AggregatePlatformEnum.DJ.getCode())) {
            callDJFestivalAPI(request);
        }
        if (beforeValue.getPlatform().equals(AggregatePlatformEnum.CP.getCode())) {
            callCPFestivalAPI(request);
        }
        return Response.returnSuccess();
    }

    @GetMapping("stateList")
    public Response<?> stateList() {
        List<JSONObject> list = AggregatePlatformEnum.list;
        List<AggregateFestivalResourceCfg> cfgList = resourceCfgService.list();
        Map<String, List<AggregateFestivalResourceCfg>> map =
                cfgList.stream().collect(Collectors.groupingBy(AggregateFestivalResourceCfg::getPlatform));
        if (map == null) {
            return Response.returnFail("error please check config");
        }
        for (JSONObject obj : list) {
            List<AggregateFestivalResourceCfg> resourceCfgList = map.get(obj.get("code").toString());
            if (resourceCfgList == null) {
                obj.put("state", 0);
            } else {
                boolean allMatch = resourceCfgList.stream().anyMatch(v -> v.getState() == 1);
                if (allMatch) {
                    obj.put("state", 1);
                } else {
                    obj.put("state", 0);
                }
            }
        }
        return Response.returnSuccess(list);
    }

    @AuthRequiredPermission("aggregateFestival:enableBatch")
    @PostMapping("/enableBatch")
    public Response<?> enableBatch(@RequestParam String platform,
                                   @RequestParam @Min(0) @Max(1) Integer state, HttpServletRequest request) {
        UpdateWrapper<AggregateFestivalResourceCfg> wrapper = new UpdateWrapper<>();
        wrapper.set("state", state).eq("platform", platform);
        resourceCfgService.update(wrapper);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getCode(), "资源状态开关",
                "运营管理—资源配置",
                MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(1), "", "资源状态", "1100001",
                "资源状态", convertResourceState(platform, state == 0 ? 1 : 0), convertResourceState(platform, state),
                request);
        if (platform.equals(AggregatePlatformEnum.DJ.getCode())) {
            callDJFestivalAPI(request);
        }
        if (platform.equals(AggregatePlatformEnum.CP.getCode())) {
            callCPFestivalAPI(request);
        }
        return Response.returnSuccess();
    }

    private List<String> convertResourceState(String platform, Integer state) {
        List<String> list = Lists.newArrayList();
        list.add("平台类型：" + AggregatePlatformEnum.platformEnumMap.get(platform).getValue());
        list.add("资源状态：" + (state == 0 ? "关" : "开"));
        return list;
    }

    private void callDJFestivalAPI(HttpServletRequest request) {
        try {
            List<AggregateFestivalResourceCfg> djList = getCfgList("DJ");
            WebClient webClient = WebClient.builder()
                    .baseUrl(djFestivalUrl)

                    .build();
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("data", JSONUtil.toJsonStr(djList));
            Mono<String> mono = webClient.post()
                    .uri("/v1/festivalUi/set")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(String.class);
            JSONObject obj = JSONUtil.parseObj(mono.block());
            String requestResult = "下发结果: 失败";
            if ("200".equals(obj.get("code"))) {
                requestResult = "下发结果: 成功";
            }
            log.info("请求DJ接口返回msg：" + obj.get("msg"));
            merchantLogService.saveOperationLog(MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getCode(), "DJ资源配置下发",
                    "运营管理—资源配置",
                    MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(4), "", "DJ资源配置下发", "1100001",
                    "DJ资源配置下发状态", Collections.singletonList(mono.block()), Collections.singletonList(requestResult),
                    request);
        } catch (Exception e) {
            log.info("dj festival issued error", e);
        }
    }

    private void callCPFestivalAPI(HttpServletRequest request) {
        try {
            List<AggregateFestivalResourceCfg> cpList = getCfgList("CP");
            cpList.forEach(v -> {
                v.setId(null);
                v.setPlatform(null);
                v.setImg1Type(null);
                v.setImg1Url(null);
                v.setImg2Type(null);
                v.setImg2Url(null);
                v.setImg3Type(null);
                v.setImg3Url(null);
                v.setImg4Type(null);
                v.setImg4Url(null);
                v.setImg5Type(null);
                v.setImg5Url(null);
                v.setImg6Type(null);
                v.setImg6Url(null);
                v.setImg7Type(null);
                v.setImg7Url(null);
//                v.setImg8(null);新增一个配置位,下发给CP
                v.setImg8Type(null);
                v.setImg8Url(null);
                v.setImg9(null);
                v.setImg9Type(null);
                v.setImg9Url(null);
                v.setImg10(null);
                v.setImg10Type(null);
                v.setImg10Url(null);
                v.setImg11(null);
                v.setImg11Type(null);
                v.setImg11Url(null);
                v.setImg12(null);
                v.setImg12Type(null);
                v.setImg12Url(null);
            });
            String jsonStr = JSONUtil.toJsonStr(cpList);
            WebClient webClient = WebClient.builder()
                    .baseUrl(cpFestivalUrl)
                    .defaultHeader("clientId", "internal service")
                    .build();
            Mono<String> mono = webClient.post()
                    .uri("/sabang/pulic/festival")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonStr)
                    .retrieve()
                    .bodyToMono(String.class);
            JSONObject obj = JSONUtil.parseObj(mono.block());
            String requestResult = "下发结果: 失败";
            Object code = obj.get("code");
            if (code != null && "0".equals(String.valueOf(code))) {
                requestResult = "下发结果: 成功";
            }
            log.info("请求CP接口返回msg：" + obj.get("msg"));
            merchantLogService.saveOperationLog(MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getCode(), "CP资源配置下发",
                    "运营管理—资源配置",
                    MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(5), "", "CP资源配置下发", "1100001",
                    "CP资源配置下发状态", Collections.singletonList(mono.block()), Collections.singletonList(requestResult),
                    request);
        } catch (Exception e) {
            log.info("cp festival issued error", e);
        }
    }

    private List<AggregateFestivalResourceCfg> getCfgList(String platform) {
        QueryWrapper<AggregateFestivalResourceCfg> wrapper = new QueryWrapper<>();
        wrapper.eq("platform", platform);
        return resourceCfgService.list(wrapper);
    }
}
