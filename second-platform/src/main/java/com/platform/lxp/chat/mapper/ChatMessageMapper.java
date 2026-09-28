package com.platform.lxp.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.lxp.chat.eneity.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author 来晓璞
 * @date 2026/4/5 22:41
 * @Description:
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    // 获取某个会话的历史消息，按时间正序
    @Select("SELECT m.*, u.nickname as senderNickname, u.avatar as senderAvatar " +
            "FROM chat_message m " +
            "JOIN sys_user u ON m.sender_id = u.id " +
            "WHERE m.thread_id = #{threadId} " +
            "ORDER BY m.created_at ASC")
    List<ChatMessage> selectHistoryMessages(@Param("threadId") Long threadId);
}
