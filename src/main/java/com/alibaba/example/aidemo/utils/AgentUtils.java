package com.alibaba.example.aidemo.utils;

import io.agentscope.core.ReActAgent;
import io.agentscope.extensions.model.dashscope.DashScopeChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AgentUtils {

    public static ReActAgent.Builder getAgent(){

        return ReActAgent.builder()
                .model(DashScopeChatModel.builder()
                        .apiKey(Const.API_KEY)
                        .modelName(Const.MODEL_NAME)
                        .build());

    }


    @Bean("agentSin")
    public static ReActAgent getAgentSin(){

        return ReActAgent.builder()
                .model(DashScopeChatModel.builder()
                        .apiKey(Const.API_KEY)
                        .modelName(Const.MODEL_NAME)
                        .build())
                .build();

    }

}
