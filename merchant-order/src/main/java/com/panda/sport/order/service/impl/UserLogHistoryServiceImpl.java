package com.panda.sport.order.service.impl;

import com.panda.sport.backup.mapper.BackupUserLogHistoryMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.bss.mapper.UserLogHistoryMapper;
import com.panda.sport.merchant.common.bo.UserInfoBO;
import com.panda.sport.merchant.common.po.merchant.UserLogHistoryPO;
import com.panda.sport.merchant.common.vo.user.UserLogHistoryVO;
import com.panda.sport.order.config.OrderRedisUtilBean;
import com.panda.sport.order.feign.MerchantApiClient;
import com.panda.sport.order.service.UserLogHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class UserLogHistoryServiceImpl implements UserLogHistoryService {

    @Autowired
    private UserLogHistoryMapper userLogHistoryMapper;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private BackupUserLogHistoryMapper backupUserLogHistoryMapper;

    @Autowired
    private OrderRedisUtilBean orderRedisUtil;


    /**
     * 查询历史纪录
     *
     * @Param: [historyVO]
     * @return: java.util.List<com.panda.sport.merchant.common.vo.user.UserLogHistoryVO>
     * @date: 2020/8/23 15:12
     */
    @Override

    public List<UserLogHistoryVO> queryHistory(UserLogHistoryVO historyVO) {
        List<UserLogHistoryVO> returnData = new ArrayList<>();
        List<UserLogHistoryPO> data = userLogHistoryMapper.selectAllByPage(null);
        final AtomicLong ids = new AtomicLong(1L);
        data.forEach(val -> {
            UserLogHistoryVO temp = new UserLogHistoryVO();
            BeanUtils.copyProperties(val, temp);
            temp.setId(ids.getAndIncrement());
            returnData.add(temp);
        });
        return returnData;
    }


    @Override
    public void collectIp() {
        try {
            Date date = new Date();
            Date hourAgo = DateUtils.addHours(date, -1);
            String hourAgoStr = DateFormatUtils.format(hourAgo, "yyyy-MM-dd HH");
            Date start = DateUtils.parseDate(hourAgoStr + ":00:00", "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.parseDate(hourAgoStr + ":59:59", "yyyy-MM-dd HH:mm:ss");
            Long startL = start.getTime();
            Long endL = end.getTime();
            log.info("处理User ip,start:" + start + ",end:" + end);
            tUserMapper.batchUpdateIp(startL, endL);
            log.info("处理User ip 完成!!!");
        } catch (Exception e) {
            log.error("开始执行采集用户IP异常!", e);
        }
    }

    /**
     * 1:最近1小时
     * 2:最近2天
     *
     * @param days
     */

    @Override
    public void preLoadUserLogin(String days) {

        int day = StringUtils.isEmpty(days) ? 1 : Integer.parseInt(days);

        if (day <= 0) return;
        Set<UserLogHistoryPO> userSetList = new HashSet<>();
        for (int i = 0; i < day; i++) {
            int temp = day - i;
            log.info("preLoadUserLogin:" + temp);
            List<UserLogHistoryPO> userList = userLogHistoryMapper.queryRecentUserList(temp);
            userSetList.addAll(userList);
        }
        if (CollectionUtils.isEmpty(userSetList)) return;
        List<Long> loginUserList = userLogHistoryMapper.queryLoginedUserList();
        List<UserLogHistoryPO> reduceList = userSetList.stream().filter(item -> !loginUserList.contains(item.getUid())).collect(toList());
        for (UserLogHistoryPO user : reduceList) {
            merchantApiClient.preLogin(user.getUsername(), "mobile", user.getMerchantCode(), user.getCurrencyCode(), System.currentTimeMillis());
        }
    }

    @Override
    public void modifyUserProperties(String merchantCode, List<UserInfoBO> uList) {
        try {
            String keyPrefix = "userLogin_filter:";
            List<UserInfoBO> newList = new ArrayList<>();
            for (UserInfoBO u : uList) {
                boolean flag = orderRedisUtil.hasKey(keyPrefix + u.getUserName());
                if (!flag) {
                    newList.add(u);
                }
            }
            if (CollectionUtils.isNotEmpty(newList)) {
                backupUserLogHistoryMapper.batchInsertOrUpdate(newList);
                for (UserInfoBO userInfoBO : newList) {
                    String redisKey = keyPrefix + userInfoBO.getUserName();
                    Boolean result = orderRedisUtil.setAndTimeOut(redisKey, "1", 5, TimeUnit.MINUTES);
                }
            }
        } catch (Exception e) {
            log.error(merchantCode + "modifyUserInfo:" + uList, e);
        }
    }
}