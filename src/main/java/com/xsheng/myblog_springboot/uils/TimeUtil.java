package com.xsheng.myblog_springboot.uils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/10 21:35:57
 */
public class TimeUtil {
    // 一天有多少秒呢
    public static final int SECOND_PER_DAY = 24 * 60 * 60;

    // 获取当前时间
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    // 精确到天 - 获取当前时间
    public static LocalDate nowDay() {
        return LocalDate.now();
    }
}
