package cn.flyzzgo.flashsaleservice.controller;

import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@RestController
@RequestMapping("/flash-sale/flash-activity")
public class FlashActivityController {

    @Resource
    private FlashActivityService flashActivityService;

    @PostMapping("/publish")
    public Response publishFlashActivity(@RequestBody FlashActivityDto flashActivityDto) {
        // TODO: 2022/2/17 鉴权,校验参数
        return flashActivityService.publishFlashActivity(flashActivityDto);
    }

    @PostMapping("/modify")
    public Response modifyFlashActivity(@RequestBody FlashActivityDto flashActivityDto) {
        // TODO: 2022/2/17 鉴权,校验参数
        return flashActivityService.modifyFlashActivity(flashActivityDto);
    }

    @GetMapping("/get/{id}")
    public Response getFlashActivity(@PathVariable("id") Long activityId) {
        // TODO: 2022/2/17 鉴权，参数校验
        return flashActivityService.getFlashActivityById(activityId);
    }
}
