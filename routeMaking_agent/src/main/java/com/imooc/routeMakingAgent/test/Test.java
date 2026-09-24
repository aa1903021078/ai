package com.imooc.routeMakingAgent.test;

import com.imooc.routeMakingAgent.agents.RouteMakingAgent;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @Resource
    private RouteMakingAgent routeMakingAgent;

    @GetMapping("a")
    public int tesq(){
     return 1;
    }


    @GetMapping("ask")
    public String tes(String msg){
        ReActAgent agent = routeMakingAgent.x();

        Msg res = agent.call(Msg.builder()
                        .textContent(msg)
                .build()).block();

        return res.getTextContent();
    }

}
