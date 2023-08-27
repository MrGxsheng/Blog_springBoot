package com.xsheng.myblog_springboot.dao;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 前端主页面展示
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/8/27 09:56:34
 */

@Setter
@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BlogDao {

    private Integer id;

    /**
     * 关联的类型
     */
    private String type;

    /**
     * 笔记名称
     */
    private String noteName;

    /**
     * 内容
     */
    private String noteText;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次的更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 图片名字
     */
    private String imgName;

    /**
     * 图片路径
     */
    private String imgPath;

    /**
     * 压缩图路路径
     */
    private String reducePath;

}
