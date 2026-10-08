package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import com.alibaba.example.aidemo.utils.Weather;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.permission.PermissionBehavior;
import io.agentscope.core.permission.PermissionContextState;
import io.agentscope.core.permission.PermissionMode;
import io.agentscope.core.permission.PermissionRule;
import io.agentscope.core.tool.Toolkit;

public class _06权限 {
    public static void main(String[] args) {
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new Weather());
        PermissionContextState permCtx =
                PermissionContextState.builder()
                        .mode(PermissionMode.DEFAULT)
                        .addAskRule("write",new PermissionRule("write",null, PermissionBehavior.ASK,""))
                        .build();
        ReActAgent agent = AgentUtils.getAgent()
                .permissionContext(permCtx)
                .toolkit(toolkit)
                .build();

        // msg消息的metadata返回里面存在PERMISSION_ASKING, 表示需要请求权限
        Msg msg = agent.call(new UserMessage("今天天气怎么样")).block();
        System.out.println(msg.getTextContent());

    }
}
