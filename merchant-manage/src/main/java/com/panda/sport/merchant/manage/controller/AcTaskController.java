package com.panda.sport.merchant.manage.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.match.mapper.BettingPlayPOMapper;
import com.panda.sport.merchant.common.po.bss.AcTaskPO;
import com.panda.sport.merchant.common.po.bss.VirtualSportTypePO;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.JosnToUtil;
import com.panda.sport.merchant.common.utils.JsonUtils;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.*;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动任务表 前端控制器
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
@RestController
@RequestMapping("/manage/activity")
@Slf4j
public class AcTaskController {


    @Resource
    private IAcTaskService acTaskService;
    @Resource
    private MerchantLogService merchantLogService;

    @Resource
    private ISportService sportService;

    @Resource
    private IBettingPlayService bettingPlayService;

    @Resource
    private BettingPlayPOMapper bettingPlayPOMapper;


    @Resource
    private IVirtualSportTypeService virtualSportTypeService;

    @GetMapping("/getSportAndPlayTree")
    public Response<List<SportAndPlayTreeVO>> getSportAndPlayTree() {
        return Response.returnSuccess(acTaskService.getSportAndPlayTree());
    }

    @GetMapping("/getSportAndPlayTree1")
    public Response<List<SportAndPlayTreeVO>> getSportAndPlayTree(long actId) {

        AcTaskPO acTaskPO = acTaskService.getById(actId);

        List<SportAndPlayTreeVO> list = Lists.newArrayList();
        //判断是否是新产生的数据
        if(StringUtils.isNotEmpty(acTaskPO.getTaskCondition()) && !JosnToUtil.isJSON2(acTaskPO.getTaskCondition())){
            String taskCondition3 = acTaskPO.getTaskCondition3();

            if (StringUtils.isBlank(taskCondition3)) return Response.returnSuccess(list);

            Map<Integer, Set<Integer>> map = JsonUtils.jsonToObject(taskCondition3, new TypeReference<Map<Integer,
                    Set<Integer>>>() {
            });

            List<Integer> acIds = new ArrayList<>(map.keySet());


            List<SportVO> sportPOList = sportService.getListByIds(acIds);
            Map<Integer, SportVO> voMap = sportPOList.stream().collect(Collectors.toMap(SportVO::getSportId,
                    Function.identity()));
            List<BettingPlayVO> playPOList = bettingPlayService.getListByIds(acIds);
            LinkedHashMap<Integer, List<BettingPlayVO>> linkedHashMap =
                    playPOList.stream().collect(Collectors.groupingBy(BettingPlayVO::getSportId, LinkedHashMap::new,
                            Collectors.toList()));
            for (Integer acId : acIds) {
                SportAndPlayTreeVO playTreeVO = SportAndPlayTreeVO.getInstance();
                playTreeVO.setTitle(voMap.get(acId).getSportName());
                playTreeVO.setKey(acId);
                playTreeVO.setParentId(0);
                List<BettingPlayVO> bettingPlayVOS = linkedHashMap.get(acId);
                linkedHashMap.get(acId).sort(Comparator.comparing(BettingPlayVO::getPlayId));
                List<SportAndPlayTreeVO> childList = Lists.newArrayList();
                for (BettingPlayVO bettingPlayVO : bettingPlayVOS) {
                    if (!map.get(acId).contains(bettingPlayVO.getPlayId())) continue;
                    SportAndPlayTreeVO detailTreeVO = SportAndPlayTreeVO.getInstance();
                    detailTreeVO.setTitle(bettingPlayVO.getPlayName());
                    detailTreeVO.setKey(bettingPlayVO.getPlayId());
                    detailTreeVO.setParentId(acId);
                    detailTreeVO.setChildren(Lists.newArrayList());
                    childList.add(detailTreeVO);
                }
                playTreeVO.setChildren(childList);
                list.add(playTreeVO);
            }
        }
        return Response.returnSuccess(list);
    }

