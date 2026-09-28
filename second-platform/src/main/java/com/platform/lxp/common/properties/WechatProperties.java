package com.platform.lxp.common.properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置类（登录 + 支付）
 * 从 application.yml 自动读取
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    // ====================== 微信登录（开放平台） ======================
    private String appId;       // 开放平台 APPID
    private String appSecret;   // 开放平台 APPSECRET

    // ====================== 微信支付（商户平台） ======================
    private String mchId;       // 商户号
    private String mchKey;      // 商户API密钥 v2
    private String apiV3Key;    // 商户APIv3密钥
    private String certPath;    //# 证书路径
    private String notifyUrl;   // 支付回调地址
}
