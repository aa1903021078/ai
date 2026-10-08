package com.alibaba.example.aidemo.demo;

import com.alibaba.example.aidemo.utils.AgentUtils;
import io.agentscope.core.ReActAgent;
import io.agentscope.harness.agent.HarnessAgent;
import redis.clients.jedis.JedisPooled;

public class _05会话存储 {
    public static void main(String[] args) {

        // 当前版本想要redis持久化需要自己创建存储类,实现AgentStateStore接口
//        JedisPooled jedis = new JedisPooled("redis://redis.prod:6379");
//        ReActAgent agent = AgentUtils.getAgent()
//                .stateStore(new RedisAgentStateStore(jedis))
//                .distributedStore(RedisDistributedStore.fromJedis(jedis))
//                .build();



    }
}
