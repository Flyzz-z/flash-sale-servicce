package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.FlashOrderDto;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderCmd;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import org.springframework.stereotype.Service;

/**
 * @author Flyzz
 */
public interface FlashOrderService {

    Response placeOrder(Long userId, PlaceOrderCmd placeOrderCmd) throws InterruptedException;

    Response cancelOrder(Long orderId);

    boolean saveFlashOrder(FlashOrderDto flashOrderDto);
}
