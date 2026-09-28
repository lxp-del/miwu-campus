package com.platform.lxp.chat.controller;

import com.platform.lxp.chat.eneity.entity.ChatMessage;
import com.platform.lxp.chat.service.Impl.ChatService;
import com.platform.lxp.chat.socketserver.ChatWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 来晓璞
 * @date 2026/4/5 22:50
 * @Description:
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 注意：实际项目中 WebSocketServer 应该用更优雅的方式注入，这里为了演示方便直接静态引用或注入
    @Autowired
    private ChatWebSocketServer webSocketServer;

    /**
     * 1. 获取当前用户的会话列表
     * GET /api/chat/threads?userId=1
     */
    @GetMapping("/threads/{currentUserId}")
    public Map<String, Object> getThreadList(@PathVariable("currentUserId") Long currentUserId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> threads = chatService.getThreadList(currentUserId);
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", threads);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }


    @GetMapping("/messages/{threadId}")
    public Map<String, Object> getHistoryMessages(@PathVariable("threadId") Long threadId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ChatMessage> messages = chatService.getHistoryMessages(threadId);
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", messages);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 3. 发送消息 (HTTP方式，作为WebSocket的兜底或补充)
     * 通常聊天用WebSocket，但如果WebSocket断了，或者发送离线消息，可以用这个
     * POST /api/chat/send
     */
    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long senderId = Long.parseLong(payload.get("senderId"));
            Long receiverId = Long.parseLong(payload.get("receiverId"));
            String content = payload.get("content");
            String type = payload.get("type"); // text or image

            // 调用Service层逻辑
            chatService.sendMessage(senderId, receiverId, content, type);

            result.put("code", 200);
            result.put("msg", "发送成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 4. 标记消息为已读
     * POST /api/chat/read
     */
    @PostMapping("/read")
    public Map<String, Object> markAsRead(@RequestBody Map<String, Long> payload) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long threadId = payload.get("threadId");
            Long userId = payload.get("userId");

            chatService.markAsRead(threadId, userId);

            result.put("code", 200);
            result.put("msg", "已读");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }
}
