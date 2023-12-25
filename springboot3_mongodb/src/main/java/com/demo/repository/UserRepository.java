package com.demo.repository;

import com.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {

}
