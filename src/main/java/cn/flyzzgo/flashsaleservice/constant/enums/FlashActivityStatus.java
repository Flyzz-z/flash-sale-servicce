package cn.flyzzgo.flashsaleservice.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Flyzz
 */
@Getter
@AllArgsConstructor
public enum FlashActivityStatus {

    PUBLISHED(0, "已发布"),
    ONLINE(1, "已上线"),
    OFFLINE(-1, "已下线");

    private final Integer code;

    private final String msg;
}
