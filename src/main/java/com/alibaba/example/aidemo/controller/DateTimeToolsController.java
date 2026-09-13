package com.alibaba.example.aidemo.controller;

import com.alibaba.example.aidemo.utils.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class DateTimeToolsController{

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;

    @GetMapping("time")
    public Flux<String> chat(String msg){
        return chatClient.prompt(msg)
                .tools(new DateTimeTools())
                .stream()
                .content();
    }




}
