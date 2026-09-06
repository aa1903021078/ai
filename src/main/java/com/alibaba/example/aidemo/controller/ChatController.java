package com.alibaba.example.aidemo.controller;

import com.alibaba.example.aidemo.service.ConsultantService;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chatAi(
            @RequestParam("memoryId") @MemoryId String memoryId,
            @RequestParam("message") @UserMessage String message) {
        Flux<String> r = consultantService.chat(memoryId,message);
        return r;
    }


    @GetMapping(value = "/c")
    public String c() {
        redisTemplate.opsForValue().set("a","test");
        return redisTemplate.opsForValue().get("a");
    }


}
