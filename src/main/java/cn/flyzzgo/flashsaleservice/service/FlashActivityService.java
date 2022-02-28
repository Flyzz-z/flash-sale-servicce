package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;

/**
 * @author Flyzz
 */
public interface FlashActivityService {

    /**
     * 发布秒杀活动
     *
     * @param flashActivityDto
     * @return
     */
    Response publishFlashActivity(FlashActivityDto flashActivityDto);

    /**
     * 修改秒杀活动
     *
     * @param flashActivityDto
     * @return
     */
    Response modifyFlashActivity(FlashActivityDto flashActivityDto);

    /**
     * 获取指定活动
     *
     * @param
     * @return
     */
    Response getFlashActivityById(Long activityId);

    Response onlineFlashActivity(Long activityId);

    Response offlineFlashActivity(Long activityId);

    boolean isAllowPlaceOrder(Long id);
}
