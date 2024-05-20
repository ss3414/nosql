package com.demo.dao;

import com.demo.model.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {

    @Nonnull
    @Override
    Optional<User> findById(@Nonnull Integer id);

    @Nonnull
    @Override
    <S extends User> S save(@Nonnull S s);

    @Override
    void deleteById(@Nonnull Integer id);

}
