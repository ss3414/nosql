package com;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String exchange = "exchange.direct";
    private String queue = "queue";
    private String routingKey = "routingKey";

    /*
     * ①Exchange：交换机，接收消息转发给Queue
     * ②Binding：Exchange和Queue之间的虚拟连接
     * ③RoutingKey：Exchange根据RoutingKey投递给Queue
     * ④Queue：消息队列
     * */
//    @Test
    public void test() {
        amqpAdmin.declareExchange(new DirectExchange(exchange));
        amqpAdmin.declareQueue(new Queue(queue, true));
        amqpAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, exchange, routingKey, null));

        QueueInformation queueInformation = amqpAdmin.getQueueInfo(queue);
    }

    @Test
    public void test2() {
        rabbitTemplate.convertAndSend(exchange, routingKey, "message");
    }

}