    @GetMapping("/getVirtualSportTree1")
    public Response<List<SportAndPlayTreeVO>> getVirtualSportTree1(long actId) {

        AcTaskPO acTaskPO = acTaskService.getById(actId);
        String taskCondition3 = acTaskPO.getTaskCondition3();
        List<SportAndPlayTreeVO> virtualSportTree = Lists.newArrayList();

        if (StringUtils.isBlank(taskCondition3)) return Response.returnSuccess(virtualSportTree);

        List<VirtualSportTypePO> list =
                virtualSportTypeService.list(new QueryWrapper<VirtualSportTypePO>()
                        .select("name_code", "virtual_sport_id", "introduction")
                        .in("id", Arrays.asList(taskCondition3.split(",")))
                        .orderByAsc("name_code"));

        for (VirtualSportTypePO virtualSportTypePO : list) {
            SportAndPlayTreeVO sportTree = SportAndPlayTreeVO.getInstance();
            sportTree.setTitle(virtualSportTypePO.getIntroduction());
            sportTree.setKey(Integer.valueOf(virtualSportTypePO.getVirtualSportId()));
            sportTree.setParentId(0);
            sportTree.setChildren(Lists.newArrayList());
            virtualSportTree.add(sportTree);

        }

        return Response.returnSuccess(virtualSportTree);
    }

    @GetMapping("/getVirtualSportTree")
    public Response<List<SportAndPlayTreeVO>> getVirtualSportTree() {
        return Response.returnSuccess(acTaskService.getVirtualSportTree());
    }

    @PostMapping("/add")
    @AuthRequiredPermission("merchant:manage:acTask:add")
    public Response<?> add(@RequestBody AcTaskVO acTaskVO, HttpServletRequest request) {
        String actName = "";
        if (acTaskVO.getActId() == 1) {
            actName = "每日任务";
        }
        if (acTaskVO.getActId() == 2) {
            actName = "成长任务";
        }
        acTaskVO.setActName(actName);
        AcTaskPO acTaskPO = convertObj(acTaskVO);
        long curTime = System.currentTimeMillis();
        if(StringUtils.isNotEmpty(acTaskVO.getTaskCondition()) && JosnToUtil.isJSON2(acTaskVO.getTaskCondition()))
        {
            acTaskPO.setRemark(JosnToUtil.get(acTaskVO.getTaskCondition(), 0l, 0l, 0l).get("remark").toString());
        }
        acTaskPO.setCreateTime(curTime);
        acTaskPO.setUpdateTime(curTime);
        //查询当前排序最大值
        Integer curMaxNo = acTaskService.getCurMaxNo(acTaskVO.getActId());
        acTaskPO.setOrderNo(curMaxNo + 1);
        acTaskService.save(acTaskPO);
        //写入日志
        try {
            MerchantLogPO logPO = new MerchantLogPO();
            Map<String, String> userInfo = extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(userId);
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(99);
            logPO.setTypeName("新增任务");
            logPO.setPageName("运营管理—" + actName);
            logPO.setPageCode("");
            logPO.setMerchantCode("");
            logPO.setMerchantName("新增" + actName);
            logPO.setDataId("1100001");
            logPO.setOperatField(JsonUtils.listToJson(Collections.singletonList("新增任务")));
            logPO.setAfterValues(JsonUtils.listToJson(convertCfgToData(null, acTaskPO).get(1)));
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            logPO.setIp(IPUtils.getIpAddr(request));
            merchantLogService.saveLog(logPO);
        } catch (Exception e) {
            log.error("add log error:", e);
        }
        return Response.returnSuccess();
    }

