package com.imooc.routeMakingAgent.agents;

import com.imooc.commons.utils.AgentUtils;
import com.imooc.commons.utils.ToolUtils;
import com.imooc.routeMakingAgent.mcp.BaiduMapMCP;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.core.tool.mcp.McpClientWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RouteMakingAgent {

    @Resource
    private AgentUtils agentUtils;

    @Resource
    private BaiduMapMCP baiduMapMCP;

    @Bean
    public ReActAgent x(){

        McpClientWrapper mcp = baiduMapMCP.getBaiduMpMCP();
        ToolUtils toolUtils = new ToolUtils();
        // 注册mcp
        Toolkit toolkit = toolUtils.getToolkit(mcp);
        // 打印所有工具
        toolUtils.getTools();


        return agentUtils.getReActAgentBuilder("RouteMakingAgent","擅长制定性价比最优的驾车路线")
                .sysPrompt("""
                    你是一个驾车路线制定助手。
                    请调用合适的API接口，
                    制定包括行程距离，高速费用这些方面，
                    性价比最优的路线方案。
                    """
                )
                // 添加工具包
              //  .toolkit(toolkit)
                .build();

    }


}


































