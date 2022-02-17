package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.mapper.FlashActivityMapper;
import cn.flyzzgo.flashsaleservice.model.convertor.FlashActivityConvertor;
import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashActivityDo;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@Service
@Slf4j
public class FlashActivityServiceImpl implements FlashActivityService {

    @Resource
    private FlashActivityMapper flashActivityMapper;

    @Override
    public Response publishFlashActivity(FlashActivityDto flashActivityDto) {
        log.info("发布秒杀活动{}",flashActivityDto);
        FlashActivityDo flashActivityDo = FlashActivityConvertor.flashActivityDtoToDo(flashActivityDto);
        flashActivityMapper.insert(flashActivityDo);
        return Response.buildSuccess();
    }

    @Override
    public Response modifyFlashActivity(FlashActivityDto flashActivityDto) {
        return null;
    }
}
