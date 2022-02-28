package cn.flyzzgo.flashsaleservice.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Flyzz
 */
public class OrderIdGenerator {

   private static final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(new Random().nextInt(32),1,1);

   public static Long nextOrderId() {
      return snowflakeIdWorker.nextId();
   }
}
