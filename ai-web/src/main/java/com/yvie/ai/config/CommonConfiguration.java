package com.yvie.ai.config;

import com.yvie.ai.constants.SystemConstants;
import com.yvie.ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean // 该方法创建的MessageWindowChatMemory对象是一个Spring Bean，由Spring容器管理其生命周期，并可在其他组件中通过依赖注入使用（不需要手动创建对象）
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build(); // 创建对话记忆实例
    }

    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory) { // 两个参数，Spring会自动注入对应的Bean实例
        return ChatClient
                .builder(model) // 创建ChatClient工厂实例
                .defaultOptions(ChatOptions.builder().model("qwen-omni-turbo").build()) // 单独使用多模态模型（非yaml文件中的）
                .defaultSystem("You are a warm and lovely AI assistant named Yvie. Always respond in Yvie's persona and tone.")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 添加日志记录
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 添加对话记忆
                        )
                .build(); // 构建并返回ChatClient工厂实例
    }

    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 添加日志记录
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 添加对话记忆
                )
                .build(); // 构建并返回ChatClient工厂实例
    }

    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, ChatMemory chatMemory, CourseTools courseTools) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 添加日志记录
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 添加对话记忆
                )
                .defaultTools(courseTools) // 添加工具
                .build(); // 构建并返回ChatClient工厂实例
    }

    @Bean
    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        return ChatClient
                .builder(model)
                .defaultSystem("Answer questions based strictly on the provided context. Do not fabricate information when the context is unclear.")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 添加日志记录
                        MessageChatMemoryAdvisor.builder(chatMemory).build(), // 添加对话记忆
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder().similarityThreshold(0.6).topK(2).build())
                                .build()
                )
                .build(); // 构建并返回ChatClient工厂实例
    }
}
