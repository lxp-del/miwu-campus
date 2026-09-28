package com.platform.lxp.admin.entity.dto;

import lombok.Data;

@Data
public class WxLoginDTO {
    private String code; // APP传过来的微信授权code
}
