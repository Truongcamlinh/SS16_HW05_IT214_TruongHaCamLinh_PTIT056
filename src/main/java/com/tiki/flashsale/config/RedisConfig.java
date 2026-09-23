package com.tiki.flashsale.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki.flashsale.dto.FlashSaleProductDTO;
import java.time.Duration;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.*;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig implements CachingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    private final RedisConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;
    private final Duration ttl;

    public RedisConfig(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper,
                       @Value("${flash-sale.cache-ttl:120s}") Duration ttl) {
        this.connectionFactory = connectionFactory;
        this.objectMapper = objectMapper;
        this.ttl = ttl;
    }

    @Bean @Override public CacheManager cacheManager() {
        Jackson2JsonRedisSerializer<FlashSaleProductDTO> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, FlashSaleProductDTO.class);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl).disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
    }

    @Bean @Override public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            public void handleCacheGetError(RuntimeException e, Cache c, Object key) {
                log.error("Redis GET lỗi key={}; chuyển sang degraded mode: {}", key, e.getMessage());
            }
            public void handleCachePutError(RuntimeException e, Cache c, Object key, Object value) {
                log.error("Redis PUT lỗi key={}; dữ liệu DB vẫn được trả: {}", key, e.getMessage());
            }
            public void handleCacheEvictError(RuntimeException e, Cache c, Object key) {
                log.error("Redis EVICT lỗi key={}: {}", key, e.getMessage());
            }
            public void handleCacheClearError(RuntimeException e, Cache c) {
                log.error("Redis CLEAR lỗi: {}", e.getMessage());
            }
        };
    }
}
