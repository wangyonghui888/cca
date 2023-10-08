package com.panda.sport.merchant.manage.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.slotMachine.*;
import com.panda.sport.merchant.manage.entity.vo.SlotJackpotCfgVO;
import com.panda.sport.merchant.manage.entity.vo.SlotPropsCfgVO;
import com.panda.sport.merchant.manage.service.*;
import com.panda.sport.merchant.manage.util.ExportUtils;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baylee
 * @version 1.0
 * @date 02/15/22 11:21:52
 */
@RestController
@Slf4j
@RequestMapping("/slotMachine")
public class SlotMachineController {

    @Resource
    private ISlotTicketDictService slotTicketDictService;
    @Resource
    private ISlotPropsDictService slotPropsDictService;
    @Resource
    private ISlotMachineCfgService slotMachineCfgService;
    @Resource
    private ISlotJackpotCfgService slotJackpotCfgService;
    @Resource
    private ISlotsLotteryRecordsService slotsLotteryRecordsService;
    @Resource
    private ISlotsSyntheticRecordsService slotsSyntheticRecordsService;
    @Resource
    private ISlotsPropResetRecordsService slotsPropResetRecordsService;
    @Resource
    private MerchantLogService merchantLogService;

    // 奖券管理
    @GetMapping("/ticketList")
    public Response<?> ticketList() {
        List<SlotTicketDict> ticketDictList =
                slotTicketDictService.list(new QueryWrapper<SlotTicketDict>().orderByAsc("create_time"));
        return Response.returnSuccess(ticketDictList);
    }