    @PostMapping("/list")
    @AuthRequiredPermission("merchant:manage:acTask:list")
    public Response<?> list(@RequestParam(name = "actId") Integer actId,
                            @RequestParam(name = "taskTittle" , required = false) String taskTittle,
                            @RequestParam(name = "invalidation", required = false) Integer invalidation,
                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        PageHelper.startPage(page, size);
        QueryWrapper<AcTaskPO> wrapper = new QueryWrapper<AcTaskPO>().eq("act_id", actId).orderByDesc("order_no");
        if (!Objects.isNull(invalidation)) {
            wrapper.eq("invalidation", invalidation);
        }
        if (StringUtils.isNotEmpty(taskTittle)) {
            wrapper.like("task_tittle", taskTittle);
        }
        List<AcTaskPO> list = acTaskService.list(wrapper);
        PageInfo<AcTaskPO> pageInfo = new PageInfo<>(list);
        return Response.returnSuccess(pageInfo);
    }

    @PostMapping("/save")
    @AuthRequiredPermission("merchant:manage:acTask:save")
    public Response<?> save(@RequestBody AcTaskVO acTaskVO, HttpServletRequest request) {
        AcTaskPO acTaskPO = convertObj(acTaskVO);
        acTaskPO.setUpdateTime(System.currentTimeMillis());
        if(StringUtils.isNotEmpty(acTaskVO.getTaskCondition()) && JosnToUtil.isJSON2(acTaskVO.getTaskCondition())) {
            acTaskPO.setRemark(JosnToUtil.get(acTaskVO.getTaskCondition(), 0l, 0l, 0l).get("remark").toString());
        }
        AcTaskPO beforeValue = acTaskService.getById(acTaskVO.getId());
        if(null == acTaskVO.getTaskCondition3()) acTaskPO.setTaskCondition3("");

        /*if(null == acTaskVO.getTaskCondition2()) acTaskPO.setTaskCondition2("");
        if(null == acTaskVO.getTaskCondition()) acTaskPO.setTaskCondition("");*/
        acTaskService.updateById(acTaskPO);
        //写入日志
        try {
            MerchantLogPO logPO = new MerchantLogPO();
            Map<String, String> userInfo = extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(userId);
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(99);
            logPO.setTypeName("任务编辑");
            logPO.setPageName("运营管理—" + acTaskVO.getActName());
            logPO.setPageCode("");
            logPO.setMerchantCode("");
            logPO.setMerchantName("编辑" + acTaskVO.getActName());
            logPO.setDataId("1100001");
            logPO.setOperatField(JsonUtils.listToJson(Collections.singletonList("编辑任务")));
            logPO.setBeforeValues(JsonUtils.listToJson(convertCfgToData(beforeValue, acTaskPO).get(0)));
            logPO.setAfterValues(JsonUtils.listToJson(convertCfgToData(beforeValue, acTaskPO).get(1)));
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            logPO.setIp(IPUtils.getIpAddr(request));
            merchantLogService.saveLog(logPO);
        } catch (Exception e) {
            log.error("save log error:", e);
        }
        return Response.returnSuccess();
    }

