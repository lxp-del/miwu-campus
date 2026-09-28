package com.platform.lxp.admin.local;

import com.platform.lxp.admin.entity.pojo.SysUser;

/**
 * @author 来晓璞
 * @date 2026/3/31 21:46
 * @Description: 用户登录态存储
 */
/**
 * 用户上下文工具类
 * 使用ThreadLocal为每个线程存储当前登录用户信息，实现线程安全的用户上下文管理
 */
public class UserContext {

    /**
     * ThreadLocal实例，用于存储当前线程的用户信息
     * 每个线程拥有独立的用户上下文，互不干扰
     */
    private static final ThreadLocal<SysUser> currentUser = new ThreadLocal<>();

    /**
     * 设置当前线程的用户信息
     * @param user 系统用户对象，包含用户的登录信息和权限等
     */
    public static void setCurrentUser(SysUser user) {
        currentUser.set(user);
    }

    /**
     * 获取当前线程的用户信息
     * @return 当前登录的系统用户对象，如果未设置则返回null
     */
    public static SysUser getCurrentUser() {
        return currentUser.get();
    }

    /**
     * 清除当前线程的用户信息
     * 通常在请求处理完成或用户登出时调用，防止内存泄漏
     */
    public static void clear() {
        currentUser.remove();
    }
}
