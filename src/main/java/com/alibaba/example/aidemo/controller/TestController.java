package com.alibaba.example.aidemo.controller;

import com.alibaba.example.aidemo.records.Student;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class TestController {

    @Resource(name = "deepseek")
    private ChatModel chatModel;

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;

    @Value("classpath:/prompttemplate/atguigu-template.txt")
    private org.springframework.core.io.Resource userTemplate;


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "你是什么模型") String msg) {
        String call = chatModel.call(msg);
        return call;
    }

    @GetMapping("/chat1")
    public String chatStream(@RequestParam(value = "msg", defaultValue = "你是什么模型") String msg) {
        String content = chatClient.prompt().user(msg).call().content();
        return content;
    }

    /**
     * 提示词模板
     */
    @GetMapping("chat2")
    public Flux<String> chat2(@RequestParam("title") String title,
                             @RequestParam("temple") String temple,
                             @RequestParam("size") String size){
        PromptTemplate promptTemplate = new PromptTemplate("""
                帮我写一个关于{title}的故事,
                格式遵循{temple},    
                字数{size}
                """);
        Prompt prompt = promptTemplate.create(Map.of(
                "title",title,
                "temple",temple,
                "size",size
        ));

        return chatClient.prompt(prompt).stream().content();
    }

    /**
     * 提示词模板配置
     */
    @GetMapping("chat3")
    public Flux<String> chat3(@RequestParam("title") String title,
                              @RequestParam("temple") String temple,
                              @RequestParam("size") String size){
        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "title",title,
                "temple",temple,
                "size",size
        ));

        return chatClient.prompt(prompt).stream().content();
    }

    /**
     * 系统消息提示词
     */
    @GetMapping("chat4")
    public Flux<String> chat4(@RequestParam("title") String title,
                              @RequestParam("temple") String temple,
                              @RequestParam("size") String size){
        Map<String,Object> map = Map.of("title", title, "temple",temple,"size", size);
        // 构造模板对象，渲染占位符
        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);
        String userContent = promptTemplate.render(map);

        return chatClient
                .prompt()
                .system("你是一个做菜小助手，不予回答做菜之外的话题")
                .user(userContent)
                .stream()
                .content();

    }


    /**
     * 格式化输出
     */
    @GetMapping("chat5")
    public Student chat5(@RequestParam("name") String name,
                              @RequestParam("gender") String gender,
                              @RequestParam("mer") String mer){
        String stringTemplate = """
               学号1002，我叫{name},性别{gender}，大学专业{mer}            
                """;

        return chatClient.prompt()
                .user(promptUserSpec ->  promptUserSpec.text(stringTemplate)
                .param("name",name)
                .param("gender",gender)
                .param("mer",mer))
                .call()
                .entity(Student.class);
    }

    /**
     * 聊天记忆
     */


    @GetMapping("/chat6")
    public String chat6(@RequestParam("msg") String msg,
                        @RequestParam("id") String id) {
        return chatClient.prompt()
                .user(msg)
                // 指定会话id
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, id))
                .call()
                .content();
    }

}










