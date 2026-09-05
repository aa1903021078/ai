package com.alibaba.example.aidemo.config;

import com.alibaba.example.aidemo.storage.RedisStorage;
import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    @Autowired
    private RedisStorage redisStorage;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private RedisEmbeddingStore redisEmbeddingStore;

    @Bean
    public ChatMemory wbwChatMemory(){
        return MessageWindowChatMemory.builder()
                .maxMessages(100)
                .build();
    }

    @Bean
    public ChatMemoryProvider wbwChatMemoryProvider(){
        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memberId) {
                return MessageWindowChatMemory.builder()
                        .id(memberId)
                        .chatMemoryStore(redisStorage)
                        .maxMessages(100)
                        .build();
            }
        };

        return chatMemoryProvider;
    }

    // @Bean
    public EmbeddingStore store(){
        // 1.加载内存文档,使用读取pdf专用的类读取
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content",new ApachePdfBoxDocumentParser());
        // 2.清空旧的向量数据,避免每次启动重复入库导致检索结果重复
        redisEmbeddingStore.removeAll();
        // 3.构建分词器
        DocumentSplitter ds = DocumentSplitters.recursive(500, 100);

        // 完成文本切割向量化
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
//                .embeddingStore(store)内存存储
                .embeddingStore(redisEmbeddingStore)
                .documentSplitter(ds)
                .embeddingModel(embeddingModel)// 把文本片段向量化
                .build();
        ingestor.ingest(documents);
        return redisEmbeddingStore;
    }

    // 构建向量数据库对象
    @Bean
    public ContentRetriever contentRetriever(/*EmbeddingStore<TextSegment> store*/){
        return EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(store) 内存存储
                .embeddingStore(redisEmbeddingStore)
                .embeddingModel(embeddingModel)
                .minScore(0.5)
                .maxResults(3)
                .build();
    }


}







