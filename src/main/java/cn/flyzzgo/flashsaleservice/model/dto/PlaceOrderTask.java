package cn.flyzzgo.flashsaleservice.model.dto;

import lombok.Data;

/**
 * @author Flyzz
 */
@Data
public class PlaceOrderTask {

    /**
     *  任务id
     */
    private Long taskId;

    private Long userId;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 秒杀品标题
     */
    private String itemTitle;

    /**
     * 秒杀价
     */
    private Long flashPrice;

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
