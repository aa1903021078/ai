package com.alibaba.example.aidemo.demo;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.time.Duration;

public class Demo {
    public static void main(String[] args) {

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://localhost:11434/v1")
                .apiKey("ollama")                       // Ollama 不校验，但别留空字符串
                .modelName("qwen3:0.6b")
                .temperature(0.7)
                .timeout(Duration.ofSeconds(120))       // 本地 CPU 推理慢，默认 60s 容易超时
                .logRequests(true)
                .logResponses(true)
                .build();

        String chat1 = model.chat("你好，用一句话介绍你自己");
        System.out.println(chat1);


        String chat = model.chat("中国的国土面积是多少");
        System.out.println(chat);
    }
}
