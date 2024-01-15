package com.demo;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    @RequestMapping("/find")
    public String find() {
        User user = userDao.findById(1).orElseGet(() -> User.builder().build());
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

    private DefaultRedisScript<Long> redisScript;

    /* 被@PostConstruct修饰的方法在整个Servlet生命周期只执行一次，即使多次实例化 */
    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("test.lua")));
    }

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
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(key), val);
        log.info(String.valueOf(result));
        return new LinkedHashMap();
    }

    @SneakyThrows
    public void lock(String key) {
        String val = UUID.randomUUID().toString();
        /* 设置3秒后释放锁 */
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, val, 3, TimeUnit.SECONDS);
        if (Objects.nonNull(flag) && flag) {
            log.info("{} 加锁成功", key);
            /* 业务执行需要5秒，则5秒后才会释放 */
            TimeUnit.SECONDS.sleep(5);
            String lockVal = (String) redisTemplate.opsForValue().get("key");
            Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(key), lockVal);
            log.info("{} 解锁成功，结果为 {}", key, result);
        } else {
            log.info("{} 加锁失败", key);
        }
    }

    /* 搭配Lua脚本实现分布式锁 */
    @Async
    @Scheduled(cron = "*/3 * * * * *")
    public void test3() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        log.info("{} 执行业务", time);
        lock("test"); /* 相同的key用同一把锁 */
    }

}
