package com.alibaba.example.aidemo.controller;


import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class MCPController {


    @Resource(name = "qwenChatClientMCP")
    private ChatClient chatClient;//使用mcp支持


    // http://localhost:8080/mcpclient/chat?msg=上海
    @GetMapping("/mcpclient/chat")
    public Flux<String> chat(@RequestParam(name = "msg",defaultValue = "北京") String msg)
    {
        System.out.println("使用了mcp");
        return chatClient.prompt(msg).stream().content();
    }


}
