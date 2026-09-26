package com.alibaba.example.aidemo.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * @Description 知识出处
 * https://help.aliyun.com/zh/model-studio/text-to-image?spm=a2c4g.11186623.help-menu-2400256.d_0_5_0.1a457d9dv6o7Kc&accounttraceid=6ec3bf09599f424a91a2a88b27b31570nrdd
 */
@RestController
public class VideoController
{
    // img model
    public static final String IMAGE_MODEL = "wanx2.1-t2i-turbo";

    public static final String DASHSCOPE_HOST = "https://dashscope.aliyuncs.com";

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private ImageModel imageModel;

    @Resource
    private SpeechSynthesisModel speechSynthesisModel;

    // voice model
    public static final String BAILIAN_VOICE_MODEL = "cosyvoice-v2";
    // voice timber 音色列表：https://help.aliyun.com/zh/model-studio/cosyvoice-java-sdk#722dd7ca66a6x
    public static final String BAILIAN_VOICE_TIMBER = "longyingcui";//龙应催


    /**
     * http://localhost:8080/t2v/voice
     * @param msg
     * @return
     */
    @GetMapping("/t2v/voice")
    public String voice(@RequestParam(name = "msg",defaultValue = "温馨提醒，支付宝到账100元请注意查收") String msg)
    {
        String filePath = "d:\\" + UUID.randomUUID() + ".mp3";

        //1 语音参数设置
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .model(BAILIAN_VOICE_MODEL)
                .voice(BAILIAN_VOICE_TIMBER)
                .build();

        //2 调用大模型语音生成对象
        SpeechSynthesisResponse response = speechSynthesisModel.call(new SpeechSynthesisPrompt(msg, options));

        //3 字节流语音转换
        ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();

        //4 文件生成
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath))
        {
            fileOutputStream.write(byteBuffer.array());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //5 生成路径OK
        return filePath;
    }


    /**
     * http://localhost:8080/t2i/image
     * @param prompt
     * @return
     */
    @GetMapping(value = "/t2i/image")
    public ResponseEntity<byte[]> image(@RequestParam(name = "prompt",defaultValue = "刺猬") String prompt)
    {
        String url = imageModel.call(
                        new ImagePrompt(prompt, DashScopeImageOptions.builder().withModel(IMAGE_MODEL).build())
                )
                .getResult()
                .getOutput()
                .getUrl();
        System.out.println(url);

        // DashScope返回的是24小时有效的OSS临时签名地址,直接发给浏览器容易过期打不开;
        // 由服务端立即下载并直接返回图片字节,浏览器访问该接口即可直接显示
        // 注意: uri(String)会把URL里的%二次编码导致签名失效,必须传URI对象原样请求
        try
        {
            byte[] imageBytes = RestClient.create().get().uri(URI.create(url)).retrieve().body(byte[].class);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (Exception e) {
            System.out.println("图片下载失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(("图片下载失败,原始地址: " + url).getBytes());
        }
    }


}
