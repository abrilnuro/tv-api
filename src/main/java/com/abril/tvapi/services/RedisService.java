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

    public void save(String key, String value){
        Assert.notNull(key, "La llave no debe ser nulo.");
        Assert.notNull(value, "El valor no debe ser nulo.");
        Assert.hasText(key, "La llave no debe ser vacía.");
        Assert.hasText(value, "El valor no debe ser vacía.");

        ValueOperations<String, JSONObject> jsonRedis = jsonRedisTemplate.opsForValue();
        Boolean redisIsAvalible = this.redisConfig.redisIsAvalible();

        if(redisIsAvalible) {
            JSONObject informacion = jsonRedis.get(key);

            if (informacion == null) {
                jsonRedis.set(key, new JSONObject(value), 30, TimeUnit.MINUTES);
            }
        }
    }

    public JSONObject get(String key){
        Assert.notNull (key, "La llave no debe ser null.");
        Assert.hasText(key, "El valor no debe ser vacía.");

        ValueOperations<String, JSONObject> jsonRedis = jsonRedisTemplate.opsForValue();
        Boolean redisIsAvalible = this.redisConfig.redisIsAvalible();

        JSONObject json = null;
        if(redisIsAvalible) {
            json = jsonRedis.get(key);
        }

        return json;
    }
}
