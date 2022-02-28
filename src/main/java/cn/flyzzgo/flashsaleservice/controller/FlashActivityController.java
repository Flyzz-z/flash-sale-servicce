package cn.flyzzgo.flashsaleservice.controller;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@RestController
@RequestMapping("/flash-sale/activity")
public class FlashActivityController {

    @Resource
    private FlashActivityService flashActivityService;

    @PostMapping("/publish")
    public Response publishFlashActivity(@RequestBody FlashActivityDto flashActivityDto) {
        // TODO: 2022/2/17 鉴权
        if (flashActivityDto.checkValid()) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashActivityService.publishFlashActivity(flashActivityDto);
    }

    @PostMapping("/modify")
    public Response modifyFlashActivity(@RequestBody FlashActivityDto flashActivityDto) {
        // TODO: 2022/2/17 鉴权
        if (flashActivityDto.checkValid()) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashActivityService.modifyFlashActivity(flashActivityDto);
    }

    @GetMapping("/{id}")
    public Response getFlashActivity(@PathVariable("id") Long activityId) {
        // TODO: 2022/2/17 鉴权，参数校验
        if (activityId == null || activityId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashActivityService.getFlashActivityById(activityId);
    }

    @PostMapping("/online/{id}")
    public Response onlineFlashActivity(@PathVariable("id") Long activityId) {
        if (activityId == null || activityId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashActivityService.onlineFlashActivity(activityId);
    }

    @PostMapping("/offline/{id}")
    public Response offlineFlashActivity(@PathVariable("id") Long activityId) {
        if (activityId == null || activityId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashActivityService.offlineFlashActivity(activityId);
    }
}
