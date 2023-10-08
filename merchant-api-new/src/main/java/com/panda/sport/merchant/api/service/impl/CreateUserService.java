package com.panda.sport.merchant.api.service.impl;

import com.panda.sport.bss.mapper.TAccountMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.api.config.BloomInit;
import com.panda.sport.merchant.api.config.IdGeneratorFactory;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.base.MQMsgBody;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.bss.AccountPO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.utils.SnowflakeIdWorker;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.api.UserApiVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName CreateUserService
 * @Description 创建用户
 * @Date 2020-08-20 14:53
 * @Version 1
 */
@Slf4j
@Service("createUserService")
@RefreshScope
public class CreateUserService {
    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private TAccountMapper tAccountMapper;

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Autowired
    private MQProducer producer;

    @Autowired
    public RedisTemp redisTemp;

    /**
     * @description:带事务注册玩家,初始化余额,发送到风控MQ
     * @Param: [newUser]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:30
     */

    public APIResponse createUserByObj(UserPO newUser) {
        this.userMapper.insertTUser(newUser);
        // 刷新用户缓存
        BloomInit.bloomFilter.put(newUser.getMerchantCode() + "_" + newUser.getUsername());
        String key = CacheService.REDIS_CACHE_PREFIX + newUser.getMerchantCode() + newUser.getUsername();
        redisTemp.setObject(key,newUser,3600 * 72);
        AccountPO accounts = new AccountPO();
        accounts.setId(snowflakeIdWorker.nextId());
        accounts.setUid(newUser.getUserId());
        accounts.setAmount(0L);
        accounts.setDisabled(0);
        accounts.setCreateUser(newUser.getUsername());
        accounts.setModifyUser(newUser.getUsername());
        this.tAccountMapper.insertAccount(accounts);
        UserApiVo result = new UserApiVo();
        result.setUserId(newUser.getUserId() + "");
        sendToMq(newUser);
        return APIResponse.returnSuccess(result);
    }

    /**
     * @description:带事务注册玩家,初始化余额,发送到风控MQ
     * @Param: [newUser]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:30
     */
    @Transactional(rollbackFor = Exception.class)
    public UserPO createUser(String username, String merchantCode, String currency, String fakeName, String agentId, Integer merchantTag) {
        Pattern p = Pattern.compile(Constant.regEx);
        Matcher m = p.matcher(username);
        if (Constant.SpecialUserNameList.contains(username) || (username.length() <= 2 && m.find())) {
            log.error(username + ",用户名包含特殊字符!" + new Date());
            return null;
        }
        String userIdStr = idGeneratorFactory.generateIdByBussiness("user", 5);
        Long userId = Long.parseLong(userIdStr);
        UserPO userPO = new UserPO();
        userPO.setUserId(userId);
        userPO.setPassword(merchantCode);
        userPO.setMerchantCode(merchantCode);
        userPO.setRealName(username);
        userPO.setUsername(username);
        userPO.setFakeName(fakeName);
        userPO.setCurrencyCode(currency);
        userPO.setUserLevel(230);
        userPO.setIpAddress("111");
        if (merchantTag != null && merchantTag == 1) {
            userPO.setSpecialBettingLimitType(5);
        }
        userPO.setAgentId(agentId);
        userPO.setUserBetPrefer(2);
        long nowTime = System.currentTimeMillis();
        userPO.setCreateTime(nowTime);
        userPO.setModifyTime(nowTime);
        userPO.setCreateUser(username);
        userPO.setSettleInAdvance(1);
        userPO.setSettleSwitchBasket(1);
        log.info("createUser:" + userId + ",merchantTag=" + merchantTag + "SpecialBettingLimitType " + userPO);
        int result = this.userMapper.insertTUser(userPO);
        if (result == 0) {
            return null;
        }
        BloomInit.bloomFilter.put(userPO.getMerchantCode() + "_" + userPO.getUsername());
        String key = CacheService.REDIS_CACHE_PREFIX + userPO.getMerchantCode() + userPO.getUsername();
        redisTemp.setObject(key,userPO,3600 * 72);
        AccountPO accounts = new AccountPO();
        accounts.setId(snowflakeIdWorker.nextId());
        accounts.setUid(userId);
        accounts.setAmount(0L);
        accounts.setDisabled(0);
        accounts.setCreateUser(username);
        accounts.setModifyUser(username);
        this.tAccountMapper.insertAccount(accounts);
        sendToMq(userPO);
        return userPO;
    }

    private void sendToMq(UserPO newUser) {
        MQMsgBody body = new MQMsgBody();
        body.setKey(newUser.getUserId() + "");
        body.setTag("user");
        body.setTopic("panda_rcs_rpc_user");
        UserPO userPO = new UserPO();
        userPO.setUserId(newUser.getUserId());
        userPO.setUserLevel(newUser.getUserLevel());
        userPO.setCreateTime(newUser.getCreateTime());
        userPO.setUsername(newUser.getFakeName());
        userPO.setMerchantCode(newUser.getMerchantCode());
        userPO.setAgentId(newUser.getAgentId());
        userPO.setSettleInAdvance(newUser.getSettleInAdvance());
        userPO.setSettleSwitchBasket(newUser.getSettleSwitchBasket());
        body.setObj(userPO);
        producer.sendMessage(body);
    }
}
