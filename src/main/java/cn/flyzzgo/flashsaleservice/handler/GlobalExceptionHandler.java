package cn.flyzzgo.flashsaleservice.handler;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.exception.BizException;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Flyzz
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    public Response bizExceptionHandler(HttpServletRequest httpServletRequest, BizException e) {
        log.info(e.getErrCode() + e.getMessage());
        return Response.buildFailure(e.getErrCode(),e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Response exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        log.info(e.getMessage());
        return Response.buildFailure(ErrorCode.SERVER_ERROR);
    }
}
