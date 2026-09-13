package com.alibaba.example.aidemo.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @auther zzyy
 * @create 2025-07-22 0:51
 */
@Configuration
public class SaaLLMConfig
{
    @Value("${spring.ai.dashscope.api-key}")
    public String key;

    private final String DEEPSEEK_MODEL = "deepseek-v3";
    private final String QWEN_MODEL = "qwen-max";

    @Bean(name = "deepseek")
    public ChatModel deepSeek(){

        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(key).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
                .build();
    }

    @Bean(name = "qwen")
    public ChatModel qwen(){
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(key).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                .build();
    }

    @Bean(name = "deepseekChatClient")
    public ChatClient deepseekClient(@Qualifier("deepseek") ChatModel chatModel){
        return ChatClient.builder(chatModel)
                // 必须用 DashScopeChatOptions（实现了 ToolCallingChatOptions），
                // 否则请求级 .tools(...) 的回调会被丢弃，模型收不到工具
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
                .build();
    }

    @Bean(name = "qwenChatClient")
    public ChatClient qwenClient(@Qualifier("qwen") ChatModel chatModel,
                                 RedisChatMemoryRepository redisChatMemoryRepository){

        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(30)
                .build();

        return ChatClient.builder(chatModel)
                // 必须用 DashScopeChatOptions（实现了 ToolCallingChatOptions），
                // 普通 ChatOptions 会导致 .tools(...) 工具回调丢失、模型不调用工具
                .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }


    /**
     * 带有MCP的配置
     */
    @Bean(name = "qwenChatClientMCP")
    public ChatClient qwenChatClientMCP(@Qualifier("qwen") ChatModel chatModel, ToolCallbackProvider tools) {
        return ChatClient.builder(chatModel)
                .defaultToolCallbacks(tools.getToolCallbacks())  //mcp协议，配置见yml文件
                .build();
    }



}






