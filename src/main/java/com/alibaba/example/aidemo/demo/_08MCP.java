package com.alibaba.example.aidemo.demo;

import io.agentscope.core.model.ToolSchema;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.core.tool.mcp.McpClientBuilder;
import io.agentscope.core.tool.mcp.McpClientBuilder;
import io.agentscope.core.tool.mcp.McpClientWrapper;

import java.time.Duration;
import java.util.List;


public class _08MCP {

    public static void main(String[] args) {

        //创建MCP客户端
        McpClientWrapper baiduMapMCP = McpClientBuilder.create("BaiduMap-mcp")
                //和MCP Server以SSE方式进行通信
                .sseTransport("https://mcp.map.baidu.com/sse?ak=eLitBCJFjyajctJ3QBOcQhjXOCkZz0fQ")
                //请求超时
                .timeout(Duration.ofSeconds(120))
                //异步请求
                .buildAsync()
                .block();

        Toolkit toolkit = new Toolkit();
        toolkit.registerMcpClient(baiduMapMCP).block();

        List<ToolSchema> tools = toolkit.getToolSchemas();

        for (ToolSchema tool : tools) {
            System.out.println("工具: " + tool.getName());
            System.out.println("描述: " + tool.getDescription());
        }

    }
}
