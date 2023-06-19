package com.xsheng.myblog_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 关联的类型
     */
    private Integer typeId;

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


}
