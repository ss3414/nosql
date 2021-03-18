package com.listener;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    /*
     * ①自定义配置覆盖自动配置
     * ②参数来自已装配的Bean
     * */
    @Bean("myContainerFactory")
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory myContainerFactory = new SimpleRabbitListenerContainerFactory();
        myContainerFactory.setConnectionFactory(connectionFactory);
//        myContainerFactory.setConcurrentConsumers(10); /* 消费/最大线程数 */
//        myContainerFactory.setMaxConcurrentConsumers(10);
        myContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL); /* 消息手动处理 */
        return myContainerFactory;
    }

    @Autowired
    private MQListener2 MQListener2;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("queue");
        container.setMessageListener(MQListener2);
        return container;
    }

}
