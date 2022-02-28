package cn.flyzzgo.flashsaleservice.model.convertor;

import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashActivityDo;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author Flyzz
 */
public class FlashActivityConvertor {

    public static FlashActivityDto flashActivityDoToDto(FlashActivityDo flashActivityDo) {
        FlashActivityDto flashActivityDto = new FlashActivityDto();
        BeanUtils.copyProperties(flashActivityDo, flashActivityDto);
        Date now = new Date();
        flashActivityDto.setInProcess(now.compareTo(flashActivityDo.getStartTime()) > 0 && now.compareTo(flashActivityDto.getEndTime()) < 0);
        return flashActivityDto;
    }

    public static FlashActivityDo flashActivityDtoToDo(FlashActivityDto flashActivityDto) {

        FlashActivityDo flashActivityDo = new FlashActivityDo();
        BeanUtils.copyProperties(flashActivityDto, flashActivityDo);
        return flashActivityDo;
    }
}
