package com.demo;

import com.demo.dao.UserDao;
import com.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@EnableCaching
@RestController
@SpringBootApplication
public class Redis {

    public static void main(String[] args) {
        SpringApplication.run(Redis.class, args);
    }

    @Autowired
    private UserDao userDao;

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    @RequestMapping("/find")
    public String find() {
        User user = userDao.findById(1).get();
        return user.toString();
    }

    @RequestMapping("/save")
    public Map save() {
        userDao.save(User.builder().id(1).name("name").build());
        return new LinkedHashMap();
    }

    @RequestMapping("/delete")
    public Map delete() {
        userDao.deleteById(2);
        return new LinkedHashMap();
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /* Redis存储对象（对象序列化与反序列化） */
    @RequestMapping("/test")
    public Map test() {
        User user = new User(1, "name1", "pwd1"); /* User必须实现Serializable接口 */
        redisTemplate.opsForValue().set("name1", user);
        log.info(String.valueOf(redisTemplate.opsForValue().get("name1")));
        return new LinkedHashMap();
    }

    /* Redis执行Lua脚本 */
    @RequestMapping("/test2")
    public Map test2() {
        String key = "key1";
        String val = "value1";
        redisTemplate.opsForValue().set(key, val);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("test.lua")));
        redisScript.setResultType(Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(key), val);
        log.info(String.valueOf(result));
        return new LinkedHashMap();
    }

}
