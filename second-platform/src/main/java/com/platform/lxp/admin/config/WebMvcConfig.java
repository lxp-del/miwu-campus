package com.platform.lxp.admin.config;

import com.platform.lxp.admin.Intercepto.AuthInterceptor;
import com.platform.lxp.admin.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 来晓璞
 * @date 2026/3/31 22:30
 * @Description: Web MVC 配置类
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ISysUserService sysUserService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(sysUserService))
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(
                        "/sys/user/login",          // 匹配没有前缀的情况
                        "/lxp-api/sys/user/login",  // 🌟 增加匹配带前缀的情况
                        "/**/sys/user/login",       // 🌟 终极方案：使用通配符忽略所有前缀的登录请求
                        "/doc.html",                // Knife4j 文档页面
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/v3/api-docs/**",
                        "/error"                    // 错误页面
                );
    }
}
