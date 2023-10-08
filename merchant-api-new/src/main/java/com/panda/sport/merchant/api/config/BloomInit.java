package com.panda.sport.merchant.api.config;


import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.panda.sport.bss.mapper.TUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 用户过滤使用
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BloomInit {

    private final TUserMapper userMapper;

    public static BloomFilter<String> bloomFilter = null;

    /**
     * CYSB 4个组 都初始化50w  最近投注的用户
     */
    @PostConstruct
    private void init() {
        long startTime = System.currentTimeMillis();

        // 初始化布隆过滤器，设置数据类型，数组长度和误差值
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 3_000_000L, 0.01);
        final long pageNum = 0;
        final long pageCount = 500_000;
        final int times = 4;
        List<String> userInfoList = Lists.newArrayList();
        final CountDownLatch countDownLatch = new CountDownLatch(times);

        ExecutorService threadPool = Executors.newFixedThreadPool(times);
        try {
            for (DataSourceType value : DataSourceType.values()) {
                threadPool.execute(() -> {
                    DynamicDataSourceContextHolder.setDateSoureType(value.name());
                    List<String> userInfoListTemp = userMapper.getAllUserName(pageNum,pageCount);
                    if(CollectionUtils.isNotEmpty(userInfoListTemp)){
                        userInfoList.addAll(userInfoListTemp);
                    }
                    countDownLatch.countDown();
                    log.info("Bloom Filter Init Success - {} ,size {}! ", value.name(),userInfoListTemp.size());
                });
            }
            countDownLatch.await();
            userInfoList.parallelStream().forEach(bloomFilter::put);
            log.info("Bloom Filter Init Success - all! 耗时：{},初始化大小：{}",System.currentTimeMillis()-startTime,userInfoList.size());

        } catch (InterruptedException e) {
            log.error("Bloom Filter Init ERROR!", e);
        } finally {
            threadPool.shutdown();
        }

    }

}
