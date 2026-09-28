package com.platform.lxp.admin.entity.dto;


import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDTO {

    // 商品ID
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    // 父评论ID（0=一级评论）
    private Long parentId = 0L;

    // 回复的用户ID
    private Long replyUserId = 0L;

    // 评论内容
    @NotBlank(message = "评论内容不能为空")
    private String content;

    // @用户ID 多个用逗号分隔 1,2,3
    private String mentionUserIds;

    // @用户昵称 多个用逗号分隔 张三,李四
    private String mentionUsernames;
}
