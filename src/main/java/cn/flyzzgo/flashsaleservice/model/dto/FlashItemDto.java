package cn.flyzzgo.flashsaleservice.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Flyzz
 */
@Data
public class FlashItemDto {
    /**
     * 秒杀品ID
     */
    private Long id;
    /**
     * 秒杀活动ID
     */
    private Long activityId;
    /**
     * 秒杀品标题
     */
    private String itemTitle;
    /**
     * 秒杀品副标题
     */
    private String itemSubTitle;
    /**
     * 秒杀品介绍
     */
    private String itemDesc;
    /**
     * 初始库存
     */
    private Integer initialStock;
    /**
     * 当前可用库存
     */
    private Integer availableStock;
    /**
     * 原价
     */
    private Long originalPrice;
    /**
     * 秒杀价
     */
    private Long flashPrice;
    /**
     * 秒杀开始时间
     */
    private Date startTime;
    /**
     * 秒杀结束时间
     */
    private Date endTime;
    /**
     * 秒杀状态
     */
    private Integer status;

    /**
     * 库存是否已经预热
     */
    private Integer stockWarmUp;
}
