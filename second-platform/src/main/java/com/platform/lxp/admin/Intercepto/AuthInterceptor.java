package com.platform.lxp.admin.Intercepto;

import com.alibaba.fastjson2.JSON;
import com.platform.lxp.admin.entity.pojo.SysUser;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.service.ISysUserService;
import com.platform.lxp.common.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 来晓璞
 * @date 2026/3/31 22:06
 * @Description: 认证拦截器
 */
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final ISysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 重要：处理 OPTIONS 预检请求，直接放行
        // 浏览器在发送跨域请求前会先发送 OPTIONS 请求，必须放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 从header中获取token
        String token = request.getHeader("Authorization");

        // 如果token为空，返回未登录
        if (token == null || token.trim().isEmpty()) {
            sendUnauthorizedResponse(request, response, "未登录，请先登录");
            return false;
        }

        // 如果是Bearer Token格式，提取真实的token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            // 验证token有效性
            SysUser user = sysUserService.validateToken(token);

            // 验证用户是否存在
            if (user == null) {
                sendUnauthorizedResponse(request, response, "用户不存在或token无效");
                return false;
            }

            // 将用户信息存入ThreadLocal
            UserContext.setCurrentUser(user);
            return true;
        } catch (Exception e) {
            // token无效，返回未登录
            sendUnauthorizedResponse(request, response, e.getMessage() != null ? e.getMessage() : "token无效或已过期");
            return false;
        }
    }

    /**
     * 返回未登录响应
     */
    private void sendUnauthorizedResponse(HttpServletRequest request, HttpServletResponse response, String message) throws Exception {
        // 设置CORS响应头，确保前端能接收到错误信息
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401状态码
        response.setContentType("application/json;charset=UTF-8");

        // 使用你定义的 ResponseResult 类
        ResponseResult result = ResponseResult.error(401, message);
        String json = JSON.toJSONString(result);

        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清除ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}
