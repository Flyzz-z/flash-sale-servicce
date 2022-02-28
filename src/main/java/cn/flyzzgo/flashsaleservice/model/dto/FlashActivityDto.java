package cn.flyzzgo.flashsaleservice.model.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

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
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 活动描述
     */
    private String activityDesc;

    private Boolean inProcess;

    public boolean checkValid() {
        return !StringUtils.hasText(activityName) || startTime == null || endTime == null ||
                !StringUtils.hasText(activityDesc);
    }
}
