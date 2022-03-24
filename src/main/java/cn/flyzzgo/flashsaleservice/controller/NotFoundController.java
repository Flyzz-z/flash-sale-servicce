package cn.flyzzgo.flashsaleservice.controller;


import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Flyzz
 */
@RestController
public class NotFoundController implements ErrorController {

    @RequestMapping("/error")
    public Response notFound() {
        return Response.buildFailure(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
