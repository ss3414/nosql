package com;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @Value("${kafka.topic.my-topic}")
    private String myTopic;

    @KafkaListener(topics = "${kafka.topic.my-topic}")
    public void receive(ConsumerRecord<Integer, String> record) {
        log.info("topic:{} partition:{} value:{}", record.topic(), record.partition(), record.value());
    }

}
