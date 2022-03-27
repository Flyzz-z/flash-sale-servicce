package cn.flyzzgo.flashsaleservice.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Flyzz
 */
@Data
@Accessors(chain = true)
public class PlaceOrderResult {

    /**
     *  任务id
     */
    private Long taskId;

    private Long orderId;

    private boolean success;

    private String msg;


}
