package com.platform.lxp.admin.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:19
 * @Description: 系统用户表
 */
@Data
@TableName("sys_user")
public class SysUser {

    /**
     * 用户ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    // 学生姓名
    private String name;

    // 学号
    private String studentId;

    /**
     * 用户名，登录账号
     */
    private String username;

    /**
     * 密码，存储加密后的密码
     */
    private String password;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 学生校园卡ID
     */
    private String studentCardId;

    /**
     * 用户标签ID
     */
    private String userTagId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 最后登录IP地址
     */
    private String lastLoginIp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 信誉分
     */
    private String score;


    private String token;
    // 登录token
    private LocalDateTime tokenExpireTime; // token过期时间

    @TableField(exist = false)
    public static final long TOKEN_EXPIRE_HOURS = 24; // token有效期24小时
}
