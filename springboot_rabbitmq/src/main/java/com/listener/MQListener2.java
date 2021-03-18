package com.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class MQListener2 implements ChannelAwareMessageListener {

    /* fixme 可以重复投递但是会报错 */
    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String msg = new String(message.getBody());
            System.out.println(msg);
            if ("".equals(msg)) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
