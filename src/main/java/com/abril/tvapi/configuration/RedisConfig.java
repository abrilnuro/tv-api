package com.abril.tvapi.configuration;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;


@Configuration
@ComponentScan
public class RedisConfig {

  @Autowired
  private GlobalConfig globalConfig;

  @Bean
  @Primary
  LettuceConnectionFactory jedisConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(this.globalConfig.getRedisHost(),
                                                                         this.globalConfig.getRedisPort()));
  }

  @Bean
  public RedisTemplate<String, JSONObject> jsonRedisTemplate() {
    RedisTemplate<String, JSONObject> template = new RedisTemplate<String, JSONObject>();
    template.setConnectionFactory(jedisConnectionFactory());
    template.setKeySerializer( new StringRedisSerializer() );
    template.setHashValueSerializer( new GenericToStringSerializer< JSONObject >( JSONObject.class ) );
    template.setValueSerializer( new GenericToStringSerializer< JSONObject >( JSONObject.class ) );
    return template;
  }

  @Bean
  RedisTemplate< String, Boolean > boolRedisTemplate() {
    final RedisTemplate< String, Boolean > template =  new RedisTemplate< String, Boolean >();
    template.setConnectionFactory( jedisConnectionFactory() );
    template.setKeySerializer( new StringRedisSerializer() );
    template.setHashValueSerializer( new GenericToStringSerializer< Boolean >( Boolean.class ) );
    template.setValueSerializer( new GenericToStringSerializer< Boolean >( Boolean.class ) );
    return template;
  }

  @Bean
  RedisTemplate< String, Integer[] > integerArrayRedisTemplate() {
    final RedisTemplate< String, Integer[] > template =  new RedisTemplate< String, Integer[] >();
    template.setConnectionFactory( jedisConnectionFactory() );
    template.setKeySerializer( new StringRedisSerializer() );
    template.setHashValueSerializer( new GenericToStringSerializer< Integer[] >( Integer[].class ) );
    template.setValueSerializer( new GenericToStringSerializer< Integer[] >( Integer[].class ) );
    return template;
  }

  @Bean
  public RedisTemplate<String, Long> getLongRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    final RedisTemplate<String,Long> redisTemplate = new RedisTemplate<String, Long>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Long>(Long.class));
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  public Boolean redisIsAvalible(){
    try {
      ValueOperations <String, Boolean> booleanRedis = boolRedisTemplate().opsForValue();
      booleanRedis.set( "test", true, 1, TimeUnit.DAYS );
      return true;
    }catch ( Exception ex ){
      return false;
    }
  }

}
