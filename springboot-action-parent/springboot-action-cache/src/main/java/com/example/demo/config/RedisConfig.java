package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
@EnableCaching
@PropertySource("classpath:application.properties")
public class RedisConfig {
    @Autowired
    private RedisConfiguration redisConfiguration;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisConfiguration.getHost());
        redisStandaloneConfiguration.setPort(redisConfiguration.getPort());
        JedisConnectionFactory jedisConnectionFactory =
                new JedisConnectionFactory(redisStandaloneConfiguration);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration.entryTtl(Duration.ofSeconds(600)).disableCachingNullValues();
        return redisCacheConfiguration;
    }

    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager build = RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration())
                .transactionAware()
                .build();
        return build;
    }

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Json???????????????
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // enableDefaultTyping ???????????????????????????????????????activateDefaultTyping
        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // String?????????
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key ???????????????
        template.setKeySerializer(stringRedisSerializer);

        // hash???key???????????????
        template.setHashKeySerializer(stringRedisSerializer);

        // value?????????????????????json????????????
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // value?????????????????????String????????????value ??????String??????????????????????????????json????????????
        // template.setValueSerializer(stringRedisSerializer);

        // hash???value??????????????????json????????????
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