    @PostMapping("/changeStatus")
    @AuthRequiredPermission("merchant:manage:acTask:changeStatus")
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
        AcTaskPO taskPO = acTaskService.getById(id);
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
        AcTaskPO beforeValue = acTaskService.getById(id);
        taskPO.setUpdateTime(System.currentTimeMillis());
        acTaskService.updateById(taskPO);
        //写入日志
        try {
            MerchantLogPO logPO = new MerchantLogPO();
            Map<String, String> userInfo = extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(userId);
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(99);
            logPO.setTypeName(typeName);
            logPO.setPageName("运营管理—" + beforeValue.getActName());
            logPO.setPageCode("");
            logPO.setMerchantCode("");
            logPO.setMerchantName(beforeValue.getActName() + merchantName);
            logPO.setDataId("1100001");
            logPO.setOperatField(JsonUtils.listToJson(Collections.singletonList(typeName)));
            if (checkInvalidation) {
                if (beforeValue.getInvalidation() == 0) {
                    logPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList("任务失效")));
                }
                if (beforeValue.getInvalidation() == 1) {
                    logPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList("任务生效")));
                }
            }
            if (checkStatus) {
                if (beforeValue.getStatus() == 0) {
                    logPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList("任务隐藏")));
                }
                if (beforeValue.getStatus() == 1) {
                    logPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList("任务显示")));
                }
            }
            logPO.setAfterValues(JsonUtils.listToJson(Collections.singletonList(typeName)));
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            logPO.setIp(IPUtils.getIpAddr(request));
            merchantLogService.saveLog(logPO);
        } catch (Exception e) {
            log.error("changeStatus log error:", e);
        }
        return Response.returnSuccess();
    }

    @PostMapping("/changeOrder")
    @AuthRequiredPermission("merchant:manage:acTask:changeOrder")
    public Response<?> changeOrder(HttpServletRequest request,@RequestParam(name = "id") Integer id,
                                   @RequestParam(name = "sort") Integer sort) {
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(sort)) {
            return Response.returnFail("参数错误");
        }
        return acTaskService.changeOrder(request,id, sort);
    }

    private AcTaskPO convertObj(AcTaskVO acTaskVO) {
        AcTaskPO acTaskPO = new AcTaskPO();
        BeanUtils.copyProperties(acTaskVO, acTaskPO);
        return acTaskPO;
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

    private List<List<String>> convertCfgToData(AcTaskPO before, AcTaskPO after) {
        List<List<String>> list = Lists.newArrayList();
        List<String> pre = Lists.newArrayList();
        List<String> cur = Lists.newArrayList();
        if (before == null) {
            pre.add("新增任务");
        } else {
            //构造操作前数据
            //每日任务
            pre.add("任务ID：" + before.getId());
            if (before.getActId() == 1) {
                if (before.getConditionId() == 1) {
                    pre.add("条件1：每日投注" + before.getTaskCondition() + "笔");
                }
                if (before.getConditionId() == 2) {
                    pre.add("条件1：当日单笔有效投注 >= " + before.getTaskCondition() + "元");
                }
                if (before.getConditionId() == 3) {
                    pre.add("条件1：当日投注注单数 >=" + before.getTaskCondition() + "笔");
                }
                if (before.getConditionId() == 4) {
                    pre.add("条件1：当日完成" + before.getTaskCondition() + "笔串关玩法");
                }
                if (before.getConditionId() == 5) {
                    pre.add("条件1：当日完成" + before.getTaskCondition() + "场VR体育赛事");
                }
                if (before.getConditionId() != 2) {
                    pre.add("条件2：任务条件最低投注" + before.getTaskCondition2() + "元生效");
                }
                //球种转换
                if (before.getConditionId() == 1) {
                    String taskCondition3 = before.getTaskCondition3();
                    pre.add("条件3：" + JSONUtil.toJsonStr(playIdToPlayName(taskCondition3)));
                }
                if (before.getConditionId() == 5) {
                    String taskCondition3 = before.getTaskCondition3();
                    pre.add("条件3：" + JSONUtil.toJsonStr(virSportPlayIdToPlayName(taskCondition3)));
                }

            }
            //成长任务
            if (before.getActId() == 2) {
                if (before.getConditionId() == 1) {
                    pre.add("条件1：本月累计投注" + before.getTaskCondition() + "天");
                }
                if (before.getConditionId() == 2) {
                    pre.add("条件1：本周累计有效投注 >= " + before.getTaskCondition() + "元/（RMB）");
                }
                if (before.getConditionId() == 3) {
                    pre.add("条件1：本月累计有效投注 >= " + before.getTaskCondition() + "元/（RMB）");
                }
            }
            pre.add("奖券数：" + before.getTicketNum());
        }
        //构造操后数据
        //每日任务
        cur.add("任务ID：" + after.getId());
        if (after.getActId() == 1) {
            if (after.getConditionId() == 1) {
                cur.add("条件1：每日投注" + after.getTaskCondition() + "笔");
            }
            if (after.getConditionId() == 2) {
                cur.add("条件1：当日单笔有效投注 >= " + after.getTaskCondition() + "元");
            }
            if (after.getConditionId() == 3) {
                cur.add("条件1：当日投注注单数 >=" + after.getTaskCondition() + "笔");
            }
            if (after.getConditionId() == 4) {
                cur.add("条件1：当日完成" + after.getTaskCondition() + "笔串关玩法");
            }
            if (after.getConditionId() == 5) {
                cur.add("条件1：当日完成" + after.getTaskCondition() + "场VR体育赛事");
            }
            if (after.getConditionId() != 2) {
                cur.add("条件2：任务条件最低投注" + after.getTaskCondition2() + "元生效");
            }
            //球种转换
            if (after.getConditionId() == 1) {
                String taskCondition3 = after.getTaskCondition3();
                cur.add("条件3：" + JSONUtil.toJsonStr(playIdToPlayName(taskCondition3)));
            }
            if (after.getConditionId() == 5) {
                String taskCondition3 = after.getTaskCondition3();
                cur.add("条件3：" + JSONUtil.toJsonStr(virSportPlayIdToPlayName(taskCondition3)));
            }

        }
        //成长任务
        if (after.getActId() == 2) {
            if (after.getConditionId() == 1) {
                cur.add("条件1：本月累计投注" + after.getTaskCondition() + "天");
            }
            if (after.getConditionId() == 2) {
                cur.add("条件1：本周累计有效投注 >= " + after.getTaskCondition() + "元/（RMB）");
            }
            if (after.getConditionId() == 3) {
                cur.add("条件1：本月累计有效投注 >= " + after.getTaskCondition() + "元/（RMB）");
            }
        }
        cur.add("奖券数：" + after.getTicketNum());
        list.add(pre);
        list.add(cur);
        return list;
    }

    private List<JSONObject> playIdToPlayName(String playId) {
        List<JSONObject> list = Lists.newArrayList();
        if (StringUtils.isBlank(playId)) {
            return list;
        }
        Map<Integer, Set<Integer>> map = JsonUtils.jsonToObject(playId, new TypeReference<Map<Integer,
                Set<Integer>>>() {
        });
        List<Integer> acIds = new ArrayList<>(map.keySet());
        List<SportVO> sportPOList = sportService.getListByIds(acIds);
        Map<Integer, SportVO> voMap = sportPOList.stream().collect(Collectors.toMap(SportVO::getSportId,
                Function.identity()));
        List<BettingPlayVO> playPOList = bettingPlayService.getListByIds(acIds);
        List<BettingPlayVO> filterList = playPOList.stream().filter(v -> StringUtils.isNotBlank(v.getPlayName()))
                .filter(v -> !v.getPlayName().contains("{")).collect(Collectors.toList());
        LinkedHashMap<Integer, List<BettingPlayVO>> linkedHashMap =
                filterList.stream().collect(Collectors.groupingBy(BettingPlayVO::getSportId, LinkedHashMap::new,
                        Collectors.toList()));
        for (Integer acId : acIds) {
            JSONObject obj = JSONUtil.createObj();
            List<BettingPlayVO> bettingPlayVOS = linkedHashMap.get(acId);
            linkedHashMap.get(acId).sort(Comparator.comparing(BettingPlayVO::getPlayId));
            List<String> childList = Lists.newArrayList();
            for (BettingPlayVO bettingPlayVO : bettingPlayVOS) {
                if (!map.get(acId).contains(bettingPlayVO.getPlayId())) continue;
                childList.add(bettingPlayVO.getPlayName());
            }
            obj.put(voMap.get(acId).getSportName(), childList);
            list.add(obj);
        }
        return list;
    }

    private List<String> virSportPlayIdToPlayName(String playId) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isBlank(playId)) {
            return list;
        }
        List<VirtualSportTypePO> plays =
                virtualSportTypeService.list(new QueryWrapper<VirtualSportTypePO>()
                        .select("name_code", "virtual_sport_id", "introduction")
                        .in("id", Arrays.asList(playId.split(",")))
                        .orderByAsc("name_code"));
        for (VirtualSportTypePO play : plays) {
            list.add(play.getIntroduction());
        }
        return list;
    }

}
