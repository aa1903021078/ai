package com.alibaba.example.aidemo.utils;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;

public class Weather {

    @Tool(description = "获取指定城市的天气信息")
    public String getWeather(@ToolParam(name = "城市名字", description = "城市名字") String city){
        return "城市:"+city+"天气晴朗,一揽无云";

    }

}
