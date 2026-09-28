package com.platform.lxp.admin.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:59
 * @Description: 用户表-查询
 */
@Data
public class SysUserDTO {



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
     * 用户ID，主键
     */
    private Long id;
    /**
     * 用户名，登录账号
     */
    private String name;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;





    /**
     * 电子邮箱
     */
    private String email;


    /**
     * 头像
     */
    private String avatar;

}
