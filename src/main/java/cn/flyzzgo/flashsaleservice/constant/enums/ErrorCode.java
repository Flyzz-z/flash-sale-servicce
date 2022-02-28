package cn.flyzzgo.flashsaleservice.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Flyzz
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    CLIENT_ERROR("A00000", "客户端错误"),
    PARAM_ERROR("A00001","参数错误"),
    NOT_ENOUGH_STOCK("A00002","库存不足"),
    SERVER_ERROR("B00000", "服务端错误");
    private String errCode;
    private String errMsg;
}
