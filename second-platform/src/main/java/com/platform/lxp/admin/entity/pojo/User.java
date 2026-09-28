package com.platform.lxp.admin.entity.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("user")
@Builder
public class User {

    /**
     * 主键ID（业务ID，返回给前端的 id）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 微信 openid（唯一标识）
     */
    private String openid;

    /**
     * 微信 unionid（多应用互通）
     */
    private String unionid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 0未知 1男 2女
     */
    private Integer sex;

    /**
     * 手机号（可用于绑定、发货、找回）
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0禁用 1正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
