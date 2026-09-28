package com.platform.lxp.admin.entity.vo;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 商品评论VO
 */
@Data
public class CommentVO {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 评论人ID
     */
    private Long userId;

    /**
     * 评论人昵称
     */
    private String username;

    /**
     * 评论人头像
     */
    private String avatar;

    /**
     * 直接父评论ID
     */
    private Long parentId;

    /**
     * 根评论ID（顶层归属）
     */
    private Long rootParentId;

    /**
     * 被回复的用户ID
     */
    private Long replyUserId;

    /**
     * 被回复的用户昵称（前端显示：回复 @张三）
     */
    private String replyUsername;

    /**
     * 评论内容
     */
    private String content;

    /**
     * @的用户ID，逗号分隔
     */
    private String mentionUserIds;

    /**
     * @的用户昵称，逗号分隔
     */
    private String mentionUsernames;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 本条评论的【子回复列表】→ 树形结构核心字段
     */
    private List<CommentVO> replyList;
}
