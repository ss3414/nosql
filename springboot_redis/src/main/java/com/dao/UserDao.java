package com.dao;

import com.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@CacheConfig(cacheNames = "user")
public interface UserDao extends JpaRepository<User, Integer> {

    @Cacheable(key = "0")
    @Override
    Optional<User> findById(Integer id);

    @CacheEvict(key = "0", beforeInvocation = true)
    @Override
    <S extends User> S save(S s);

    @CacheEvict(key = "0", allEntries = true)
    @Override
    void deleteById(Integer id);

}
