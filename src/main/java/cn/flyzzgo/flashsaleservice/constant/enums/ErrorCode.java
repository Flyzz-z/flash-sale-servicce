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
    PARAM_ERROR("A00001", "参数错误"),
    NOT_ENOUGH_STOCK("A00002", "库存不足"),
    RESOURCE_NOT_FOUND("A00004","页面不存在"),
    TASK_NOT_EXIST("A00005","任务不存在"),
    SERVER_ERROR("B00000", "服务端错误");

    private String errCode;
    private String errMsg;
}
