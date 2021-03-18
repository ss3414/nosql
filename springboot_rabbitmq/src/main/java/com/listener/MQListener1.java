//package com.listener;
//
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MQListener1 {
//
////    @RabbitListener(queues = "queue")
////    private void test(Message message) {
////        System.out.println(new String(message.getBody()));
////    }
//
//    /* 手动消费 */
//    @RabbitListener(containerFactory = "myContainerFactory", queues = "queue")
//    private void test2(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
//        try {
//            System.out.println(message);
//            if ("".equals(message)) {
//                channel.basicAck(deliveryTag, false);
//            } else {
//                channel.basicNack(deliveryTag, false, true); /* 拒绝后返回队列 */
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
