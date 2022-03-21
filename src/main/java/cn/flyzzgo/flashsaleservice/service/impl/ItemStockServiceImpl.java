package cn.flyzzgo.flashsaleservice.service.impl;

import cn.flyzzgo.flashsaleservice.constant.enums.DecreaseItemStockResult;
import cn.flyzzgo.flashsaleservice.model.dto.FlashItemDto;
import cn.flyzzgo.flashsaleservice.model.response.Response;
import cn.flyzzgo.flashsaleservice.model.response.SingleResponse;
import cn.flyzzgo.flashsaleservice.service.FlashItemService;
import cn.flyzzgo.flashsaleservice.service.ItemStockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Flyzz
 */
@Service
@Slf4j
public class ItemStockServiceImpl implements ItemStockService {

    private final String ITEM_Stock_PREFIX = "item_stock";
    private final String ALIGN_ITEM_STOCK_LOCK = "align_item_stock_lock";
    private static final String DECREASE_ITEM_STOCK_LUA;
    private static final String INIT_ITEM_STOCK_LUA;
    private static final String INCREASE_ITEM_STOCK_LUA;


    static {
        INIT_ITEM_STOCK_LUA = "if (redis.call('exists', KEYS[1]) == 1) then" +
                "   return -1;" +
                "end;" +
                "local stockNumber = tonumber(ARGV[1]);" +
                "redis.call('set', KEYS[1] , stockNumber);" +
                "return 1";

        DECREASE_ITEM_STOCK_LUA = "if (redis.call('exists', KEYS[1]) == 1) then" +
                "    local stock = tonumber(redis.call('get', KEYS[1]));" +
                "    local num = tonumber(ARGV[1]);" +
                "    if (stock < num) then" +
                "        return -3" +
                "    end;" +
                "    if (stock >= num) then" +
                "        redis.call('incrby', KEYS[1], 0 - num);" +
                "        return 1" +
                "    end;" +
                "    return -2;" +
                "end;" +
                "return -1;";

        INCREASE_ITEM_STOCK_LUA = "if (redis.call('exists', KEYS[1]) == 1) then" +
                "    local stock = tonumber(redis.call('get', KEYS[1]));" +
                "    local num = tonumber(ARGV[1]);" +
                "    redis.call('incrby', KEYS[1] , num);" +
                "    return 1;" +
                "end;" +
                "return -1;";
    }

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private FlashItemService flashItemService;

    @Override
    public DecreaseItemStockResult decreaseItemStock(Long itemId, Integer num) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(DECREASE_ITEM_STOCK_LUA, Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(ITEM_Stock_PREFIX + itemId);
        Long res = redisTemplate.execute(redisScript, keys, num);
        if (res == null || res == -1 || res == -2) {
            return DecreaseItemStockResult.DECREASE_ERROR;
        }
        if (res == -3) {
            return DecreaseItemStockResult.NOT_ENOUGH_STOCK;
        }
        return DecreaseItemStockResult.DECREASE_SUCCESS;
    }

    @Override
    public boolean increaseItemStock(Long itemId, Integer num) {

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(INCREASE_ITEM_STOCK_LUA, Long.class);
        List<String> keys = Collections.singletonList(ITEM_Stock_PREFIX + itemId);
        Long res = redisTemplate.execute(redisScript, keys, num);
        if (res == null) {
            return false;
        }
        return res == 1;
    }

    @Override
    public boolean initItemStock(Long itemId) {

        if (itemId == null) {
            log.info("alignItemStock 参数为空");
            return false;
        }

        RLock rLock = redissonClient.getLock(ALIGN_ITEM_STOCK_LOCK + itemId);
        try {
            boolean isLockSuccess = rLock.tryLock();
            if (!isLockSuccess) {
                return false;
            }

            Response response = flashItemService.getFlashItemById(itemId);
            if (!response.isSuccess()) {
                return false;
            }


            FlashItemDto flashItemDto = (FlashItemDto) ((SingleResponse<?>) response).getData();

            if (flashItemDto == null) {
                log.info("initItemStock查询秒杀品成功，但结果为空");
                return false;
            }

            if (flashItemDto.getInitialStock() == null) {
                log.info("秒杀品id{}未设置库存", itemId);
                return false;
            }

            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(INIT_ITEM_STOCK_LUA, Long.class);
            List<String> keys = new ArrayList<>();
            keys.add(ITEM_Stock_PREFIX + itemId);
            Long result = redisTemplate.execute(redisScript, keys, flashItemDto.getAvailableStock());
            if (result == null) {
                log.info("initItemStock秒杀品库存初始化失败,id{}", itemId);
                return false;
            }
            if (result != 1) {
                return false;
            }
            flashItemDto.setStockWarmUp(1);
            Response modifyResponse = flashItemService.modifyFlashItem(flashItemDto);
            return modifyResponse.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            rLock.unlock();
        }
    }
}