    @AuthRequiredPermission("slotMachine:addTicket")
    @PostMapping("/addTicket")
    public Response<?> addTicket(@RequestBody @Valid SlotTicketForm slotTicketForm, HttpServletRequest request) {
        long currentTimeMillis = System.currentTimeMillis();
        SlotTicketDict afterValue = new SlotTicketDict();
        BeanUtils.copyProperties(slotTicketForm, afterValue);
        afterValue.setState(0);
        afterValue.setCreateTime(currentTimeMillis);
        afterValue.setLastUpdateTime(currentTimeMillis);
        slotTicketDictService.save(afterValue);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.LOTTERY_MANAGEMENT.getCode(), "新增奖券", "运营管理-奖券管理",
                MerchantLogTypeEnum.LOTTERY_MANAGEMENT.getPageCode().get(0), "", "新增奖券", "1100001", "新增奖券",
                convertTicketLog(null), convertTicketLog(afterValue), request);
        return Response.returnSuccess();
    }

    @AuthRequiredPermission("slotMachine:editTicket")
    @PostMapping("/editTicket")
    public Response<?> editTicket(@RequestBody @Valid SlotTicketForm slotTicketForm, HttpServletRequest request) {
        Long ticketFormId = slotTicketForm.getId();
        if (ticketFormId == null) {
            return Response.returnFail("id can not be null");
        }
        if (ticketFormId == 1L) {
            return Response.returnFail("this ticket can not be edit");
        }
        if (ticketFormId.intValue() == slotTicketForm.getBaseTicketId()) {
            return Response.returnFail("合成材料与目标奖券不能为同一类型奖券");
        }
        SlotTicketDict beforeValue = slotTicketDictService.getById(ticketFormId);
        if (beforeValue == null) {
            return Response.returnFail("奖券ID错误!");
        }
        SlotTicketDict afterValue = new SlotTicketDict();
        BeanUtils.copyProperties(slotTicketForm, afterValue);
        afterValue.setLastUpdateTime(System.currentTimeMillis());
        slotTicketDictService.updateById(afterValue);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.LOTTERY_MANAGEMENT.getCode(), "编辑", "运营管理-奖券管理",
                MerchantLogTypeEnum.LOTTERY_MANAGEMENT.getPageCode().get(1), "", "编辑奖券", "1100003", "编辑奖券",
                convertTicketLog(beforeValue), convertTicketLog(afterValue), request);
        return Response.returnSuccess();
    }

    private List<String> convertTicketLog(SlotTicketDict ticketDict) {
        List<String> list = Lists.newArrayList();
        if (ticketDict == null) {
            list.add("--");
            return list;
        }
        Map<Integer, String> ticketMap = getTicketMap();
        list.add("奖券名称：" + ticketDict.getTicketName());
        list.add("合成材料：" + ticketMap.get(ticketDict.getBaseTicketId()));
        list.add("合成率：" + ticketDict.getSyntheticRate());
        list.add("合成材料数：" + ticketDict.getBaseTicketNumber());
        list.add("返还率：" + ticketDict.getReturnRate());
        list.add("返还奖券类型：" + ticketMap.get(ticketDict.getReturnTicketId()));
        list.add("合成提升概率：" + ticketDict.getSyntheticImproveRate());
        list.add("消耗幸运奖券数：" + ticketDict.getSyntheticImproveNumber());
        return list;
    }

    private Map<Integer, String> getTicketMap() {
        List<SlotTicketDict> dictList = slotTicketDictService.list();
        return dictList.stream().collect(Collectors.toMap(o -> o.getId().intValue(),
                SlotTicketDict::getTicketName));
    }

    @AuthRequiredPermission("slotMachine:modifyTicketState")
    @PostMapping("/modifyTicketState")
    public Response<?> modifyTicketState(@RequestParam Long id,
                                         @RequestParam Integer state, HttpServletRequest request) {
        if (id == 1L) {
            return Response.returnFail("普通奖券不能被关闭");
        }
        if (state == 0) {
            // 点击关闭该奖券时，系统校验当前的奖券是否有被设置为材料奖券或老虎机抽奖奖券，
            // 若是，则提示：“当前奖券已被使用，请修改老虎机抽奖或奖券合成配置
            //查询合成配置
            List<SlotTicketDict> checkTicket = slotTicketDictService.list(new QueryWrapper<SlotTicketDict>().eq(
                    "base_ticket_id", id));
            if (!checkTicket.isEmpty()) {
                return Response.returnFail("当前奖券已被使用，请修改" + checkTicket.get(0).getTicketName() + "合成配置");
            }
            //查询老虎机配置
            List<SlotMachineCfg> checkMachine = slotMachineCfgService.list(new QueryWrapper<SlotMachineCfg>().eq(
                    "slot_ticket_id", id));
            if (!checkMachine.isEmpty()) {
                return Response.returnFail("当前奖券已被使用，请修改" + checkMachine.get(0).getSlotMachineName() + "抽奖配置");
            }
        }
        SlotTicketDict ticketDict = slotTicketDictService.getById(id);
        if (Objects.isNull(ticketDict)) {
            return Response.returnFail("id error, can not find cfg");
        }
        Integer preState = ticketDict.getState();
        ticketDict.setState(state);
        ticketDict.setLastUpdateTime(System.currentTimeMillis());
        slotTicketDictService.updateById(ticketDict);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.LOTTERY_MANAGEMENT.getCode(), "开关奖券状态", "运营管理-奖券管理",
                MerchantLogTypeEnum.LOTTERY_MANAGEMENT.getPageCode().get(2), "", "奖券状态", "1100002", "奖券状态",
                Arrays.asList("奖券ID：" + ticketDict.getId(), "奖券状态：" + (preState == 0 ? "关" : "开")),
                Arrays.asList("奖券ID：" + ticketDict.getId(), "奖券状态：" + (state == 0 ? "关" : "开")),
                request);
        return Response.returnSuccess();
    }

    @GetMapping("/selectTicketList")
    public Response<?> selectTicketList() {
        List<SlotTicketDict> ticketDictList = slotTicketDictService.list(new QueryWrapper<SlotTicketDict>()
                .select("id", "ticket_name")
                .eq("state", 1)
                .orderByAsc("create_time"));
        return Response.returnSuccess(ticketDictList);
    }

    //道具管理
    @GetMapping("/propsList")
    public Response<?> propsList() {
        List<SlotPropsDict> propsList = slotPropsDictService.list(new QueryWrapper<SlotPropsDict>().orderByAsc(
                "create_time"));
        propsList.forEach(v -> {
            v.setBonusMultiple(v.getBonusMultiple().divide(new BigDecimal(100), 2, RoundingMode.DOWN));
        });
        return Response.returnSuccess(propsList);
    }

    @AuthRequiredPermission("slotMachine:addProps")
    @PostMapping("/addProps")
    public Response<?> addProps(@RequestParam Integer propsType,
                                @RequestParam String propsName,
                                @RequestParam BigDecimal bonusMultiple,
                                HttpServletRequest request) {

        BigDecimal multiple = bonusMultiple.multiply(new BigDecimal(100));
        if (multiple.compareTo(BigDecimal.TEN) < 0) {
            return Response.returnFail("最小值不能小于0.1");
        }
        long currentTimeMillis = System.currentTimeMillis();
        SlotPropsDict slotPropsDict = new SlotPropsDict();
        slotPropsDict.setPropsType(propsType);
        slotPropsDict.setPropsName(propsName);
        slotPropsDict.setBonusMultiple(multiple);
        slotPropsDict.setState(1);
        slotPropsDict.setCreateTime(currentTimeMillis);
        slotPropsDict.setLastUpdateTime(currentTimeMillis);
        slotPropsDictService.save(slotPropsDict);
        //保存日志
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.PROPS_MANAGEMENT.getCode(), "新增道具", "运营管理—道具管理",
                MerchantLogTypeEnum.PROPS_MANAGEMENT.getPageCode().get(0), "", "新增道具", "1100004", "新增道具",
                convertPropsLog(null), convertPropsLog(slotPropsDict), request);
        return Response.returnSuccess();
    }

    @AuthRequiredPermission("slotMachine:editProps")
    @PostMapping("/editProps")
    public Response<?> editProps(@RequestParam Long id,
                                 @RequestParam Integer propsType,
                                 @RequestParam String propsName,
                                 @RequestParam BigDecimal bonusMultiple,
                                 HttpServletRequest request) {
        BigDecimal multiple = bonusMultiple.multiply(new BigDecimal(100));
        if (multiple.compareTo(BigDecimal.TEN) < 0) {
            return Response.returnFail("最小值不能小于0.1");
        }
        long currentTimeMillis = System.currentTimeMillis();
        SlotPropsDict slotPropsDict = slotPropsDictService.getById(id);
        List<String> beforeValue = convertPropsLog(slotPropsDict);
        if (Objects.isNull(slotPropsDict)) {
            return Response.returnFail("id error, can not find cfg");
        }
        slotPropsDict.setPropsType(propsType);
        slotPropsDict.setPropsName(propsName);
        slotPropsDict.setBonusMultiple(multiple);
        slotPropsDict.setLastUpdateTime(currentTimeMillis);
        slotPropsDictService.updateById(slotPropsDict);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.PROPS_MANAGEMENT.getCode(), "编辑道具", "运营管理—道具管理",
                MerchantLogTypeEnum.PROPS_MANAGEMENT.getPageCode().get(1), "", "编辑道具", "1100005", "编辑道具",
                beforeValue, convertPropsLog(slotPropsDict), request);
        return Response.returnSuccess();
    }

    private List<String> convertPropsLog(SlotPropsDict propsDict) {
        List<String> list = Lists.newArrayList();
        if (propsDict == null) {
            list.add("--");
            return list;
        }
        list.add("道具ID：" + propsDict.getId());
        list.add("道具名称：" + propsDict.getPropsName());
        list.add("道具类型：" + (propsDict.getPropsType() == 1 ? "奖金倍率卡" : "幸运奖券"));
        list.add("彩金倍数/奖券数：" + propsDict.getBonusMultiple().divide(new BigDecimal(100), 2, RoundingMode.DOWN));
        return list;
    }

    @AuthRequiredPermission("slotMachine:modifyPropsState")
    @PostMapping("/modifyPropsState")
    public Response<?> modifyPropsState(@RequestParam Long id,
                                        @RequestParam Integer state) {
        SlotPropsDict slotPropsDict = slotPropsDictService.getById(id);
        if (Objects.isNull(slotPropsDict)) {
            return Response.returnFail("id error, can not find cfg");
        }
        slotPropsDict.setState(state);
        slotPropsDict.setLastUpdateTime(System.currentTimeMillis());
        return Response.returnSuccess();
    }

    //老虎机配置
    @GetMapping("/slotMachineList")
    public Response<?> slotMachineList() {
        QueryWrapper<SlotMachineCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "slot_machine_name", "slot_ticket_id", "lottery_number", "state", "sort_no",
                "create_time", "last_update_time").orderByAsc("sort_no");
        List<SlotMachineCfg> cfgList = slotMachineCfgService.list(queryWrapper);
        return Response.returnSuccess(cfgList);
    }

    @AuthRequiredPermission("slotMachine:addSlotMachineCfg")
    @PostMapping("/addSlotMachineCfg")
    public Response<?> addSlotMachineCfg(@Valid @RequestBody SlotMachineForm slotMachineForm,
                                         HttpServletRequest request) {

        return slotMachineCfgService.addSlotMachineCfg(slotMachineForm, request);
    }

    @AuthRequiredPermission("slotMachine:editSlotMachineCfg")
    @PostMapping("/editSlotMachineCfg")
    public Response<?> editSlotMachineCfg(@Valid @RequestBody SlotMachineForm slotMachineForm,
                                          HttpServletRequest request) {

        Long formId = slotMachineForm.getId();
        if (formId == null) {
            return Response.returnFail("id can not be null");
        }
        SlotMachineCfg machineCfg = slotMachineCfgService.getById(formId);
        if (Objects.isNull(machineCfg)) {
            return Response.returnFail("can not find slot machine cfg");
        }
        List<String> beforeValue = convertSlotMachineLog(machineCfg);
        BeanUtils.copyProperties(slotMachineForm, machineCfg);
        machineCfg.setLastUpdateTime(System.currentTimeMillis());
        slotMachineCfgService.updateById(machineCfg);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "编辑", "运营管理—游戏配置",
                MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(2), "", "编辑老虎机", "1100010", "编辑老虎机",
                beforeValue, convertSlotMachineLog(machineCfg), request);
        return Response.returnSuccess();
    }

    private List<String> convertSlotMachineLog(SlotMachineCfg machineCfg) {
        List<String> list = Lists.newArrayList();
        if (machineCfg == null) {
            list.add("--");
            return list;
        }
        Map<Integer, String> ticketMap = getTicketMap();
        list.add("老虎机名称：" + machineCfg.getSlotMachineName());
        list.add("奖券消费类型：" + ticketMap.get(machineCfg.getSlotTicketId()));
        list.add("奖券消耗数：" + machineCfg.getLotteryNumber());
        return list;
    }

    @AuthRequiredPermission("slotMachine:draggableSort")
    @PostMapping("/draggableSort")
    public Response<?> draggableSort(HttpServletRequest request,@RequestParam Long id,
                                     @RequestParam Integer sortNo) {
        return slotMachineCfgService.draggableSort(request,id, sortNo);
    }

    @AuthRequiredPermission("slotMachine:modifySlotMachineState")
    @PostMapping("/modifySlotMachineState")
    public Response<?> modifySlotMachineState(@RequestParam Long id,
                                              @RequestParam Integer state,
                                              HttpServletRequest request) {
        SlotMachineCfg machineCfg = slotMachineCfgService.getById(id);
        if (Objects.isNull(machineCfg)) {
            return Response.returnFail("can not find slot machine cfg");
        }
        List<String> beforeValue = convertMachineState(machineCfg);
        if (state == 1) {
            //开启时校验彩金和道具配置，未配置时禁止打开
            List<SlotJackpotCfg> cfgList = slotJackpotCfgService.list(new QueryWrapper<SlotJackpotCfg>().eq(
                    "slot_machine_id", id));
            BigDecimal reduce =
                    cfgList.stream().filter(v -> v.getNumber() == 0).map(SlotJackpotCfg::getAppearanceRate).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (reduce.compareTo(new BigDecimal(40000)) == 0) {
                return Response.returnFail("请配置老虎机彩金");
            }
            if ("{}".equals(machineCfg.getPropsCfg())) {
                return Response.returnFail("请配置老虎机道具项");
            }
        }
        machineCfg.setState(state);
        machineCfg.setLastUpdateTime(System.currentTimeMillis());
        slotMachineCfgService.updateById(machineCfg);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "开关老虎机", "运营管理—游戏配置",
                MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(0), "", "老虎机状态", "1100013", "老虎机状态",
                beforeValue, convertMachineState(machineCfg), request);
        return Response.returnSuccess();
    }

    private List<String> convertMachineState(SlotMachineCfg machineCfg) {
        List<String> list = Lists.newArrayList();
        if (machineCfg == null) {
            list.add("--");
            return list;
        }
        list.add("老虎机名称：" + machineCfg.getSlotMachineName());
        list.add("老虎机状态：" + (machineCfg.getState() == 0 ? "关" : "开"));
        return list;
    }

    @GetMapping("/querySlotJackpotCfg")
    public Response<?> querySlotJackpotCfg(@RequestParam Long slotMachineId) {
        SlotJackpotCfgVO slotJackpotCfgVO = new SlotJackpotCfgVO();
        List<List<SlotJackpotCfg>> objList = Lists.newArrayList();
        SlotMachineCfg machineCfg = slotMachineCfgService.getById(slotMachineId);
        if (Objects.isNull(machineCfg)) {
            return Response.returnFail("can not find slot machine cfg");
        }
        List<SlotJackpotCfg> cfgList = slotJackpotCfgService.list(new QueryWrapper<SlotJackpotCfg>().eq(
                "slot_machine_id", slotMachineId));
        if (cfgList.isEmpty()) {
            return Response.returnFail("can not find slot machine jackpot cfg");
        }
        Map<Integer, List<SlotJackpotCfg>> cfgMap =
                cfgList.stream().collect(Collectors.groupingBy(SlotJackpotCfg::getNumber));

        for (Map.Entry<Integer, List<SlotJackpotCfg>> entry : cfgMap.entrySet()) {
            List<SlotJackpotCfg> list = entry.getValue();
            list.forEach(v -> v.setAppearanceRate(v.getAppearanceRate().divide(new BigDecimal(100), 2,
                    RoundingMode.DOWN)));
            objList.add(list);
        }
        slotJackpotCfgVO.setJackpotList(objList);
        slotJackpotCfgVO.setDailyGameNumber(machineCfg.getDailyGameNumber());
        return Response.returnSuccess(slotJackpotCfgVO);
    }

    @AuthRequiredPermission("slotMachine:editSlotJackpotCfg")
    @PostMapping("/editSlotJackpotCfg")
    public Response<?> editSlotJackpotCfg(@RequestBody SlotJackpotCfgForm slotJackpotCfgForm,
                                          HttpServletRequest request) {
        List<SlotJackpotCfg> jackpotList = slotJackpotCfgForm.getJackpotList();
        Integer cfgFormId = slotJackpotCfgForm.getId();
        SlotMachineCfg machineCfg = slotMachineCfgService.getById(cfgFormId);
        if (!jackpotList.isEmpty()) {
            List<SlotJackpotCfg> cfgList = slotJackpotCfgService.list(new QueryWrapper<SlotJackpotCfg>().eq(
                    "slot_machine_id", machineCfg.getId()));
            cfgList.sort(Comparator.comparing(SlotJackpotCfg::getNumber).thenComparing(SlotJackpotCfg::getId));
            jackpotList.forEach(v -> v.setAppearanceRate(v.getAppearanceRate().multiply(new BigDecimal(100))));
            slotJackpotCfgService.saveOrUpdateBatch(jackpotList);
            merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "彩金配置", "运营管理—游戏配置",
                    MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(4), "", "彩金配置", "1100008", "彩金配置",
                    convertJackpotLog(cfgList, machineCfg.getSlotMachineName()), convertJackpotLog(jackpotList,
                            machineCfg.getSlotMachineName()), request);
        }
        Integer dailyGameNumber = slotJackpotCfgForm.getDailyGameNumber();
        if (dailyGameNumber != null && cfgFormId != null) {
            if (machineCfg == null) {
                return Response.returnFail("can not find slot machine cfg");
            }
            machineCfg.setDailyGameNumber(dailyGameNumber);
            slotMachineCfgService.updateById(machineCfg);
            merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "彩金配置", "运营管理—游戏配置",
                    MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(5), "", "彩金配置", "1100008", "游戏次数设置",
                    convertNumberLog(machineCfg.getDailyGameNumber(), machineCfg.getSlotMachineName()),
                    convertNumberLog(dailyGameNumber, machineCfg.getSlotMachineName()), request);
        }
        return Response.returnSuccess();
    }

    private List<String> convertNumberLog(Integer dailyGameNumber, String slotMachineName) {
        List<String> list = Lists.newArrayList();
        list.add("老虎机名称：" + slotMachineName);
        if (dailyGameNumber == 0) {
            list.add("每日游戏次数：不限次数");
        } else {
            list.add("每日游戏次数：" + dailyGameNumber);
        }
        return list;
    }

    private List<String> convertJackpotLog(List<SlotJackpotCfg> cfgList, String slotMachineName) {
        List<String> list = Lists.newArrayList();
        if (cfgList.isEmpty()) {
            list.add("--");
            return list;
        }
        list.add("老虎机名称：" + slotMachineName);
        for (SlotJackpotCfg cfg : cfgList) {
            list.add(convertPlaceToName(cfg.getPlace()));
            list.add(cfg.getNumber() + "：" + cfg.getAppearanceRate().divide(new BigDecimal(100), 2,
                    RoundingMode.DOWN) + "%");
        }
        return list;
    }

    private String convertPlaceToName(Integer place) {
        String placeName = "";
        switch (place) {
            case 1:
                placeName = "个位";
                break;
            case 2:
                placeName = "十位";
                break;
            case 3:
                placeName = "百位";
                break;
            case 4:
                placeName = "千位";
                break;
            default:

        }
        return placeName;
    }


    @GetMapping("/selectPropsList")
    public Response<?> selectPropsList() {
        QueryWrapper<SlotPropsDict> queryWrapper = new QueryWrapper<SlotPropsDict>().select("id", "props_type",
                "props_name", "bonus_multiple").eq("state", 1);
        List<SlotPropsDict> dictList = slotPropsDictService.list(queryWrapper);
        dictList.forEach(v -> v.setBonusMultiple(v.getBonusMultiple().divide(new BigDecimal(100), 2,
                RoundingMode.DOWN)));
        return Response.returnSuccess(dictList);
    }

    @AuthRequiredPermission("slotMachine:saveSlotMachineProps")
    @PostMapping("/saveSlotMachineProps")
    public Response<?> addSlotMachineProps(@RequestBody @Valid SlotPropsCfgFrom slotPropsCfgFrom,
                                           HttpServletRequest request) {

        Long fromId = slotPropsCfgFrom.getId();
        SlotMachineCfg machineCfg = slotMachineCfgService.getById(fromId);
        if (machineCfg == null) {
            return Response.returnFail("can not find slot machine cfg");
        }
        List<String> beforeValue = convertPropsCfg(machineCfg);

        BigDecimal decimal =
                slotPropsCfgFrom.getProps().stream().map(SlotPropsCfgVO::getPropsRate).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        if (decimal.compareTo(new BigDecimal(100)) != 0) {
            return Response.returnFail("出现概率配置不得低于或高于100");
        }
        //道具倍率放大100倍
        slotPropsCfgFrom.getProps().forEach(v -> v.setPropsRate(v.getPropsRate().multiply(new BigDecimal(100))));
        machineCfg.setPropsCfg(JSONUtil.toJsonStr(slotPropsCfgFrom));
        slotMachineCfgService.updateById(machineCfg);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "道具配置", "运营管理—游戏配置",
                MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(6), "", "道具配置", "1100009", "道具配置",
                beforeValue, convertPropsCfg(machineCfg), request);
        return Response.returnSuccess();
    }

    private List<String> convertPropsCfg(SlotMachineCfg machineCfg) {
        List<String> list = Lists.newArrayList();
        if ("{}".equals(machineCfg.getPropsCfg())) {
            list.add("--");
            return list;
        }
        SlotPropsCfgFrom cfgFrom = JSONUtil.toBean(machineCfg.getPropsCfg(), SlotPropsCfgFrom.class);
        list.add("奖券消费类型：" + cfgFrom.getTicketName());
        list.add("重置消耗奖券数：" + cfgFrom.getResetTicketNumber());
        for (SlotPropsCfgVO prop : cfgFrom.getProps()) {
            list.add("道具名称：" + prop.getPropsName() + ", 出现概率：" + prop.getPropsRate().divide(new BigDecimal(100), 2,
                    RoundingMode.DOWN));
        }
        return list;
    }


    @GetMapping("/queryMachinePropsCfg")
    public Response<?> queryMachinePropsCfg(@RequestParam Long id) {
        SlotMachineCfg machineCfg = slotMachineCfgService.getById(id);
        if (machineCfg == null) {
            return Response.returnFail("can not find slot machine cfg");
        }
        SlotPropsCfgFrom cfgFrom = JSONUtil.toBean(machineCfg.getPropsCfg(), SlotPropsCfgFrom.class);
        if (cfgFrom.getProps() != null && !cfgFrom.getProps().isEmpty()) {
            //道具倍率缩小100倍
            cfgFrom.getProps().forEach(v -> v.setPropsRate(v.getPropsRate().divide(new BigDecimal(100), 2,
                    RoundingMode.DOWN)));
        }
        return Response.returnSuccess(cfgFrom);
    }

    //游戏记录
    //彩金记录
    @PostMapping("/jackpotRecord")
    public Response<?> jackpotRecord(@RequestBody JackpotRecordForm recordForm) {
        PageHelper.startPage(recordForm.getCurrent(), recordForm.getSize());
        List<SlotsLotteryRecords> records = queryLotteryRecord(recordForm);
        PageInfo<SlotsLotteryRecords> pageInfo = new PageInfo<>(records);
        return Response.returnSuccess(pageInfo);
    }

    @AuthRequiredPermission("slotMachine:jackpotRecordExport")
    @PostMapping("/jackpotRecordExport")
    public void jackpotRecordExport(@RequestBody JackpotRecordForm recordForm, HttpServletResponse response) {
        List<SlotsLotteryRecords> recordsList = queryLotteryRecord(recordForm);
        LinkedHashMap<String, String> headers = Maps.newLinkedHashMap();
        headers.put("1", "用户ID");
        headers.put("2", "用户名");
        headers.put("3", "商户编号");
        headers.put("4", "滚轴彩金");
        headers.put("5", "道具名称");
        headers.put("6", "道具倍率");
        headers.put("7", "总计彩金");
        headers.put("8", "领取时间");
        List<LinkedHashMap<String, Object>> mapList = Lists.newArrayList();
        for (SlotsLotteryRecords records : recordsList) {
            LinkedHashMap<String, Object> datas = Maps.newLinkedHashMap();
            datas.put("1", records.getUid().concat("\t"));
            datas.put("2", records.getUserName());
            datas.put("3", records.getMerchantCode().concat("\t"));
            datas.put("4", records.getAward());
            datas.put("5", records.getPropType());
            datas.put("6", records.getPropTimes());
            datas.put("7", records.getTotalAward());
            datas.put("8", DateUtil.formatDateTime(new Date(records.getCreateTime())));
            mapList.add(datas);
        }
        String fileName = DateUtil.formatDate(new Date()) + "_彩金记录.csv";
        ExportUtils.browserDownload(fileName, headers, mapList, response);
    }

    private List<SlotsLotteryRecords> queryLotteryRecord(JackpotRecordForm recordForm) {
        QueryWrapper<SlotsLotteryRecords> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "user_name", "merchant_code", "award", "prop_type", "prop_times", "total_award",
                "create_time").orderByDesc("create_time");
        String merchantCode = recordForm.getMerchantCode();
        if (merchantCode != null) {
            wrapper.eq("merchant_code", merchantCode);
        }
        String uid = recordForm.getUid();
        if (uid != null) {
            wrapper.and(o -> o.eq("uid", uid).or().eq("user_name", uid));
        }
        Long startTime = recordForm.getStartTime();
        if (startTime != null) {
            wrapper.ge("create_time", startTime);
        }
        Long endTime = recordForm.getEndTime();
        if (endTime != null) {
            wrapper.le("create_time", endTime);
        }
        List<SlotsLotteryRecords> records = slotsLotteryRecordsService.list(wrapper);
        records.forEach(v -> {
            v.setAward(v.getAward().divide(new BigDecimal(100), 2, RoundingMode.DOWN));
            v.setTotalAward(v.getTotalAward().divide(new BigDecimal(100), 2, RoundingMode.DOWN));
        });
        return records;
    }

    //合成记录
    @PostMapping("/syntheticRecord")
    public Response<?> syntheticRecord(@RequestBody SyntheticRecordForm recordForm) {
        PageHelper.startPage(recordForm.getCurrent(), recordForm.getSize());
        List<SlotsSyntheticRecords> recordsList = querySyntheticRecord(recordForm);
        PageInfo<SlotsSyntheticRecords> pageInfo = new PageInfo<>(recordsList);
        return Response.returnSuccess(pageInfo);
    }

    @AuthRequiredPermission("slotMachine:syntheticRecordExport")
    @PostMapping("/syntheticRecordExport")
    public void syntheticRecordExport(@RequestBody SyntheticRecordForm recordForm, HttpServletResponse response) {
        List<SlotsSyntheticRecords> recordsList = querySyntheticRecord(recordForm);
        LinkedHashMap<String, String> headers = Maps.newLinkedHashMap();
        headers.put("1", "用户ID");
        headers.put("2", "用户名");
        headers.put("3", "商户编号");
        headers.put("4", "消耗奖券数");
        headers.put("5", "消耗奖券类型");
        headers.put("6", "合成奖券数");
        headers.put("7", "合成奖券类型");
        headers.put("8", "返还奖券数");
        headers.put("9", "返还奖券类型");
        headers.put("10", "合成时间");
        List<LinkedHashMap<String, Object>> mapList = Lists.newArrayList();
        for (SlotsSyntheticRecords records : recordsList) {
            LinkedHashMap<String, Object> datas = Maps.newLinkedHashMap();
            datas.put("1", records.getUid().concat("\t"));
            datas.put("2", records.getUserName());
            datas.put("3", records.getMerchantCode().concat("\t"));
            datas.put("4", records.getSourceToken());
            datas.put("5", records.getSourceTokenType());
            datas.put("6", records.getTargetToken());
            datas.put("7", records.getTargetTokenType());
            datas.put("8", records.getReturnToken());
            datas.put("9", records.getReturnTokenType());
            datas.put("10", DateUtil.formatDateTime(new Date(records.getCreateTime())));
            mapList.add(datas);
        }
        String fileName = DateUtil.formatDate(new Date()) + "_合成记录.csv";
        ExportUtils.browserDownload(fileName, headers, mapList, response);
    }

    private List<SlotsSyntheticRecords> querySyntheticRecord(SyntheticRecordForm recordForm) {
        QueryWrapper<SlotsSyntheticRecords> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "user_name", "merchant_code", "source_token", "source_token_type", "target_token",
                "target_token_type", "return_token", "return_token_type", "create_time").orderByDesc("create_time");
        String merchantCode = recordForm.getMerchantCode();
        if (merchantCode != null) {
            wrapper.eq("merchant_code", merchantCode);
        }
        String uid = recordForm.getUid();
        if (uid != null) {
            wrapper.and(o -> o.eq("uid", uid).or().eq("user_name", uid));
        }
        Integer sourceTokenId = recordForm.getSourceTokenId();
        if (sourceTokenId != null) {
            wrapper.eq("source_token_id", sourceTokenId);
        }
        Integer targetTokenId = recordForm.getTargetTokenId();
        if (targetTokenId != null) {
            wrapper.eq("target_token_id", targetTokenId);
        }
        Integer returnTokenId = recordForm.getReturnTokenId();
        if (returnTokenId != null) {
            wrapper.eq("return_token_id", returnTokenId);
        }
        Long startTime = recordForm.getStartTime();
        if (startTime != null) {
            wrapper.ge("create_time", startTime);
        }
        Long endTime = recordForm.getEndTime();
        if (endTime != null) {
            wrapper.le("create_time", endTime);
        }
        return slotsSyntheticRecordsService.list(wrapper);
    }

    //重置记录
    @PostMapping("/resetRecord")
    public Response<?> resetRecord(@RequestBody ResetRecordForm recordForm) {
        PageHelper.startPage(recordForm.getCurrent(), recordForm.getSize());
        List<SlotsPropResetRecords> recordsList = queryResetRecord(recordForm);
        PageInfo<SlotsPropResetRecords> pageInfo = new PageInfo<>(recordsList);
        return Response.returnSuccess(pageInfo);
    }

    @AuthRequiredPermission("slotMachine:resetRecordExport")
    @PostMapping("/resetRecordExport")
    public void resetRecordExport(@RequestBody ResetRecordForm recordForm, HttpServletResponse response) {
        List<SlotsPropResetRecords> recordsList = queryResetRecord(recordForm);
        LinkedHashMap<String, String> headers = Maps.newLinkedHashMap();
        headers.put("1", "用户ID");
        headers.put("2", "用户名");
        headers.put("3", "商户编号");
        headers.put("4", "道具名称");
        headers.put("5", "道具倍率");
        headers.put("6", "奖券消耗");
        headers.put("7", "奖券类型");
        headers.put("8", "重置时间");
        List<LinkedHashMap<String, Object>> mapList = Lists.newArrayList();
        for (SlotsPropResetRecords records : recordsList) {
            LinkedHashMap<String, Object> datas = Maps.newLinkedHashMap();
            datas.put("1", records.getUid().concat("\t"));
            datas.put("2", records.getUserName());
            datas.put("3", records.getMerchantCode().concat("\t"));
            datas.put("4", records.getPropType());
            datas.put("5", records.getPropTimes());
            datas.put("6", records.getUseToken());
            datas.put("7", records.getUseTokenType());
            datas.put("8", DateUtil.formatDateTime(new Date(records.getCreateTime())));
            mapList.add(datas);
        }
        String fileName = DateUtil.formatDate(new Date()) + "_重置记录.csv";
        ExportUtils.browserDownload(fileName, headers, mapList, response);
    }

    private List<SlotsPropResetRecords> queryResetRecord(ResetRecordForm recordForm) {
        QueryWrapper<SlotsPropResetRecords> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "user_name", "merchant_code", "prop_type", "prop_times", "use_token", "use_token_type",
                "create_time").orderByDesc("create_time");
        Long startTime = recordForm.getStartTime();
        if (startTime != null) {
            wrapper.ge("create_time", startTime);
        }
        Long endTime = recordForm.getEndTime();
        if (endTime != null) {
            wrapper.le("create_time", endTime);
        }
        String merchantCode = recordForm.getMerchantCode();
        if (merchantCode != null) {
            wrapper.eq("merchant_code", merchantCode);
        }
        String uid = recordForm.getUid();
        if (uid != null) {
            wrapper.and(o -> o.eq("uid", uid).or().eq("user_name", uid));
        }
        return slotsPropResetRecordsService.list(wrapper);
    }

}
