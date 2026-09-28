package com.platform.lxp.admin.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author 来晓璞
 * @date 2026/3/30 20:14
 * @Description: DeepSeekConfig
 */
@Slf4j
@Configuration
public class DeepSeekConfig {

    private final String apiKey;
    private final String baseUrl;
    private final String modelName;
    private final Integer timeout;
    private final Integer maxRetries;
    private final Double temperature;
    private final Integer maxTokens;
    private final Double topP;

    public DeepSeekConfig(
            @Value("${deepseek.api.key}") String apiKey,
            @Value("${deepseek.api.base-url:https://api.deepseek.com}") String baseUrl,
            @Value("${deepseek.api.model:deepseek-chat}") String modelName,
            @Value("${deepseek.api.timeout:60}") Integer timeout,
            @Value("${deepseek.api.max-retries:3}") Integer maxRetries,
            @Value("${deepseek.api.temperature:0.7}") Double temperature,
            @Value("${deepseek.api.max-tokens:2000}") Integer maxTokens,
            @Value("${deepseek.api.top-p:0.95}") Double topP) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.modelName = modelName;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP = topP;
    }

    /**
     * 配置 DeepSeek 聊天模型
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        log.info("初始化 DeepSeek 聊天模型，模型: {}, Base URL: {}", modelName, baseUrl);
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeout))
                .maxRetries(maxRetries)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .topP(topP)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * 配置嵌入模型
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        log.info("初始化嵌入模型");
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName("text-embedding-ada-002")
                .timeout(Duration.ofSeconds(timeout))
                .maxRetries(maxRetries)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * 配置向量存储
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        log.info("初始化 InMemoryEmbeddingStore");
        return new InMemoryEmbeddingStore<>();
    }

    /**
     * 配置内容检索器
     */
    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore,
                                             EmbeddingModel embeddingModel) {
        log.info("初始化 EmbeddingStoreContentRetriever");
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .minScore(0.7)
                .build();
    }

    /**
     * 配置聊天记忆
     */
    @Bean
    public ChatMemory chatMemory() {
        log.info("初始化 MessageWindowChatMemory，窗口大小: 10");
        return MessageWindowChatMemory.withMaxMessages(10);
    }

    /**
     * 配置 Tokenizer
     */
    @Bean
    public OpenAiTokenizer openAiTokenizer() {
        log.info("初始化 OpenAiTokenizer");
        return new OpenAiTokenizer(modelName);
    }
}
