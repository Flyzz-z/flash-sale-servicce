package cn.flyzzgo.flashsaleservice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Flyzz
 */
@Data
@TableName("flash_item")
public class FlashItemDo {

    /**
     * 秒杀品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 秒杀活动ID
     */
    @TableField("activity_id")
    private Long activityId;

    /**
     * 秒杀品标题
     */
    @TableField("item_title")
    private String itemTitle;

    /**
     * 秒杀品副标题
     */
    @TableField("item_sub_title")
    private String itemSubTitle;

    /**
     * 秒杀品介绍
     */
    @TableField("item_desc")
    private String itemDesc;

    /**
     * 初始库存
     */
    @TableField("initial_stock")
    private Integer initialStock;

    /**
     * 当前可用库存
     */
    @TableField("available_stock")
    private Integer availableStock;

    /**
     * 原价
     */
    @TableField("original_price")
    private Long originalPrice;

    /**
     * 秒杀价
     */
    @TableField("flash_price")
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
    @TableField("status")
    private Integer status;

    /**
     * 库存是否已经预热
     */
    @TableField("stock_warm_up")
    private Integer stockWarmUp;

    @TableField("modified_time")
    private Date modifiedTime;

    @TableField("create_time")
    private Date createTime;
}
