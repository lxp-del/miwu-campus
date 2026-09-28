package com.platform.lxp.admin.utils;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author 来晓璞
 * @date 2026/4/6 12:17
 * @Description: 获取用户当前位置
 */
@Component
public class IPLocationUtils {

    private static Searcher searcher;

    @PostConstruct
    public void init() {
        try {
            // 1. 明确指定路径，ClassPathResource 会从 resources 根目录开始找
            String dbPath = "ip2region_v4.xdb";
            ClassPathResource resource = new ClassPathResource(dbPath);

            if (!resource.exists()) {
                throw new FileNotFoundException("致命错误：在 resources 目录下未找到 " + dbPath + " 文件！");
            }

            // 2. 使用输入流读取（兼容 JAR 打包模式）
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
                // 3. 初始化查询器
                searcher = Searcher.newWithBuffer(dbBinStr);
                System.out.println("✅ [Ip2Region] 离线库初始化成功，路径: " + dbPath);
            }
        } catch (Exception e) {
            System.err.println("❌ [Ip2Region] 初始化失败，请检查资源文件是否存在！");
            e.printStackTrace();
        }
    }

    /**
     * 获取城市信息
     * 返回格式: 国家|区域|省份|城市|ISP
     */
    public static String getRegion(String ip) {
        if (searcher == null) {
            return "未知|0|0|0|0";
        }
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            return "解析异常";
        }
    }
}
