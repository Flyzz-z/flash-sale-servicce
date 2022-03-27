package cn.flyzzgo.flashsaleservice.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Flyzz
 */
@Getter
@AllArgsConstructor
public enum FlashOrderStatus {

    PROCESSING(0,"订单进行中"),
    SUCCESS(1,"成功"),
    FAIL(2,"失败"),
    CANCEL(3,"撤销");
    private final Integer code;

    private final String msg;
}
