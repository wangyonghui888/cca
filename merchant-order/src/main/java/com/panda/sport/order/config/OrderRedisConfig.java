package com.panda.sport.order.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Order(0)
@Component
@EnableCaching
public class OrderRedisConfig {

/*    @Value("${spring.redis4.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis4.password}")
    private String password;

    @Value("${spring.redis4.commandTimeout}")
    private long timeout;

    @Value("${spring.redis4.lettuce.shutdown-timeout}")
    private long shutDownTimeout;

    @Value("${spring.redis4.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis4.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis4.lettuce.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis4.lettuce.pool.max-wait}")
    private long maxWait;*/


    @Value("${spring.redis4.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis4.password}")
    private String password;

    @Value("${spring.redis4.commandTimeout}")
    private long commondtimeout;

    @Value("${spring.redis4.expireSeconds}")
    private long expireSeconds;

    @Value("${spring.redis4.maxRedirections}")
    private int maxRedirections;

    @Value("${spring.redis4.maxTotal}")
    private int maxTotal;

    @Value("${spring.redis4.maxActive}")
    private long maxActive;

    @Value("${spring.redis4.maxIdle}")
    private long maxIdle;

    @Value("${spring.redis4.maxWait}")
    private long maxWait;

    @Value("${spring.redis4.minIdle}")
    private long minIdle;
    @Value("${spring.redis4.maxAttempts}")
    private long maxAttempts;

    @Value("${spring.redis4.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${spring.redis4.testOnReturn}")
    private Boolean testOnReturn;

    @Value("${spring.redis4.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${spring.redis4.blockWhenExhausted}")
    private Boolean blockWhenExhausted;

    @Value("${spring.redis4.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.redis4.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.redis4.numTestsPerEvictionRun}")
    private int numTestsPerEvictionRun;

    @Value("${spring.redis4.lettuce.pool.max-active}")
    private int lettuceMaxActive;

    @Value("${spring.redis4.lettuce.pool.max-idle}")
    private int lettuceMaxIdle;

    @Value("${spring.redis4.lettuce.pool.min-idle}")
    private int lettuceMinIdle;

    @Value("${spring.redis4.lettuce.pool.max-wait}")
    private long lettuceMaxWait;

    @Value("${spring.redis4.timeout}")
    private long timeout;

    @Value("${spring.redis4.lettuce.cluster.refresh.period}")
    private long lettuceperiod;

    @Value("${spring.redis4.lettuce.cluster.refresh.adaptive}")
    private Boolean lettuceadaptive;

    @Value("${spring.redis4.lettuce.shutdown-timeout}")
    private long shutDownTimeout;

    @Bean("orderLettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory() {
        ClusterTopologyRefreshOptions topologyRefreshOptions =
                ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh(Duration.ofSeconds(lettuceperiod)).
                        enableAllAdaptiveRefreshTriggers().build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder().timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(timeout))).
                topologyRefreshOptions(topologyRefreshOptions).build();

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(lettuceMaxIdle);
        genericObjectPoolConfig.setMinIdle(lettuceMinIdle);
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMaxWaitMillis(lettuceMaxWait);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(100);
        genericObjectPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        genericObjectPoolConfig.setTestOnBorrow(testOnBorrow);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        genericObjectPoolConfig.setTestOnReturn(testOnReturn);
        genericObjectPoolConfig.setTestWhileIdle(testWhileIdle);
        genericObjectPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        Set<String> redisNodes = new HashSet<>(Arrays.asList(clusterNodes.split(",")));

        Set<RedisNode> clusterNodeSet = new HashSet<>();
        for (String node : redisNodes) {
            String[] ipAndPort = StringUtils.split(node, ":");
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            clusterNodeSet.add(new RedisNode(ip, port));
        }
        redisClusterConfiguration.setClusterNodes(clusterNodeSet);
        redisClusterConfiguration.setPassword(RedisPassword.of(password));
        redisClusterConfiguration.setMaxRedirects(maxRedirections);
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(commondtimeout))
                .shutdownTimeout(Duration.ofMillis(shutDownTimeout))
                .clientOptions(clusterClientOptions)
                .poolConfig(genericObjectPoolConfig)
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
        factory.setShareNativeConnection(true);
        factory.setValidateConnection(false);
        return factory;
    }

    @Bean(name = "orderTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("orderLettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        //使用Jackson2JsonRedisSerializer替换默认的JdkSerializationRedisSerializer来序列化和反序列化redis的value值
/*        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jdkSerializationRedisSerializer.setObjectMapper(mapper);*/
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        //key采用String的序列化方式
        template.setKeySerializer(fastJsonRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(fastJsonRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(fastJsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
