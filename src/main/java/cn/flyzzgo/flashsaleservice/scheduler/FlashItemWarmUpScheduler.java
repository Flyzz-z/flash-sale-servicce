package cn.flyzzgo.flashsaleservice.scheduler;

import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashItemService;
import cn.flyzzgo.flashsaleservice.service.ItemStockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Flyzz
 */
@Component
public class FlashItemWarmUpScheduler {

    @Resource
    private FlashItemService flashItemService;

    @Resource
    private ItemStockService itemStockService;

    @Scheduled(cron = "*/5 * * * * ?")
    public void warmUpFlashItemTask() {
        Response response = flashItemService.getNotWarmUpItemList(30L);
        if (!response.isSuccess()) {
            return;
        }

        List<FlashItemDto> flashItemDoList = (List<FlashItemDto>) ((SingleResponse) response).getData();
        if (CollectionUtils.isEmpty(flashItemDoList)) {
            return;
        }

        flashItemDoList.forEach(item -> {
            itemStockService.initItemStock(item.getId());
        });
    }
}
