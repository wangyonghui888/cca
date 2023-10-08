package com.panda.sport.merchant.manage.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.AcTaskPOMapper;
import com.panda.sport.bss.mapper.OlympicLuckyboxDictMapper;
import com.panda.sport.bss.mapper.TActivityConfigMapper;
import com.panda.sport.bss.mapper.TActivityConfigMapper.TActivityConfig;
import com.panda.sport.merchant.common.enums.BoxTypeEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.AcTaskPO;
import com.panda.sport.merchant.common.po.bss.SDailyLuckyBoxNumberPO;
import com.panda.sport.merchant.common.po.bss.SOlympicLuckyboxDictPo;
import com.panda.sport.merchant.common.po.bss.VirtualSportTypePO;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.JsonUtils;
import com.panda.sport.merchant.common.vo.activity.SDailyLuckyBoxNumberVo;
import com.panda.sport.merchant.manage.entity.form.BoxListForm;
import com.panda.sport.merchant.manage.service.IAcTaskService;
import com.panda.sport.merchant.manage.service.IBettingPlayService;
import com.panda.sport.merchant.manage.service.ISportService;
import com.panda.sport.merchant.manage.service.IVirtualSportTypeService;
import com.panda.sport.merchant.manage.service.MerchantLogService;

import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 活动任务表 服务实现类
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
@Service
@RefreshScope
public class AcTaskServiceImpl extends ServiceImpl<AcTaskPOMapper, AcTaskPO> implements IAcTaskService {

    @Resource
    private ISportService sportService;
    @Resource
    private IVirtualSportTypeService virtualSportTypeService;
    @Resource
    private IBettingPlayService bettingPlayService;

    @Value("${activity.sportIds:1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}")
    private List<Integer> acIds;

    @Value("${activity.virtual.sportIds:1001,1002,1004,1011}")
    private List<Integer> virtualIds;

    @Resource
    private AcTaskPOMapper acTaskPOMapper;
    @Resource
    private OlympicLuckyboxDictMapper olympicLuckyboxDictMapper;

    @Autowired
    MerchantLogService merchantLogService;
    @Autowired
    TActivityConfigMapper tActivityConfigMapper;

    @Override
    public List<SportAndPlayTreeVO> getSportAndPlayTree() {
        List<SportAndPlayTreeVO> list = Lists.newArrayList();
        List<SportVO> sportPOList = sportService.getListByIds(acIds);
        Map<Integer, SportVO> voMap = sportPOList.stream().collect(Collectors.toMap(SportVO::getSportId,
                Function.identity()));
        List<BettingPlayVO> playPOList = bettingPlayService.getListByIds(acIds);
        //List<BettingPlayVO> filterList = playPOList.stream().filter(v -> StringUtils.isNotBlank(v.getPlayName()))
        //        .filter(v -> !v.getPlayName().contains("{")).collect(Collectors.toList());
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
        return list;
    }

    @Override
    public List<SportAndPlayTreeVO> getVirtualSportTree() {
        List<VirtualSportTypePO> list =
                virtualSportTypeService.list(new QueryWrapper<VirtualSportTypePO>()
                        .select("name_code", "virtual_sport_id", "introduction")
                        .in("id", virtualIds)
                        .orderByAsc("name_code"));
        List<SportAndPlayTreeVO> virtualSportTree = Lists.newArrayList();
        for (VirtualSportTypePO virtualSportTypePO : list) {
            SportAndPlayTreeVO sportTree = SportAndPlayTreeVO.getInstance();
            sportTree.setTitle(virtualSportTypePO.getIntroduction());
            sportTree.setKey(Integer.valueOf(virtualSportTypePO.getVirtualSportId()));
            sportTree.setParentId(0);
            sportTree.setChildren(Lists.newArrayList());
            virtualSportTree.add(sportTree);

        }
        return virtualSportTree;
    }

