package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.constant.enums.FlashActivityStatus;
import cn.flyzzgo.flashsaleservice.mapper.FlashActivityMapper;
import cn.flyzzgo.flashsaleservice.model.convertor.FlashActivityConvertor;
import cn.flyzzgo.flashsaleservice.model.dto.FlashActivityDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashActivityDo;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Flyzz
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "FlashActivity")
public class FlashActivityServiceImpl implements FlashActivityService {

    @Resource
    private FlashActivityMapper flashActivityMapper;

    @Override
    public Response publishFlashActivity(FlashActivityDto flashActivityDto) {
        log.info("发布秒杀活动{}", flashActivityDto);
        flashActivityDto.setStatus(FlashActivityStatus.PUBLISHED.getCode());
        FlashActivityDo flashActivityDo = FlashActivityConvertor.flashActivityDtoToDo(flashActivityDto);
        flashActivityMapper.insert(flashActivityDo);
        log.info("发布秒杀活动成功");
        return Response.buildSuccess();
    }

    @Override
    @CacheEvict(key = "'FlashActivityCache'.concat(#flashActivityDto.id)")
    public Response modifyFlashActivity(FlashActivityDto flashActivityDto) {
        log.info("修改秒杀活动{}", flashActivityDto);
        FlashActivityDo flashActivityDo = FlashActivityConvertor.flashActivityDtoToDo(flashActivityDto);
        flashActivityMapper.updateById(flashActivityDo);
        log.info("修改秒杀活动成功");
        return Response.buildSuccess();
    }

    @Override
    @Cacheable(key = "'FlashActivityCache'.concat(#activityId)")
    public Response getFlashActivityById(Long activityId) {
        log.info("查询秒杀活动 id={}", activityId);
        FlashActivityDo flashActivityDo = flashActivityMapper.selectById(activityId);
        if (flashActivityDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        FlashActivityDto flashActivityDto = FlashActivityConvertor.flashActivityDoToDto(flashActivityDo);
        return SingleResponse.of(flashActivityDto);
    }

    @Override
    @CacheEvict(key = "'FlashActivityCache'.concat(#activityId)")
    public Response onlineFlashActivity(Long activityId) {
        log.info("上线活动id{}",activityId);
        FlashActivityDo flashActivityDo = flashActivityMapper.selectById(activityId);
        if(flashActivityDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        if(flashActivityDo.getStatus().equals(FlashActivityStatus.ONLINE.getCode())) {
            log.info("无需重复上线{}",activityId);
            return Response.buildSuccess();
        }

        flashActivityDo.setStatus(FlashActivityStatus.ONLINE.getCode());
        flashActivityMapper.updateById(flashActivityDo);
        log.info("上线成功id{}",activityId);
        return Response.buildSuccess();
    }

    @Override
    @CacheEvict(key = "'FlashActivityCache'.concat(#activityId)")
    public Response offlineFlashActivity(Long activityId) {
        log.info("下线活动id{}",activityId);
        FlashActivityDo flashActivityDo = flashActivityMapper.selectById(activityId);
        if(flashActivityDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        if(!flashActivityDo.getStatus().equals(FlashActivityStatus.ONLINE.getCode())) {
            log.info("下线活动id{}失败，活动未上线",activityId);
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        flashActivityDo.setStatus(FlashActivityStatus.OFFLINE.getCode());
        flashActivityMapper.updateById(flashActivityDo);
        log.info("下线成功id{}",activityId);
        return Response.buildSuccess();
    }

    @Override
    public boolean isAllowPlaceOrder(Long id) {
        return true;
    }
}
