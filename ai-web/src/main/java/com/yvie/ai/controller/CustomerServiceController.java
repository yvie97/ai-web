package com.yvie.ai.controller;

import com.yvie.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor // Lombok注解，自动生成包含所有final字段的构造函数，用于Spring容器创建对象和依赖注入
@RestController // 组合了@Controller和@ResponseBody
@RequestMapping("/ai")
public class CustomerServiceController {
    // 依赖注入
    private final ChatClient serviceChatClient; // 之前配置的serviceChatClient Bean，用于与AI模型交互

    private final ChatHistoryRepository chatHistoryRepository; // 聊天历史仓库，用于存储对话相关信息

    @RequestMapping(value = "/service", produces = "text/html;charset=utf-8") // 中文编码
    public Flux<String> service(String prompt, String chatId) { // prompt用户输入的聊天内容，chatId用于区分不同用户的对话，均从前端的请求参数中获取
        // 1. 保存会话ID
        chatHistoryRepository.save("service", chatId);
        // 2. 请求模型
        return serviceChatClient.prompt()
                .user(prompt) // 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) // 设置对话ID
                .stream()
                .content(); // 提取响应内容，返回字符串流
    }
}
