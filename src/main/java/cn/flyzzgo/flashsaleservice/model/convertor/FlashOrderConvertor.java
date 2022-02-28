package cn.flyzzgo.flashsaleservice.model.convertor;

import cn.flyzzgo.flashsaleservice.model.dto.FlashOrderDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashItemDo;
import cn.flyzzgo.flashsaleservice.model.entity.FlashOrderDo;
import org.springframework.beans.BeanUtils;

/**
 * @author Flyzz
 */
public class FlashOrderConvertor {

    public static FlashOrderDto flashOrderDoToFlashOrderDto(FlashOrderDo flashOrderDo) {
        FlashOrderDto flashOrderDto = new FlashOrderDto();
        BeanUtils.copyProperties(flashOrderDo, flashOrderDto);
        return flashOrderDto;
    }

    public static FlashOrderDo flashOrderDtoToFlashOrderDo(FlashOrderDto flashOrderDto) {
        FlashOrderDo flashOrderDo = new FlashOrderDo();
        BeanUtils.copyProperties(flashOrderDto, flashOrderDo);
        return flashOrderDo;
    }
}
