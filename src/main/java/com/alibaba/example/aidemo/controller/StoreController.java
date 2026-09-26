package com.alibaba.example.aidemo.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StoreController {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private VectorStore vectorStore;

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;




    /**
     * http://localhost:8080/rag4aiops?msg=00000
     * http://localhost:8080/rag4aiops?msg=C2222
     * @param msg
     * @return
     */
    @GetMapping("/rag4aiops")
    public Flux<String> rag(String msg)
    {
        String systemInfo = """
                你是一个运维工程师,按照给出的编码给出对应故障解释,否则回复找不到信息。
                """;

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build())
                .build();

        return chatClient
                .prompt()
                .system(systemInfo)
                .user(msg)
                .advisors(advisor)
                .stream()
                .content();
    }

    @GetMapping("text2mbed")
    public EmbeddingResponse text2Embed(String msg){

        EmbeddingResponse call = embeddingModel.call(new EmbeddingRequest(List.of(msg),
                 DashScopeEmbeddingOptions.builder().withModel("text-embedding-v3")
                        .build()));
        System.out.println(call.getResult().getOutput());

        return call;
    }

    @GetMapping("vetor")
    public void em(){
        List<Document> documents = List.of(
                new Document("我用华为看战狼"),
                new Document("余大嘴的车就是好,不接受反驳"),
                new Document("华为未来看大嘴")

        );

        vectorStore.add(documents);
    }


    @GetMapping("get")
    public List getAll(String msg){

        SearchRequest build = SearchRequest.builder()
                .query(msg)
                .topK(3)
                .build();
        List<Document> documents = vectorStore.similaritySearch(build);
        System.out.println(documents);

        return documents;
    }

}














