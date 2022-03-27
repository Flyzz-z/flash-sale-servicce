package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.DecreaseItemStockResult;
import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.dto.FlashOrderDto;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderCmd;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderTask;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.*;
import cn.flyzzgo.flashsaleservice.utils.OrderIdGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Flyzz
 */
@Service
@ConditionalOnProperty(name="flash.order.type",havingValue = "async")
public class AsyncFlashOrderServiceImpl implements FlashOrderService {

    private final String PLACE_ORDER_USER_LOCK_KEY = "flash-sale-service.placeOrderUserLockKey";

    @Resource
    private FlashActivityService flashActivityService;

    @Resource
    private FlashItemService flashItemService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ItemStockService itemStockService;

    @Resource
    private FlashOrderTaskService flashOrderTaskService;


    @Override
    public Response placeOrder(Long userId, PlaceOrderCmd placeOrderCmd) {
        RLock placeOrderUserLock = redissonClient.getLock(PLACE_ORDER_USER_LOCK_KEY + userId);
        DecreaseItemStockResult preDecreaseStockSuccess;
        Long taskId;
        try {
            if(!placeOrderUserLock.tryLock(5,4, TimeUnit.SECONDS)) {
                return Response.buildFailure(ErrorCode.CLIENT_ERROR);
            }

            if (!flashActivityService.isAllowPlaceOrder(placeOrderCmd.getActivityId())) {
                return Response.buildFailure(ErrorCode.CLIENT_ERROR);
            }

            if (!flashItemService.isAllowPlaceOrder(placeOrderCmd.getItemId())) {
                return Response.buildFailure(ErrorCode.CLIENT_ERROR);
            }

            Response response = flashItemService.getFlashItemById(placeOrderCmd.getItemId());
            if (!response.isSuccess()) {
                return response;
            }
            FlashItemDto flashItemDto = (FlashItemDto) ((SingleResponse) response).getData();

            if(!flashItemDto.getActivityId().equals(placeOrderCmd.getActivityId())) {
                return Response.buildFailure(ErrorCode.CLIENT_ERROR);
            }

            preDecreaseStockSuccess = itemStockService.decreaseItemStock(flashItemDto.getId(), placeOrderCmd.getQuantity());
            if (preDecreaseStockSuccess.equals(DecreaseItemStockResult.NOT_ENOUGH_STOCK)) {
                return Response.buildFailure(ErrorCode.NOT_ENOUGH_STOCK);
            } else if (preDecreaseStockSuccess.equals(DecreaseItemStockResult.DECREASE_ERROR)) {
                return Response.buildFailure(ErrorCode.SERVER_ERROR);
            }


            taskId = OrderIdGenerator.nextOrderTaskId();
            PlaceOrderTask placeOrderTask = new PlaceOrderTask();
            placeOrderTask.setTaskId(taskId);
            placeOrderTask.setQuantity(placeOrderCmd.getQuantity());
            placeOrderTask.setItemId(placeOrderCmd.getItemId());
            placeOrderTask.setFlashPrice(flashItemDto.getFlashPrice());
            placeOrderTask.setItemTitle(flashItemDto.getItemTitle());
            placeOrderTask.setTotalAmount(placeOrderCmd.getQuantity() * flashItemDto.getFlashPrice());
            placeOrderTask.setActivityId(placeOrderCmd.getActivityId());

            //插入任务,两次尝试
            if(!flashOrderTaskService.submitTask(placeOrderTask)) {
                if(!flashOrderTaskService.submitTask(placeOrderTask)) {
                    return Response.buildFailure(ErrorCode.SERVER_ERROR);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            placeOrderUserLock.unlock();
        }

        return Response.buildSuccess();
    }

    @Override
    public Response cancelOrder(Long orderId) {
        return null;
    }

    @Override
    public boolean saveFlashOrder(FlashOrderDto flashOrderDto) {
        return false;
    }
}
