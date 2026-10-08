package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.skill.repository.FileSystemSkillRepository;

import java.nio.file.Paths;

public class _09Skill {
    public static void main(String[] args) {

        ReActAgent agent = AgentUtils.getAgent()
                .skillRepository(new FileSystemSkillRepository(Paths.get("/Users/weibaiwang/Desktop/AAA/work/ai-demo/src/main/resources/Make-Table"), false))
                .build();

        String textContent = agent.call(new UserMessage("帮我推荐豆瓣评分top100的电影并制作出表格")).block().getTextContent();
        System.out.println(textContent);

    }
}
