package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.DecreaseItemStockResult;
import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.mapper.FlashOrderMapper;
import cn.flyzzgo.flashsaleservice.model.convertor.FlashOrderConvertor;
import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.dto.FlashOrderDto;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderCmd;
import cn.flyzzgo.flashsaleservice.model.entity.FlashOrderDo;
import cn.flyzzgo.flashsaleservice.model.exception.BizException;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import cn.flyzzgo.flashsaleservice.service.FlashItemService;
import cn.flyzzgo.flashsaleservice.service.FlashOrderService;
import cn.flyzzgo.flashsaleservice.service.ItemStockService;
import cn.flyzzgo.flashsaleservice.utils.OrderIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Flyzz
 */
@Service
@Slf4j
public class FlashOrderServiceImpl implements FlashOrderService {

    private final String PLACE_ORDER_USER_LOCK_KEY = "flash-sale-service.placeOrderUserLockKey";

    @Resource
    private FlashOrderMapper flashOrderMapper;

    @Resource
    private FlashActivityService flashActivityService;

    @Resource
    private FlashItemService flashItemService;

    @Resource
    private ItemStockService itemStockService;

    @Resource
    private RedissonClient redissonClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response placeOrder(Long userId, PlaceOrderCmd placeOrderCmd) {

        RLock placeOrderUserLock = redissonClient.getLock(PLACE_ORDER_USER_LOCK_KEY + userId);
        Long orderId;
        DecreaseItemStockResult preDecreaseStockSuccess = DecreaseItemStockResult.DECREASE_ERROR;
        FlashOrderDto flashOrderDto = new FlashOrderDto();
        try {
            if (!placeOrderUserLock.tryLock(5, 5, TimeUnit.SECONDS)) {
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
            orderId = OrderIdGenerator.nextOrderId();


            flashOrderDto.setId(orderId);
            flashOrderDto.setActivityId(flashItemDto.getActivityId());
            flashOrderDto.setItemId(flashItemDto.getId());
            flashOrderDto.setItemTitle(flashItemDto.getItemTitle());
            flashOrderDto.setUserId(userId);
            flashOrderDto.setQuantity(placeOrderCmd.getQuantity());
            flashOrderDto.setFlashPrice(flashItemDto.getFlashPrice());
            flashOrderDto.setTotalAmount(flashItemDto.getFlashPrice() * placeOrderCmd.getQuantity());


            preDecreaseStockSuccess = itemStockService.decreaseItemStock(flashItemDto.getId(), placeOrderCmd.getQuantity());
            if (preDecreaseStockSuccess.equals(DecreaseItemStockResult.NOT_ENOUGH_STOCK)) {
                return Response.buildFailure(ErrorCode.NOT_ENOUGH_STOCK);
            } else if(preDecreaseStockSuccess.equals(DecreaseItemStockResult.DECREASE_ERROR)) {
                return Response.buildFailure(ErrorCode.SERVER_ERROR);
            }

            boolean decreaseStockSuccess = flashItemService.decreaseItemStock(flashItemDto.getId(), placeOrderCmd.getQuantity());
            if (!decreaseStockSuccess) {
                return Response.buildFailure(ErrorCode.CLIENT_ERROR);
            }

            boolean saveOrderSuccess = this.saveFlashOrder(flashOrderDto);
            if (!saveOrderSuccess) {
                throw new BizException(ErrorCode.CLIENT_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if(preDecreaseStockSuccess.equals(DecreaseItemStockResult.DECREASE_SUCCESS)) {
                boolean recoverStockSuccess = itemStockService.increaseItemStock(flashOrderDto.getItemId(),flashOrderDto.getQuantity());
                if(!recoverStockSuccess) {
                    log.info("恢复预扣库存失败:{}",flashOrderDto);
                }
            }
            throw new BizException(ErrorCode.CLIENT_ERROR);
        } finally {
            placeOrderUserLock.unlock();
        }

        return SingleResponse.of(orderId);
    }

    @Override
    public Response cancelOrder(Long orderId) {
        FlashOrderDo flashOrderDo = flashOrderMapper.selectById(orderId);
        boolean recoverItemStockSuccess = flashItemService.increaseItemStock(flashOrderDo.getItemId(),flashOrderDo.getQuantity());
        if(!recoverItemStockSuccess) {
            return Response.buildFailure(ErrorCode.SERVER_ERROR);
        }

        boolean recoverItemStockCacheSuccess = itemStockService.increaseItemStock(flashOrderDo.getItemId(),flashOrderDo.getQuantity());
        if(!recoverItemStockCacheSuccess) {
            return Response.buildFailure(ErrorCode.SERVER_ERROR);
        }
        return Response.buildSuccess();
    }


    @Override
    public boolean saveFlashOrder(FlashOrderDto flashOrderDto) {
        FlashOrderDo flashOrderDo = FlashOrderConvertor.flashOrderDtoToFlashOrderDo(flashOrderDto);
        return flashOrderMapper.insert(flashOrderDo) == 1;
    }


}