    /**
     * 获取幸运盲盒
     *
     * @return
     */
    @Override
    public List<SOlympicLuckyboxDictPo> getLuckyBoxDict(BoxListForm boxListForm) {

        //List<SOlympicLuckyboxDictVo> sOlympicLuckyboxDictVos = acTaskPOMapper.getLuckyBoxDict();

        QueryWrapper<SOlympicLuckyboxDictPo> queryWrapper = new QueryWrapper<>();

        List<Integer> isUps = Lists.newArrayList(0, 1);
        if (boxListForm.getIsUp() != 2) {
            isUps.clear();
            isUps.add(boxListForm.getIsUp());
        }
        queryWrapper.in("is_up", isUps);
        queryWrapper.orderByAsc("order_num");
        //Page<SOlympicLuckyboxDictPo> page = PageHelper.startPage(boxListForm.getPg(), boxListForm.getSize(), true);
        //Page<SOlympicLuckyboxDictPo> page = new Page<SOlympicLuckyboxDictPo>(boxListForm.getPageNum(), boxListForm
        // .getPageSize());
        PageHelper.startPage(boxListForm.getPageNum(), boxListForm.getPageSize(), true);
        //IPage<SOlympicLuckyboxDictPo> selectPage = olympicLuckyboxDictMapper.selectPage(page, queryWrapper);
        List<SOlympicLuckyboxDictPo> selectList = olympicLuckyboxDictMapper.selectList(queryWrapper);
        if (!selectList.isEmpty()) {
            int orderNum = selectList.get(selectList.size() - 1).getOrderNum();
            if (orderNum == 0) {
                synchronized (this) {
                    List<SOlympicLuckyboxDictPo> selectList2 = olympicLuckyboxDictMapper.selectList(null);
                    int orderNum2 = selectList2.get(selectList.size() - 1).getOrderNum();
                    if (orderNum2 == 0)
                        //修改排序值
                        for (int i = 1; i <= selectList2.size(); i++) {
                            SOlympicLuckyboxDictPo sOlympicLuckyboxDictPo = selectList2.get(i - 1);
                            sOlympicLuckyboxDictPo.setOrderNum(i);
                            olympicLuckyboxDictMapper.updateById(sOlympicLuckyboxDictPo);
                        }

                }

            }
        }
        return selectList;
    }

    public boolean gameTime() {
        //取本地缓存
        TActivityConfig config = tActivityConfigMapper.selectById(10009);
        if (config == null) {
            return false;
        }
        Integer status = config.getStatus();
        if (status == 0) return false;

        Long startTime = config.getInStartTime();
        Long endTime = config.getInEndTime();

        if (startTime == null || endTime == null) {
            return false;
        }

        if (startTime == 0 && endTime == 0) return true;

        long now = System.currentTimeMillis();

        if (startTime == 0 && now <= endTime) return true;

        if (endTime == 0 && now >= startTime) return true;

        return now >= startTime && now <= endTime;
    }

