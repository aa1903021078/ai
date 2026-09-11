package com.alibaba.example.aidemo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource(name = "deepseek")
    private ChatModel chatModel;

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;



    @GetMapping("/chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "你是什么模型") String msg) {
        String call = chatModel.call(msg);
        return call;
    }

    @GetMapping("/chat1")
    public String chatStream(@RequestParam(value = "msg", defaultValue = "你是什么模型") String msg) {
        String content = chatClient.prompt().user(msg).call().content();
        return content;
    }


}
