package com.yvie.ai.controller;

import com.yvie.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor // Lombok注解，自动生成包含所有final字段的构造函数，用于Spring容器创建对象和依赖注入
@RestController // 组合了@Controller和@ResponseBody
@RequestMapping("/ai")
public class ChatController {
    // 依赖注入
    private final ChatClient chatClient; // 之前配置的ChatClient Bean，用于与AI模型交互

    private final ChatHistoryRepository chatHistoryRepository; // 聊天历史仓库，用于存储对话相关信息

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8") // 中文编码
    public Flux<String> chat(
            @RequestParam("prompt") String prompt,
            @RequestParam("chatId") String chatId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) { // prompt用户输入的聊天内容，chatId用于区分不同用户的对话，均从前端的请求参数中获取
        // 1. 保存会话ID
        chatHistoryRepository.save("chat", chatId);
        // 2. 请求模型
        if (files == null || files.isEmpty()) {
            // 没有附件，纯文本聊天
            return textChat(prompt, chatId);
        } else {
            // 有附件，多模态聊天
            return multiModalChat(prompt, chatId, files);
        }
    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        // 1.解析多媒体
        List<Media> medias = files.stream()
                .map(file -> new Media(
                        MimeType.valueOf(Objects.requireNonNull(file.getContentType())),
                        file.getResource()
                        )
                )
                .toList();
        // 2.请求模型
        return chatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new))) // 设置用户输入和多媒体
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) // 设置对话ID
                .stream()
                .content(); // 提取响应内容，返回字符串流
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt) // 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) // 设置对话ID
                .stream()
                .content(); // 提取响应内容，返回字符串流
    }
}
