package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.permission.PermissionContextState;
import io.agentscope.core.permission.PermissionMode;
import io.agentscope.core.state.JsonFileAgentStateStore;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.core.tool.file.ReadFileTool;
import io.agentscope.core.tool.file.WriteFileTool;
import jakarta.annotation.Resource;

import java.nio.file.Path;

public class _02ReActAgent常用参数 {



    public static void main(String[] args) {

        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new WriteFileTool());
        toolkit.registerTool(new ReadFileTool());

        // 权限设置
        PermissionContextState permissionContext = PermissionContextState.builder()
                .mode(PermissionMode.DEFAULT)
                .build();

        ReActAgent agent = AgentUtils.getAgent()

                // 设置内存存储目录
                .stateStore(new JsonFileAgentStateStore(Path.of(".wbwagent","demo")))
                .defaultSessionId("150316")
                .toolkit(toolkit)
                .permissionContext(permissionContext)
                .build();

//        String textContent = agent.call(new UserMessage("市面上北京agent开发岗位平均月薪多少")).block().getTextContent();
        String textContent = agent.call(new UserMessage(" 帮我读取 /Users/weibaiwang/Desktop/ai.txt 文件内容")).block().getTextContent();
        System.out.println(textContent);


    }

}
