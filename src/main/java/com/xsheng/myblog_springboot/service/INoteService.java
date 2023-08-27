package com.xsheng.myblog_springboot.service;

import com.xsheng.myblog_springboot.dao.BlogDao;
import com.xsheng.myblog_springboot.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
public interface INoteService extends IService<Note> {

    boolean noteNameExists(Note note);
    List<Note> getAll(Integer userId, String type);
    boolean noteTextExists(Note note);
    Integer getNoteId(Note note,String name);
    void addNote(MultipartFile[] file, String type, Integer userId) throws IOException;
    void deleteNote(Integer id,String type);
    List<BlogDao> showBlog(Integer userId,String type);
}
