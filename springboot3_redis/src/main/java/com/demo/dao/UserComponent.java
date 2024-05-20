package com.demo.dao;

import com.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheManager = "redisCacheManager", cacheNames = "user")
public class UserComponent {

    @Autowired
    private UserDao userDao;

    @Cacheable(key = "#id", unless = "#result == null")
    public User findById(Integer id) {
        return userDao.findById(id).orElseGet(() -> User.builder().build());
    }

    @CacheEvict(key = "#user.id", beforeInvocation = true)
    public User save(User user) {
        return userDao.save(user);
    }

    @CacheEvict(key = "#id")
    public void deleteById(Integer id) {
        userDao.deleteById(id);
    }

}
