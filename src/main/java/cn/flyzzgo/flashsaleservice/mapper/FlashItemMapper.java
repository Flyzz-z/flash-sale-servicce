package cn.flyzzgo.flashsaleservice.mapper;

import cn.flyzzgo.flashsaleservice.model.entity.FlashItemDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author Flyzz
 */
public interface FlashItemMapper extends BaseMapper<FlashItemDo> {

    int decreaseItemStock(Long itemId,Integer quantity);

    int increaseItemStock(Long itemId,Integer quantity);
}
