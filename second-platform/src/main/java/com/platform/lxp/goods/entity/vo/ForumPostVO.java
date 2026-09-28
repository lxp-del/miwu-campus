package com.platform.lxp.goods.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.lxp.admin.entity.pojo.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/6 13:42
 * @Description:
 */
@Data
public class ForumPostVO {

    /**
     * 主键ID
     */

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

    private Date createTime;

    /**
     * 更新时间
     */

    private Date updateTime;

    private SysUser createSysUser;

    private Long views;

    private Long likes;
}
