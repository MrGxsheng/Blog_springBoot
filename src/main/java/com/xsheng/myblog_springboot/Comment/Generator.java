package com.xsheng.myblog_springboot.Comment;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 代码生成器
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/3/16 15:53:16
 */
public class Generator {

    // 表名
    public static final String TABLE_NAME = "image";
    // 作者
    public static final String AUTHOR = "xsheng";
    //Mapper 路径
    public static final String OUT_PUT_FILE = "E:/My_Project/MyBlog_SpringBoot/src/main/resources/mapper";
    // 父级路径
    public static final String PARENT_PATH = "E:/My_Project/MyBlog_SpringBoot/src/main/java";
    // 父级包名
    public static final String PARENT_CLASS = "com.xsheng.myblog_springboot";


    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/my_blog",
                        "root",
                        "1234")
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            //.enableSwagger () // 开启 swagger 模式，这个没啥用
                            .outputDir(PARENT_PATH)
                            .disableOpenDir() // 不打开目录
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_CLASS) // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, OUT_PUT_FILE));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAME)// 设置表名
                            .entityBuilder()    //entity 前置，才能用 lombok
                            .enableLombok() //lombok 注解
                            .mapperBuilder()//mapper 注解
                            .enableMapperAnnotation()// 使用 lombok
                            .controllerBuilder() //RestController 前置
                            .enableRestStyle();// 使用 RestController
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
