package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.Const;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.Msg;
import io.agentscope.extensions.model.dashscope.DashScopeChatModel;
import io.agentscope.harness.agent.HarnessAgent;
import io.agentscope.harness.agent.memory.compaction.CompactionConfig;

public class Demo {
    public static void main(String[] args) {

        System.out.println(block());

    }


    public static String block(){
        HarnessAgent agent = HarnessAgent.builder()
                .model(DashScopeChatModel.builder()
                        .apiKey(Const.API_KEY)
                        .modelName("qwen-plus")
                        .build())
                .compaction(CompactionConfig.builder()
                        .triggerMessages(30)
                        .keepMessages(10)
                        .build())
                .build();
        RuntimeContext context = RuntimeContext.builder()
                .userId("12")
                .sessionId("bubu")
                .build();
        System.out.println(agent.getDefaultSessionId());

//        Msg msg = agent.call(Msg.builder().textContent("我是00年生的,属性是十二生肖的什么").build()).block();
        Msg msg = agent.call(Msg.builder().textContent("我今年多大").build(),context).block();
        return msg.getTextContent();

    }

}
