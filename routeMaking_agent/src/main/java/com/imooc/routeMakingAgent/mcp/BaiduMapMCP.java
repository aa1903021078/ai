package com.imooc.routeMakingAgent.mcp;

import io.agentscope.core.tool.mcp.McpClientBuilder;
import io.agentscope.core.tool.mcp.McpClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
public class BaiduMapMCP {

    // 获取客户端并建立连接
    public McpClientWrapper getBaiduMpMCP(){
        //和MCP Server以SSE方式进行通信
        McpClientWrapper baiduMapMCP = McpClientBuilder.create("BaiduMap-mcp")
                .sseTransport("https://mcp.map.baidu.com/sse?ak=eLitBCJFjyajctJ3QBOcQhjXOCkZz0fQ")
                .timeout(Duration.ofSeconds(120))
                // 异步请求
                .buildAsync()
                .block();

        Optional<McpClientWrapper> mcpClientWrapper = Optional.ofNullable(baiduMapMCP);
        if (mcpClientWrapper.isPresent()){
            log.info("==================");
            log.info("百度MCP客户端已经创建");
            log.info("==================");

            baiduMapMCP.initialize().block();

            // 获取MCP服务端工具列表
            if (baiduMapMCP.isInitialized()) log.info("百度地图MCP 客户端初始化成功！加在工具列表如下");
        }
        return baiduMapMCP;
    }

}












