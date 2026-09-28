package com.platform.lxp.chat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.lxp.chat.eneity.entity.ChatMessage;
import com.platform.lxp.chat.eneity.entity.MessageThread;
import com.platform.lxp.chat.mapper.ChatMessageMapper;
import com.platform.lxp.chat.mapper.MessageThreadMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 来晓璞
 * @date 2026/4/5 22:44
 * @Description:
 */
@Service
public class ChatServiceImpl implements ChatService {

    // 1. 声明 final 字段，不再使用 @Resource 或 @Autowired
    private final MessageThreadMapper threadMapper;
    private final ChatMessageMapper messageMapper;

    // 2. 构造器注入
    // Spring 会自动调用这个构造器，并传入 Mapper 的代理对象
    public ChatServiceImpl(MessageThreadMapper threadMapper, ChatMessageMapper messageMapper) {
        this.threadMapper = threadMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageThread getOrCreateThread(Long currentUserId, Long targetUserId, Long goodId) {
        // 1. 尝试查找已存在的会话
        LambdaQueryWrapper<MessageThread> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageThread::getStatus, 1)
                .and(w -> {
                    w.eq(MessageThread::getParticipantAId, currentUserId)
                            .eq(MessageThread::getParticipantBId, targetUserId);
                })
                .or(w -> {
                    w.eq(MessageThread::getParticipantAId, targetUserId)
                            .eq(MessageThread::getParticipantBId, currentUserId);
                });
        // 如果有商品ID，最好也加上商品ID的匹配条件

        MessageThread thread = threadMapper.selectOne(wrapper);

        // 2. 如果不存在，创建新会话
        if (thread == null) {
            thread = new MessageThread();
            // 约定：ID小的做A，ID大的做B，保证唯一性
            if (currentUserId < targetUserId) {
                thread.setParticipantAId(currentUserId);
                thread.setParticipantBId(targetUserId);
            } else {
                thread.setParticipantAId(targetUserId);
                thread.setParticipantBId(currentUserId);
            }
            thread.setGoodId(goodId);
            thread.setStatus(1);
            thread.setLastMessageTime(new Date());
            threadMapper.insert(thread);
        }
        return thread;
    }

    /**
     * 获取历史消息
     */
    @Override
    public List<ChatMessage> getHistoryMessages(Long threadId) {
        return messageMapper.selectHistoryMessages(threadId);
    }

    /**
     * 获取会话列表（包含对方信息）
     */
    @Override
    public List<Map<String, Object>> getThreadList(Long userId) {
        return threadMapper.selectThreadList(userId);
    }

    @Override
    public void saveMessage(Long threadId, Long senderId, Long receiverId, String content, String type) {
        // 1. 保存消息
        ChatMessage msg = new ChatMessage();
        msg.setThreadId(threadId);
        msg.setSenderId(senderId);
        msg.setMessageType(type);
        msg.setContent(content);
        msg.setIsRead(0);
        messageMapper.insert(msg);

        // 2. 更新会话表的“最后一条消息”和“未读数”
        MessageThread thread = threadMapper.selectById(threadId);
        thread.setLastMessage(content.length() > 20 ? content.substring(0, 20) + "..." : content);
        thread.setLastMessageTime(new Date());

        // 增加接收者的未读数
        if (thread.getParticipantAId().equals(receiverId)) {
            thread.setParticipantAUnreadCount(thread.getParticipantAUnreadCount() + 1);
        } else {
            thread.setParticipantBUnreadCount(thread.getParticipantBUnreadCount() + 1);
        }
        threadMapper.updateById(thread);
    }

    /**
     * 核心逻辑：发送消息
     * 1. 找到或创建会话
     * 2. 插入消息记录
     * 3. 更新会话表的“最后一条消息”和“未读数”
     */


    public void sendMessage(Long senderId, Long receiverId, String content, String type) {
        // 1. 获取或创建会话ID
        MessageThread thread = getOrCreateThread(senderId, receiverId);

        // 2. 保存消息到 chat_message 表
        ChatMessage message = new ChatMessage();
        message.setThreadId(thread.getId());
        message.setSenderId(senderId);
        message.setMessageType(type);
        message.setContent(content);
        message.setIsRead(0); // 默认未读
        message.setCreatedAt(new Date());
        messageMapper.insert(message);

        // 3. 更新 message_thread 表
        // 更新最后消息内容
        thread.setLastMessage(content.length() > 30 ? content.substring(0, 30) + "..." : content);
        thread.setLastMessageTime(new Date());

        // 增加接收者的未读数
        // 判断接收者是 A 还是 B
        if (thread.getParticipantAId().equals(receiverId)) {
            thread.setParticipantAUnreadCount(thread.getParticipantAUnreadCount() + 1);
        } else if (thread.getParticipantBId().equals(receiverId)) {
            thread.setParticipantBUnreadCount(thread.getParticipantBUnreadCount() + 1);
        }

        threadMapper.updateById(thread);
    }

    /**
     * 辅助方法：获取或创建会话
     * 保证 A和B 之间只有一个会话
     */
    private MessageThread getOrCreateThread(Long userId1, Long userId2) {
        // 这里的逻辑是：不管谁大谁小，只要包含这两个ID的记录都查出来
        LambdaQueryWrapper<MessageThread> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageThread::getStatus, 1)
                .and(w -> w.eq(MessageThread::getParticipantAId, userId1).eq(MessageThread::getParticipantBId, userId2))
                .or()
                .and(w -> w.eq(MessageThread::getParticipantAId, userId2).eq(MessageThread::getParticipantBId, userId1));

        MessageThread thread = threadMapper.selectOne(wrapper);

        if (thread == null) {
            thread = new MessageThread();
            // 规范化：ID小的做A，ID大的做B（这是一个好习惯，方便后续维护）
            if (userId1 < userId2) {
                thread.setParticipantAId(userId1);
                thread.setParticipantBId(userId2);
            } else {
                thread.setParticipantAId(userId2);
                thread.setParticipantBId(userId1);
            }
            thread.setStatus(1);
            thread.setLastMessage("");
            thread.setParticipantAUnreadCount(0);
            thread.setParticipantBUnreadCount(0);
            thread.setLastMessageTime(new Date());
            threadMapper.insert(thread);
        }
        return thread;
    }

    @Override
    public void markAsRead(Long threadId, Long userId) {
        MessageThread thread = threadMapper.selectById(threadId);
        if (thread != null) {
            // 如果当前用户是A，清空A的未读数
            if (thread.getParticipantAId().equals(userId)) {
                thread.setParticipantAUnreadCount(0);
            }
            // 如果当前用户是B，清空B的未读数
            else if (thread.getParticipantBId().equals(userId)) {
                thread.setParticipantBUnreadCount(0);
            }
            threadMapper.updateById(thread);
        }
    }
}
