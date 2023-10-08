package com.panda.sport.merchant.api.util;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class DistributedLockerUtil {

    private RedissonClient redissonClient;

    @Value("${spring.redis.cluster.nodes}")
    String nodeAddress;

    @Value("${spring.redis.password}")
    String password;

    @PostConstruct
    private void init() {
        if (redissonClient != null) {
            return;
        }
        if (StringUtils.isEmpty(nodeAddress)) {
            return;
        }
        String[] nodeStr = nodeAddress.split(",");

        StringBuilder addresses = new StringBuilder();
        for (String node : nodeStr) {
            addresses.append("redis://").append(node).append(",");
        }
        addresses = new StringBuilder(addresses.substring(0, addresses.length() - 1));
        //配置config，如果是单机版的redis，那么就是使用config.useSingleServer()
        //如果是集群，那么请使用config.useClusterServers()
        Config config = new Config();

        String[] addr = addresses.toString().split(",");
        //config.useSingleServer().setAddress("redis://10.10.100.116:6379");

        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        for (String node : addr) {
            clusterServersConfig.addNodeAddress(node);
        }
        clusterServersConfig.setPassword(password);
        redissonClient = Redisson.create(config);
    }

    /**
     * 获取RedissonClient
     *
     * @return
     */
    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }

    /**
     * 加锁
     *
     * @return
     */
    public void lock(String lockKey) {
        redissonClient.getLock(lockKey).lock();
    }

    /**
     * 释放锁
     */
    public void unLock(String lockKey) {
        redissonClient.getLock(lockKey).unlock();
    }

    /**
     * 带超时的加锁
     *
     * @param lockKey
     * @param tomeout 秒为单位
     */
    public void lock(String lockKey, Long tomeout) {
        redissonClient.getLock(lockKey).lock(tomeout, TimeUnit.SECONDS);
    }

    /**
     * 带超市的加锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param tomeout
     */
    public void lock(String lockKey, TimeUnit unit, Long tomeout) {
        redissonClient.getLock(lockKey).lock(tomeout, unit);
    }


    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @return
     */
    public boolean tryLock(String lockKey) {
        return redissonClient.getLock(lockKey).tryLock();
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param timeout 尝试等待多少秒时间
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String lockKey, Long timeout) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(timeout, TimeUnit.SECONDS);
    }


    /**
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待多久时间
     * @param leaseTime 上锁后多久释放
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(waitTime, leaseTime, unit);
    }


}
