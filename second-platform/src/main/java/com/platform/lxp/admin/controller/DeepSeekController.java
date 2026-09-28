package com.platform.lxp.admin.controller;

import com.platform.lxp.admin.entity.dto.ChatModel;
import com.platform.lxp.admin.service.Impl.DeepSeekAssistantImpl;
import com.platform.lxp.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 来晓璞
 * @date 2026/3/30 20:22
 * @Description: DeepSeek AI对话管理
 */
@Api(tags = "DeepSeek AI对话管理")
@RestController
@RequestMapping("/api/deepseek")
@RequiredArgsConstructor
public class DeepSeekController {

    private final DeepSeekAssistantImpl deepSeekAssistant;

    @ApiOperation("简单对话")
    @PostMapping("/chat")
    public ResponseResult chat(@RequestBody ChatModel model) {
        String message = model.getMessage();
        String response = deepSeekAssistant.chat(message);
        return ResponseResult.success(response);
    }

    @ApiOperation("简单对话cy")
    @PostMapping("/chatcy")
    public ResponseResult chatcy(@RequestBody Map<String, String> params) {
        String message = params.get("message");
        String roleName = params.get("roleName");
        String response = deepSeekAssistant.chatcy(message,roleName);
        return ResponseResult.success(response);
    }

    @ApiOperation("带系统提示的对话")
    @PostMapping("/chat/system")
    public Map<String, String> chatWithSystem(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = deepSeekAssistant.chatWithSystem(message);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    @ApiOperation("带记忆的对话")
    @PostMapping("/chat/memory")
    public Map<String, String> chatWithMemory(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = deepSeekAssistant.chatWithMemory(message);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    @ApiOperation("RAG对话（基于知识库）")
    @PostMapping("/chat/rag")
    public Map<String, String> chatWithRag(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = deepSeekAssistant.chatWithRag(message);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    @ApiOperation("解析用户信息")
    @PostMapping("/parse/user")
    public Map<String, String> parseUserInfo(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = deepSeekAssistant.parseUserInfo(message);

        Map<String, String> result = new HashMap<>();
        result.put("result", response);
        return result;
    }

    @ApiOperation("生成代码")
    @PostMapping("/generate/code")
    public Map<String, String> generateCode(@RequestBody Map<String, String> request) {
        String requirement = request.get("requirement");
        String response = deepSeekAssistant.generateCode(requirement);

        Map<String, String> result = new HashMap<>();
        result.put("code", response);
        return result;
    }
}
