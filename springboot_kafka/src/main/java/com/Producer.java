package com;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class Producer {

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public void send(String topic, Object data) {
        kafkaTemplate.send(topic, data);
    }

}
