package com.demo;

import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@RestController
@SpringBootApplication
public class Kafka {

    public static void main(String[] args) {
        SpringApplication.run(Kafka.class, args);
    }

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    @Value("${kafka.topic.my-topic}")
    private String myTopic;

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @RequestMapping("/send")
    public Map send() {
//        kafkaTemplate.send(myTopic, "msg");
        String json = JSONObject.toJSONString(User.builder().id(2).name("name2").password("pwd2").build());
        kafkaTemplate.send(myTopic, json);
        return new LinkedHashMap();
    }

    @KafkaListener(topics = "${kafka.topic.my-topic}")
    public void receive(ConsumerRecord<String, Object> record) {
//        log.info("topic:{} partition:{} value:{}", record.topic(), record.partition(), record.value());
        User user = JSONObject.parseObject(record.value().toString(), User.class);
        log.info("user:{}", user);
    }

    @SneakyThrows
    @RequestMapping("/topics")
    public Map topics() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        AdminClient adminClient = AdminClient.create(properties);
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        log.info(adminClient.listTopics(listTopicsOptions).names().get().toString());
        return new LinkedHashMap();
    }

}
