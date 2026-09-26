package com.alibaba.example.aidemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Description 自定义重试模板,修复文生图"Image generation still pending"
 * spring-ai 默认的 RetryTemplate 只重试 TransientAiException,
 * 而 DashScopeImageModel 轮询任务状态时抛的是普通 RuntimeException,
 * 导致任务状态只查询一次(第一次必是PENDING)就放弃。
 * 这里换成重试所有异常的重试模板,让"提交->轮询->完成"流程真正生效。
 */
@Configuration
public class RetryConfig
{
    @Bean
    public RetryTemplate retryTemplate()
    {
        RetryTemplate template = new RetryTemplate();

        // 最多尝试20次,重试所有异常(包括SDK轮询抛出的RuntimeException)
        template.setRetryPolicy(new SimpleRetryPolicy(20));

        // 指数退避:首次等2秒,之后每次×5,最多等30秒
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(2000);
        backOffPolicy.setMultiplier(5);
        backOffPolicy.setMaxInterval(30000);
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }
}
