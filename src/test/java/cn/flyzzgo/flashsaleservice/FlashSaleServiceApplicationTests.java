package cn.flyzzgo.flashsaleservice;

import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderResult;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderTask;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashOrderTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class FlashSaleServiceApplicationTests {

    @Resource
    private FlashOrderTaskService flashOrderTaskService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        PlaceOrderTask placeOrderTask = new PlaceOrderTask();
        placeOrderTask.setTaskId(1L);
        placeOrderTask.setUserId(1L);
        placeOrderTask.setItemId(1L);
        placeOrderTask.setQuantity(2);
        flashOrderTaskService.submitTask(placeOrderTask);
    }

    @Test
    void test2() {
        Response response = flashOrderTaskService.getTaskResult(1L);
        if(response.isSuccess()) {
            System.out.println(((SingleResponse)response).getData());
        } else {
            System.out.println(response.getErrMessage());
        }
    }

    @Test
    void test3() {
        System.out.println(redisTemplate.getDefaultSerializer());
        System.out.println(redisTemplate.getValueSerializer());
        redisTemplate.opsForValue().set("test",new PlaceOrderResult());
        System.out.println(redisTemplate.opsForValue().get("test"));
    }
}
