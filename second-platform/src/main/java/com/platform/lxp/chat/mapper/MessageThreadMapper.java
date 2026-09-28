package com.platform.lxp.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.lxp.chat.eneity.entity.ChatMessage;
import com.platform.lxp.chat.eneity.entity.MessageThread;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author 来晓璞
 * @date 2026/4/5 22:41
 * @Description:
 */
// MessageThreadMapper.java
@Mapper
public interface MessageThreadMapper extends BaseMapper<MessageThread> {

    // 获取当前用户的会话列表（包含对方昵称、头像）
    @Select("SELECT t.*, " +
            "CASE WHEN t.participant_a_id = #{userId} THEN t.participant_b_unread_count ELSE t.participant_a_unread_count END as unread_count, " +
            "CASE WHEN t.participant_a_id = #{userId} THEN u.id ELSE (SELECT id FROM sys_user WHERE id = t.participant_a_id) END as other_user_id, " +
            "u.name as other_nickname, u.avatar as other_avatar " +
            "FROM message_thread t " +
            "JOIN sys_user u ON (t.participant_a_id = #{userId} AND u.id = t.participant_b_id) OR (t.participant_b_id = #{userId} AND u.id = t.participant_a_id) " +
            "WHERE t.status = 1 AND (t.participant_a_id = #{userId} OR t.participant_b_id = #{userId}) " +
            "ORDER BY t.last_message_time DESC")
    List<Map<String, Object>> selectThreadList(@Param("userId") Long userId);
}

