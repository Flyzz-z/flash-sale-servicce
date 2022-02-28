package cn.flyzzgo.flashsaleservice.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String getStringValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
