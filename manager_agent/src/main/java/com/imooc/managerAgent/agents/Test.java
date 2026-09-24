package com.imooc.managerAgent.agents;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;

import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.classpath.ClasspathSkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;


public class Test {

    public static void main(String[] args) throws Exception {

        DashScopeApi dashScope = DashScopeApi.builder()
                .apiKey("sk-ws-H.PMPLELM.4SZm.MEQCICrWkumM7FKYq2PGINGBPaoFh3N5HGpkinxO_MG2ZQO7AiBx0QhPBuhs3PDy2wEMOeDZBiInFuUVTM0na2JDDaQgVQ")
                .build();

        ChatModel chatModel = DashScopeChatModel.builder()

                .dashScopeApi(dashScope)
                .build();

        // 加在skills文件
        ClasspathSkillRegistry classpathSkillRegistry = ClasspathSkillRegistry.builder()
                .classpathPath("skills")
                .build();

        SkillsAgentHook skillsAgentHook = SkillsAgentHook.builder()
                .skillRegistry(classpathSkillRegistry)
                .build();

        ReactAgent agent = ReactAgent.builder()
                .name("测试")
                .model(chatModel)
                .hooks(List.of(skillsAgentHook))
                .build();


        //运行 Agent
        AssistantMessage response = agent.call("帮我规划一下北京到上海的行程" );

        //打印出Agent的回答
        System.out.println(response.getText());
    }

}
