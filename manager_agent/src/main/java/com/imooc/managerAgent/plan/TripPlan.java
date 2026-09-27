package com.imooc.managerAgent.plan;

import io.agentscope.core.plan.PlanNotebook;

/**
 * 自定义 Agent自主分解旅游规划任务
 */
public class TripPlan {

    public PlanNotebook getPlan(){
        return  PlanNotebook.builder()
                // 是否需要用户确定
                .needUserConfirm(false)
                .maxSubtasks(6)
                .build();
    }

}
