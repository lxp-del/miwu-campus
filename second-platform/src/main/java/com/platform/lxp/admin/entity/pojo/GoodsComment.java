package com.platform.lxp.admin.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品评论表
 */
@Data
@TableName("goods_comment")
public class GoodsComment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long goodsId;
    private Long userId;

    // 直接父评论ID（我直接回复的那条评论）
    private Long parentId;

    // 根评论ID（我归属的最顶层根评论）
    private Long rootParentId;

    private Long replyUserId;
    private String content;
    private String mentionUserIds;
    private String mentionUsernames;
    private Date createTime;
    private Date updateTime;
}
