package com;

import com.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /* Redis最简示例 */
    //    @Test
    public void test() {
        stringRedisTemplate.opsForValue().set("key1", "value1");
        System.out.println(stringRedisTemplate.opsForValue().get("key1"));
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /* Redis存储对象（对象序列化与反序列化） */
    @Test
    public void test2() {
        User user = new User(1, "name1", "pwd1"); /* User必须实现Serializable接口 */
        redisTemplate.opsForValue().set("name1", user);
        System.out.println(redisTemplate.opsForValue().get("name1"));
    }

}
