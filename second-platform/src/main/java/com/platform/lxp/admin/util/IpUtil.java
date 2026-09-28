package com.platform.lxp.admin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * @author 来晓璞
 * @date 2026/4/6 12:27
 * @Description: IP工具类 - 支持获取内网IP和公网IP
 */
public class IpUtil {
    public static final String DEFAULT_IP = "127.0.0.1";

    /**
     * 获取公网IP
     * 原理：访问外部查询IP的服务，获取响应内容
     *
     * @return 公网IP地址，获取失败返回 null
     */
    public static String getPublicIp() {
        // 这里使用几个常用的免费IP查询接口，可以根据网络环境选择
        String[] urls = {
                "https://ifconfig.me/ip",
                "https://ip.3322.net",
                "https://api.ipify.org"
        };

        for (String url : urls) {
            try {
                URL realUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

                // 设置超时时间，避免卡死
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String result = in.readLine();
                    if (result != null && !result.isEmpty()) {
                        // 简单的清洗，防止返回多余空格
                        return result.trim();
                    }
                }
            } catch (IOException e) {
                // 尝试下一个接口
                continue;
            }
        }
        return null;
    }

    /**
     * 直接根据第一个网卡地址作为其内网ipv4地址，避免返回 127.0.0.1
     * 优化：增加了过滤 169.254.x.x 的逻辑
     *
     * @return
     */
    public static String getLocalIpByNetcard() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        String ip = inet4Address.getHostAddress();

                        // 【关键修改】过滤掉 169.254.x.x 开头的链路本地地址
                        // 这种地址通常是网卡未获取到DHCP分配时自动生成的，没有实际路由意义
                        if (ip.startsWith("169.254.")) {
                            continue;
                        }

                        return "芜湖市";
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
