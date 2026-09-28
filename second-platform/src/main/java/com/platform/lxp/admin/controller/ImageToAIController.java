package com.platform.lxp.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageToAIController {

    @Value("${tongyi.api-key}")
    private String apiKey;

    // 核心：图片 → 姓名、学号
    public String[] recognizeImage(byte[] bytes) {
        String base64 = Base64.getEncoder().encodeToString(bytes);
        String aiJson = callAI(base64);
        String text = extractPureText(aiJson);

        String name = extractField(text, "姓名");
        String studentId = extractField(text, "学号");
        return new String[]{name, studentId};
    }

    // 调用AI
    private String callAI(String base64) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String json = "{"
                + "\"model\":\"qwen-vl-ocr\","
                + "\"input\":{"
                + "\"messages\":[{"
                + "\"role\":\"user\","
                + "\"content\":["
                + "{\"image\":\"data:image/jpeg;base64,"+base64+"\"},"
                + "{\"text\":\"提取所有文字\"}"
                + "]"
                + "}]"
                + "}"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(
                "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation",
                request, String.class
        );
    }

    // 提取纯文字（无依赖）
    private String extractPureText(String aiJson) {
        Pattern pattern = Pattern.compile("\"text\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(aiJson);
        if (matcher.find()) {
            return matcher.group(1).replace("\\n", "\n");
        }
        return "";
    }

    // 提取字段
    private String extractField(String text, String field) {
        Pattern p = Pattern.compile(field + "[:：]\\s*([^\\n\\s]+)");
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1).trim() : null;
    }

    // 商品图片 → AI 识别 + 二手市场估价
    public String estimateGoodsPrice(byte[] imageBytes) {
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        String aiResp = callPriceAI(base64);
        return extractPriceFromAI(aiResp);
    }

    // 调用AI估价（兼容所有Java版本，不报错）
    private String callPriceAI(String base64) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + apiKey);

        String prompt = "你是一名专业的二手物品估价师。"
                + "请根据图片识别物品名称，并将物品名称根据国内的二手平台（如咸鱼、爱回收等）进行估价，给出合理参考价。"
                + "判断规则："
                + "1. 先判断：品类→品牌→年份/代次→型号（Pro/Max/顶配/普通）→成色→容量→保修"
                + "2. 如果是普通款商品，按普通二手价评估，不要当成限量版、联名款、收藏款溢价估价。"
                + "3. 如果是新款、高端款、热门款，可以按合理市场高价评估，不刻意压低。"
                + "4. 如果是旧款、入门款、普通款，按正常二手低价评估。"
                + "5. 价格必须符合现实逻辑，不允许出现明显偏离市场的离谱价格。"
                + "6. 价格必须符合现实：新款不低估、老款不高估、限量不瞎溢价、普通不乱高价"
                + "只返回一个纯数字价格，单位：元，不要任何多余文字、符号、解释。";
        String json = "{"
                + "\"model\":\"qwen-vl-plus\","
                + "\"input\":{"
                + "\"messages\":[{"
                + "\"role\":\"user\","
                + "\"content\":["
                + "{\"image\":\"data:image/jpeg;base64," + base64 + "\"},"
                + "{\"text\":\"" + prompt + "\"}"
                + "]"
                + "}]"
                + "}"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(
                "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation",
                request, String.class
        );
    }

    // 提取价格（解决返回0元问题）
    private String extractPriceFromAI(String aiJson) {
        StringBuilder fullText = new StringBuilder();
        Pattern pattern = Pattern.compile("\"text\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(aiJson);
        while (matcher.find()) {
            fullText.append(matcher.group(1)).append(" ");
        }

        Matcher numMatcher = Pattern.compile("\\d+").matcher(fullText);
        int maxPrice = 0;
        while (numMatcher.find()) {
            try {
                int num = Integer.parseInt(numMatcher.group());
                if (num > maxPrice) maxPrice = num;
            } catch (Exception ignored) {}
        }
        return String.valueOf(maxPrice);
    }
}