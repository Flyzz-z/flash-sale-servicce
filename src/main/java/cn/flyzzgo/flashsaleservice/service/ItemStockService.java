package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.constant.enums.DecreaseItemStockResult;

/**
 * @author Flyzz
 */
public interface ItemStockService {

    DecreaseItemStockResult decreaseItemStock(Long itemId, Integer num);

    boolean increaseItemStock(Long itemId, Integer num);

    boolean initItemStock(Long itemId);
}
