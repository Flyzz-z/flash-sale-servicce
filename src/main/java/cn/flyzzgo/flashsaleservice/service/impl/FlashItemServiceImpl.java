package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.ErrorCode;
import cn.flyzzgo.flashsaleservice.constant.enums.FlashItemStatus;
import cn.flyzzgo.flashsaleservice.mapper.FlashItemMapper;
import cn.flyzzgo.flashsaleservice.model.convertor.FlashItemConvertor;
import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.entity.FlashItemDo;
import cn.flyzzgo.flashsaleservice.model.response.MultiResponse;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Flyzz
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "FlashItem")
public class FlashItemServiceImpl implements FlashItemService {

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public Response publishFlashItem(FlashItemDto flashItemDto) {
        log.info("发布秒杀品{}", flashItemDto);
        flashItemDto.setStatus(FlashItemStatus.PUBLISHED.getCode());
        flashItemDto.setStockWarmUp(0);
        FlashItemDo flashItemDo = FlashItemConvertor.flashItemDtoToDo(flashItemDto);
        flashItemMapper.insert(flashItemDo);
        log.info("发布秒杀品成功id{}", flashItemDo.getId());
        return Response.buildSuccess();
    }

    @Override
    @CacheEvict(key = "'FlashItemCache'.concat(#itemId)")
    public Response onlineFlashItem(Long itemId) {
        log.info("上线秒杀品id{}", itemId);
        FlashItemDo flashItemDo = flashItemMapper.selectById(itemId);
        if (flashItemDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        if (flashItemDo.getStatus().equals(FlashItemStatus.ONLINE.getCode())) {
            return Response.buildSuccess();
        }
        flashItemDo.setStatus(FlashItemStatus.ONLINE.getCode());
        flashItemMapper.updateById(flashItemDo);
        log.info("上线秒杀品成功id{}", itemId);
        return Response.buildSuccess();
    }

    @Override
    @CacheEvict(key = "'FlashItemCache'.concat(#itemId)")
    public Response offlineFlashItem(Long itemId) {
        log.info("下线秒杀品id{}", itemId);
        FlashItemDo flashItemDo = flashItemMapper.selectById(itemId);
        if (flashItemDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        if (!flashItemDo.getStatus().equals(FlashItemStatus.ONLINE.getCode())) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        flashItemDo.setStatus(FlashItemStatus.OFFLINE.getCode());
        flashItemMapper.updateById(flashItemDo);
        log.info("下线秒杀品成功id{}", itemId);
        return Response.buildSuccess();
    }

    @Override
    @Cacheable(key = "'FlashItemCache'.concat(#itemId)")
    public Response getFlashItemById(Long itemId) {

        FlashItemDo flashItemDo = flashItemMapper.selectById(itemId);
        if (flashItemDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        FlashItemDto flashItemDto = FlashItemConvertor.flashItemDoToDto(flashItemDo);
        return SingleResponse.of(flashItemDto);
    }

    @Override
    @Cacheable(key = "'ActivityFlashItemCache'.concat(#activityId)", sync = true)
    public Response getFlashItemsByActivityId(Long activityId) {

        QueryWrapper<FlashItemDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlashItemDo::getActivityId, activityId);
        List<FlashItemDo> flashItemDoList = flashItemMapper.selectList(queryWrapper);
        List<FlashItemDto> flashItemDtoList = flashItemDoList.stream().map(FlashItemConvertor::flashItemDoToDto).collect(Collectors.toList());
        return MultiResponse.of(flashItemDtoList);
    }

    @Override
    @CacheEvict(key = "'FlashItemCache'.concat(#flashItemDto.id)")
    public Response modifyFlashItem(FlashItemDto flashItemDto) {
        FlashItemDo flashItemDo = FlashItemConvertor.flashItemDtoToDo(flashItemDto);
        flashItemMapper.updateById(flashItemDo);
        return Response.buildSuccess();
    }

    @Override
    public Response getNotWarmUpItemList(Long size) {
        QueryWrapper<FlashItemDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlashItemDo::getStockWarmUp, 0)
                .eq(FlashItemDo::getStatus, FlashItemStatus.ONLINE.getCode())
                .last(String.format("limit %d", size));
        List<FlashItemDo> flashItemDoList = flashItemMapper.selectList(queryWrapper);
        List<FlashItemDto> flashItemDtoList = flashItemDoList.stream().map(FlashItemConvertor::flashItemDoToDto).collect(Collectors.toList());
        return SingleResponse.of(flashItemDtoList);
    }


    @Override
    public boolean isAllowPlaceOrder(Long itemId) {
        return true;
    }

    @Override
    @CacheEvict(key = "'FlashItemCache'.concat(#itemId)")
    public boolean decreaseItemStock(Long itemId, Integer quantity) {
        return flashItemMapper.decreaseItemStock(itemId, quantity) == 1;
    }

    @Override
    public boolean increaseItemStock(Long itemId, Integer quantity) {
        return flashItemMapper.increaseItemStock(itemId, quantity) == 1;
    }


}
