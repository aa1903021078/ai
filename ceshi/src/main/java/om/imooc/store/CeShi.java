//package om.imooc.store;
//
//import com.imooc.commons.utils.AgentUtils;
//import io.agentscope.core.ReActAgent;
//import io.agentscope.extensions.redis.state.RedisAgentStateStore;
//import jakarta.annotation.Resource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CeShi {
//
//    @Resource
//    private AgentUtils agentUtils;
//
//    @Resource
//    private RedisAgentStateStore redisStore;
//
//    @Bean
//    public ReActAgent getStoreAgent(){
//       return agentUtils.getReActAgentBuilder("代码助手","你是一个10年全站开发工程师,删除java和前端")
//                .stateStore(redisStore)
//                .build();
//
//    }
//
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
