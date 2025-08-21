package com.yvie.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class GameController {
    // 依赖注入
    private final ChatClient gameChatClient; // 之前配置的ChatClient Bean，用于与AI模型交互

    @RequestMapping(value = "/game", produces = "text/html;charset=utf-8") // 中文编码
    public Flux<String> chat(String prompt, String chatId) { // prompt用户输入的聊天内容，chatId用于区分不同用户的对话，均从前端的请求参数中获取
        return gameChatClient.prompt()
                .user(prompt) // 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) // 设置对话ID
                .stream()
                .content(); // 提取响应内容，返回字符串流
    }
}
