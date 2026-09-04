package com.alibaba.example.aidemo.config;

import com.alibaba.example.aidemo.storage.RedisStorage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Autowired
    private RedisStorage redisStorage;

    @Bean
    public ChatMemory wbwChatMemory(){
        return MessageWindowChatMemory.builder()
                .maxMessages(100)
                .build();
    }

    @Bean
    public ChatMemoryProvider wbwChatMemoryProvider(){
        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memberId) {
                return MessageWindowChatMemory.builder()
                        .id(memberId)
                        .chatMemoryStore(redisStorage)
                        .maxMessages(100)
                        .build();
            }
        };

        return chatMemoryProvider;
    }

}
