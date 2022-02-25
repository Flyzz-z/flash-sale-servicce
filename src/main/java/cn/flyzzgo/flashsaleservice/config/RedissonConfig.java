package cn.flyzzgo.flashsaleservice.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Flyzz
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String redissonHost;

    @Value("${spring.redis.port}")
    private String redissonPort;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redissonHost+redissonPort);
        return Redisson.create(config);
    }
}
