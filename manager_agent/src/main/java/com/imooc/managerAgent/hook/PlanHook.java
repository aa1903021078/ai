package com.imooc.managerAgent.hook;

import io.agentscope.core.agent.user.UserAgent;
import io.agentscope.core.hook.Hook;
import io.agentscope.core.hook.HookEvent;
import io.agentscope.core.hook.PostReasoningEvent;
import io.agentscope.core.plan.PlanNotebook;
import io.agentscope.core.plan.model.Plan;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class PlanHook implements Hook {

    private final UserAgent userAgent;

    private final PlanNotebook plan;

    //第n轮思考
    private int thinkingNum = 1;


    public PlanHook(PlanNotebook planNotebook){
        this.userAgent = UserAgent.builder()
                        .name("User")
                .build();
        this.plan = planNotebook;
    }



    @Override
    public <T extends HookEvent> Mono<T> onEvent(T event) {

        //推理思考事件
        switch (event){
            case PostReasoningEvent e -> {
                String reason = e.getReasoningMessage().getTextContent();
                String agentName = e.getReasoningMessage().getName();
                if(reason != null) {
                    log.info("=============="+agentName+" 第 "+ thinkingNum +" 轮思考：==================");
                    log.info(reason);
                }
                thinkingNum++;

                Plan currentPlan = plan.getCurrentPlan();
                if (currentPlan != null){
//                    System.out.println("请输入修改意见: ");
//                    userAgent.call().block();
                }

            }


            default -> {

            }

        }


        return Mono.just(event);
    }

}
