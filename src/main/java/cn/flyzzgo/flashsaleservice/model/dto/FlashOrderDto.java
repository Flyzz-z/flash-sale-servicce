package cn.flyzzgo.flashsaleservice.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Flyzz
 */
@Data
public class FlashOrderDto {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 秒杀品标题
     */
    private String itemTitle;

    /**
     * 秒杀价
     */
    private Long flashPrice;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 下单商品数量
     */
    private Integer quantity;

    /**
     * 总金额
     */
    private Long totalAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单创建时间
     */
    private Date createTime;
}
