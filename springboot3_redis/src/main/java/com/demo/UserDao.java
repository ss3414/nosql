package com.demo;

import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@CacheConfig(cacheNames = "user")
public interface UserDao extends JpaRepository<User, Integer> {

    @Cacheable(key = "0")
    @Nonnull
    @Override
    Optional<User> findById(@Nonnull Integer id);

    @CacheEvict(key = "0", beforeInvocation = true)
    @Nonnull
    @Override
    <S extends User> S save(@Nonnull S s);

    @CacheEvict(key = "0", allEntries = true)
    @Override
    void deleteById(@Nonnull Integer id);

}
