package cn.flyzzgo.flashsaleservice.model.convertor;

import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashItemDo;
import org.springframework.beans.BeanUtils;

/**
 * @author Flyzz
 */
public class FlashItemConvertor {

    public static FlashItemDo flashItemDtoToDo(FlashItemDto flashItemDto) {
        FlashItemDo flashItemDo = new FlashItemDo();
        BeanUtils.copyProperties(flashItemDto,flashItemDo);
        return flashItemDo;
    }

    public static FlashItemDto flashItemDtoToDo(FlashItemDo flashItemDo) {
        FlashItemDto flashItemDto = new FlashItemDto();
        BeanUtils.copyProperties(flashItemDo,flashItemDto);
        return flashItemDto;
    }
}
