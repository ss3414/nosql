package com.demo.redis;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LuaScriptSupport {

    @Autowired
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<Long> deleteScript;

    /* 被@PostConstruct修饰的方法在整个Servlet生命周期只执行一次，即使多次实例化 */
    @PostConstruct
    public void init() {
        deleteScript = new DefaultRedisScript<>();
        deleteScript.setResultType(Long.class);
        deleteScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("delete.lua")));
    }

    public Long delete(String key, String val) {
        return (Long) redisTemplate.execute(deleteScript, Collections.singletonList(key), val);
    }

}
