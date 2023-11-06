package com;

import com.dao.UserDao;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@EnableCaching
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

}
