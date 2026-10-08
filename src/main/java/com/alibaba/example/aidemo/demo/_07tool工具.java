package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import com.alibaba.example.aidemo.utils.Weather;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.Agent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.event.TextBlockDeltaEvent;
import io.agentscope.core.event.ToolResultTextDeltaEvent;
import io.agentscope.core.message.ToolResultBlock;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.state.AgentState;
import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolEmitter;
import io.agentscope.core.tool.ToolParam;
import io.agentscope.core.tool.Toolkit;

public class _07tool工具 {


    public static void main(String[] args) {
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new ToolWeather());

        ReActAgent agent = AgentUtils.getAgent()
                // 挂载工具
                .toolkit(toolkit)
                .build();

        agent.streamEvents(new UserMessage("北京天气怎么样")).doOnNext(e -> {
            if (e instanceof TextBlockDeltaEvent event){
                System.out.print(event.getDelta());
            } else if (e instanceof ToolResultTextDeltaEvent event) { // 打印工具执行的日志信息
                System.out.println(event.getDelta());

            }
        }).blockLast();

    }

}


class ToolWeather{

    @Tool(description = "获取天气",stateInjected=true)
    public String getWeather(
            @ToolParam(name = "城市名字", description = "城市名字") String city,
            RuntimeContext runtimeContext,
            Agent agent,
            ToolEmitter toolEmitter,
            AgentState agentState
    ){
        toolEmitter.emit(ToolResultBlock.text("查询天气信息中....."));
        String res ="城市:"+city+"天气晴朗,一揽无云";
        toolEmitter.emit(ToolResultBlock.text("天气信息查询完成"));

        return res;
    }

}
