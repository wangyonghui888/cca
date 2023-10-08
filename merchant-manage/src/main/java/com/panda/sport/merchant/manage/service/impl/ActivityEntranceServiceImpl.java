package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.bss.mapper.ActivityEntranceMapper;
import com.panda.sport.bss.mapper.ActivityMerchantMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.activity.ActivityMaintainVO;
import com.panda.sport.merchant.manage.entity.form.MerchantTreeForm;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.service.ActivityEntranceService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.RedisTemp;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author : dorf
 * @Date: 2021-08-20
 * @Description :dorf
 */
@Slf4j
@RefreshScope
@Service
public class ActivityEntranceServiceImpl implements ActivityEntranceService {

    @Autowired
    private ActivityEntranceMapper activityEntranceMapper;

    @Autowired
    private ActivityEntranceService activityEntranceService;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private ActivityMerchantMapper activityMerchantMapper;
    @Resource
    private MerchantApiClient merchantApiClient;
    @Autowired
    private MerchantLogService merchantLogService;
    @Resource
    private AsyncScheduleImpl asyncSchedule;

    @Value("${nacos.api.url:null}")
    private String nacosUrl;

    @Value("${nacos.api.namespace:null}")
    private String nacosNameSpace;

    private List<ActivityEntrancePo> convertPObyList(List<ActivityEntrancePo> list) {
        List<ActivityEntrancePo> returnList = new ArrayList<>();
        list.forEach(val -> {
            ActivityEntrancePo tempVo = new ActivityEntrancePo();
            BeanUtils.copyProperties(val, tempVo);
            tempVo.setStartTime(val.getInStartTime());
            tempVo.setEndTime(val.getInEndTime());
            if (null != val.getInStartTime()) {
                if ("0".equals(val.getInStartTime())) {
                    tempVo.setStartTime("0");
                } else {
                    tempVo.setStartTime(DateUtils.transferLongToDateStrings(Long.valueOf(val.getInStartTime())));
                }
            }
            if (null != val.getInEndTime()) {
                if ("0".equals(val.getInEndTime())) {
                    //处理成特殊的常驻活动
                    tempVo.setEndTime("0");
                } else {
                    tempVo.setEndTime(DateUtils.transferLongToDateStrings(Long.valueOf(val.getInEndTime())));
                }
            }
//            if(val.getType()==2 && (StringUtil.isNotBlank(tempVo.getStartTime())&&!"0".equals(tempVo.getStartTime()))
//                    && (StringUtil.isNotBlank(tempVo.getEndTime())&&!"0".equals(tempVo.getEndTime()))){
//                tempVo.setEndTime(DateUtils.getSpecifiedDayAfter(tempVo.getStartTime())+" 00:00:00");
//            }

            tempVo.setEntranceStatus(val.getEntranceStatus() == null ? 0 : val.getEntranceStatus());
            tempVo.setStatus(val.getStatus() == null ? 0 : val.getStatus());
            returnList.add(tempVo);
        });
        return returnList;
    }

    @Override
    public Response queryList(ActivityEntranceVO activityEntranceVO) {
        Integer start = (activityEntranceVO.getPageNum() - 1) * activityEntranceVO.getPageSize();
        activityEntranceVO.setStart(start);
        if (StringUtil.isNotBlank(activityEntranceVO.getMerchantCode())) {
            activityEntranceVO.setMerchantCode(activityEntranceVO.getMerchantCode().trim());
        }
        if (StringUtil.isNotBlank(activityEntranceVO.getActivityName()) && ActivityTypeEnum.ALL.getDescribe().equals(activityEntranceVO.getActivityName())) {
            activityEntranceVO.setActivityName(null);
        } else if (StringUtil.isNotBlank(activityEntranceVO.getActivityName())) {
            activityEntranceVO.setActivityName(activityEntranceVO.getActivityName().trim());
        }
        log.info("活动入口设置列表==>{}", JSON.toJSONString(activityEntranceVO));
        int count = activityEntranceMapper.selectByMerchantCount(activityEntranceVO);
        PageVO poPageVO = new PageVO<ActivityEntrancePo>(count, activityEntranceVO.getPageSize(), activityEntranceVO.getPageNum());
        List<ActivityEntrancePo> activityEntrancePolist = activityEntranceMapper.selectByMerchantName(activityEntranceVO);
        log.info("活动入口设置==>{}==>{}", JSON.toJSONString(activityEntrancePolist), activityEntrancePolist.size());
        poPageVO.setRecords(this.convertPObyList(activityEntrancePolist));
        return Response.returnSuccess(poPageVO);
    }

