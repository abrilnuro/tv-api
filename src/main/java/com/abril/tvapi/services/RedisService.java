package com.abril.tvapi.services;

import com.abril.tvapi.configuration.RedisConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, JSONObject> jsonRedisTemplate;

    @Autowired
    private RedisConfig redisConfig;

    public void saveValue(String key, JSONObject value) throws Exception {
        Assert.notNull(key, "key no debe ser nulo.");
        Assert.notNull(value, "value no debe ser nulo.");
        Assert.hasText(key, "key no debe ser vacía.");

        ValueOperations<String, JSONObject> jsonRedis = jsonRedisTemplate.opsForValue();
        Boolean redisIsAvalible = this.redisConfig.redisIsAvalible();

        if(redisIsAvalible) {
            jsonRedis.set(key, value, 30, TimeUnit.MINUTES);
        }else{
            throw new Exception("No fue posible guardar en redis.");
        }
    }

    public JSONObject getValue(String key) throws Exception {
        Assert.notNull (key, "key no debe ser null.");
        Assert.hasText(key, "key no debe ser vacía.");

        ValueOperations<String, JSONObject> jsonRedis = jsonRedisTemplate.opsForValue();
        Boolean redisIsAvalible = this.redisConfig.redisIsAvalible();

        JSONObject json = null;
        if(redisIsAvalible) {
            json = jsonRedis.get(key);
        }else {
            throw new Exception("No fue posible obtener valor de redis.");
        }

        return json;
    }
}
