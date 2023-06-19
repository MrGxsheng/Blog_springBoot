package com.xsheng.myblog_springboot.controller;

import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.NoteType;
import com.xsheng.myblog_springboot.service.INoteTypeService;
import com.xsheng.myblog_springboot.uils.NoteUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xsheng.myblog_springboot.Comment.Constants.CODE_10001;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/noteType")
@RequiredArgsConstructor
public class NoteTypeController {
    private final INoteTypeService noteTypeService;

    @PostMapping()
    public Result addType(@RequestBody NoteType noteType){
        if(noteTypeService.typeExists(noteType.getType())){
            noteTypeService.save(noteType);
            return Result.success();
        }else return Result.error(CODE_10001,"别加了哇,已经存在了");
    }


    // 仍上去所有的类型
    @GetMapping()
    public Result allType(){
        return Result.success(noteTypeService.lambdaQuery().list());
    }




}
