package com.platform.lxp.chat.service.Impl;

import com.platform.lxp.chat.eneity.entity.ChatMessage;
import com.platform.lxp.chat.eneity.entity.MessageThread;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 来晓璞
 * @date 2026/4/5 22:43
 * @Description:
 */
public interface ChatService {
    // 获取或创建会话
    MessageThread getOrCreateThread(Long currentUserId, Long targetUserId, Long goodId);
    // 获取历史消息
    List<ChatMessage> getHistoryMessages(Long threadId);
    // 获取会话列表
    List<Map<String, Object>> getThreadList(Long userId);
    // 保存消息并更新会话状态
    void saveMessage(Long threadId, Long senderId, Long receiverId, String content, String type);



    // 发送消息（核心业务）
    void sendMessage(Long senderId, Long receiverId, String content, String type);

    // 标记已读
    void markAsRead(Long threadId, Long userId);
}
