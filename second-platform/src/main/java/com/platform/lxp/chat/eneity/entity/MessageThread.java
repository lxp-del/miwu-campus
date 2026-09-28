package com.platform.lxp.chat.eneity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/5 22:40
 * @Description:
 */
@Data
@TableName("message_thread")
public class MessageThread {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long participantAId;
    private Long participantBId;
    private Long goodId;
    private String lastMessage;
    private Date lastMessageTime;
    private Integer participantAUnreadCount;
    private Integer participantBUnreadCount;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}
