package com.imooc.managerAgent.agents;

import com.imooc.commons.data.ResponseSchema;
import com.imooc.commons.utils.AgentUtils;
import com.imooc.commons.utils.ToolUtils;
import com.imooc.managerAgent.hook.PlanHook;
import com.imooc.managerAgent.plan.TripPlan;
import com.imooc.managerAgent.tool.RemoteAgentTool;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.Event;
import io.agentscope.core.message.ContentBlock;
import io.agentscope.core.message.TextBlock;
import io.agentscope.core.model.StructuredOutputReminder;

import io.agentscope.core.tool.Toolkit;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 主管agent
 */
@Component
public class ManagerAgent {

    @Resource
    private AgentUtils agentUtils;

    private ReActAgent agent;

    public ReActAgent getManagerAgent(){

        TripPlan tripPlan = new TripPlan();
        PlanNotebook planNotebook = tripPlan.getPlan();

        //Toolkit
        ToolUtils toolUtils = new ToolUtils();


        agent = agentUtils.getReActAgentBuilder("主管agent","负责用户需求的解决方案和执行计划制定, 以及任务分发")
                .sysPrompt("""
                    你是一个旅游管理主管。
                    当用户要求你规划旅游行程时，
                    请先创建一个详细的计划，
                    以及执行计划步骤,
                    并对每个计划步骤,要列出擅长执行这个步骤任务的Agent
                    然后按计划逐步执行。
                    """)
                .planNotebook(planNotebook)
                .hook(new PlanHook(planNotebook))
                .toolkit(toolUtils.getToolkit(new RemoteAgentTool()))
                //结构化输出
                .structuredOutputReminder(StructuredOutputReminder.PROMPT)
                .build();


        return agent;
    }


    public List<ContentBlock> run(String prompt,ReActAgent agent){

        Flux<Event> streamed = agentUtils.streamResponse(agent, prompt);

        List<ContentBlock> content = streamed.blockLast().getMessage().getContent();
        return content;
    }

}
