package com.itheima.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class QueueConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("itheima.fanout");
    }

    @Bean
    public Queue fanoutQueue1(){
        return new Queue("itheima.fanout.queue1");
    }

    @Bean
    public Binding binding(Queue queue, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }


}
