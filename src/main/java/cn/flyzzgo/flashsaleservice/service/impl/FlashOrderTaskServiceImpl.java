package cn.flyzzgo.flashsaleservice.service.impl;


import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderResult;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderTask;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashOrderTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Flyzz
 */
@Service
@Slf4j
public class FlashOrderTaskServiceImpl implements FlashOrderTaskService {

    private final String routingKey = "order.task";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean submitTask(PlaceOrderTask placeOrderTask) {

        try {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            messageProperties.setDeliveryTag(placeOrderTask.getTaskId());
            messageProperties.setExpiration("70000");
            Message message = rabbitTemplate.getMessageConverter().toMessage(placeOrderTask,messageProperties);
            CorrelationData correlationData = new CorrelationData();
            rabbitTemplate.convertAndSend("flashOrderTask.exchange", "order.task", message, correlationData);
            CorrelationData.Confirm confirm = correlationData.getFuture().get(5, TimeUnit.SECONDS);
            if (!confirm.isAck()) {
                log.info("消息发送失败 {}",confirm.getReason());
                throw new RuntimeException("消息发送失败");
            } else {
                log.info("消息发送成功");
            }
        } catch (Exception e) {
            log.info("下单任务提交失败 {} {}", placeOrderTask.getTaskId(), e.getCause());
            return false;
        }

        return true;
    }

    @Override
    public Response getTaskResult(Long taskId) {
        PlaceOrderResult placeOrderResult = (PlaceOrderResult) redisTemplate.opsForValue().get("FlashOrderTask"+taskId);
        if (placeOrderResult == null) {
            return Response.buildFailure(ErrorCode.TASK_NOT_EXIST);
        }
        return SingleResponse.of(placeOrderResult);
    }
}
