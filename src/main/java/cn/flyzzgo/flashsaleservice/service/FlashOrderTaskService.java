package cn.flyzzgo.flashsaleservice.service;

import cn.flyzzgo.flashsaleservice.model.dto.PlaceOrderTask;
import cn.flyzzgo.flashsaleservice.model.response.Response;

/**
 * @author Flyzz
 */
public interface FlashOrderTaskService {

    /**
     * 提交下单任务，
     * @return 返回提交任务是否成功
     */
    boolean submitTask(PlaceOrderTask placeOrderTask);


    Response getTaskResult(Long taskId);
}
