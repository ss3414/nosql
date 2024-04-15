package com.demo;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
        /* opsForValue/opsForHash通过相同的前缀取出来的是不同值 */
        Map<String, Object> map = new LinkedHashMap();
        map.put("data", 100);
        stringRedisTemplate.opsForValue().set("nosql:redis:test", JSON.toJSONString(map));
        stringRedisTemplate.opsForHash().put("nosql:redis", "test", JSON.toJSONString(map));
    }

    //    @Test
    void test2() {
        DefaultRedisScript<String> demoScript = new DefaultRedisScript<>();
        demoScript.setResultType(String.class);
        demoScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("demo.lua")));

        String key = "1";
        String val = "2";
        String result = stringRedisTemplate.execute(demoScript, Collections.singletonList(key), val);
        System.out.println(result);
    }

}
