package com.imooc.managerAgent;

import com.imooc.commons.data.ResponseSchema;
import com.imooc.managerAgent.agents.ManagerAgent;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.ContentBlock;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Resource
    private ManagerAgent managerAgent;

    @GetMapping("/m")
    public List<ContentBlock> call(String msg){
        ReActAgent agent = managerAgent.getManagerAgent();
        return managerAgent.run(msg,agent);
    }

}
