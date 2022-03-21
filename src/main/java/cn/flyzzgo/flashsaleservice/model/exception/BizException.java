package cn.flyzzgo.flashsaleservice.model.exception;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import lombok.Getter;

/**
 * @author Flyzz
 */
@Getter
public class BizException extends RuntimeException {

    private final String errCode;

    public BizException(String msg) {
        super(msg);
        this.errCode = "BIZ_EXCEPTION";
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getErrMsg());
        this.errCode = errorCode.getErrCode();
    }

}
