package cn.flyzzgo.flashsaleservice.model.convertor;

import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashActivityDo;
import org.springframework.beans.BeanUtils;

/**
 * @author Flyzz
 */
public class FlashActivityConvertor {

    public static FlashActivityDto flashActivityDoToDto(FlashActivityDo flashActivityDo) {
        FlashActivityDto flashActivityDto = new FlashActivityDto();
        BeanUtils.copyProperties(flashActivityDo,flashActivityDto);
        return flashActivityDto;
    }

    public static FlashActivityDo flashActivityDtoToDo(FlashActivityDto flashActivityDto) {

        FlashActivityDo flashActivityDo = new FlashActivityDo();
        BeanUtils.copyProperties(flashActivityDto,flashActivityDo);
        return flashActivityDo;
    }
}
