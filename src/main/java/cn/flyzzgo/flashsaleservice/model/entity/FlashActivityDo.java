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
@TableName("flash_activity")
public class FlashActivityDo {

    /**
     * 活动ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;

    /**
     * 活动描述
     */
    @TableField("activity_desc")
    private String activityDesc;

    /**
     * 活动开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 活动状态
     */
    @TableField("status")
    private Integer status;

    @TableField("modified_time")
    private Date modifiedTime;

    @TableField("create_time")
    private Date createTime;

}
