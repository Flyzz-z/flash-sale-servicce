package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;

/**
 * @author Flyzz
 */
public interface FlashActivityService {

    /**
     * 发布秒杀活动
     * @param flashActivityDto
     * @return
     */
    public Response publishFlashActivity(FlashActivityDto flashActivityDto);

    /**
     * 修改秒杀活动
     * @param flashActivityDto
     * @return
     */
    public Response modifyFlashActivity(FlashActivityDto flashActivityDto);
}
