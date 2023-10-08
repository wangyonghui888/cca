package com.panda.sport.merchant.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.panda.sport.bss.mapper.UserOrderUpdateMapper;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.api.service.UserOrderUpdateService;
import com.panda.sport.merchant.api.service.UserService;
import com.panda.sport.merchant.common.dto.UserOrderTimePO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@RefreshScope
@Service("userOrderUpdateService")
@Slf4j
public class UserOrderUpdateServiceImpl implements UserOrderUpdateService {

    @Autowired
    private UserOrderUpdateMapper updateMapper;

    @Autowired
    private UserService userService;

    @Autowired
    public RedisTemp redisTemp;

    @Override
    public int updateUserOrderTime(List<UserOrderTimePO> list) {
        updateMapper.updateUserOrderTime(list);
        return 0;
    }

    @Override
    public int updateUserSevenOrder(List<UserOrderTimePO> list) {
        updateMapper.updateUserSevenOrder(list);
        updateMapper.updateUserSevenOrderLast1();
        return 0;
    }

    @Override
    public int updateUserAllLifeData(List<UserOrderTimePO> list) {
        updateMapper.updateUserAllLifeData(list);
        return 0;
    }

    @Override
    public int updateUserAllStatistic(List<UserOrderDayPO> list) {
        updateMapper.batchUpdateUserBetAmount(list);
        return 0;
    }

    @Override
    public int upsertUserVip(String merchantCode, List<UserVipVO> list) {
        if (CollectionUtil.isEmpty(list)) {
            return 1;
        }
        int result = updateMapper.updateUserVip(list);
        log.info("导入VIP用户，upsertUserVip, merchantCode{}, list{}, result{}.", merchantCode, list, result);
        List<Long> uidList = list.stream().map(UserVipVO::getUid).collect(Collectors.toList());
        userService.batchUpdateUserAndDomainCache(uidList, merchantCode, 2);
        return result;
    }

    @Override
    public int upsertUserDisabled(String merchantCode, List<UserVipVO> list) {
        int result = 0;
        if (CollectionUtil.isNotEmpty(list)) {
            result = updateMapper.upsertUserDisabled(list, list.get(0).getDisabled(), list.get(0).getRemark());
        }
        log.info("导入问题用户，upsertUserDisabled, merchantCode{}, list{}, result{}.", merchantCode, list, result);
        //剔除用户，删除对应用户的redis缓存
        userService.kickoutDisableUser(merchantCode, list);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upsertUserDisabled2Allow(String merchantCode, Integer disabled, List<UserVipVO> list) {
        try {
            int result = 0;
            String remark = "";
            if (CollectionUtil.isEmpty(list) && !Objects.equals(merchantCode, "ALLIN")) {
                return result;
            }
            if (CollectionUtil.isNotEmpty(list) && StringUtils.isNotBlank(list.get(0).getRemark()))
                remark = list.get(0).getRemark();

            // 暂时使用商户code分组执行
//            if(Objects.equals(merchantCode, "ALLIN")){
//                // 每个组都执行一次
//                final String remarkF = remark;
//                CountDownLatch countDownLatch = new CountDownLatch(4);
//                ExecutorService threadPool = Executors.newFixedThreadPool(4);
//                AtomicInteger resultA = new AtomicInteger(0);
//                try {
//                    for (DataSourceType value : DataSourceType.values()) {
//                        threadPool.execute(() -> {
//                            DynamicDataSourceContextHolder.setDateSoureType(value.name());
//                            resultA.getAndAdd(updateMapper.upsertUserDisabled2Allow(list,disabled,remarkF));
//                            log.info("导入白名单用户，upsertUserDisabled2Allow, group:{}, list{}, result{}.", value.name(), list, resultA.get());
//                            countDownLatch.countDown();
//                        });
//                    }
//                    countDownLatch.await();
//                    log.info("导入白名单用户  所有线程都执行处理数据完毕!");
//                } catch (InterruptedException e) {
//                    log.error("导入白名单用户  线程释放异常!", e);
//                } finally {
//                    threadPool.shutdown();
//                }
//                return resultA.get();
//            }else{
            result = updateMapper.upsertUserDisabled2Allow(list,disabled,remark);
            log.info("导入白名单用户，upsertUserDisabled2Allow, merchantCode{}, list{}, result{}.", merchantCode, list, result);
//            }
            return result;
        } catch (Exception e) {
            log.error("api-update user disabled error ", e);
            throw e;
        }

    }

    @Override
    public int updateDisabled(String merchantCode, UserVipVO userVipVO) {
        int result = 0;
        if (Objects.nonNull(userVipVO)) {
            result = updateMapper.updateDisabled(userVipVO.getDisabled(), userVipVO.getUid());
        }
        log.info("updateDisabled修改启/禁用状态，updateDisabled, merchantCode{}, user{}", merchantCode, JSON.toJSONString(userVipVO));
        List<UserVipVO> list = new ArrayList<>();
        list.add(userVipVO);
        //剔除用户，删除对应用户的redis缓存
        userService.kickoutDisableUser(merchantCode, list);
        return result;
    }

    @Override
    public void updateIsVipDomain(String merchantCode, String userId, Integer isVipDomain) {
        updateMapper.updateIsVipDomain(Long.parseLong(userId), isVipDomain);
        // 同时刷新用户域名缓存
        userService.updateUserAndDomainCache(Long.valueOf(userId), merchantCode, isVipDomain);
    }

    @Override
    public void upsertUserToDisabled(String merchantCode, List<Long> uidList, Integer disabled) {
        updateMapper.upsertUserToDisabled(uidList, disabled);
    }

}
