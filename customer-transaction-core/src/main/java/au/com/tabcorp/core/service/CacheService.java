package au.com.tabcorp.core.service;

import au.com.tabcorp.common.BeanNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service(BeanNames.Core.CACHE_SERVICE)
public class CacheService {

    protected final ReactiveRedisTemplate<String, String> redisTemplate;

    protected final ObjectMapper objectMapper;

    public CacheService(@Autowired()@Qualifier(BeanNames.Core.REDIS_TEMPLATE) final ReactiveRedisTemplate<String, String> redisTemplate,
                        @Autowired()@Qualifier(BeanNames.Common.OBJECT_MAPPER)final ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<String> get(final String key){
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> set(final String key, final String value){
        return redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(30));
    }

    public Mono<Boolean> set(final String key, final Object value) {
        try {
            return redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), Duration.ofMinutes(30));
        }
        catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    public <T> Mono<T> get(final String key, final Class<T> clazz){
        try {
            return redisTemplate.opsForValue().get(key).handle((t, sink) -> {
                try {
                    sink.next(objectMapper.readValue(t, clazz));
                } catch (JsonProcessingException e) {
                    sink.error(new RuntimeException(e));
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

}
