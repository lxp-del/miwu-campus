package com.platform.lxp.admin.utils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author 来晓璞
 * @date 2026/3/31 21:51
 * @Description:
 */
public class TokenUtil {
    // 生成token
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 判断token是否过期
    public static boolean isTokenExpired(LocalDateTime expireTime) {
        return expireTime == null || LocalDateTime.now().isAfter(expireTime);
    }
}
