package com.panda.sport.order.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @auth: YK
 * @Description:redis装载配置
 * @Date:2020/5/10 15:13
 */
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisTemplateConfig {

    private final LettuceConnectionFactory factory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

        redisTemplate.setKeySerializer(fastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(fastJsonRedisSerializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
  /*  @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,  MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅多个频道
        container.addMessageListener(listenerAdapter,new PatternTopic("MERCHANT_FILE_TOPIC"));
        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        container.setTopicSerializer(fastJsonRedisSerializer);
        return container;
    }*/

    /**
     * 监听一个频道
     * @param receiver
     * @return
     */
 /*   @Bean
    MessageListenerAdapter listenerAdapter(OrderFileMessageReceive receiver){
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“MessageReceiveTwo ”
        return new MessageListenerAdapter(receiver,"getMessage");
    }*/
}
