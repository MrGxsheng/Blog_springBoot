package com.xsheng.myblog_springboot.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * 前端表展示
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/14 13:41:59
 */

@Setter
@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class NoteDao {

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次的更新时间
     */
    private LocalDateTime updateTime;


}
