package cn.flyzzgo.flashsaleservice.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Flyzz
 */
@Data
public class FlashActivityDto {

    /**
     * 活动ID
     */
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 活动描述
     */
    private String activityDesc;
}
