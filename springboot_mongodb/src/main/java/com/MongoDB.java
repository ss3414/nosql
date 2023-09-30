package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class MongoDB {

    public static void main(String[] args) {
        SpringApplication.run(MongoDB.class, args);
    }

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

}
