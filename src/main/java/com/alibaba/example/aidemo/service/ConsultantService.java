package com.alibaba.example.aidemo.service;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        streamingChatModel = "openAiStreamingChatModel",
//        chatMemory = "wbwChatMemory"
        chatMemoryProvider = "wbwChatMemoryProvider",
        contentRetriever = "contentRetriever"
)
public interface ConsultantService {

//   String chat(String message);

//    @SystemMessage("你是一个十年程序员擅长Java+Python开发")
    @SystemMessage(fromResource = "system.txt")
//    @UserMessage("五句话回复,{{it}}")
    @UserMessage("{{msg}}")
    Flux<String> chat(@MemoryId String memoryId, @V("msg") String message);

}
