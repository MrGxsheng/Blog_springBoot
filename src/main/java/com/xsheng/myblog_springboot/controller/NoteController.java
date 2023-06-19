package com.xsheng.myblog_springboot.controller;

import cn.hutool.aop.interceptor.SpringCglibInterceptor;
import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.dao.NoteDao;
import com.xsheng.myblog_springboot.entity.Note;
import com.xsheng.myblog_springboot.service.INoteService;
import com.xsheng.myblog_springboot.service.INoteTypeService;
import com.xsheng.myblog_springboot.uils.FileUtil;
import com.xsheng.myblog_springboot.uils.MybatisPlusUtil;
import com.xsheng.myblog_springboot.uils.NoteUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {
    private final INoteService noteService;
    private final INoteTypeService noteTypeService;

    @PostMapping("/upload/{type}/{userId}")
    public Result addNote(@RequestPart("file") MultipartFile file,
                          @PathVariable("type") String type,
                          @PathVariable("userId") int userId) throws IOException {

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        int typeId = noteTypeService.getTypeId(type);

        File fz = FileUtil.convertMultipartFileToFile(file);
        String text = FileUtil.convertMarkdownFileToString(fz);

        Note note = Note.builder().noteName(title).noteText(text).userId(userId).typeId(typeId).build();


        if (!noteService.noteExists(note)) {
            note.setCreateTime(TimeUtil.now());
            note.setUpdateTime(TimeUtil.now());
            noteService.save(note);
        } else {
            note.setId(noteService.getNoteId(note));
            updateNote(note);
        }
        return Result.success();
    }

    public void updateNote(Note note) {
        if (!noteService.noteTextExists(note)) {
            note.setUpdateTime(TimeUtil.now());

            noteService.updateById(note);
        }
    }


    @GetMapping("/all")
    public Result AllNote(@RequestParam("userId") Integer userId,
                          @RequestParam("currentPage") Integer currentPage,
                          @RequestParam("pageSize") Integer pageSize) {

        Long count = noteService.lambdaQuery().eq(Note::getUserId, userId).count();

        List<NoteDao> collect = noteService.lambdaQuery()
                .eq(Note::getUserId,userId)
                .last(MybatisPlusUtil.getLimitString(currentPage, pageSize))
                .list()
                .stream()
                .map(val -> NoteDao.builder().id(userId).type(noteTypeService.getTypeById(val.getTypeId())).noteName(val.getNoteName()).updateTime(val.getUpdateTime()).createTime(val.getCreateTime()).build())
                .collect(Collectors.toList());

        return Result.success(collect,count);
    }

    @GetMapping("/blog")
    public Result AllBlog(@RequestParam("userId") Integer userId,
                          @RequestParam("type") String type){

        List<Note> list = noteService.lambdaQuery()
                .eq(Note::getUserId, userId)
                .eq(Note::getTypeId, noteTypeService.getTypeId(type))
                .list();

        return Result.success(list);
    }

    @DeleteMapping("/delete")
    public Result NoteDelete(@RequestParam Integer id){
        noteService.removeById(id);
        return Result.success();
    }


}