    @Override
    public void update(HttpServletRequest request, Long id, Integer status) {
        activityEntranceMapper.update(id, status);
        String userId = SsoUtil.getUserId(request).toString();
        String userName = request.getHeader("merchantName");
        MerchantLogFiledVO merchantLogFiledVO = new MerchantLogFiledVO();
        String[] str = {status == 0 ? "开" : "关"};
        merchantLogFiledVO.setBeforeValues(Arrays.asList(str));
        String[] str1 = {status == 0 ? "关" : "开"};
        merchantLogFiledVO.setAfterValues(Arrays.asList(str1));
        List<String> list = new ArrayList<>();
        list.add("活动状态");
        merchantLogFiledVO.setFieldName(list);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        ActivityMerchantVO activityMerchantVO = activityEntranceMapper.getActivityMerchantById(id);
        if (activityMerchantVO != null) {
            log.info("清楚缓存:" + activityMerchantVO.getMerchantCode());
            merchantApiClient.clearMerchantActivityCache(activityMerchantVO.getMerchantCode());
        }

        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_ACTIVITY, MerchantLogTypeEnum.EDIT_INFO, merchantLogFiledVO, MerchantLogConstants.MERCHANT_IN, userId,
                userName, activityMerchantVO.getMerchantCode(),userName, userId, language, IPUtils.getIpAddr(request));
    }

    @Override
    public Boolean queryActivityConfigPoById(Long id) {
        List<ActivityMerchantPo> activityMerchantPo = activityMerchantMapper.queryActivityMerchantById(id);
        boolean flag = false;
        for (ActivityMerchantPo obj : activityMerchantPo) {
            if (obj.getEntranceStatus() == 1) {
                flag = true;
                break;
            }
        }
        ;
        return flag;
//        if (null == activityConfigPo.getEndTime() || null == activityConfigPo.getStartTime()) {
//            return false;
//        }
//        if (0L == activityConfigPo.getEndTime() || 0L == activityConfigPo.getStartTime()) {
//            return true;
//        }
//        Long currentTime = System.currentTimeMillis();
//        if (activityConfigPo.getStartTime() <= currentTime && currentTime <= activityConfigPo.getEndTime()) {
//            return true;
//        }
//        return false;
    }

    @Override
    public Response save(HttpServletRequest request, ActivityConfigVO activityConfigVO) {
        //List<ActivityMerchantPo> before = activityMerchantMapper.queryMerchantByMerchant();
        Response res = activityEntranceService.queryActivityEntranceList();
        ActivityEntranceShowVO oldInfo = (ActivityEntranceShowVO)res.getData();
        Map<String, ActivityMerchantPo> map = this.getAllActityMerchant(activityConfigVO);

        List<MerchantPO> list = new ArrayList<>();
        //为空代表全部商户，否则是对应的商户
        if (StringUtil.isNotBlank(activityConfigVO.getActivityMerchants()) && "all".equals(activityConfigVO.getActivityMerchants())) {
            list = merchantMapper.getMerchantAllList();
        }
        Response response = this.saveActivityConfig(activityConfigVO, list, map);
        String userId = SsoUtil.getUserId(request).toString();
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }

        /**
         *  添加系统日志
         * */
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        MerchantUtil util = new MerchantUtil();
        MerchantLogFiledVO vo1 = util.compareObject(oldInfo,activityConfigVO,MerchantUtil.filterActiveFieldAddNames,MerchantUtil.FIELD_ACTIVE_MAPPING);
        merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.SET_ACTIVE.getCode(),  MerchantLogTypeEnum.SAVE_INFO.getRemark()
                , MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_52.getRemark(),
                MerchantLogTypeEnum.SET_ACTIVE.getPageCode().get(0), userId, null, "活动入口",
                vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(),username,userId,ip);
        return response;
    }

    private Map<String, ActivityMerchantPo> getAllActityMerchant(ActivityConfigVO activityConfigVO) {
        List<ActivityMerchantPo> list = null;
        if (StringUtil.isNotBlank(activityConfigVO.getActivityMerchants()) && "all".equals(activityConfigVO.getActivityMerchants())) {
            list = activityMerchantMapper.queryMerchant();
        } else {
            list = activityMerchantMapper.queryMerchantByMerchantCode(Arrays.asList(activityConfigVO.getActivityMerchants().split(",")));
        }
        //***************** 覆盖模式的代码
//        String[] strs = activityConfigVO.getActivityMerchants().split(",");
//        List<String> exchangeChecklist = new ArrayList<>();
//        for (int i=0;i<strs.length;i++){
//            exchangeChecklist.add(strs[i]);
//        }
//        //数据库存在的集合
//        List<String> existsMechant = activityMerchantMapper.queryActivityMerchantCode();
//        //剩下需要删除的商户
//        existsMechant.removeAll(exchangeChecklist);
//        //删除多余的商户
//        activityMerchantMapper.deleteActityMerchantByCode(existsMechant);
        //**************
        Map<String, ActivityMerchantPo> map = new ConcurrentHashMap<>();
        list.forEach(e -> {
            map.put(e.getMerchantCode() + "_" + e.getActivityId(), e);
        });
//        if(null==list || list.size()==0){
//            return true;
//        }
        //activityMerchantMapper.deleteAllActityMerchant(list);
        //activityEntranceMapper.deleteAllActityConfig(list);
        String[] activityTypes = activityConfigVO.getActivityType().split(",");
        List<ActivityConfigPo> updatList = new ArrayList<>();
        for (String e : activityTypes) {
            ActivityConfigPo activityConfigPo = new ActivityConfigPo();
            Long id = this.getActivityId(Integer.valueOf(e));
            activityConfigPo.setId(id);
//            if(activityConfigPoMap.get(id) != null){
//                activityConfigPo.setStartTime(activityConfigPoMap.get(id).getStartTime());
//                activityConfigPo.setEndTime(activityConfigPoMap.get(id).getEndTime());
//            }
            activityConfigPo.setName(ActivityTypeEnum.getDescByCode(Integer.valueOf(e)));
            //activityConfigPo.setName("测试"+(Integer.valueOf(e)+3));
            activityConfigPo.setPcUrl(activityConfigVO.getPcUrl());
            activityConfigPo.setH5Url(activityConfigVO.getH5Url());
            activityConfigPo.setType(1);
            activityConfigPo.setStatus(1);
            log.info("写死保存活动==》{}==>{}", e, JSON.toJSONString(activityConfigPo));
            updatList.add(activityConfigPo);
        }
        if (updatList.size() > 0) {
            activityEntranceMapper.insertPojo(updatList);
        }
        return map;
    }

    @Override
    public ActivityMerchantVO detail(Long id, String merchantCode) {
        ActivityMerchantPo activityMerchantPo = activityMerchantMapper.getActivityMerchantById(id);
        List<ActivityConfigPo> activityConfigPoList = activityEntranceMapper.queryActivityConfigPoByCode(activityMerchantPo.getMerchantCode());
        ActivityMerchantVO activityMerchantVO = new ActivityMerchantVO();
        activityMerchantVO.setId(activityMerchantPo.getId());
        activityMerchantVO.setActivityId(activityMerchantPo.getActivityId());
        activityMerchantVO.setStatus(activityMerchantPo.getStatus() == null ? 0 : activityMerchantPo.getStatus());
        activityMerchantVO.setEntranceStatus(activityMerchantPo.getEntranceStatus() == null ? 0 : activityMerchantPo.getEntranceStatus());
        activityMerchantVO.setMerchantCode(activityMerchantPo.getMerchantCode());
        StringBuffer sbf = new StringBuffer();
        activityConfigPoList.forEach(e -> {

            //String code = ActivityTypeEnum.getCodeByDesc(e.getName()).toString();
            Integer code = this.getActivityType(e.getId());
            if (code == 0) {
                return;
            }
            if (StringUtil.isBlankOrNull(sbf.toString())) {
                sbf.append(code);
            } else {
                sbf.append(",").append(code);
            }
        });
        activityMerchantVO.setActivityType(sbf.toString());
        return activityMerchantVO;
    }

    @Override
    public Boolean activityCheckById(Long activityId) {
        ActivityConfigPo activityConfigPo = activityEntranceMapper.queryActivityConfigPoById(activityId);
        if (null == activityConfigPo.getStartTime() || null == activityConfigPo.getEndTime()) {
            return false;
        }
        //为0的情况下 代表常驻活动，常驻活动正在进行中，无法修改直接返回
        if (0 == activityConfigPo.getStartTime() && 0 == activityConfigPo.getEndTime()) {
            return true;
        }
        Long currentTime = System.currentTimeMillis();
        if (StringUtil.isNotBlank(activityConfigPo.getName()) && activityConfigPo.getStartTime() <= currentTime && currentTime <= activityConfigPo.getEndTime()) {
            return true;
        }
        return false;
    }

    @Override
    public void activityMechantUpdate(ActivityMerchantVO activityMerchantVO, HttpServletRequest request) {
        //List<ActivityMerchantPo> activityMerchantPos = activityMerchantMapper.getActivityMerchantByCode(activityMerchantVO.getMerchantCode());
        //this.deleteActity(activityMerchantPos,activityMerchantVO.getMerchantCode());
        List<String> activity = Arrays.asList(activityMerchantVO.getActivityType().split(","));
        List<ActivityMerchantPo> activityMerchantPo = activityMerchantMapper.getActivityMerchantByCode(activityMerchantVO.getMerchantCode());
        boolean flag = false;
        //修改的时候判断是都是全部活动，是的话就更新所有，否则就增加商会活动
        if (activity.size() == 4) {
            //如果存在一个活动变成多个活动的情况
            List<ActivityConfigPo> activityConfigPoList = activityEntranceMapper.queryActivityConfigPoList(activityMerchantVO.getId());
            List<Integer> activityTypes = this.exchangeValue(activityConfigPoList, null);
            if (CollUtil.isEmpty(activityTypes)) {
                log.error("集合获取的值为空==》{}", JSONUtil.toJsonStr(activityTypes));
            } else if (activity.size() == activityMerchantPo.size()) {
                flag = true;
                List<Long> str = activityMerchantPo.stream().map(ActivityMerchantPo::getId).collect(Collectors.toList());
                log.info("集合获取的为==>{}", JSONUtil.toJsonStr(str));
                activityEntranceMapper.updateActivityMerchant(str, activityMerchantVO.getEntranceStatus());
            } else {
                //商户只有一个活动的情况下，增加成多个活动
                this.updateActivityMechant(activityMerchantVO, activityTypes, activityMerchantVO.getEntranceStatus());
                List<Long> str = activityMerchantPo.stream().map(ActivityMerchantPo::getId).collect(Collectors.toList());
                activityEntranceMapper.updateActivityMerchant(str, activityMerchantVO.getEntranceStatus());
            }
        } else if (activity.size() > 1) {
            //如果存在一个活动变成多个活动的情况
            List<ActivityConfigPo> activityConfigPoList = activityEntranceMapper.queryActivityConfigPoList(activityMerchantVO.getId());
            List<ActivityConfigPo> activityConfigPoLists = activityEntranceMapper.queryActivityConfigPoByCode(activityMerchantVO.getMerchantCode());
            List<Integer> activityTypes = this.exchangeValue(activityConfigPoList, activity);
            if (CollUtil.isEmpty(activityTypes)) {
                log.error("集合获取的值为空==》{}", JSONUtil.toJsonStr(activityTypes));
            } else {
                //商户只有一个活动的情况下，增加成多个活动
                this.updateActivityMechant(activityMerchantVO, activityTypes, activityMerchantVO.getEntranceStatus());
            }
            log.info("集合获取的值为空==》{}", JSON.toJSONString(activityConfigPoList));
            if (CollUtil.isEmpty(activityConfigPoList)) {
                log.info("集合获取的3232值为空==》{}", JSON.toJSONString(activityConfigPoList));
            }
            List<ActivityConfigPo> activityConfigs = this.getActivityConfigList(activityConfigPoLists, activityMerchantVO.getActivityType());
            if (null != activityConfigs) {
                this.deleteActityMerchant(activityConfigs, activityMerchantVO);
            }
            for (String val : activity) {
                log.info("获取的为==>{}", JSONUtil.toJsonStr(val));
                activityEntranceMapper.updateActivityMerchantStatusBycode(activityMerchantVO.getMerchantCode(), this.getActivityId(Integer.valueOf(val)), activityMerchantVO.getEntranceStatus());
            }
        } else {
            List<ActivityConfigPo> activityConfigPoLists = activityEntranceMapper.queryActivityConfigPoByCode(activityMerchantVO.getMerchantCode());
            activityMerchantMapper.update(activityMerchantVO.getId(), activityMerchantVO.getEntranceStatus());
            //activityEntranceMapper.activityMechantUpdate(activityMerchantVO.getActivityId(),ActivityTypeEnum.getDescByCode(Integer.valueOf(activityMerchantVO.getActivityType())));
            List<ActivityConfigPo> activityConfigs = this.getActivityConfigList(activityConfigPoLists, activityMerchantVO.getActivityType());
            if (null != activityConfigs) {
                this.deleteActityMerchant(activityConfigs, activityMerchantVO);
            }
            log.info("获取的为==>{}", JSONUtil.toJsonStr(activity.get(0)));
            activityEntranceMapper.updateActivityMerchantBycode(activityMerchantVO.getMerchantCode(), this.getActivityId(Integer.valueOf(activity.get(0))), activityMerchantVO.getEntranceStatus());
        }
        if (!flag) {
            List<ActivityMerchantPo> updateActivityMerchantPo = activityMerchantMapper.getActivityMerchantByCode(activityMerchantVO.getMerchantCode());
            //查询是否全部更新为关闭
            List<Long> str = updateActivityMerchantPo.stream().map(ActivityMerchantPo::getId).collect(Collectors.toList());
            activityEntranceMapper.updateActivityMerchantStatus(str, activityMerchantVO.getEntranceStatus());
        }
        try {
            //保存日志
            String userId = SsoUtil.getUserId(request).toString();
            String userName = request.getHeader("merchantName");
            MerchantLogFiledVO merchantLogFiledVO = new MerchantLogFiledVO();
            List<String> str = CollUtil.isEmpty(activityMerchantPo) ? null : activityMerchantPo.stream().distinct().map(e -> e.getActivityId().toString()).collect(Collectors.toList());
            List<String> beforeValue = new ArrayList<>();
            beforeValue.add(0 == activityMerchantVO.getEntranceStatus()? "开" : "关");
            str.forEach(e -> {
                beforeValue.add(ActivityTypeEnum.getDescByCode(this.getActivityType(Long.valueOf(e))));
            });
            merchantLogFiledVO.setBeforeValues(beforeValue);
            List<String> str1 = new ArrayList<>();
            str1.add(0 == activityMerchantVO.getEntranceStatus()? "关" : "开");
            String[] activityTypes = activityMerchantVO.getActivityType().split(",");
            for (String activityType : activityTypes) {
                str1.add(ActivityTypeEnum.getDescByCode(Integer.valueOf(activityType)));
            }
            merchantLogFiledVO.setAfterValues(str1);
            List<String> list = new ArrayList<>();
            list.add("活动入口开关");
            list.add("活动类型配置");
            merchantLogFiledVO.setFieldName(list);
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }

            log.info("清楚缓存:" + activityMerchantVO.getMerchantCode());
            merchantApiClient.clearMerchantActivityCache(activityMerchantVO.getMerchantCode());

            merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_ACTIVITY_CONFIG, MerchantLogTypeEnum.ACTIVITY_CONFIG, merchantLogFiledVO, MerchantLogConstants.MERCHANT_IN, userId,
                    userName, null, userName,userId, language,IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.info("日志异常==》{}", e);
        }
    }

    private void deleteActityMerchant(List<ActivityConfigPo> activityConfigs, ActivityMerchantVO activityMerchantVO) {
        List<Long> list = activityConfigs.stream().map(ActivityConfigPo::getId).collect(Collectors.toList());
        //activityEntranceMapper.deleteActityConfig(list);
        activityMerchantMapper.deleteActityMerchant(list, activityMerchantVO.getMerchantCode());
    }

    /**
     * 应水货产品的需求，需要更新活动类型
     *
     * @param activityConfigPoList
     * @param activityType
     * @return
     */
    private List<ActivityConfigPo> getActivityConfigList(List<ActivityConfigPo> activityConfigPoList, String activityType) {
        List<Integer> list = new ArrayList<>();
        List<String> list1 = Arrays.asList(activityType.split(","));
        activityConfigPoList.forEach(e -> {
            Integer type = this.getActivityType(e.getId());
            //Integer type = ActivityTypeEnum.getCodeByDesc(e.getName());
            log.info("#对应的值为={}==》{}", JSON.toJSONString(type), JSON.toJSONString(e.getId()));
            if (type != 0) {
                list.add(type);
            }
        });
        List<Integer> list2 = ActivityTypeEnum.removeOtherList(list1, list);
        log.info("#对象获取值=={}==》{}==》{}", JSON.toJSONString(list2), JSON.toJSONString(list1), JSON.toJSONString(list));
        if (list2.size() == 0) {
            return null;
        }
        List<Long> ids = new ArrayList<>();
        list2.forEach(e -> {
            Long type = this.getActivityId(e);
            if (null != type) {
                ids.add(type);
            }
        });
        List<ActivityConfigPo> poJo = new ArrayList<>();
        activityConfigPoList.forEach(e -> {
            ids.forEach(obj -> {
                if (obj.equals(e.getId())) {
                    ActivityConfigPo activityConfigPo = new ActivityConfigPo();
                    BeanUtils.copyProperties(e, activityConfigPo);
                    poJo.add(activityConfigPo);
                }
            });
        });
//        List<String> names = new ArrayList<>();
//        list2.forEach(e -> {
//            String type = ActivityTypeEnum.getDescByCode(e);
//            if (null != type) {
//                names.add(type);
//            }
//        });
//        List<ActivityConfigPo> poJo = new ArrayList<>();
//        activityConfigPoList.forEach(e -> {
//            names.forEach(name -> {
//                if (name.equals(e.getName())) {
//                    ActivityConfigPo activityConfigPo = new ActivityConfigPo();
//                    BeanUtils.copyProperties(e, activityConfigPo);
//                    poJo.add(activityConfigPo);
//                }
//            });
//        });
        log.info("对象获取的值为空==》{}==》{}", JSON.toJSONString(poJo), JSON.toJSONString(ids));
        return poJo;
    }

    private void deleteActity(List<ActivityMerchantPo> activityMerchantPos, String merchantCode) {
        List<Long> list = activityMerchantPos.stream().map(ActivityMerchantPo::getActivityId).collect(Collectors.toList());
        activityMerchantMapper.deleteActivityMerchant(merchantCode);
        activityEntranceMapper.deleteActityConfig(list);
    }

    private void updateActivityMechant(ActivityMerchantVO activityMerchantVO, List<Integer> activityTypes, Integer status) {
        //Long id = activityEntranceMapper.selectKeyId();
        ActivityMerchantPo activityMerchantPo = activityMerchantMapper.getActivityMerchantById(activityMerchantVO.getId());
        activityTypes.forEach(e -> {
            savePoJo(e, ActivityTypeEnum.getDescByCode(e), activityMerchantPo.getMerchantCode(), null, 0, status);
        });
    }

    @Override
    public Response<?> activityTimeUpdate(ActivityTimeVO activityTimeVO, HttpServletRequest request) {
        String isExecuting = RedisTemp.get(Constant.IS_RESET_GROWTH_TASK_DATA);
        if (StringUtils.isNotBlank(isExecuting)) {
            return Response.returnFail("任务重置中，请稍后再试");
        }
        ActivityConfigPo oldGrowConfig = activityEntranceMapper.getActivityConfigPO(10008L);
        String activityType = "2,3,4,5";
        List<String> list = Arrays.asList(activityType.split(","));
        //保存日志
        String userId = SsoUtil.getUserId(request).toString();
        String userName = request.getHeader("merchantName");

        List<ActivityConfigPo> activityConfigPos = activityEntranceMapper.queryActivityConfigGroupByName();
        list.forEach(e -> {
            MerchantLogFiledVO merchantLogFiledVO = new MerchantLogFiledVO();
            Map<String, Long> map = this.getActivityTime(activityTimeVO, Integer.valueOf(e));
            activityMerchantMapper.activityTimeUpdate(map.get("startTime"), map.get("endTime"), map.get("activityType"), this.getActivityId(Integer.valueOf(e)));
            //保存系统日志
            List<String> names = new ArrayList<>();
            names.add("活动时间/常驻活动");
            names.add("活动时间-时间区间");
            names.add("常驻活动-设置起始时间");
            names.add("常驻活动-活动结束");
            List<String> beforeValue = new ArrayList<>();
            List<String> afterValue = new ArrayList<>();
            for(int i =0;i<activityConfigPos.size();i++) {
                if (getIdStrTask(e) .equals(activityConfigPos.get(i).getId())) {
                    Long inStartTime = null;
                    Long inEndTime = null;
                    beforeValue.add(getIdStr(activityConfigPos.get(i).getId()));
                    if (activityConfigPos.get(i).getId().equals(getActivityId(2))) {
                        inStartTime = activityTimeVO.getBlindBoxStartTime();
                        inEndTime = activityTimeVO.getBlindBoxEndTime();
                    } else if (activityConfigPos.get(i).getId().equals(getActivityId(3))) {
                        inStartTime = activityTimeVO.getDailyTaskStartTime();
                        inEndTime = activityTimeVO.getDailyTaskEndTime();
                    } else if (activityConfigPos.get(i).getId().equals (getActivityId(4))) {
                        inStartTime = activityTimeVO.getGrowthTaskStartTime();
                        inEndTime = activityTimeVO.getGrowthTaskEndTime();
                    } else if (activityConfigPos.get(i).getId().equals(getActivityId(5))) {
                        inStartTime = activityTimeVO.getTigerStartTime();
                        inEndTime = activityTimeVO.getTigerEndTime();
                    }
                    //活动类型(1常规活动 2自定义活动 3特殊活动)
                    if (1 == activityConfigPos.get(i).getType()) {//活动时间-时间区间
                        beforeValue.add(DateUtils.transferLongToDateStrings(inStartTime) + "-" + DateUtils.transferLongToDateStrings(inEndTime));
                    } else {
                        beforeValue.add(DateUtils.transferLongToDateStrings(inStartTime));
                    }
                    // 常驻活动-活动结束
                    beforeValue.add(null==activityConfigPos.get(i).getStatus() ? "结束" : activityConfigPos.get(i).getStatus().toString().equals("1") ? "未结束" :"结束");
                   }
               }
                 if(!CollectionUtils.isEmpty(map)){
                     afterValue.add(map.get("activityType").toString().equals("1")?"活动时间":"常驻时间");
                     //活动类型(1常规活动 2自定义活动 3特殊活动)
                     if("1".equals(map.get("activityType").toString())){//活动时间-时间区间
                         afterValue.add(DateUtils.transferLongToDateStrings(map.get("startTime"))+"-"+ DateUtils.transferLongToDateStrings(map.get("endTime")));
                     }else{
                         afterValue.add(null==map.get("startTime")?"-" : DateUtils.transferLongToDateStrings(map.get("startTime")));
                     }
                     afterValue.add(null==map.get("activityStatus") ? "结束" : map.get("activityStatus").toString().equals("1") ? "未结束" :"结束");

                 }
            merchantLogFiledVO.setBeforeValues(beforeValue);
            merchantLogFiledVO.setAfterValues(afterValue);
            merchantLogFiledVO.setFieldName(names);
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }
            merchantApiClient.clearMerchantActivityCache(null);
            merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_ACTIVITY_SET, MerchantLogTypeEnum.ACTIVITY_SET_TIME, merchantLogFiledVO, MerchantLogConstants.MERCHANT_IN, userId,
                    userName, null, null, null, language,IPUtils.getIpAddr(request));
        });

        ActivityConfigPo newGrowConfig = activityEntranceMapper.getActivityConfigPO(10008L);
        //对比成长任务信息
        try {
            Long oldSt = oldGrowConfig.getInStartTime();
            Long oldEt = oldGrowConfig.getInEndTime();
            Integer oldT = oldGrowConfig.getType();

            Long newSt = newGrowConfig.getInStartTime();
            Long newEt = newGrowConfig.getInEndTime();
            Integer newT = newGrowConfig.getType();
            log.info("oldSt:{},oldEt:{},oldT:{}", oldSt, oldEt, oldT);
            log.info("newSt:{},newEt:{},newT:{}", newSt, newEt, newT);
            //常驻活动关闭 不更新
            if (Constant.INT_2.equals(newT) && newEt != 0) {
                return Response.returnSuccess();
            }
            //延长活动时间不更新
            if (Constant.INT_1.equals(newT) && oldSt.equals(newSt)) {
                return Response.returnSuccess();
            }
            if (!oldSt.equals(newSt) || !oldEt.equals(newEt) || !oldT.equals(newT)) {
                log.info("execute clear growth task data");
                RedisTemp.set(Constant.IS_RESET_GROWTH_TASK_DATA, "execute");
                asyncSchedule.clearGrowthTaskData();
            }
        } catch (Exception e) {
            log.error("data comparison error", e);
        }
        return Response.returnSuccess();
    }

    private String getIdStr(Long id) {
        String name = null;
        if (id == 10007L) {
            name="每日任务";
        } else if (id == 10008L) {
            name="成长任务";
        } else if (id == 10009L) {
            name="幸运盲盒";
        } else if (id == 10010L) {
            name="老虎机活动";
        }

        return name;
    }


    private Long getIdStrTask(String id) {
        Long idl = null;
        if (id.equals("2") ) {
            idl=10009L;
        } else if (id .equals("4")) {
            idl=10007L;
        } else if (id .equals("3")) {
            idl=10008L;
        } else if (id .equals("5")) {
            idl=10010L;
        }

        return idl;
    }
    private String getTimeStr(Long id, Long inStartTime, Long inEndTime) {
        StringBuffer sbf = new StringBuffer();
        if (id == 10007L) {
            sbf.append("每日任务");
        } else if (id == 10008L) {
            sbf.append("成长任务");
        } else if (id == 10009L) {
            sbf.append("幸运盲盒");
        } else if (id == 10010L) {
            sbf.append("老虎机活动");
        }
        if (StringUtil.isNotBlank(sbf.toString())) {
            sbf.append(":{时间:").
                    append(this.getStr(inStartTime)).
                    append("-").
                    append(this.getStr(inEndTime)).
                    append("}");
        }
        return sbf.toString();
    }

    private String getStr(Long time) {
        return time == null ? "未设置" : time == 0 ? "活动长期有效" : DateUtils.transferLongToDateStrings(time);
    }

    @Override
    public ActivityTimeVO queryActivityTime() {
        List<ActivityConfigPo> activityConfigPoList = activityEntranceMapper.queryActivityConfigGroupByName();
        log.info("queryActivityConfigGroupByName:" + activityConfigPoList);
        ActivityTimeVO activityTimeVO = new ActivityTimeVO();
        activityConfigPoList.forEach(e -> {
            Long startTime = e.getInStartTime() == null ? 0 : e.getInStartTime();
            Long endTime = e.getInEndTime() == null ? 0 : e.getInEndTime();
            Integer status = 0;
            if (e.getType() == 2 && startTime > 0 && endTime > 0) {
                status = 1;
            }
            if (this.getActivityType(e.getId()).equals(ActivityTypeEnum.Blind_box_activity.getCode())) {
                if (e.getType() == 1) {
                    activityTimeVO.setBlindBoxStartTime(e.getInStartTime());
                    activityTimeVO.setBlindBoxEndTime(e.getInEndTime());
                } else if (e.getType() == 2) {
                    activityTimeVO.setBlindResidentStartTime(e.getInStartTime());
                    activityTimeVO.setBlindActivityEnd(status);
                    activityTimeVO.setBlindBoxStartTime(0L);
                    activityTimeVO.setBlindBoxEndTime(0L);
                }
            } else if (this.getActivityType(e.getId()).equals(ActivityTypeEnum.Growth_task.getCode())) {
                if (e.getType() == 1) {
                    activityTimeVO.setGrowthTaskStartTime(e.getInStartTime());
                    activityTimeVO.setGrowthTaskEndTime(e.getInEndTime());
                } else if (e.getType() == 2) {
                    activityTimeVO.setGrowthResidentStartTime(e.getInStartTime());
                    activityTimeVO.setGrowthActivityEnd(status);
                    activityTimeVO.setGrowthTaskStartTime(0L);
                    activityTimeVO.setGrowthTaskEndTime(0L);
                }
            } else if (this.getActivityType(e.getId()).equals(ActivityTypeEnum.Daily_task.getCode())) {
                if (e.getType() == 1) {
                    activityTimeVO.setDailyTaskStartTime(e.getInStartTime());
                    activityTimeVO.setDailyTaskEndTime(e.getInEndTime());
                } else if (e.getType() == 2) {
                    activityTimeVO.setDailyResidentStartTime(e.getInStartTime());
                    activityTimeVO.setDailyActivityEnd(status);
                    activityTimeVO.setDailyTaskStartTime(0L);
                    activityTimeVO.setDailyTaskEndTime(0L);
                }
            } else if (this.getActivityType(e.getId()).equals(ActivityTypeEnum.TIGER_DRAW_task.getCode())) {
                if (e.getType() == 1) {
                    activityTimeVO.setTigerStartTime(e.getInStartTime());
                    activityTimeVO.setTigerEndTime(e.getInEndTime());
                } else if (e.getType() == 2) {
                    activityTimeVO.setTigerResidentStartTime(e.getInStartTime());
                    activityTimeVO.setTigerActivityEnd(status);
                    activityTimeVO.setTigerStartTime(0L);
                    activityTimeVO.setTigerEndTime(0L);
                }
            }
        });
        return activityTimeVO;
    }

    @Override
    public Response getMerchantListTree(MerchantTreeForm merchantTreeForm) {
        try {
            return queryGetMerchantTree(merchantTreeForm);
        } catch (Exception e) {
            log.error("获取商户树失败!", e);
            return Response.returnFail("获取商户树失败");
        }
    }

    @Override
    public Response getActivityMerchantCode() {
        List<String> list = activityMerchantMapper.queryActivityMerchantCode();
        List<String> merchantList = activityMerchantMapper.queryMerchantCode();
        String str = String.join(",", list);
        if (null != list && null != merchantList && list.size() == merchantList.size()) {
            str = "all";
        }
        return Response.returnSuccess(str);
    }

    @Override
    public Response queryActivityEntranceList() {
        List<String> list = activityMerchantMapper.queryActivityMerchantCode();
        List<String> merchantList = activityMerchantMapper.queryMerchantCode();
        String str = null == list || list.size() == 0 ? null : String.join(",", list);
        ActivityEntranceShowVO activityEntranceShowVO = new ActivityEntranceShowVO();
        if (null != list && null != merchantList && list.size() == merchantList.size()) {
            activityEntranceShowVO.setActivityMerchants("all");
        } else {
            activityEntranceShowVO.setActivityMerchants(str);
        }
        if (null != list && merchantList != null) {
            log.info("活动表==>{}商户表==>{}", list.size(), merchantList.size());
        }
        List<ActivityConfigPo> activityConfigPos = activityEntranceMapper.queryActivityConfigPo();
        if (activityConfigPos != null && activityConfigPos.size() > 0) {
            activityEntranceShowVO.setH5Url(activityConfigPos.get(0).getH5Url());
            activityEntranceShowVO.setPcUrl(activityConfigPos.get(0).getPcUrl());
//            StringBuffer sbf = new StringBuffer();
//            activityConfigPos.forEach(e->{
//                Integer code = ActivityTypeEnum.getCodeByDesc(e.getName());
//                if(sbf.toString().contains(code.toString()) || code ==0){
//                    return ;
//                }
//                if(StringUtil.isBlankOrNull(sbf.toString())){
//                    sbf.append(ActivityTypeEnum.getCodeByDesc(e.getName()));
//                }else{
//                    sbf.append(",").append(ActivityTypeEnum.getCodeByDesc(e.getName()));
//                }
//            });
            //activityEntranceShowVO.setActivityType("2,3,4");
        }
        activityEntranceShowVO.setActivityType("2,3,4,5");
        return Response.returnSuccess(activityEntranceShowVO);
    }

    @Override
    public void onceOperationMerchantUpdate(ActivityStatusVO activityStatusVO, HttpServletRequest request) {
        List<String> list;
        List<String> beforeValue = new ArrayList<>();
        if ("all".equals(activityStatusVO.getActivityMerchants())) {
            beforeValue.add("全部");
            list = activityMerchantMapper.queryMerchantByMerchant().stream().map(ActivityMerchantPo::getMerchantCode).collect(Collectors.toList());
            activityMerchantMapper.updateActivityMerchantEntranceAll(activityStatusVO.getStatus());
        } else {
            list = Arrays.asList(activityStatusVO.getActivityMerchants().split(","));
            activityMerchantMapper.updateActivityMerchantEntrance(list, activityStatusVO.getStatus());
        }
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        List<String> fieldNames = new ArrayList<>();
        if(1==activityStatusVO.getStatus()){
            beforeValue.add("关");
            list.add("开");
        }
        if(0==activityStatusVO.getStatus()){
            beforeValue.add("开");
            list.add("关");
        }
        vo.setBeforeValues(beforeValue);
        vo.setAfterValues(list);
        fieldNames.add("商户编号");
        fieldNames.add("活动状态");
        vo.setFieldName(fieldNames);

        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_46, MerchantLogTypeEnum.EDIT_INFO, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, username, userId, language, ip);
    }

    private void saveLog(String str, HttpServletRequest request, List<String> list) {
        list.forEach(e -> {
            merchantApiClient.clearMerchantActivityCache(e);
        });
        String userId = SsoUtil.getUserId(request).toString();
        String userName = request.getHeader("merchantName");
        MerchantLogFiledVO merchantLogFiledVO = new MerchantLogFiledVO();
        List<String> beforeValue = new ArrayList<>();
        merchantLogFiledVO.setBeforeValues(beforeValue);
        merchantLogFiledVO.setAfterValues(list);
        List<String> timeList = new ArrayList<>();
        timeList.add(str);
        merchantLogFiledVO.setFieldName(timeList);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_ACTIVITY, MerchantLogTypeEnum.ACTIVITY_SET, merchantLogFiledVO, MerchantLogConstants.MERCHANT_IN, userId,
                userName, null, null,null, language,IPUtils.getIpAddr(request));
    }

    @Override
    public void onceOperationMerchantActivity(ActivityStatusVO activityStatusVO, HttpServletRequest request) {
        List<String> list;
        if ("all".equals(activityStatusVO.getActivityMerchants())) {
            list = activityMerchantMapper.queryMerchantByMerchant().stream().map(ActivityMerchantPo::getMerchantCode).collect(Collectors.toList());
            activityMerchantMapper.updateMerchantActivityAll(activityStatusVO.getStatus());
        } else {
            list = Arrays.asList(activityStatusVO.getActivityMerchants().split(","));
            activityMerchantMapper.updateMerchantActivity(list, activityStatusVO.getStatus());
        }
        //保存日志
        String info = activityStatusVO.getStatus() == 0 ? "关闭" : "开启";
        StringBuffer sbf = new StringBuffer();
        sbf.append("一键").
                append(info).
                append("活动入口状态");
        this.saveLog(sbf.toString(), request, list);
    }

    @Override
    public void onceOperationMerchantDelete(ActivityStatusVO activityStatusVO, HttpServletRequest request) {
        List<String> list;
        if ("all".equals(activityStatusVO.getActivityMerchants())) {
            list = activityMerchantMapper.queryMerchantByMerchant().stream().map(ActivityMerchantPo::getMerchantCode).collect(Collectors.toList());
            activityMerchantMapper.deleteMerchantActivityAll();
        } else {
            list = Arrays.asList(activityStatusVO.getActivityMerchants().split(","));
            activityMerchantMapper.deleteMerchantActivity(list);
        }
        //保存日志
        String info = "一键删除商户活动";
        this.saveLog(info, request, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activityMaintain(ActivityMaintainVO activityMaintainVO,HttpServletRequest request) throws Exception {
        try {
            Integer maintainStatus = activityMaintainVO.getMaintainStatus();
            Long maintainEndTime = activityMaintainVO.getMaintainEndTime();
            String h5MaintainUrl = activityMaintainVO.getH5MaintainUrl();
            String pcMaintainUrl = activityMaintainVO.getPcMaintainUrl();
            String title = activityMaintainVO.getTitle();
            String content = activityMaintainVO.getContent();
            if (maintainEndTime == null || maintainStatus == null || StringUtils.isAnyEmpty(h5MaintainUrl, pcMaintainUrl, title, content)) {
                log.error("activityMaintain存在入参为空!!!");
                return;
            }
            content = content.replaceAll("\r\n|\r|\n|\n\r", "");
            log.info("activityMaintain.content:" + content);
            Map<String, Object> old= activityEntranceMapper.getActivityMaintain();
            activityEntranceMapper.updateActivity(maintainStatus, maintainEndTime, h5MaintainUrl, pcMaintainUrl, title, content);
             //添加日志
            MerchantUtil filedUtil = new MerchantUtil<MerchantCodeConfig>();
            ActivityMaintainVO oldActivity = new ActivityMaintainVO();
            oldActivity.setContent(old.get("content").toString());
            oldActivity.setTitle(old.get("title").toString());
            oldActivity.setMaintainStatus(Integer.valueOf(old.get("maintainStatus").toString()));
            oldActivity.setPcMaintainUrl(old.get("pcMaintainUrl").toString());
            oldActivity.setH5MaintainUrl(old.get("h5MaintainUrl").toString());


            MerchantLogFiledVO filedVO = filedUtil.compareObject(oldActivity, activityMaintainVO,MerchantUtil.filterActivityField,MerchantUtil.FIELD_ACTIVITY_MAPPING);

            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_ACTIVITY_EDIT, MerchantLogTypeEnum.EDIT_INFO, filedVO,
                    MerchantLogConstants.MERCHANT_IN, request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), "", request.getHeader("user-id"),request.getHeader("language") , IPUtils.getIpAddr(request));
/*            if (maintainStatus == 1) {
                activityEntranceMapper.updateActivityStatus(0, 10009);
                activityEntranceMapper.updateActivityStatus(0, 10008);
            } else {
                activityEntranceMapper.updateActivityStatus(1, 10009);
                activityEntranceMapper.updateActivityStatus(1, 10008);
            }*/
            String url = nacosUrl + "?tenant=" + nacosNameSpace + "&dataId=panda-bss-activity-config.properties&group=DEFAULT_GROUP&type=properties";
            Mono<?> mono = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class);
            log.info("result:" + mono.block());
            String configStr = (String) mono.block();
            String[] lines = configStr.split("\\r?\\n");
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (StringUtils.isBlank(line)) continue;
                String[] tempSTrAr = line.split("=");
                String key = tempSTrAr[0];
                if (key.equalsIgnoreCase("activity.maintaining")) {
                    sb.append("activity.maintaining=").append(maintainStatus).append("\r\n\r\n");
                } else if (key.equalsIgnoreCase("activity.maintaining.time")) {
                    sb.append("activity.maintaining.time=").append(maintainEndTime).append("\r\n\r\n");
                } else if (key.equalsIgnoreCase("activity.maintaining.pc.url")) {
                    sb.append("activity.maintaining.pc.url=").append(pcMaintainUrl).append("\r\n\r\n");
                } else if (key.equalsIgnoreCase("activity.maintaining.h5.url")) {
                    sb.append("activity.maintaining.h5.url=").append(h5MaintainUrl).append("\r\n\r\n");
                } else if (key.equalsIgnoreCase("activity.maintaining.title")) {
                    sb.append("activity.maintaining.title=").append(title).append("\r\n\r\n");
                } else if (key.equalsIgnoreCase("activity.maintaining.content")) {
                    sb.append("activity.maintaining.content=").append(content).append("\r\n\r\n");
                }
            }
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("dataId", "panda-bss-activity-config.properties");
            params.add("tenant", nacosNameSpace);
            params.add("group", "DEFAULT_GROUP");
            params.add("type", "properties");
            params.add("content", sb.toString());
            Mono<String> result = WebClient.create().post()
                    .uri(nacosUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve().bodyToMono(String.class);
            log.info("publish:{}", result.block());
        } catch (Exception e) {
            log.error("activityMaintain:", e);
            throw new Exception("未查询到数据");
        }
    }

    @Override
    public Map getActivityMaintain() {
        try {
            Map<String, Object> result = activityEntranceMapper.getActivityMaintain();
            return result;
        } catch (Exception e) {
            log.error("getActivityMaintain:", e);
            return null;
        }
    }

    public Response queryGetMerchantTree(MerchantTreeForm merchantTreeForm) {
        try {
            List<MerchantTree> merchantTrees = merchantMapper.getMerchantByTag(merchantTreeForm.getTag(), merchantTreeForm.getName());
            List<MerchantTree> merchantTreeLists = new ArrayList<>();
            List<String> merchantList = activityMerchantMapper.queryActivityMerchantCode();

            //merchantTreeLists =merchantTrees;
            //过滤出存在的商户
            for (MerchantTree merchantTreeVo : merchantTrees) {
                if (merchantList.contains(merchantTreeVo.getMerchantCode())) {
                    merchantTreeLists.add(merchantTreeVo);
                }
            }

            List<String> merchantIds = new ArrayList<>();
            // 拿到顶级用户
            List<MerchantTree> rootMerchant = new ArrayList<>();
            for (MerchantTree merchantTreeVo : merchantTreeLists) {
                if (merchantTreeVo.getParentId() == null) {
                    rootMerchant.add(merchantTreeVo);
                }
                if (merchantTreeVo.getAgentLevel() == 2 && merchantTreeVo.getParentId() != null) {
                    merchantIds.add(merchantTreeVo.getParentId());
                }
            }

            // 渠道商户的父级
            if (merchantIds.size() > 0) {
                List<MerchantTree> merchantPOListOther = merchantMapper.getMerchantByIds(merchantIds);
                for (MerchantTree merchantPO : merchantPOListOther) {
                    log.info(merchantPO.getMerchantCode() + "*****");
                    rootMerchant.add(merchantPO);
                }
            }

            for (MerchantTree amv : rootMerchant) {
                List<MerchantTree> childList = getChild(amv, merchantTreeLists);
                amv.setTrees(childList);
            }

            return Response.returnSuccess(rootMerchant);
        } catch (Exception e) {
            log.error("getMerchantTree", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 获取子集
     *
     * @param parent
     * @param merchantTreeVoList
     * @return
     */
    private List<MerchantTree> getChild(MerchantTree parent, List<MerchantTree> merchantTreeVoList) {

        List<MerchantTree> childList = new ArrayList<>();
        for (MerchantTree merchantTreeVo : merchantTreeVoList) {
            if (parent.getId().equals(merchantTreeVo.getParentId())) {
                childList.add(merchantTreeVo);
            }
        }
        for (MerchantTree nav : childList) {
            nav.setTrees(getChild(nav, merchantTreeVoList));
        }
        if (childList.size() == 0) {
            return Collections.emptyList();
        }
        return childList;
    }

    private void saveObj(Map<String, Integer> map, MerchantTree obj, ActivityMerchantTree activityMerchantTree) {
        activityMerchantTree.setId(obj.getId());
        activityMerchantTree.setMerchantCode(obj.getMerchantCode());
        activityMerchantTree.setMerchantName(obj.getMerchantName());
        activityMerchantTree.setAgentLevel(obj.getAgentLevel());
        activityMerchantTree.setParentId(obj.getParentId());
        activityMerchantTree.setIsChoose(map.get(obj.getMerchantCode()) == null ? 0 : 1);
    }

    /**
     * 先这样写 后续使用递归
     *
     * @param obj
     * @param merchantTree
     * @param map
     */
    private void addTree(MerchantTree obj, ActivityMerchantTree merchantTree, Map<String, Integer> map) {
        if (null != obj.getTrees() && obj.getTrees().size() > 0) {
            List<ActivityMerchantTree> activityMerchantTrees = new ArrayList<>();
            obj.getTrees().forEach(e -> {
                MerchantTree obj1 = obj;
                ActivityMerchantTree activityMerchantTree = new ActivityMerchantTree();
                this.saveObj(map, obj, activityMerchantTree);
                if (null != obj.getTrees() && obj.getTrees().size() > 0) {
                    List<ActivityMerchantTree> activityMerchantTreeList1 = new ArrayList<>();
                    obj1.getTrees().forEach(e1 -> {
                        ActivityMerchantTree activityMerchantTree1 = new ActivityMerchantTree();
                        this.saveObj(map, obj1, activityMerchantTree1);
                        activityMerchantTreeList1.add(activityMerchantTree1);
                    });
                    activityMerchantTree.setTrees(activityMerchantTreeList1);
                }
                activityMerchantTrees.add(activityMerchantTree);
            });
            merchantTree.setTrees(activityMerchantTrees);
        }

    }

    /**
     * 活动时间更新
     *
     * @param activityTimeVO
     * @return
     */
    private Map<String, Long> getActivityTime(ActivityTimeVO activityTimeVO, Integer type) {
        Long startTime = null;
        Long endTime = null;
        //0关闭，1 开起
        Long activityStatus = null;
        //1常规活动，2常驻活动
        Long activityType = null;
        if (type == 2) {
            startTime = activityTimeVO.getBlindBoxStartTime();
            endTime = activityTimeVO.getBlindBoxEndTime();
            activityType = 1L;
            if (activityTimeVO.getBlindResidentStartTime() != null && activityTimeVO.getBlindResidentStartTime() != 0) {
                startTime = activityTimeVO.getBlindResidentStartTime();
                activityStatus = activityTimeVO.getBlindActivityEnd().longValue();
                if (activityStatus == 1) {
                    endTime = Long.valueOf(DateUtils.dateToStamp(DateUtils.transferLongToString(System.currentTimeMillis()) + " 23:59:59"));
                } else {
                    endTime = 0L;
                }
                activityType = 2L;
            }
        } else if (type == 3) {
            startTime = activityTimeVO.getDailyTaskStartTime();
            endTime = activityTimeVO.getDailyTaskEndTime();
            activityType = 1L;
            if (activityTimeVO.getDailyResidentStartTime() != null && activityTimeVO.getDailyResidentStartTime() != 0) {
                startTime = activityTimeVO.getDailyResidentStartTime();
                activityStatus = activityTimeVO.getDailyActivityEnd().longValue();
                if (activityStatus == 1) {
                    endTime = Long.valueOf(DateUtils.dateToStamp(DateUtils.transferLongToString(System.currentTimeMillis()) + " 23:59:59"));
                } else {
                    endTime = 0L;
                }
                activityType = 2L;
            }
        } else if (type == 4) {
            startTime = activityTimeVO.getGrowthTaskStartTime();
            endTime = activityTimeVO.getGrowthTaskEndTime();
            activityType = 1L;
            if (activityTimeVO.getGrowthResidentStartTime() != null && activityTimeVO.getGrowthResidentStartTime() != 0) {
                startTime = activityTimeVO.getGrowthResidentStartTime();
                activityStatus = activityTimeVO.getGrowthActivityEnd().longValue();
                if (activityStatus == 1) {
                    endTime = Long.valueOf(DateUtils.dateToStamp(DateUtils.transferLongToString(System.currentTimeMillis()) + " 23:59:59"));
                } else {
                    endTime = 0L;
                }
                activityType = 2L;
            }
        }else if (type == 5) {
            startTime = activityTimeVO.getTigerStartTime();
            endTime = activityTimeVO.getTigerEndTime();
            activityType = 1L;
            if (activityTimeVO.getTigerResidentStartTime() != null && activityTimeVO.getTigerResidentStartTime() != 0) {
                startTime = activityTimeVO.getTigerResidentStartTime();
                activityStatus = activityTimeVO.getTigerActivityEnd().longValue();
                if (activityStatus == 1) {
                    endTime = Long.valueOf(DateUtils.dateToStamp(DateUtils.transferLongToString(System.currentTimeMillis()) + " 23:59:59"));
                } else {
                    endTime = 0L;
                }
                activityType = 2L;
            }
        }
        Map<String, Long> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("activityStatus", activityStatus);
        map.put("activityType", activityType);
        return map;
    }

    //转换活动名称去重
    private List<Integer> exchangeValue(List<ActivityConfigPo> activityConfigPoList, List<String> activity) {
        List<Integer> list = new ArrayList<>();
        activityConfigPoList.forEach(e -> {
            //Integer type = ActivityTypeEnum.getCodeByDesc(e.getName());
            Integer type = this.getActivityType(e.getId());
            if (type != 0) {
                list.add(type);
            }
        });
        List<Integer> valus = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            if (null == activity) {
                valus = ActivityTypeEnum.removeRepeat(list);
            } else {
                valus = ActivityTypeEnum.removeOther(list, activity);
            }
        }
        return valus;
    }

    //转换活动名称去重
    private List<Integer> repeatValue(List<Integer> poList, List<Integer> activity) {
        poList.removeAll(activity);
        return poList;
    }

    private Response saveActivityConfig(ActivityConfigVO activityConfigVO, List<MerchantPO> list, Map<String, ActivityMerchantPo> map) {
        try {
//            Long id =activityEntranceMapper.selectKeyId();
//            if(id == null || id==0L){
//                id = 100L;
//            }
            List<String> activityTypes = Arrays.asList(activityConfigVO.getActivityType().split(","));
            log.info("长度==》{}==>{}", activityTypes.size(), JSON.toJSONString(activityTypes));
            if (StringUtil.isNotBlank(activityConfigVO.getActivityMerchants()) && "all".equals(activityConfigVO.getActivityMerchants())) {
                for (MerchantPO obj : list) {
                    this.saveActivity(activityConfigVO, activityTypes, obj.getMerchantCode(), map);
                }
            } else {
                String[] str = activityConfigVO.getActivityMerchants().split(",");
                for (int i = 0; i < str.length; i++) {
                    this.saveActivity(activityConfigVO, activityTypes, str[i], map);
                }
            }
//            if(null != map){
//                log.info("map的值==》{}",JSON.toJSONString(map));
//                map.forEach((e,v)->{
//                    activityMerchantMapper.deleteActivityMerchantPO(v.getMerchantCode(), v.getActivityId());
//                });
//            }
        } catch (Exception e) {
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }

    private void saveActivity(ActivityConfigVO activityConfigVO, List<String> activityTypes, String merchantCode, Map<String, ActivityMerchantPo> map) {
        //全部活动
        if (activityTypes.size() == 3) {
            String str = merchantCode + "_" + this.getActivityId(ActivityTypeEnum.Daily_task.getCode());
            //String  str = merchantCode+"_"+10011;
            if (null == map || null == map.get(str)) {
                savePoJo(ActivityTypeEnum.Daily_task.getCode(), ActivityTypeEnum.Daily_task.getDescribe(), merchantCode, activityConfigVO, 1, 0);
                //savePoJo(5, "测试5",merchantCode,activityConfigVO,1,0);
            }
            str = merchantCode + "_" + this.getActivityId(ActivityTypeEnum.Blind_box_activity.getCode());
            //str = merchantCode+"_"+10010;
            if (null == map || null == map.get(str)) {
                savePoJo(ActivityTypeEnum.Blind_box_activity.getCode(), ActivityTypeEnum.Blind_box_activity.getDescribe(), merchantCode, activityConfigVO, 1, 0);
                //savePoJo(6, "测试6",merchantCode,activityConfigVO,1,0);
            }
            str = merchantCode + "_" + this.getActivityId(ActivityTypeEnum.Growth_task.getCode());
            //str = merchantCode+"_"+10012;
            if (null == map || null == map.get(str)) {
                savePoJo(ActivityTypeEnum.Growth_task.getCode(), ActivityTypeEnum.Growth_task.getDescribe(), merchantCode, activityConfigVO, 1, 0);
                //savePoJo(7, "测试7",merchantCode,activityConfigVO,1,0);
            }
        } else {
            for (String e : activityTypes) {
                log.info("保存活动==》{}==>{}", e, JSON.toJSONString(activityTypes));
                String str = merchantCode + "_" + this.getActivityId(Integer.valueOf(e));
                if (null == map || null == map.get(str)) {
                    savePoJo(Integer.valueOf(e), ActivityTypeEnum.getDescByCode(Integer.valueOf(e)), merchantCode, activityConfigVO, 1, 0);
                    //savePoJo(Integer.valueOf(e)+3, "测试"+(Integer.valueOf(e)+3),merchantCode,activityConfigVO,1,0);
                }
            }
        }
    }

    private void savePoJo(Integer activityType, String name, String merchantCode, ActivityConfigVO activityConfigVO, Integer type, Integer status) {
        ActivityConfigPo activityConfig = activityEntranceMapper.getActivityConfigPO(this.getActivityId(activityType));
        if (activityConfig == null) {
            ActivityConfigPo activityConfigPo = new ActivityConfigPo();
            activityConfigPo.setId(this.getActivityId(activityType));
            //activityConfigPo.setName(ActivityTypeEnum.getDescByCode(activityType));
            activityConfigPo.setName(name);
            activityConfigPo.setType(1);
            activityConfigPo.setStatus(1);
            if (activityConfigVO != null) {
                activityConfigPo.setH5Url(activityConfigVO.getH5Url());
                activityConfigPo.setPcUrl(activityConfigVO.getPcUrl());
            }
            log.info("save==>{}", JSON.toJSONString(activityConfigPo));
            activityEntranceMapper.saveActivityConfig(activityConfigPo);
        } else if (type == 1) {
            activityEntranceMapper.updateActivityConfig(activityConfig.getId(), activityConfigVO.getH5Url(), activityConfigVO.getPcUrl());
        }
        //商户活动中间表保存
        ActivityMerchantPo activityMerchantPo = new ActivityMerchantPo();
        if (activityConfig == null) {
            activityMerchantPo.setActivityId(this.getActivityId(activityType));
        } else {
            activityMerchantPo.setActivityId(activityConfig.getId());
        }
        activityMerchantPo.setMerchantCode(merchantCode);
        activityMerchantPo.setEntranceStatus(status);
        if (activityConfig != null) {
            ActivityMerchantPo activityMerchant = activityMerchantMapper.getActivityMerchantPO(activityConfig.getId(), merchantCode);
            if (null == activityMerchant) {
                activityMerchantMapper.insert(activityMerchantPo);
            }
        } else {
            activityMerchantMapper.insert(activityMerchantPo);
        }
    }

    private Long getActivityId(Integer activityType) {
        Long id = 0L;
        switch (activityType) {
            case 2:
                id = 10009L;
                break;
            case 3:
                id = 10007L;
                break;
            case 4:
                id = 10008L;
                break;
            case 5:
                id = 10010L;
                break;
//            case 2:
//                id = 10010L;
//                break;
//            case 3:
//                id = 10011L;
//                break;
//            case 4:
//                id = 10012L;
//                break;
        }
        return id;
    }

    private Integer getActivityType(Long activityId) {
        Integer id = 0;
        if (activityId == 10009L) {
            id = 2;
        } else if (activityId == 10008L) {
            id = 4;
        } else if (activityId == 10007L) {
            id = 3;
        } else if (activityId == 10010L) {
            id = 5;
        }
        return id;
    }
}
