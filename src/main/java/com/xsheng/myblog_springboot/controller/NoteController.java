package com.xsheng.myblog_springboot.controller;

import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.annotation.MyLog;
import com.xsheng.myblog_springboot.dao.NoteDao;
import com.xsheng.myblog_springboot.entity.Note;
import com.xsheng.myblog_springboot.entity.NoteType;
import com.xsheng.myblog_springboot.service.IImageService;
import com.xsheng.myblog_springboot.service.INoteService;
import com.xsheng.myblog_springboot.service.INoteTypeService;
import com.xsheng.myblog_springboot.service.impl.NoteTypeServiceImpl;
import com.xsheng.myblog_springboot.uils.MybatisPlusUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@Slf4j
public class NoteController {

    private final INoteService noteService;
    private final INoteTypeService noteTypeService;
    private final IImageService imageService;


    @PostMapping("/upload/{type}/{userId}")
    public Result addNote(@RequestPart("file") MultipartFile[] file,
                          @PathVariable("type") String type,
                          @PathVariable("userId") Integer userId) throws IOException {



        noteService.addNote(file, type, userId);
        return Result.success();
    }


    @GetMapping("/all")
    public Result AllNote(@RequestParam("userId") Integer userId,
                          @RequestParam("currentPage") Integer currentPage,
                          @RequestParam("pageSize") Integer pageSize) {

        Long count = noteService.lambdaQuery().eq(Note::getUserId, userId).count();

        List<NoteDao> collect = noteService.lambdaQuery()
                .eq(Note::getUserId, userId)
                .last(MybatisPlusUtil.getLimitString(currentPage, pageSize))
                .list()
                .stream()
                .map(val ->
                        NoteDao.builder()
                                .id(userId)
                                .type(noteTypeService.getTypeById(val.getTypeId()))
                                .noteName(val.getNoteName())
                                .updateTime(val.getUpdateTime())
                                .createTime(val.getCreateTime())
                                .build())
                .collect(Collectors.toList());

        return Result.success(collect, count);
    }

    @MyLog(title = "博客模块",content = "获取全部博客")
    @GetMapping("/blog")
    public Result AllBlog(@RequestParam("userId") Integer userId,
                          @RequestParam("type") String type) {

        return Result.success(noteService.getAll(userId, type));
    }

    @DeleteMapping("/delete")
    public Result NoteDelete(@RequestParam Integer id) {
        Integer typeId = noteService.lambdaQuery().eq(Note::getId, id).one().getTypeId();
        String type = noteTypeService.lambdaQuery().eq(NoteType::getId, typeId).one().getType();
        noteService.deleteNote(id, type);

        return Result.success();
    }

    @MyLog(title = "博客模块",content = "首页展示")
    @GetMapping("/showBlog")
    public Result showBlog(@RequestParam Integer userId,
                           @RequestParam String type){
        return Result.success(noteService.showBlog(userId,type));
    }


}
