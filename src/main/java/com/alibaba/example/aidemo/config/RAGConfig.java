package com.alibaba.example.aidemo.config;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class RAGConfig {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private VectorStore vectorStore;


    @Value("classpath:ops.txt")
    private Resource opsFile;

    @PostConstruct
    public void  init(){

        //1. 读取文件
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(Charset.defaultCharset());

        //2. 分词
        List<Document> documents = new TokenTextSplitter().transform(textReader.read());

        //3. 去重复
        String source = (String) textReader.getCustomMetadata().get("source");
        String fileName = SecureUtil.md5(source);

        Boolean b = redisTemplate.opsForValue().setIfAbsent(fileName, "1");
        if (Boolean.TRUE.equals(b)){
            // 一个文件只初始化一次
            vectorStore.add(documents);
        }else {
            System.out.println("------向量初始化数据已经加载过，请不要重复操作");
        }

    }


}















