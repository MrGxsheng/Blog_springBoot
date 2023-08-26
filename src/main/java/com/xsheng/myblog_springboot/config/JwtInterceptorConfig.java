package com.xsheng.myblog_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * jwt拦截器配置
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/8/25 10:31:22
 */
@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer {

        @Resource
        JwtInterceptor interceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(interceptor).addPathPatterns("/**").order(-1)// 似乎是小的先走
                    .excludePathPatterns(
                            "/user/shutdown", // 关机接口
                            "/user/test", // 测试接口
                            "/user/login", // 用户登录 *
                            "/user", // 用户注册 *
                            "/blog/*/blogs", // 博客s
                            "/blog/*/blog/*", // 博客
                            "/img/*/banner", // 主页banner
                            "/img/*/background", // 背景图片
                            "/ip", // 获取ip
                            "/toy/**", // 小玩具
                            "/ipBan/**", // 这个就当是压测nginx接口了
                            "/static/**");
        }
}
