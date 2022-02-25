package cn.flyzzgo.flashsaleservice.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Flyzz
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    CLIENT_ERROR("A00000","客户端错误"),
    SERVER_ERROR("B00000","服务端错误");
    private String errCode;
    private String errMsg;
}
