package cn.flyzzgo.flashsaleservice.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Flyzz
 */
@AllArgsConstructor
@Getter
public enum DecreaseItemStockResult {
    DECREASE_SUCCESS(1,"成功"),
    NOT_ENOUGH_STOCK(2,"库存不足"),
    DECREASE_ERROR(3,"服务端错误");

    private Integer code;
    private String msg;
}
