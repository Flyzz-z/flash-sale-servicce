package cn.flyzzgo.flashsaleservice.controller;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.service.FlashItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@RestController
@RequestMapping("/flash-sale/item")
public class FlashItemController {

    @Resource
    private FlashItemService flashItemService;

    @PostMapping("/publish")
    public Response publishFlashItem(@RequestBody FlashItemDto flashItemDto) {
        // TODO: 2022/2/17 鉴权
        if (flashItemDto.checkValid()) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashItemService.publishFlashItem(flashItemDto);
    }

    @PostMapping("/modify")
    public Response modifyFlashItem(@RequestBody FlashItemDto flashItemDto) {
        // TODO: 2022/2/17 鉴权
        if (flashItemDto.checkValid()) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashItemService.modifyFlashItem(flashItemDto);
    }

    @GetMapping("/{id}")
    public Response getFlashItem(@PathVariable("id") Long itemId) {
        // TODO: 2022/2/17 鉴权
        if (itemId == null || itemId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashItemService.getFlashItemById(itemId);
    }

    @GetMapping("/activity/{id}")
    public Response getFlashItemByActivity(@PathVariable("id") Long activityId) {

        if (activityId == null || activityId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashItemService.getFlashItemsByActivityId(activityId);
    }

    @PostMapping("/online/{id}")
    public Response onlineFlashItem(@PathVariable("id") Long itemId) {
        if (itemId == null || itemId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashItemService.onlineFlashItem(itemId);
    }

    @PostMapping("/offline/{id}")
    public Response offlineFlashItem(@PathVariable("id") Long itemId) {
        if (itemId == null || itemId <= 0) {
            return Response.buildFailure(ErrorCode.PARAM_ERROR);
        }
        return flashItemService.offlineFlashItem(itemId);
    }
}
