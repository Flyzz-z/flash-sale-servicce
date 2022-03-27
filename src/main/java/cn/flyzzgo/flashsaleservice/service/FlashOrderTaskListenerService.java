package cn.flyzzgo.flashsaleservice.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author Flyzz
 */
public interface FlashOrderTaskListenerService {

    /**
     * 消息监听
     * @param task
     * @param channel
     */
    void handleOrderTask(Message task, Channel channel);
}
