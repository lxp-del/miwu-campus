package com.platform.lxp.admin.utils;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    /**
     * 计算发布时间到当前时间的差：Date 类型
     */
    public static String getTimeBefore(Date createTime) {
        if (createTime == null) {
            return "";
        }

        // 当前时间
        Date now = new Date();
        long diffMs = now.getTime() - createTime.getTime();

        // 转成 天、时、分
        long days = TimeUnit.MILLISECONDS.toDays(diffMs);
        long hours = TimeUnit.MILLISECONDS.toHours(diffMs) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMs) % 60;

        if (days > 0) {
            return days + "天前";
        } else if (hours > 0) {
            return hours + "小时前";
        } else if (minutes > 0) {
            return minutes + "分钟前";
        } else {
            return "刚刚";
        }
    }
}
