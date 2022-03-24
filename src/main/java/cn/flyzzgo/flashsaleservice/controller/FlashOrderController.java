package cn.flyzzgo.flashsaleservice.controller;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderCmd;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.service.FlashOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Flyzz
 */
@RestController
@RequestMapping("/flash-sale/order")
public class FlashOrderController {

    @Resource
    private FlashOrderService flashOrderService;

    @PostMapping("/placeOrder")
    public Response placeOrder(@RequestBody PlaceOrderCmd placeOrderCmd) throws InterruptedException {

        // TODO: 2022/2/26 鉴权
        if (!placeOrderCmd.checkValid()) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashOrderService.placeOrder(new Random().nextLong(), placeOrderCmd);
    }
}
