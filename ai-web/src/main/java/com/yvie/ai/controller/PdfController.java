package com.yvie.ai.controller;

import com.yvie.ai.entity.vo.Result;
import com.yvie.ai.repository.ChatHistoryRepository;
import com.yvie.ai.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/pdf")
public class PdfController {

    private final FileRepository fileRepository;

    private final VectorStore vectorStore;

    private final ChatClient pdfChatClient; // 之前配置的ChatClient Bean，用于与AI模型交互

    private final ChatHistoryRepository chatHistoryRepository; // 聊天历史仓库，用于存储对话相关信息

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8") // 中文编码
    public Flux<String> chat(String prompt, String chatId) { // prompt用户输入的聊天内容，chatId用于区分不同用户的对话，均从前端的请求参数中获取
        // 1.找到会话文件
        Resource file = fileRepository.getFile(chatId);
        if (!file.exists()) { // 如果文件不存在
            throw new RuntimeException("Conversation file not found!");
        }
        // 2.保存会话ID
        chatHistoryRepository.save("pdf", chatId);
        // 3.请求模型
        return pdfChatClient.prompt()
                .user(prompt) // 设置用户输入
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)) // 设置对话ID
                .advisors(a -> a.param(FILTER_EXPRESSION, "file_name == '" + file.getFilename() + "'"))
                .stream()
                .content(); // 提取响应内容，返回字符串流
    }
    /**
     * 文件上传
     */
    @RequestMapping("/upload/{chatId}")
    public Result uploadPdf(@PathVariable String chatId, @RequestParam("file") MultipartFile file) {
        try {
            // 1. 校验文件是否为PDF格式
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return Result.fail("Only PDF files are allowed!");
            }
            // 2.保存文件
            boolean success = fileRepository.save(chatId, file.getResource());
            if(! success) {
                return Result.fail("Failed to save file!");
            }
            // 3.写入向量库
            this.writeToVectorStore(file.getResource());
            return Result.ok();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return Result.fail("File upload failed!");
        }
    }

    /**
     * 文件下载
     */
    @GetMapping("/file/{chatId}")
    public ResponseEntity<Resource> download(@PathVariable("chatId") String chatId) throws IOException {
        // 1.读取文件
        Resource resource = fileRepository.getFile(chatId);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 2.文件名编码，写入响应头
        String filename = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8);
        // 3.返回文件
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    private void writeToVectorStore(Resource resource) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3.写入向量库
        vectorStore.add(documents);
    }
}