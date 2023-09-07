package com.xsheng.myblog_springboot.uils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/9/6 15:48:27
 */
@Component
public class CacheUtil{
        // 存储封禁ip的缓存
        @Bean
        public Map<String, List<String>> getIpBan() {
            return new HashMap<>();
        }
}
