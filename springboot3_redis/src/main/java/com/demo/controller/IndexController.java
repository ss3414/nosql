package com.demo.controller;

import com.demo.dao.UserComponent;
import com.demo.model.User;
import com.demo.ratelimit.RateLimit;
import com.demo.redis.LuaScriptSupport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private UserComponent userComponent;

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    @RequestMapping("/find")
    public String find() {
        User user = userComponent.findById(1);
        return user.toString();
    }

    @RequestMapping("/save")
    public Map save() {
        userComponent.save(User.builder().id(1).name("name").build());
        return new LinkedHashMap();
    }

    @RequestMapping("/delete")
    public Map delete() {
        userComponent.deleteById(1);
        return new LinkedHashMap();
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LuaScriptSupport luaScriptSupport;

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
        Long result = luaScriptSupport.delete(key, val);
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
            Long result = luaScriptSupport.delete(key, val);
            log.info("{} 解锁成功，结果为 {}", key, result);
        } else {
            log.info("{} 加锁失败", key);
        }
    }

    /* 搭配Lua脚本实现分布式锁 */
    @Async
//    @Scheduled(cron = "*/3 * * * * *")
    public void test3() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        log.info("{} 执行业务", time);
        lock("test"); /* 相同的key用同一把锁 */
    }

    /* 注解实现分布式限流 */
    @RateLimit
    @SneakyThrows
    @RequestMapping("/test4")
    public Map test4(@RequestParam(defaultValue = "1") Integer id) {
        log.info("test4-{} 执行业务", id);
        /* 注意，若业务时间超过RateLimit默认值，则加锁时间也会延长 */
        TimeUnit.SECONDS.sleep(5);
        return new LinkedHashMap();
    }

    @RequestMapping("/test5")
    public Map test5() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1/test4?id=1"))
                .build();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Runnable runnable = () -> {
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            future.whenComplete((response, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                }
            }).join();
        };
        executor.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.SECONDS);
        return new LinkedHashMap();
    }

}
