package com.yvie.ai.controller;

import com.yvie.ai.entity.vo.MessageVO;
import com.yvie.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {
    private final ChatHistoryRepository chatHistoryRepository; // 编程面向接口（ChatHistoryRepository），运行时注入实现（InMemoryChatHistoryRepository）

    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) { // 从URL路径中提取参数。getChatIds；http客户端传入参数，服务端解析参数，转换为JSON响应
        return chatHistoryRepository.getChatIds(type); // getChatIds：Java方法调用，获取指定类型的对话ID列表，返回原始数据
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if (messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}
