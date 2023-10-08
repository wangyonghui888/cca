package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.SlotMachineCfgMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.SlotJackpotCfg;
import com.panda.sport.merchant.common.po.bss.SlotMachineCfg;
import com.panda.sport.merchant.common.po.bss.SlotTicketDict;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.slotMachine.SlotMachineForm;
import com.panda.sport.merchant.manage.service.ISlotJackpotCfgService;
import com.panda.sport.merchant.manage.service.ISlotMachineCfgService;
import com.panda.sport.merchant.manage.service.ISlotTicketDictService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 老虎机游戏配置表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
@Service
public class SlotMachineCfgServiceImpl extends ServiceImpl<SlotMachineCfgMapper, SlotMachineCfg> implements ISlotMachineCfgService {

    @Resource
    private SlotMachineCfgMapper slotMachineCfgMapper;
    @Resource
    private ISlotJackpotCfgService slotJackpotCfgService;
    @Resource
    private MerchantLogService merchantLogService;
    @Resource
    private ISlotTicketDictService slotTicketDictService;

    @Override
    public Response<?> addSlotMachineCfg(SlotMachineForm slotMachineForm, HttpServletRequest request) {
        Integer maxSortNo = slotMachineCfgMapper.selectMaxSortNo();
        long currentTimeMillis = System.currentTimeMillis();
        SlotMachineCfg slotMachineCfg = new SlotMachineCfg();
        slotMachineCfg.setSlotMachineName(slotMachineForm.getSlotMachineName());
        slotMachineCfg.setSlotTicketId(slotMachineForm.getSlotTicketId());
        slotMachineCfg.setLotteryNumber(slotMachineForm.getLotteryNumber());
        slotMachineCfg.setDailyGameNumber(1);
        slotMachineCfg.setPropsCfg(JSONUtil.createObj().toString());
        slotMachineCfg.setState(0);
        slotMachineCfg.setSortNo(maxSortNo + 1);
        slotMachineCfg.setCreateTime(currentTimeMillis);
        slotMachineCfg.setLastUpdateTime(currentTimeMillis);
        slotMachineCfgMapper.insert(slotMachineCfg);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "新增", "运营管理—游戏配置",
                MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(1), "", "新增老虎机", "1100011", "新增老虎机",
                null, convertSlotMachineLog(slotMachineCfg), request);
        //init jackpot config
        List<SlotJackpotCfg> jackpotCfgList = initJackpotCfgList(slotMachineCfg.getId());
        slotJackpotCfgService.saveBatch(jackpotCfgList);
        merchantLogService.saveOperationLog(MerchantLogTypeEnum.GAME_CONFIGURATION.getCode(), "新增", "运营管理—游戏配置",
                MerchantLogTypeEnum.GAME_CONFIGURATION.getPageCode().get(3), "", "新增彩金配置", "1100012", "新增彩金配置",
                null, convertJackpotLog(jackpotCfgList, slotMachineCfg.getSlotMachineName()), request);
        return Response.returnSuccess();
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

    private Map<Integer, String> getTicketMap() {
        List<SlotTicketDict> dictList = slotTicketDictService.list();
        return dictList.stream().collect(Collectors.toMap(o -> o.getId().intValue(),
                SlotTicketDict::getTicketName));
    }

    private static List<SlotJackpotCfg> initJackpotCfgList(Long slotMachineId) {
        List<SlotJackpotCfg> jackpotCfgList = Lists.newArrayList();
        for (int i = 1; i < 5; i++) {
            List<SlotJackpotCfg> subList = Lists.newArrayList();
            for (int j = 0; j < 10; j++) {
                SlotJackpotCfg slotJackpotCfg = new SlotJackpotCfg();
                slotJackpotCfg.setSlotMachineId(slotMachineId);
                slotJackpotCfg.setPlace(i);
                slotJackpotCfg.setNumber(j);
                if (j == 0) {
                    slotJackpotCfg.setAppearanceRate(new BigDecimal(10000));
                } else {
                    slotJackpotCfg.setAppearanceRate(BigDecimal.ZERO);
                }
                subList.add(slotJackpotCfg);
            }
            jackpotCfgList.addAll(subList);
        }
        return jackpotCfgList;
    }

    @Override
    public Response<?> draggableSort(HttpServletRequest request,Long id, Integer sortNo) {
        //获取当前排序
        SlotMachineCfg machineCfg = this.getById(id);
        if (machineCfg == null) {
            return Response.returnFail("can not find slot machine cfg");
        }
        Integer oldSortNO = machineCfg.getSortNo();
        long currentTimeMillis = System.currentTimeMillis();
        if (oldSortNO > sortNo) {
            //上移
            List<SlotMachineCfg> sortList = this.list(new QueryWrapper<SlotMachineCfg>()
                    .ge("sort_no", sortNo)
                    .lt("sort_no", oldSortNO)
                    .orderByAsc("sort_no"));
            //依次+1
            sortList.forEach(v -> {
                v.setSortNo(v.getSortNo() + 1);
                v.setLastUpdateTime(currentTimeMillis);
            });
            machineCfg.setSortNo(sortNo);
            sortList.add(machineCfg);
            this.saveOrUpdateBatch(sortList);
        }
        if (oldSortNO < sortNo) {
            //下移
            List<SlotMachineCfg> sortList = this.list(new QueryWrapper<SlotMachineCfg>()
                    .gt("sort_no", oldSortNO)
                    .le("sort_no", sortNo)
                    .orderByAsc("sort_no"));
            //依次-1
            sortList.forEach(v -> {
                v.setSortNo(v.getSortNo() - 1);
                v.setLastUpdateTime(currentTimeMillis);
            });
            machineCfg.setSortNo(sortNo);
            sortList.add(machineCfg);
            this.saveOrUpdateBatch(sortList);
        }
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("order"));
        vo.getBeforeValues().add("排序"+oldSortNO);
        vo.getAfterValues().add("排序"+sortNo);
        merchantLogService.saveLog(MerchantLogPageEnum.TASK_BOX_SET1, MerchantLogTypeEnum.PUSH_MANAGER_ODER, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, username, id.toString(), language , ip);
        return Response.returnSuccess();
    }
}
