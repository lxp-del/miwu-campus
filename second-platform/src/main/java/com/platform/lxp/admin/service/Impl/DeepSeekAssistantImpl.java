package com.platform.lxp.admin.service.Impl;

import cn.hutool.db.sql.Order;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.lxp.admin.entity.dto.ShoppingCartDTO;
import com.platform.lxp.admin.entity.pojo.OrderGoods;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.mapper.OrderGoodsMapper;
import com.platform.lxp.admin.service.DeepSeekAssistant;
import com.platform.lxp.admin.service.ShoppingCartService;
import com.platform.lxp.admin.util.RedisUtil;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 来晓璞
 * @date 2026/3/30 20:20
 * @Description: DeepSeek AI 服务实现类
 */
@Slf4j
@Service
public class DeepSeekAssistantImpl implements DeepSeekAssistant {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private ChatLanguageModel chatLanguageModel;

    @Resource
    private ChatMemory chatMemory;

    @Resource
    private ContentRetriever contentRetriever;

    private DeepSeekAssistant deepSeekAssistant;
    private DeepSeekAssistant deepSeekAssistantWithMemory;
    private DeepSeekAssistant ragDeepSeekAssistant;

    @PostConstruct
    public void init() {
        log.info("初始化 DeepSeek Assistant 服务");

        // 普通对话助手
        this.deepSeekAssistant = AiServices.builder(DeepSeekAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .build();

        // 带记忆的对话助手
        this.deepSeekAssistantWithMemory = AiServices.builder(DeepSeekAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .build();

        // 带RAG的对话助手（检索增强生成）
        this.ragDeepSeekAssistant = AiServices.builder(DeepSeekAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .build();
    }


    @Override
    public String chat(String userMessage) {
        log.info("用户消息: {}", userMessage);
        // 这里的 userId 建议从 SecurityContext 或 Token 中获取，此处暂存演示
        String userId = UserContext.getCurrentUser().getId().toString();
        String redisKey = "user:last_search:" + userId;

        try {
            List<OrderGoods> matchedGoods;

            // ======================= 逻辑：意图拦截与上下文获取 =======================
            if (userMessage.contains("确认下单") || userMessage.contains("购买")) {
                // 1. 从 Redis 获取上次缓存的商品 ID 列表
                List<Object> cachedIds = redisUtil.lGet(redisKey, 0, -1);

                if (cachedIds != null && !cachedIds.isEmpty()) {
                    // 2. 将字符串 ID 转化为 Long 列表，并在数据库中查询对应实体
                    List<Long> ids = cachedIds.stream()
                            .map(obj -> Long.valueOf(obj.toString()))
                            .collect(Collectors.toList());

                    matchedGoods = orderGoodsMapper.selectList(
                            new LambdaQueryWrapper<OrderGoods>().in(OrderGoods::getId, ids)
                    );

                    // 3. 执行下单流程
                    try {
                        for (OrderGoods item : matchedGoods) {
                            ShoppingCartDTO dto = new ShoppingCartDTO();
                            dto.setGoodsId(item.getId());
                            dto.setPayType("DEFAULT");
                            shoppingCartService.addCart(dto);
                            log.info("自动下单成功: ID={}", item.getId());
                        }
                        // 下单成功后清理缓存
                        redisUtil.expire(redisKey, 0);
                        return "已为您将匹配到的 " + matchedGoods.size() + " 件商品全部下单成功！\n[ACTION]VIEW_CART[/ACTION]";
                    } catch (Exception e) {
                        log.error("批量下单失败", e);
                        return "下单过程中出现异常：" + e.getMessage();
                    }
                } else {
                    return "抱歉，由于长时间未操作，请重新搜索商品后再尝试下单。";
                }
            } else {
                // 4. 正常搜索流程：数据库模糊匹配
                matchedGoods = orderGoodsMapper.selectList(
                        new LambdaQueryWrapper<OrderGoods>()
                                .like(OrderGoods::getTitle, userMessage)
                                .last("LIMIT 3")
                );

                // 5. 将搜索到的商品 ID 存入 Redis 缓存（有效期建议设置如 10 分钟）
                if (!matchedGoods.isEmpty()) {
                    // 先清理旧的搜索记录
                    redisUtil.expire(redisKey, 0);
                    for (OrderGoods item : matchedGoods) {
                        // 利用你的 RedisUtil.lSet 存入 List
                        redisUtil.lSet(redisKey, item.getId().toString(), 600);
                    }
                }
            }
            // =========================================================================

            // 6. 构造 AI 提示词（逻辑保持不变）
            StringBuilder contextBuilder = new StringBuilder();
            if (!matchedGoods.isEmpty()) {
                contextBuilder.append("\n【系统提示：以下是数据库中匹配到的相关商品信息，请优先向用户推荐这些内容】\n");
                for (OrderGoods item : matchedGoods) {
                    contextBuilder.append(String.format("- 商品名称：%s，价格：￥%s，ID：%s \n",
                            item.getTitle(), item.getPrice(), item.getId()));
                }
                contextBuilder.append("\n请基于以上信息回答用户，并以亲切的口吻告知用户我们有这些商品。");
            }

            // 7. 调用 AI 并处理回复
            String finalMessage = userMessage + contextBuilder.toString();
            String aiResponse = deepSeekAssistant.chat(finalMessage);

            // 8. 拼接商品卡片数据供前端渲染（逻辑保持不变）
            if (!matchedGoods.isEmpty()) {
                StringBuilder productShowcase = new StringBuilder("\n\n--- 相关商品推荐 ---\n");
                for (OrderGoods item : matchedGoods) {
                    String firstImg = (item.getImageUrls() != null && !item.getImageUrls().isEmpty())
                            ? item.getImageUrls().split(",")[0]
                            : "";
                    productShowcase.append(item.getTitle()).append("\n");
                    productShowcase.append("[ID]").append(item.getId()).append("[/ID]\n");
                    productShowcase.append("[IMG]").append(firstImg).append("[/IMG]\n");
                }
                aiResponse += productShowcase.toString();
            }

            log.info("最终回复: {}", aiResponse);
            return aiResponse;

        } catch (Exception e) {
            log.error("DeepSeek对话失败", e);
            return "抱歉，服务暂时不可用：" + e.getMessage();
        }
    }

    @Override
    public String chatcy(String userMessage,String roleName) {

        // 根据角色名称设置系统提示词（角色扮演）
        String systemPrompt = getSystemPromptByRole(roleName);
        String mg = systemPrompt+userMessage;
        // 调用AI服务，传入系统提示词和用户消息
        String aiResponse = deepSeekAssistant.chat(mg);
        return aiResponse;
    }

    private String getSystemPromptByRole(String roleName) {
        if ("销售专家".equals(roleName)) {
            return "你是一位专业的销售专家，请回答用户的问题，注意回复内容不超过300字。";
        }
        // 可扩展其他角色
        return "你是一个有帮助的AI助手，请回答用户的问题，注意回复内容不超过300字。";
    }

    @Override
    public String chatWithSystem(String userMessage) {


        log.info("带系统提示的用户消息: {}", userMessage);
        try {

            String response = deepSeekAssistant.chatWithSystem(userMessage);
            log.info("DeepSeek回复: {}", response);
            return response;
        } catch (Exception e) {
            log.error("DeepSeek对话失败", e);
            return "抱歉，服务暂时不可用：" + e.getMessage();
        }
    }

    @Override
    public String chatWithMemory(String userMessage) {
        log.info("带记忆的用户消息: {}", userMessage);
        try {
            String response = deepSeekAssistantWithMemory.chatWithMemory(userMessage);
            log.info("DeepSeek回复: {}", response);
            return response;
        } catch (Exception e) {
            log.error("DeepSeek对话失败", e);
            return "抱歉，服务暂时不可用：" + e.getMessage();
        }
    }

    /**
     * RAG 对话（基于向量库检索）
     */
    public String chatWithRag(String userMessage) {
        log.info("RAG用户消息: {}", userMessage);
        try {
            String response = ragDeepSeekAssistant.chat(userMessage);
            log.info("DeepSeek回复: {}", response);
            return response;
        } catch (Exception e) {
            log.error("DeepSeek RAG对话失败", e);
            return "抱歉，服务暂时不可用：" + e.getMessage();
        }
    }

    @Override
    public String parseUserInfo(String userMessage) {
        log.info("解析用户信息: {}", userMessage);
        try {
            String response = deepSeekAssistant.parseUserInfo(userMessage);
            log.info("解析结果: {}", response);
            return response;
        } catch (Exception e) {
            log.error("解析用户信息失败", e);
            return "解析失败：" + e.getMessage();
        }
    }

    @Override
    public String generateCode(String requirement) {
        log.info("生成代码需求: {}", requirement);
        try {
            String response = deepSeekAssistant.generateCode(requirement);
            log.info("生成的代码: {}", response);
            return response;
        } catch (Exception e) {
            log.error("代码生成失败", e);
            return "代码生成失败：" + e.getMessage();
        }
    }
}
