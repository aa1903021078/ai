package com.imooc.managerAgent.tool;

import com.alibaba.nacos.api.exception.NacosException;
import com.imooc.commons.utils.NacosUtil;
import com.imooc.commons.utils.PromptUtils;
import io.agentscope.core.a2a.agent.A2aAgent;
import io.agentscope.core.message.GenerateReason;
import io.agentscope.core.message.Msg;
import io.agentscope.core.nacos.a2a.discovery.NacosAgentCardResolver;
import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteAgentTool {

    @Tool(description = "擅长制定最优驾驶路线")
    public String callRouteMakingAgent(@ToolParam(name = "prompt", description = "驾车起点和终点") String prompt) throws NacosException {

        log.info("工具方法：路线制定智能体...正在调用中");
        A2aAgent a2aAgent = A2aAgent.builder()
                .name("RouteMakingAgent")
                .agentCardResolver(
                        new NacosAgentCardResolver(NacosUtil.getNacosClient())
                )
                .build();

        log.info("获取到的远程Agent描述：" + a2aAgent.getDescription());
        prompt = "制定最优驾车路线："+prompt+", 并预估费用";
        PromptUtils promptUtils =  new PromptUtils();
        Msg userMsg = promptUtils.getPrompt(prompt);

        log.info("远程 "+ a2aAgent.getName()+" 开始执行任务....");

        // 调用智能体返回
        Msg remoteAgentResponse = a2aAgent.call(userMsg).block();
        System.out.println(remoteAgentResponse.getContent());
        String response = remoteAgentResponse.getTextContent();

        log.info("======= 远程Agent返回 ========");
        log.info(response);

        GenerateReason reason = remoteAgentResponse.getGenerateReason();
        switch (reason){
            case MODEL_STOP:
                // 任务正常完成
                log.info("此轮任务正常完成");
            case INTERRUPTED:
                // 任务被中断
                log.info("此轮任务被中断");
                break;
        }


        return response;

    }


}
















