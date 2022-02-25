package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;

/**
 * @author Flyzz
 */
public interface FlashItemService {

    Response publishFlashItem(FlashItemDto flashItemDto);

    Response onlineFlashItem(Long itemId);

    Response offlineFlashItem(Long itemId);

    Response getFlashItemById(Long itemId);

    Response getFlashItemsByActvityId(Long itemId);

    boolean decreaseItemStock(Long itemId,Integer quantity);

    boolean increaseItemStock(Long itemId,Integer quantity);

    boolean isAllowPlaceOrder(Long itemId);
}
