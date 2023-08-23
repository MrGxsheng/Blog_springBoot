package com.xsheng.myblog_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyBlogSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBlogSpringBootApplication.class, args);
    }

}
