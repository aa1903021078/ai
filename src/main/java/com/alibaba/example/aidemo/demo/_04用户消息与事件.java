package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import com.alibaba.example.aidemo.utils.Weather;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.event.*;
import io.agentscope.core.message.Base64Source;
import io.agentscope.core.message.DataBlock;
import io.agentscope.core.message.TextBlock;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.tool.Toolkit;

public class _04用户消息与事件 {
    public static void main(String[] args) {

        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new Weather());

        ReActAgent agent = AgentUtils.getAgent()
                .toolkit(toolkit)
                .build();

        StringBuilder accumulated = new StringBuilder();

        agent.streamEvents(new UserMessage("帮我查看一下北京的天气"))
                .doOnNext(event -> {
                    if (event instanceof AgentStartEvent start) {
                        System.out.println("[start replyId=" + start.getReplyId() + "]");
                    } else if (event instanceof TextBlockDeltaEvent delta) {
                        accumulated.append(delta.getDelta());
                    } else if (event instanceof ToolCallStartEvent tc) { // 调用工具
                        System.out.println("[tool] " + tc.getToolCallName());
                    } else if (event instanceof ToolResultEndEvent end) { // 调用工具结果
                        System.out.println("[tool result state=" + end.getState() + "]");
                    } else if (event instanceof AgentEndEvent end) {
                        System.out.println("\n[end] full text:\n" + accumulated);
                    }
                })
                .blockLast();

        System.out.println("最终输出:"+accumulated.toString());

    }

    public void buildUserMessage(){
        ReActAgent agent = AgentUtils.getAgent().build();

        UserMessage userMulti =
                new UserMessage(
                        "user",
                        TextBlock.builder().text("描述这张图片：").build(),
                        DataBlock.builder()
                                .source(Base64Source.builder()
                                        .data("")
                                        .mediaType("image/png")
                                        .build())
                                .build());

        String textContent = agent.call(userMulti).block().getTextContent();
        System.out.println(textContent);
    }

}
