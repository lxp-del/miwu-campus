package com.platform.lxp.chat.socketserver;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.platform.lxp.chat.eneity.entity.MessageThread;
import com.platform.lxp.chat.service.Impl.ChatService;
import com.platform.lxp.chat.util.SpringContextUtil;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 来晓璞
 * @date 2026/4/5 22:46
 * @Description:
 */
@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocketServer {

    private static final ConcurrentHashMap<Long, Session> SESSION_MAP = new ConcurrentHashMap<>();

    // 2. 移除 @Autowired 和静态变量

    // 连接建立
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        SESSION_MAP.put(userId, session);
        System.out.println("用户上线: " + userId);
    }

    // 连接关闭
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        SESSION_MAP.remove(userId);
        System.out.println("用户下线: " + userId);
    }

    // 收到消息
    @OnMessage
    public void onMessage(String message, @PathParam("userId") Long fromUserId) {
        try {
            // 3. 在方法内部通过工具类获取 Service
            // 这样获取到的 chatService 是完整的 Spring Bean，包含了事务代理
            ChatService chatService = SpringContextUtil.getBean(ChatService.class);

            // 解析前端发来的JSON
            JSONObject json = JSON.parseObject(message);
            Long goodId = json.getLong("goodId");
            Long toUserId = json.getLong("toUserId");
            String content = json.getString("content");
            String type = json.getString("type");

            // 4. 调用 Service，此时事务会正常开启，threadMapper 也不会为 null
            MessageThread thread = chatService.getOrCreateThread(fromUserId, toUserId, goodId);
            chatService.saveMessage(thread.getId(), fromUserId, toUserId, content, type);

            // 5. 封装返回给聊天详情页的消息对象 (用于显示气泡)
            JSONObject chatResult = new JSONObject();
            chatResult.put("type", "chat");
            chatResult.put("content", content);
            chatResult.put("senderId", fromUserId);
            chatResult.put("threadId", thread.getId());
            chatResult.put("time", new Date().getTime());

            // --- 新增代码开始：推送列表更新通知 ---

            // 6. 封装列表更新通知 (用于刷新列表页)
            JSONObject listUpdateResult = new JSONObject();
            listUpdateResult.put("type", "list_update"); // 定义一个专门的类型
            listUpdateResult.put("threadId", thread.getId()); // 告诉前端哪个会话更新了

            // 7. 广播通知给所有在线用户 (除了发送者自己)
            // 这样可以确保接收者和其他设备的列表都能实时刷新
            SESSION_MAP.forEach((onlineUserId, session) -> {
                // 排除发送者自己，并且确保连接是打开的
                if (!onlineUserId.equals(fromUserId) && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(listUpdateResult.toJSONString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // --- 新增代码结束 ---

            // 8. 推送聊天消息给接收者 (如果在线)
            Session toSession = SESSION_MAP.get(toUserId);
            if (toSession != null && toSession.isOpen()) {
                toSession.getBasicRemote().sendText(chatResult.toJSONString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
