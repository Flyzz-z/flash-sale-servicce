package cn.flyzzgo.flashsaleservice.service.impl;

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
    @Transactional
    public Response placeOrder(Long userId, PlaceOrderCmd placeOrderCmd) {

        RLock placeOrderUserLock = redissonClient.getLock(PLACE_ORDER_USER_LOCK_KEY + userId);
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
            FlashItemDto flashItemDto = (FlashItemDto) ((SingleResponse)response).getData();

            FlashOrderDto flashOrderDto = new FlashOrderDto();
            flashOrderDto.setActivityId(flashItemDto.getActivityId());
            flashOrderDto.setItemId(flashItemDto.getId());
            flashOrderDto.setItemTitle(flashItemDto.getItemTitle());
            flashOrderDto.setUserId(userId);
            flashOrderDto.setQuantity(placeOrderCmd.getQuantity());
            flashOrderDto.setFlashPrice(flashItemDto.getFlashPrice());
            flashOrderDto.setTotalAmount(placeOrderCmd.getTotalAmount());


            boolean preDecreaseStockSuccess = itemStockService.decreaseItemStock(flashItemDto.getId());
            if (!preDecreaseStockSuccess) {
                return Response.buildFailure(ErrorCode.CLIENT_ERROR);
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
            throw new BizException(ErrorCode.CLIENT_ERROR);
        } finally {
            placeOrderUserLock.unlock();
        }

        return null;
    }

    @Override
    public Response cancelOrder() {
        return null;
    }


    @Override
    public boolean saveFlashOrder(FlashOrderDto flashOrderDto) {
        FlashOrderDo flashOrderDo = FlashOrderConvertor.flashOrderDtoToFlashOrderDo(flashOrderDto);
        return flashOrderMapper.insert(flashOrderDo) == 1;
    }
}
