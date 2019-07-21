package com.abril.tvapi.services;

import com.abril.tvapi.configuration.RedisConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService extends RedisAutoConfiguration {

    private JSONObject resultKey;

    private final static String PONG_RESPONSE = "PONG";

    @Autowired
    private RedisTemplate<String, JSONObject> jsonRedisTemplate;

    @Autowired
    private RedisConfig redisConfig;

    final private Long time = 1L;
    final private TimeUnit timeUnit = TimeUnit.MINUTES;

    public static final Gson GSON = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create();

    public void set(String key, JSONObject value, Long time, TimeUnit timeUnit) {
        this.jsonRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public void setIfIsAvailable(String key, JSONObject value) throws Exception {
        if (this.isAvailable()) {
            this.jsonRedisTemplate.opsForValue().set(key, value);
        }
    }

    public void setIfIsAvailable(String key, JSONObject value, Long time) throws Exception {
        if (this.isAvailable()) {
            this.jsonRedisTemplate.opsForValue().set(key, value, time, timeUnit);
        }
    }

    public RedisService set(String key, JSONObject value) {
        ValueOperations<String, JSONObject> jsonRedis = jsonRedisTemplate.opsForValue();
        jsonRedis.set(key, value);
        return this;
    }

    public RedisService set(String key, JSONObject value, Long time) {
        ValueOperations<String, JSONObject> jsonRedis = jsonRedisTemplate.opsForValue();
        jsonRedis.set(key, value, time, timeUnit);
        return this;
    }


    public RedisService get(String key) {
        this.resultKey =  this.jsonRedisTemplate.opsForValue().get(key);
        return this;
    }

    Boolean isAvailable() throws Exception {
        try {
            String pingResponse = redisConfig.jsonRedisTemplate().getConnectionFactory().getConnection().ping();
            if (pingResponse == null) {
                return false;
            }
            return pingResponse.equals(PONG_RESPONSE);
        } catch (Exception ex) {
            throw new Exception("Ocurrio un error al conectar a redis.");
        }
    }

    public <T> T toCast(Class<T> clase) {
        if (this.resultKey == null) {
            return null;
        }
        return GSON.fromJson(this.resultKey.toString(), clase);
    }

    public void setIfIsAvailable(String key, Object object, Long time) throws Exception {
        String json = GSON.toJson(object);
        JSONObject result = new JSONObject(json);
        this.setIfIsAvailable(key, result, time);
    }

    public void setIfIsAvailable(String key, Object object) throws Exception {
        String json = GSON.toJson(object);
        JSONObject result = new JSONObject(json);
        this.setIfIsAvailable(key, result);
    }

    public JSONObject toJSon() {
        return resultKey;
    }

}
