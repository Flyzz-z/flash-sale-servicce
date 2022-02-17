package cn.flyzzgo.flashsaleservice.controller;

import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@RestController
@RequestMapping("/flash-sale")
public class FlashActivityController {

    @Resource
    private FlashActivityService flashActivityService;

    @PostMapping("/flash-activity/publish")
    public Response publishFlashActivity(@RequestBody FlashActivityDto flashActivityDto) {
        // TODO: 2022/2/17 鉴权,校验参数
        return flashActivityService.publishFlashActivity(flashActivityDto);
    }

    @PostMapping("/flash-activity/modify")
    public Response modifyFlashActivity(@RequestBody FlashActivityDto flashActivityDto) {
        return null;
    }
}
