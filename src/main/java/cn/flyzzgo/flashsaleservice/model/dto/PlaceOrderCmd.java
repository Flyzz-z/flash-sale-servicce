package cn.flyzzgo.flashsaleservice.model.dto;

import lombok.Data;

/**
 * @author Flyzz
 */
@Data
public class PlaceOrderCmd {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 下单商品数量
     */
    private Integer quantity;

    /**
     * 总金额
     */
    private Long totalAmount;
}
