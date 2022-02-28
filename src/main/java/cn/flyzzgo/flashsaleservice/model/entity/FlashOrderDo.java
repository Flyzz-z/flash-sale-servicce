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
@TableName("flash_order")
public class FlashOrderDo {

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 商品ID
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 秒杀品标题
     */
    @TableField("item_title")
    private String itemTitle;

    /**
     * 秒杀价
     */
    @TableField("flash_price")
    private Long flashPrice;

    /**
     * 活动ID
     */
    @TableField("activity_id")
    private Long activityId;

    /**
     * 下单商品数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private Long totalAmount;

    /**
     * 订单状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    @TableField("modified_time")
    private Date modifiedTime;

    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
