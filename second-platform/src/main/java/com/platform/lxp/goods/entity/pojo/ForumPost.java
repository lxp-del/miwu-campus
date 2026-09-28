package com.platform.lxp.goods.entity.pojo;

import  com.baomidou.mybatisplus.annotation.*;
import com.platform.lxp.admin.entity.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/6 12:51
 * @Description:
 */
@Data
@TableName("forum_post")
public class ForumPost {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布人用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子图片URL
     */
    private String imageUrl;

    /**
     * 帖子正文内容
     */
    private String content;

    /**
     * 状态 (0:草稿, 1:已发布, 2:已删除)
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
