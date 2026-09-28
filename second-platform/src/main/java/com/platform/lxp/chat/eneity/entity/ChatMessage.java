package com.platform.lxp.chat.eneity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author 来晓璞
 * @date 2026/4/5 22:40
 * @Description:
 */
@Data
@TableName("chat_message")
public class ChatMessage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long threadId;
    private Long senderId;
    private String messageType; // text, image
    private String content;
    private Integer isRead;
    private Date createdAt;

    // 非数据库字段，用于前端展示
    @TableField(exist = false)
    private String senderNickname;
    @TableField(exist = false)
    private String senderAvatar;
}
