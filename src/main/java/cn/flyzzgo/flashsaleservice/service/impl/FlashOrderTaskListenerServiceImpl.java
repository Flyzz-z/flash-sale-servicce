package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.dto.FlashOrderDto;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderResult;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderTask;
import cn.flyzzgo.flashsaleservice.model.exception.BizException;
import cn.flyzzgo.flashsaleservice.service.FlashItemService;
import cn.flyzzgo.flashsaleservice.service.FlashOrderService;
import cn.flyzzgo.flashsaleservice.service.FlashOrderTaskListenerService;
import cn.flyzzgo.flashsaleservice.utils.OrderIdGenerator;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Flyzz
 */
@Service
@Slf4j
@ConditionalOnProperty(name="flash.order.type",havingValue = "async")
public class FlashOrderTaskListenerServiceImpl implements FlashOrderTaskListenerService {

    @Resource
    private FlashOrderService flashOrderService;

    @Resource
    private FlashItemService flashItemService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    @RabbitListener(containerFactory = "rabbitListenerContainerFactory", queues = "order.task",ackMode = "MANUAL")
    @Transactional(rollbackFor = Exception.class)
    public void handleOrderTask(Message message, Channel channel)  {
        PlaceOrderTask task = (PlaceOrderTask) rabbitTemplate.getMessageConverter().fromMessage(message);
        log.info("收到下单任务 {}", task.getTaskId());
        if(redisTemplate.opsForValue().get("FlashOrderResult" + task.getTaskId()) != null) {
            log.info("任务 {} 已经处理过了", task.getTaskId());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        FlashOrderDto flashOrderDto = new FlashOrderDto();
        flashOrderDto.setId(OrderIdGenerator.nextOrderId());
        flashOrderDto.setUserId(task.getUserId());
        flashOrderDto.setFlashPrice(task.getFlashPrice());
        flashOrderDto.setItemId(task.getItemId());
        flashOrderDto.setItemTitle(task.getItemTitle());
        flashOrderDto.setQuantity(task.getQuantity());
        flashOrderDto.setTotalAmount(task.getTotalAmount());
        flashOrderDto.setStatus(1);
        PlaceOrderResult placeOrderResult = new PlaceOrderResult()
                .setTaskId(task.getTaskId())
                .setMsg("下单成功")
                .setSuccess(true);

        try {
            if (!flashItemService.decreaseItemStock(flashOrderDto.getItemId(), flashOrderDto.getQuantity())) {
                placeOrderResult.setMsg("库存不足").setSuccess(false);
                flashOrderDto.setStatus(2);
            }
            flashOrderService.saveFlashOrder(flashOrderDto);
            placeOrderResult.setOrderId(flashOrderDto.getId());
        } catch (Exception e) {
            log.info("下单失败 {} {}", task.getTaskId(), e.getMessage());
            placeOrderResult.setMsg("服务错误").setSuccess(false);
            throw new BizException(ErrorCode.SERVER_ERROR);
        } finally {
            redisTemplate.opsForValue().set("FlashOrderTask"+placeOrderResult.getTaskId(), placeOrderResult,2, TimeUnit.HOURS);
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}