package om.imooc.controller;

import com.imooc.commons.utils.PromptUtils;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import jakarta.annotation.Resource;
import om.imooc.store.CeShi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Tert {

    @Resource
    private CeShi ceShi;

    @GetMapping("ts")
    public String t(String msg,String userId,String sessionId){
        ReActAgent agent = ceShi.getStoreAgent();
        Msg r = agent.call(List.of(new UserMessage(msg)),
                RuntimeContext.builder().userId(userId).sessionId(sessionId).build()).block();

        return r.getTextContent();
    }

}
