package com.itheima.consumer.lister;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQLister {

    @RabbitListener(queues = "wbw.queue5")
    public void lister(String msg){
      log.info("消费者接收到消息:{}", msg);
    }

}
