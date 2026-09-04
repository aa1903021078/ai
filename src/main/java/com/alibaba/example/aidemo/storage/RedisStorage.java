package com.alibaba.example.aidemo.storage;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class RedisStorage implements ChatMemoryStore {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {

        String json = redisTemplate.opsForValue().get(memoryId.toString());
        List<ChatMessage> chatMessageList = ChatMessageDeserializer.messagesFromJson(json);

        return chatMessageList;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {

        String json = ChatMessageSerializer.messagesToJson(list);
        redisTemplate.opsForValue().set(memoryId.toString(), json, Duration.ofDays(1));
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(memoryId.toString());
    }
}




