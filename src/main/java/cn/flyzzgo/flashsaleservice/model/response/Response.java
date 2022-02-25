package cn.flyzzgo.flashsaleservice.model.response;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import lombok.Data;

/**
 * @author Flyzz
 */
@Data
public class Response {

    private boolean success;
    private String errCode;
    private String errMessage;

    public static Response buildSuccess() {
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response buildFailure(String errCode, String errMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static Response buildFailure(ErrorCode errorCode) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errorCode.getErrCode());
        response.setErrMessage(errorCode.getErrMsg());
        return response;
    }
}
