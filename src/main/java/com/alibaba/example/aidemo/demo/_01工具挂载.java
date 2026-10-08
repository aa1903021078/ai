package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import com.alibaba.example.aidemo.utils.Weather;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.tool.Toolkit;

public class _01工具挂载 {

    public static void main(String[] args) {

        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new Weather());

        ReActAgent agent = AgentUtils.getAgent()
                // 挂载工具
                .toolkit(toolkit)
                .build();

        String textContent = agent.call(new UserMessage("北京的天气怎么样")).block().getTextContent();
        System.out.println(textContent);

    }

}
