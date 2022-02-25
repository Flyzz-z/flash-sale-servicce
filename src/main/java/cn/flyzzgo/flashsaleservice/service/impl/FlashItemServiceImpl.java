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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Flyzz
 */
@Service
public class FlashItemServiceImpl implements FlashItemService {

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public Response publishFlashItem(FlashItemDto flashItemDto) {
        FlashItemDo flashItemDo = FlashItemConvertor.flashItemDtoToDo(flashItemDto);
        flashItemMapper.insert(flashItemDo);
        return Response.buildSuccess();
    }

    @Override
    public Response onlineFlashItem(Long itemId) {
        FlashItemDo flashItemDo = flashItemMapper.selectById(itemId);
        if (flashItemDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        if (!flashItemDo.getStatus().equals(FlashItemStatus.PUBLISHED.getCode())) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        flashItemDo.setStatus(FlashItemStatus.ONLINE.getCode());
        flashItemMapper.updateById(flashItemDo);
        return Response.buildSuccess();
    }

    @Override
    public Response offlineFlashItem(Long itemId) {
        FlashItemDo flashItemDo = flashItemMapper.selectById(itemId);
        if (flashItemDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }

        if (!flashItemDo.getStatus().equals(FlashItemStatus.ONLINE.getCode())) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        flashItemDo.setStatus(FlashItemStatus.OFFLINE.getCode());
        flashItemMapper.updateById(flashItemDo);
        return Response.buildSuccess();
    }

    @Override
    public Response getFlashItemById(Long itemId) {

        FlashItemDo flashItemDo = flashItemMapper.selectById(itemId);
        if (flashItemDo == null) {
            return Response.buildFailure(ErrorCode.CLIENT_ERROR);
        }
        return SingleResponse.of(flashItemDo);
    }

    @Override
    public Response getFlashItemsByActvityId(Long itemId) {

        QueryWrapper<FlashItemDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlashItemDo::getActivityId, itemId);
        List<FlashItemDo> flashItemDoList = flashItemMapper.selectList(queryWrapper);
        return MultiResponse.of(flashItemDoList);
    }

    @Override
    public boolean isAllowPlaceOrder(Long itemId) {
        return true;
    }

    @Override
    public boolean decreaseItemStock(Long itemId, Integer quantity) {
        return flashItemMapper.decreaseItemStock(itemId, quantity) == 1;
    }

    @Override
    public boolean increaseItemStock(Long itemId, Integer quantity) {
        return flashItemMapper.increaseItemStock(itemId, quantity) == 1;
    }
}
