package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderCmd;
import cn.flyzzgo.flashsaleservice.model.response.Response;

/**
 * @author Flyzz
 */
public interface FlashOrderService {

    Response placeOrder(PlaceOrderCmd placeOrderCmd);

    Response cancelOrder();
}
