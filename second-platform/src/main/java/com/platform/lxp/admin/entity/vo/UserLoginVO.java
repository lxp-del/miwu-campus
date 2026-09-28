package com.platform.lxp.admin.entity.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private Long id;
    private String token;
    private String nickname;
    private String avatar;
}
