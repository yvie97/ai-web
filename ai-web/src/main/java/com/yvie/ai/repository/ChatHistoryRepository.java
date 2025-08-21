package com.yvie.ai.repository;

import java.util.List;

public interface ChatHistoryRepository {
    /**
     * 保存会话记录
     * @param type 业务类型，如：chat、service、pdf
     * @param chatId 会话ID
     */
    void save(String type, String chatId);

    /**
     * 保存会话记录
     * @param type 业务类型，如：chat、service、pdf
     * @return 会话ID列表
     */
    List<String> getChatIds(String type);

    // 删除会话记录等，可自行实现
}
