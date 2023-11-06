package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class Kafka {

    public static void main(String[] args) {
        SpringApplication.run(Kafka.class, args);
    }

    @Value("${kafka.topic.my-topic}")
    private String myTopic;

    @Autowired
    private Producer producer;

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    /* fixme docker kafka未成功 */
    @RequestMapping("/send")
    public Map send() {
        producer.send(myTopic, "msg");
        return new LinkedHashMap();
    }

}
