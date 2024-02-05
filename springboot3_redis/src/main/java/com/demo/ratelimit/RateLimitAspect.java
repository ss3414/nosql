package com.demo.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.demo.ratelimit.RateLimit)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        RateLimit annotation = AnnotationUtils.findAnnotation(method, RateLimit.class);

        String key = String.format("%s-%s", method.getName(), args[0]);
        String val = UUID.randomUUID().toString();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, val, annotation.timeout(), TimeUnit.SECONDS);
        if (Objects.nonNull(flag) && flag) {
            log.info("{} 加锁成功", key);
            return point.proceed();
        } else {
            log.info("{} 加锁失败", key);
            return null;
        }
    }

}
