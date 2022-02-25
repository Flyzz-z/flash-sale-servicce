package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.mapper.FlashActivityMapper;
import cn.flyzzgo.flashsaleservice.model.convertor.FlashActivityConvertor;
import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashActivityDo;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
        log.info("发布秒杀活动成功");
        return Response.buildSuccess();
    }

    @Override
    public Response modifyFlashActivity(FlashActivityDto flashActivityDto) {
        log.info("修改秒杀活动{}",flashActivityDto);
        FlashActivityDo flashActivityDo = FlashActivityConvertor.flashActivityDtoToDo(flashActivityDto);
        flashActivityMapper.updateById(flashActivityDo);
        log.info("修改秒杀活动成功");
        return Response.buildSuccess();
    }

    @Override
    @Cacheable(cacheNames = "FlashActivity",keyGenerator = "cacheKey")
    public Response getFlashActivityById(Long id) {
        log.info("查询秒杀活动 id={}",id);
        FlashActivityDo flashActivityDo = flashActivityMapper.selectById(id);
        if(flashActivityDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        FlashActivityDto flashActivityDto = FlashActivityConvertor.flashActivityDoToDto(flashActivityDo);
        return SingleResponse.of(flashActivityDto);
    }

    @Override
    public boolean isAllowPlaceOrder(Long id) {
        return true;
    }
}
