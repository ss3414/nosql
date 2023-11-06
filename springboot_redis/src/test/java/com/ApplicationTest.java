package com;

import com.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /* Redis存储对象（对象序列化与反序列化） */
//    @Test
    public void test() {
        User user = new User(1, "name1", "pwd1"); /* User必须实现Serializable接口 */
        redisTemplate.opsForValue().set("name1", user);
        log.info(String.valueOf(redisTemplate.opsForValue().get("name1")));
    }

    /* Redis执行Lua脚本 */
    @Test
    public void test2() {
        String key = "key1";
        String val = "value1";
        redisTemplate.opsForValue().set(key, val);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("test.lua")));
        redisScript.setResultType(Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(key), val);
        log.info(String.valueOf(result));
    }

}
