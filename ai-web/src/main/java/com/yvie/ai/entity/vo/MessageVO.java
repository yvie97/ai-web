package com.yvie.ai.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

@NoArgsConstructor
@Data // Lombok注解，生成getter和setter方法
public class MessageVO {
    private String role;
    private String content;

    // 将Message对象转换为MessageVO对象，用于返回给前端
    public MessageVO(Message message) {
        switch (message.getMessageType()) {
            case USER:
                role = "user";
                break;
            case ASSISTANT:
                role = "assistant";
                break;
            default:
                role = "";
                break;
        }
        this.content = message.getText();
    }
}