    @Override
    public void changeOrder1(HttpServletRequest request,long id, int sort) {
        SOlympicLuckyboxDictPo selectById = olympicLuckyboxDictMapper.selectById(id);

        int orderNum = selectById.getOrderNum();

        if (orderNum > sort) {
            //上移
            List<SOlympicLuckyboxDictPo> sortList =
                    olympicLuckyboxDictMapper.selectList(new QueryWrapper<SOlympicLuckyboxDictPo>()
                            //大于等于
                            .ge("order_num", sort)
                            //小于
                            .lt("order_num", orderNum)
                            .orderByAsc("order_num"));
            //依次+1
            sortList.forEach(v -> {
                v.setOrderNum(v.getOrderNum() + 1);
                olympicLuckyboxDictMapper.updateById(v);
            });
        }
        if (orderNum < sort) {
            //下移
            List<SOlympicLuckyboxDictPo> sortList =
                    olympicLuckyboxDictMapper.selectList(new QueryWrapper<SOlympicLuckyboxDictPo>()
                            .gt("order_num", orderNum)
                            .le("order_num", sort)
                            .orderByAsc("order_num"));
            sortList.forEach(v -> {
                v.setOrderNum(v.getOrderNum() - 1);
                olympicLuckyboxDictMapper.updateById(v);
            });
        }
        selectById.setOrderNum(sort);
        olympicLuckyboxDictMapper.updateById(selectById);
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("order"));
        vo.getBeforeValues().add("排序"+orderNum);
        vo.getAfterValues().add("排序"+sort);
        merchantLogService.saveLog(MerchantLogPageEnum.OPERATION_BOX_XET, MerchantLogTypeEnum.PUSH_MANAGER_ODER, vo,
                MerchantLogConstants.MERCHANT_OUT, userId, username, null, username, id+"", language , ip);

    }

    /**
     * 更新幸运盲盒
     *
     * @param sOlympicLuckyboxDict
     * @return
     */
    @Override
    public Integer boxUpdate(SOlympicLuckyboxDictPo sOlympicLuckyboxDict, String userId, String userName,String ip) {

        SOlympicLuckyboxDictPo beforeValue = acTaskPOMapper.findBox(sOlympicLuckyboxDict.getId());

        if (sOlympicLuckyboxDict.getDelete() == 1) {

            //上架的不能删除
            int isUp = beforeValue.getIsUp();
            if (isUp == 1) throw new RuntimeException("该奖品已经上架，不能被删除");

            //活动开启不能删除
            if (gameTime()) throw new RuntimeException("活动已经上线，不能被删除");

            olympicLuckyboxDictMapper.deleteById(sOlympicLuckyboxDict.getId());

        } else {
            if (sOlympicLuckyboxDict.getId() == null) {

                sOlympicLuckyboxDict.setCreateTime(System.currentTimeMillis());

                int selectMinOrder = olympicLuckyboxDictMapper.selectMinOrder();

                sOlympicLuckyboxDict.setOrderNum(selectMinOrder - 1);

                olympicLuckyboxDictMapper.insert(sOlympicLuckyboxDict);
            } else {
                //acTaskPOMapper.boxUpdate(sOlympicLuckyboxDict);
                olympicLuckyboxDictMapper.updateById(sOlympicLuckyboxDict);

            }
        }

        SOlympicLuckyboxDictPo afterValue = acTaskPOMapper.findBox(sOlympicLuckyboxDict.getId());

        MerchantLogPO logPO = new MerchantLogPO();
        logPO.setUserId(userId).setUserName(userName)
                .setOperatType(MerchantLogTypeEnum.SET_BOX_NUMBER.getCode())
                .setTypeName("盲盒奖品编辑").setPageName("运营管理—盲盒奖品设置").setPageCode("")
                .setMerchantCode("").setMerchantName("编辑盲盒奖品").setDataId("1100002")
                .setOperatField(JsonUtils.listToJson(Collections.singletonList("编辑盲盒奖品")))
                .setBeforeValues(JsonUtils.listToJson(convertLuckBoxLog(beforeValue)))
                .setAfterValues(JsonUtils.listToJson(convertLuckBoxLog(afterValue)))
                .setLogTag(1).setOperatTime(System.currentTimeMillis())
                .setIp(ip);
        // 插入日志
        merchantLogService.saveLog(logPO);
        return 1;
    }


    private List<String> convertLuckBoxLog(SOlympicLuckyboxDictPo dictPo) {
        List<String> list = Lists.newArrayList();
        if (dictPo == null) {
            list.add("null");
        } else {
            list.add("盲盒类型：" + convertLuckBoxName(dictPo.getBoxType()));
            list.add("奖品名称：" + dictPo.getName());
            list.add("奖品数量：" + dictPo.getVisitNumber());
            list.add("奖品金额：" + dictPo.getAward() / 100 + "元");
            list.add("必中概率：" + dictPo.getMustHitDate() + "%");
            list.add("必中次数：" + dictPo.getMustHitNumber() + "次");
            list.add("必中日期：" + dictPo.getMustHitDate());
            list.add("奖品状态：" + (dictPo.getIsUp() == 0 ? "下架" : "上架"));
        }
        return list;
    }

    private String convertLuckBoxName(Integer boxType) {
        String luckBoxName = "";
        switch (boxType) {
            case 1:
                luckBoxName = "白银盲盒";
                break;
            case 2:
                luckBoxName = "黄金盲盒";
                break;
            case 3:
                luckBoxName = "钻石盲盒";
                break;
            default:
        }
        return luckBoxName;
    }

    /**
     * 发现幸运盲盒
     *
     * @param id
     * @return
     */
    @Override
    public SOlympicLuckyboxDictPo findBox(Long id) {
        return acTaskPOMapper.findBox(id);
    }


    /**
     * 发现每日的盲盒的设置
     *
     * @return
     */
    @Override
    public List<SDailyLuckyBoxNumberPO> getDailyBox() {
        return acTaskPOMapper.getDailyBox();
    }

    @Override
    public SDailyLuckyBoxNumberVo findDaily() {

        List<SDailyLuckyBoxNumberPO> dailyLuckyBoxNumberPOList = acTaskPOMapper.getDailyBox();
        // 赋值输出
        SDailyLuckyBoxNumberVo sDailyLuckyBoxNumberVo = new SDailyLuckyBoxNumberVo();
        for (SDailyLuckyBoxNumberPO sDailyLuckyBoxNumberPO : dailyLuckyBoxNumberPOList) {
            if (BoxTypeEnum.SILVER.getCode().equals(sDailyLuckyBoxNumberPO.getBoxType())) {
                sDailyLuckyBoxNumberVo.setSilverShowNumber(sDailyLuckyBoxNumberPO.getShowNumber()).setSilverDailyNumber(sDailyLuckyBoxNumberPO.getDailyNumber());
                sDailyLuckyBoxNumberVo.setSilverToken(sDailyLuckyBoxNumberPO.getToken());
            }
            if (BoxTypeEnum.GOLD.getCode().equals(sDailyLuckyBoxNumberPO.getBoxType())) {
                sDailyLuckyBoxNumberVo.setGoldShowNumber(sDailyLuckyBoxNumberPO.getShowNumber()).setGoldDailyNumber(sDailyLuckyBoxNumberPO.getDailyNumber());
                sDailyLuckyBoxNumberVo.setGoldToken(sDailyLuckyBoxNumberPO.getToken());
            }
            if (BoxTypeEnum.DIAMOND.getCode().equals(sDailyLuckyBoxNumberPO.getBoxType())) {
                sDailyLuckyBoxNumberVo.setDiamondShowNumber(sDailyLuckyBoxNumberPO.getShowNumber()).setDiamondDailyNumber(sDailyLuckyBoxNumberPO.getDailyNumber()).setShowRate(sDailyLuckyBoxNumberPO.getShowRate());
                sDailyLuckyBoxNumberVo.setDiamondToken(sDailyLuckyBoxNumberPO.getToken());
            }
        }
        return sDailyLuckyBoxNumberVo;

    }

    /**
     * 更新每日
     *
     * @param sOlympicLuckyboxDictPo
     * @return
     */
    @Override
    public Integer dailyUpdate(SDailyLuckyBoxNumberPO sOlympicLuckyboxDictPo) {

        return acTaskPOMapper.dailyUpdate(sOlympicLuckyboxDictPo);
    }

    /**
     * 插入
     *
     * @param beforeValue
     * @param afterValue
     * @param userId
     * @param userName
     * @return
     */
    @Override
    public Integer dailyLog(SDailyLuckyBoxNumberVo beforeValue, SDailyLuckyBoxNumberVo afterValue, String userId,
                            String userName,String ip) {

        MerchantLogPO logPO = new MerchantLogPO();
        logPO.setUserId(userId).setUserName(userName)
                .setOperatType(MerchantLogTypeEnum.SET_BOX_NUMBER.getCode())
                .setTypeName("盲盒个数设置").setPageName("运营管理—盲盒奖品设置").setPageCode("")
                .setMerchantCode("").setMerchantName("盲盒个数设置").setDataId("1100002")
                .setOperatField(JsonUtils.listToJson(Collections.singletonList("盲盒个数设置")))
                .setBeforeValues(JsonUtils.listToJson(convertLuckBoxNumberLog(beforeValue)))
                .setAfterValues(JsonUtils.listToJson(convertLuckBoxNumberLog(afterValue)))
                .setLogTag(1).setOperatTime(System.currentTimeMillis())
                .setIp(ip);

        // 插入日志
        merchantLogService.saveLog(logPO);
        return null;
    }

    private List<String> convertLuckBoxNumberLog(SDailyLuckyBoxNumberVo boxNumberVo) {
        List<String> list = Lists.newArrayList();
        if (boxNumberVo == null) {
            return list;
        }
        list.add("钻石盲盒每日个数：" + boxNumberVo.getDiamondDailyNumber());
        list.add("黄金盲盒每日个数：" + boxNumberVo.getGoldDailyNumber());
        list.add("白银盲盒每日个数：" + boxNumberVo.getSilverDailyNumber());
        list.add("钻石盲盒奖券消耗：" + boxNumberVo.getDiamondToken());
        list.add("黄金盲盒奖券消耗：" + boxNumberVo.getGoldToken());
        list.add("白银盲盒奖券消耗：" + boxNumberVo.getSilverToken());
        list.add("每隔 " + boxNumberVo.getShowRate() + " 分钟，开放 " + boxNumberVo.getDiamondShowNumber() + " 个钻石盲盒，"
                + boxNumberVo.getGoldShowNumber() + " 个黄金盲盒，" + boxNumberVo.getSilverShowNumber() + " 个白银盲盒");
        return list;
    }


    @Override
    public Integer getCurMaxNo(Integer actId) {
        return acTaskPOMapper.getCurMaxNo(actId);
    }

    @Override
    public Response<?> changeOrder(HttpServletRequest request,Integer id, Integer sort) {
        //获取当前排序
        AcTaskPO acTaskPO = acTaskPOMapper.selectById(id);
        if (ObjectUtil.isNull(acTaskPO)) {
            return Response.returnFail("data empty");
        }
        Integer curNo = acTaskPO.getOrderNo();
        if (curNo > sort) {
            //上移
            List<AcTaskPO> sortList = acTaskPOMapper.selectList(new QueryWrapper<AcTaskPO>()
                    .eq("act_id", acTaskPO.getActId())
                    .ge("order_no", sort)
                    .lt("order_no", curNo)
                    .orderByAsc("order_no"));
            //依次+1
            sortList.forEach(v -> v.setOrderNo(v.getOrderNo() + 1));
            this.updateBatchById(sortList);
        }
        if (curNo < sort) {
            //下移
            List<AcTaskPO> sortList = acTaskPOMapper.selectList(new QueryWrapper<AcTaskPO>()
                    .eq("act_id", acTaskPO.getActId())
                    .gt("order_no", curNo)
                    .le("order_no", sort)
                    .orderByAsc("order_no"));
            //依次+1
            sortList.forEach(v -> v.setOrderNo(v.getOrderNo() - 1));
            this.updateBatchById(sortList);
        }
        //更新当前数据
        acTaskPO.setOrderNo(sort);
        this.updateById(acTaskPO);

        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("order"));
        vo.getBeforeValues().add("排序"+curNo);
        vo.getAfterValues().add("排序"+sort);
        merchantLogService.saveLog(MerchantLogPageEnum.TASK_DAY_SET, MerchantLogTypeEnum.PUSH_MANAGER_ODER, vo,
                MerchantLogConstants.MERCHANT_OUT, userId, username, null, username, id.toString(), language , ip);
        return Response.returnSuccess();
    }


}
