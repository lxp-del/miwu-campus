package com.platform.lxp.goods.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/6 11:31
 * @Description: 商品评论表
 */
@Data
@TableName("order_comments")
 public class OrderComments {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联商品ID
     */
    @TableField("good_id")
    private Long goodId;

    /**
     * 评论用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 评分(1-5星)
     */
    @TableField("star_rating")
    private Integer starRating;

    /**
     * 创建人ID
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人ID
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

   /**
    * 发布地址
    */
   private String address;
}
