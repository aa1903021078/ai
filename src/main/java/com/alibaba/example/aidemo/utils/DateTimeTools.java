package com.alibaba.example.aidemo.utils;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

public class DateTimeTools {

    // 工具名必须是 ASCII（^[a-zA-Z0-9_-]+$）；用中文名会导致 DashScope 端名称回传错位，
    // 报 "No ToolCallback found for tool name: getNowTime"。描述可以用中文。
    @Tool(name = "getCurrentDateTime", description = "获取当前的日期和时间")
    public LocalDateTime getNowTime(){
        return LocalDateTime.now();
    }

}
