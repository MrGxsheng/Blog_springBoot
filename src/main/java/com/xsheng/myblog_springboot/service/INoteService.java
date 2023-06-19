package com.xsheng.myblog_springboot.service;

import com.xsheng.myblog_springboot.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
public interface INoteService extends IService<Note> {

    boolean noteExists(Note note);

    boolean noteTextExists(Note note);
    Integer getNoteId(Note note);

}
