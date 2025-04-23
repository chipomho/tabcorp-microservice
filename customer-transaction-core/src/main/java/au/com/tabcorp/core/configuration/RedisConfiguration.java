package au.com.tabcorp.core.configuration;

import au.com.tabcorp.common.BeanNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration()
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class})
public class RedisConfiguration {

    /**
     * Allow the reactive redis connection factory to be created automatically
     *
     * @param factory
     * @return
     */
    @Bean(BeanNames.Core.REDIS_TEMPLATE)
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(@Autowired()@Qualifier(BeanNames.Core.REDIS_CONNECTION_FACTORY)
                                                                       final ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, String> context = RedisSerializationContext
                .<String, String>newSerializationContext(RedisSerializer.string())
                .value(RedisSerializer.string())
                .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }


    @Bean(BeanNames.Core.REDIS_CONNECTION_FACTORY)
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(final Environment environment) {
        return new LettuceConnectionFactory(environment.getRequiredProperty("spring.redis.host",String.class),
                environment.getRequiredProperty("spring.redis.port", Integer.class));
    }

}
