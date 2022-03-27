package cn.flyzzgo.flashsaleservice.utils;

import java.util.Random;

/**
 * @author Flyzz
 */
public class OrderIdGenerator {

    private static final SnowflakeIdWorker orderSnowflakeIdWorker = new SnowflakeIdWorker(new Random().nextInt(32), 1, 1);
    private static final SnowflakeIdWorker taskSnowflakeIdWorker = new SnowflakeIdWorker(new Random().nextInt(32), 1, 1);


    public static Long nextOrderId() {
        return orderSnowflakeIdWorker.nextId();
    }

    public static Long nextOrderTaskId() {
        return taskSnowflakeIdWorker.nextId();
    }
}
