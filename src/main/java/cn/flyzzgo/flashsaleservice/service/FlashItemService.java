package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;

/**
 * @author Flyzz
 */
public interface FlashItemService {

    Response publishFlashItem(FlashItemDto flashItemDto);

    Response onlineFlashItem(Long id);

    Response offlineFlashItem(Long id);

    Response getFlashItemById(Long id);

    Response getFlashItemsByActvityId(Long id);
}
