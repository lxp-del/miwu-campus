package com.platform.lxp.admin.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * @author 来晓璞
 * @date 2026/3/30 20:20
 * @Description: AI 对话服务接口（使用 DeepSeek）
 */
public interface DeepSeekAssistant {

    /**
     * 简单对话
     */
    String chat(String userMessage);

    String chatcy(String userMessage,String roleName);

    /**
     * 带系统提示的对话
     */
    @SystemMessage("你是一个专业的Java开发助手，擅长解答技术问题。")
    String chatWithSystem(@UserMessage String userMessage);

    /**
     * 带记忆的对话（自动维护上下文）
     */
    @SystemMessage("你是一个友好的客服助手")
    String chatWithMemory(@UserMessage String userMessage);

    /**
     * 结构化输出示例
     */
    @SystemMessage("将用户输入的内容解析为JSON格式，包含name和age字段")
    String parseUserInfo(@UserMessage String userMessage);

    /**
     * 代码生成助手
     */
    @SystemMessage("你是一个专业的代码生成助手，请根据用户需求生成高质量的代码，并添加详细注释。")
    String generateCode(@UserMessage String requirement);
}
