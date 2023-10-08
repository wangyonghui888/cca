//package com.panda.sport.merchant.api.config;
//
//import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
//import io.lettuce.core.cluster.ClusterClientOptions;
//import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.core.env.MapPropertySource;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class RedisConfig {
//
//    @Autowired
//    private Environment environment;
//
//    @Value("${spring.redis.lettuceperiod:60000}")
//    private long lettuceperiod;
//
//    /**#设置命令的执行时间，如果超过这个时间，则报错*/
//    @Value("${spring.redis.timeout:4000}")
//	private long timeout;
//
//    @Value("${spring.redis.shutDownTimeout:200}")
//	private long shutDownTimeout;
//
//    @Value("${spring.redis.newFactory:false}")
//    private boolean newFactory;
//
//
//    /**
//     * 配置lettuce连接池
//     *
//     * @return
//     */
//	@Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.redis")
//    public GenericObjectPoolConfig redisPool() {
//        return new GenericObjectPoolConfig();
//    }
//
//    /**
//     * 配置第一个数据源的
//     *
//     * @return
//     */
//    @Bean("persistentRedisClusterConfig")
//    @Primary
//    public RedisClusterConfiguration redisClusterConfig() {
//        Map<String, Object> source = new HashMap<>(8);
//        source.put("spring.redis.cluster.nodes", environment.getProperty("spring.redis.cluster.nodes"));
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
//        redisClusterConfiguration.setPassword(environment.getProperty("spring.redis.password"));
//
//        int maxRedirects = 2;
//
//        String property = environment.getProperty("spring.redis.cluster.max-redirects");
//
//        if(!StringUtils.isBlank(property)) maxRedirects = Integer.valueOf(property);
//
//        redisClusterConfiguration.setMaxRedirects(maxRedirects);
//
//        return redisClusterConfiguration;
//
//    }
//
//    /**
//     * 配置第一个数据源的连接工厂
//     * 这里注意：需要添加@Primary 指定bean的名称，目的是为了创建两个不同名称的LettuceConnectionFactory
//     *
//     * @param redisPool
//     * @param persistentRedisClusterConfig
//     * @return
//     */
//    @Bean("persistentLettuceConnectionFactory")
//    @Primary
//    public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig redisPool, @Qualifier("persistentRedisClusterConfig") RedisClusterConfiguration persistentRedisClusterConfig) {
//
//        if(newFactory) return getFactory0(redisPool, persistentRedisClusterConfig,lettuceperiod, timeout,shutDownTimeout);
//
//        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(redisPool).build();
//        return getFactory(persistentRedisClusterConfig, clientConfiguration);
//    }
//
//	private LettuceConnectionFactory getFactory(RedisClusterConfiguration redisClusterConfig, LettuceClientConfiguration clientConfiguration) {
//
//		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfig, clientConfiguration);
//        return factory;
//	}
//
//
//	public LettuceConnectionFactory getFactory0(GenericObjectPoolConfig genericObjectPoolConfig,
//												RedisClusterConfiguration redisClusterConfiguration,
//												long lettuceperiod,long timeout,long shutDownTimeout) {
//		ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh(Duration.ofMillis(lettuceperiod)).enableAllAdaptiveRefreshTriggers().build();
//		ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()/*.timeoutOptions(TimeoutOptions.enabled(Duration.ofMillis(timeout)))*/.topologyRefreshOptions(topologyRefreshOptions).build();
//        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
//                .commandTimeout(Duration.ofMillis(timeout))
//                .shutdownTimeout(Duration.ofMillis(shutDownTimeout))
//                .clientOptions(clusterClientOptions)
//                .poolConfig(genericObjectPoolConfig)
//                .build();
//
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
//        factory.setShareNativeConnection(true);
//        factory.setValidateConnection(false);
//        return factory;
//    }
//
//
//
//    /**
//     * 配置第一个数据源的RedisTemplate
//     * 注意：这里指定使用名称=factory 的 RedisConnectionFactory
//     * 并且标识第一个数据源是默认数据源 @Primary
//     *
//     * @param persistentLettuceConnectionFactory
//     * @return
//     */
//	@Bean("persistentRedisTemplate")
//	public StringRedisTemplate stringRedisTemplate(@Qualifier("persistentLettuceConnectionFactory") RedisConnectionFactory persistentLettuceConnectionFactory) {
//		StringRedisTemplate template = new StringRedisTemplate();
//		template.setConnectionFactory(persistentLettuceConnectionFactory);
//		return template;
//	}
//
//
//    @Bean("nonPersistentRedis")
//    @ConfigurationProperties(prefix = "spring.redis2")
//    public GenericObjectPoolConfig cachRedis(GenericObjectPoolConfig redisPool) {
//    	GenericObjectPoolConfig cachRedis = new GenericObjectPoolConfig();
//		BeanUtils.copyProperties(redisPool, cachRedis);
//
//		String commandTimeout = environment.getProperty("spring.redis2.commandTimeout");
//		if(!StringUtils.isBlank(commandTimeout)) ;
//
//		String expireSeconds = environment.getProperty("spring.redis2.expireSeconds");
//		if(!StringUtils.isBlank(expireSeconds)) ;
//
//		String maxRedirections = environment.getProperty("spring.redis2.maxRedirections");
//		if(!StringUtils.isBlank(maxRedirections)) ;
//
//		String maxActive = environment.getProperty("spring.redis2.maxActive");
//		if(!StringUtils.isBlank(maxActive)) ;
//
//		String maxAttempts = environment.getProperty("spring.redis2.maxAttempts");
//		if(!StringUtils.isBlank(maxAttempts));
//
//		String maxTotal = environment.getProperty("spring.redis2.maxTotal");
//		if(!StringUtils.isBlank(maxTotal))  cachRedis.setMaxTotal(Integer.valueOf(maxTotal));
//
//		String maxIdle = environment.getProperty("spring.redis2.maxIdle");
//		if(!StringUtils.isBlank(maxIdle)) cachRedis.setMaxIdle(Integer.valueOf(maxIdle));
//
//		String maxWait = environment.getProperty("spring.redis2.maxWait");
//		if(!StringUtils.isBlank(maxWait)) {
//			int valueOf = Integer.valueOf(maxWait);
//			if(valueOf != -1)
//				cachRedis.setMaxWaitMillis(valueOf * 1000);
//		}
//		String minIdle = environment.getProperty("spring.redis2.minIdle");
//		if(!StringUtils.isBlank(minIdle)) cachRedis.setMinIdle(Integer.valueOf(minIdle));
//
//		String testOnBorrow = environment.getProperty("spring.redis2.testOnBorrow");
//		if(!StringUtils.isBlank(testOnBorrow))  cachRedis.setTestOnBorrow(Boolean.valueOf(testOnBorrow));
//
//		String testOnReturn = environment.getProperty("spring.redis2.testOnReturn");
//		if(!StringUtils.isBlank(testOnReturn)) cachRedis.setTestOnReturn(Boolean.valueOf(testOnReturn));
//
//		String testWhileIdle = environment.getProperty("spring.redis2.testWhileIdle");
//		if(!StringUtils.isBlank(testWhileIdle)) cachRedis.setTestWhileIdle(Boolean.valueOf(testWhileIdle));
//
//		String blockWhenExhausted = environment.getProperty("spring.redis2.blockWhenExhausted");
//		if(!StringUtils.isBlank(blockWhenExhausted)) cachRedis.setBlockWhenExhausted(Boolean.valueOf(blockWhenExhausted));
//
//		String timeBetweenEvictionRunsMillis = environment.getProperty("spring.redis2.timeBetweenEvictionRunsMillis");
//		if(!StringUtils.isBlank(timeBetweenEvictionRunsMillis)) cachRedis.setTimeBetweenEvictionRunsMillis(Long.valueOf(timeBetweenEvictionRunsMillis));
//
//		String minEvictableIdleTimeMillis = environment.getProperty("spring.redis2.minEvictableIdleTimeMillis");
//		if(!StringUtils.isBlank(minEvictableIdleTimeMillis)) cachRedis.setMinEvictableIdleTimeMillis(Long.valueOf(minEvictableIdleTimeMillis));
//
//		String numTestsPerEvictionRun = environment.getProperty("spring.redis2.numTestsPerEvictionRun");
//		if(!StringUtils.isBlank(numTestsPerEvictionRun))  cachRedis.setNumTestsPerEvictionRun(Integer.valueOf(numTestsPerEvictionRun));
//
//		return cachRedis;
//    }
//
//
//    /**
//     * 配置第二个数据源
//     *
//     * @return
//     */
//    @Bean("nonPersistentRedisClusterConfig")
//    public RedisClusterConfiguration secondaryRedisConfig() {
//        Map<String, Object> source = new HashMap<>(8);
//        String nodes = environment.getProperty("spring.redis2.cluster.nodes");
//        if(StringUtils.isBlank(nodes)) nodes = environment.getProperty("spring.redis.cluster.nodes");
//        source.put("spring.redis.cluster.nodes",nodes );
//        RedisClusterConfiguration redisClusterConfiguration;
//        redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
//        String property = environment.getProperty("spring.redis2.password");
//        if(StringUtils.isBlank(property)) property = environment.getProperty("spring.redis.password");
//        redisClusterConfiguration.setPassword(property);
//
//        int maxRedirects = 2;
//
//        String redirects = environment.getProperty("spring.redis2.cluster.max-redirects");
//
//        if(!StringUtils.isBlank(redirects)) maxRedirects = Integer.valueOf(redirects);
//
//        redisClusterConfiguration.setMaxRedirects(maxRedirects);
//
//        return redisClusterConfiguration;
//    }
//
//
//
//    @Bean("nonPersistentLettuceConnectionFactory")
//    public LettuceConnectionFactory secondaryLettuceConnectionFactory(@Qualifier("nonPersistentRedis") GenericObjectPoolConfig nonPersistentRedis, @Qualifier("nonPersistentRedisClusterConfig")RedisClusterConfiguration nonPersistentRedisClusterConfig) {
//
//    	if(newFactory) {
//    	  long lettuceperiod = this.lettuceperiod;
//
//       	  if(!StringUtils.isBlank(environment.getProperty("spring.redis2.lettuceperiod"))) lettuceperiod = Long.valueOf(environment.getProperty("spring.redis2.lettuceperiod"));
//
//       	  long timeout = this.timeout;
//
//       	  if(!StringUtils.isBlank(environment.getProperty("spring.redis2.timeout"))) timeout = Long.valueOf(environment.getProperty("spring.redis2.timeout"));
//
//       	  long shutDownTimeout = this.shutDownTimeout;
//
//       	  if(!StringUtils.isBlank(environment.getProperty("spring.redis2.shutDownTimeout"))) shutDownTimeout = Long.valueOf(environment.getProperty("spring.redis2.shutDownTimeout"));
//
//    		return getFactory0(nonPersistentRedis, nonPersistentRedisClusterConfig,lettuceperiod,timeout,shutDownTimeout);
//    	}
//    	LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(nonPersistentRedis).build();
//        return getFactory(nonPersistentRedisClusterConfig, clientConfiguration);
//    }
//
//
//
//    /**
//     * 配置第一个数据源的RedisTemplate
//     * 注意：这里指定使用名称=factory2 的 nonPersistentLettuceConnectionFactory
//     *
//     * @param nonPersistentLettuceConnectionFactory
//     * @return
//     */
//    @Bean("nonPersistentRedisTemplate")
//    public RedisTemplate secondaryRedisTemplate(@Qualifier("nonPersistentLettuceConnectionFactory") RedisConnectionFactory nonPersistentLettuceConnectionFactory) {
//        StringRedisTemplate template = new StringRedisTemplate();
//        template.setConnectionFactory(nonPersistentLettuceConnectionFactory);
//        return template;
////        return getRedisTemplate(nonPersistentLettuceConnectionFactory);
//    }
//
//
//    private RedisTemplate getRedisTemplate(RedisConnectionFactory factory) {
//    	 RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//         GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
//         redisTemplate.setKeySerializer(fastJsonRedisSerializer);
//         redisTemplate.setHashKeySerializer(fastJsonRedisSerializer);
//         redisTemplate.setValueSerializer(fastJsonRedisSerializer);
//         redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
//         redisTemplate.setConnectionFactory(factory);
//         return redisTemplate;
//    }
//
//}