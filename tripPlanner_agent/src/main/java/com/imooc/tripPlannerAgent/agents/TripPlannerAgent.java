package com.imooc.tripPlannerAgent.agents;

import com.imooc.commons.utils.AgentUtils;
import io.agentscope.core.ReActAgent;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TripPlannerAgent {

    @Resource
    private AgentUtils agentUtils;

    @Bean
    public ReActAgent getSuggestSightAgent() {

        // 说明：结果 JSON 的封装与追加写入 ai.txt 已改为在 Controller(Test) 中用 Java 确定性完成，
        // 不再依赖大模型调用 shell/write 工具，因此这里只需构建一个纯文本行程规划智能体。
        return agentUtils.getReActAgentBuilder("TripPlannerAgent",
                        """
                        你是一个旅游行程规划助手。
                        制定包括旅游景点，小吃，住宿这些方面，
                        并且费用性价比高的旅游行程。
                        """)
                .build();
    }

}
