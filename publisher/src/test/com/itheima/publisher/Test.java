package com.itheima.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class Test {//

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @org.junit.jupiter.api.Test
    public void testSimQueue(){

        rabbitTemplate.convertAndSend("wbw.queue5", "hello,itheima.news");
    }


    @org.junit.jupiter.api.Test
    public void testQueue(){
        Map<String, Object> map = new HashMap<>();
        map.put("name","里斯");
        rabbitTemplate.convertAndSend("itheima.fanout.queue1", map);
    }
}
