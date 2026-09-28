//package om.imooc.store;
//
//import com.imooc.commons.data.Const;
//import io.agentscope.extensions.redis.state.RedisAgentStateStore;
//import io.agentscope.extensions.redis.state.RedisClientAdapter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.UnifiedJedis;
//
//@Component
//public class AgentStoreConfig {
//
//    @Bean
//    public RedisAgentStateStore redisAgentStateStore() {
//        // 创建 Jedis 客户端，根据你的 Redis 配置修改连接地址
//        UnifiedJedis client = new UnifiedJedis("redis://127.0.0.1:6379");
//        return RedisAgentStateStore.builder()
//                .jedisClient(client)
//                .keyPrefix(Const.KEY_API)
//                .build();
//    }
//}
