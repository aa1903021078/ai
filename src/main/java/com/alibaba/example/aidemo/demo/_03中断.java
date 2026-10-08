package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.event.AgentEvent;
import io.agentscope.core.event.TextBlockDeltaEvent;
import io.agentscope.core.message.UserMessage;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class _03中断 {

    @Resource(name = "agentSin")
    private ReActAgent agent;

    @GetMapping(value = "ask",produces = MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8")
    public Flux<String> ask(String msg, String session){

        return agent.streamEvents(new UserMessage(msg),
                RuntimeContext.builder().sessionId(session).userId("wbw").build())
                .filter(agentEvent -> agentEvent instanceof TextBlockDeltaEvent)
                .map(agentEvent -> ((TextBlockDeltaEvent)agentEvent).getDelta())
                ;
    }

    @GetMapping("in")
    public String inter(String session){

        agent.interrupt(RuntimeContext.builder().userId("wbw").sessionId(session).build());

        return "success";
    }


}







