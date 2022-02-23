package cn.flyzzgo.flashsaleservice.service.impl;

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

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Flyzz
 */
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
    public Response onlineFlashItem(Long id) {
        FlashItemDo flashItemDo = flashItemMapper.selectById(id);
        if(flashItemDo == null) {

        }

        if(!flashItemDo.getStatus().equals(FlashItemStatus.PUBLISHED.getCode())) {

        }
        flashItemDo.setStatus(FlashItemStatus.ONLINE.getCode());
        flashItemMapper.updateById(flashItemDo);
        return Response.buildSuccess();
    }

    @Override
    public Response offlineFlashItem(Long id) {
        FlashItemDo flashItemDo = flashItemMapper.selectById(id);
        if(flashItemDo == null) {

        }

        if(!flashItemDo.getStatus().equals(FlashItemStatus.ONLINE.getCode())) {

        }
        flashItemDo.setStatus(FlashItemStatus.OFFLINE.getCode());
        flashItemMapper.updateById(flashItemDo);
        return Response.buildSuccess();
    }

    @Override
    public Response getFlashItemById(Long id) {

        FlashItemDo flashItemDo = flashItemMapper.selectById(id);
        if(flashItemDo == null) {

        }
        return SingleResponse.of(flashItemDo);
    }

    @Override
    public Response getFlashItemsByActvityId(Long id) {

        QueryWrapper<FlashItemDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlashItemDo::getActivityId,id);
        List<FlashItemDo> flashItemDoList = flashItemMapper.selectList(queryWrapper);
        return MultiResponse.of(flashItemDoList);
    }

}
