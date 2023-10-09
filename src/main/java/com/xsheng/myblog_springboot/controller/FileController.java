package com.xsheng.myblog_springboot.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.xsheng.myblog_springboot.Comment.Constants;
import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.File;
import com.xsheng.myblog_springboot.service.IFileService;
import com.xsheng.myblog_springboot.service.IImageService;
import com.xsheng.myblog_springboot.uils.FileUtil;
import com.xsheng.myblog_springboot.uils.Md5Util;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsheng
 * @since 2023-06-29
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final IFileService fileService;
    private final IImageService imageService;

    /**
     * 关机接口
     */
    @GetMapping("/shutdown")
    public void shutdown() {
        SpringApplication.exit(SpringUtil.getApplicationContext(), () -> 0);
    }


    @GetMapping("/all")
    public Result getAll(@RequestParam Integer userId){
        List<File> list = fileService.lambdaQuery().eq(File::getUserId, userId).list();

        return Result.success(list);
    }

    @PostMapping("/add/{userId}")
    public Result addFile(@RequestPart("file") MultipartFile file,
                          @PathVariable Integer userId) throws IOException {


        Map<String,String> mp = FileUtil.fileUpload(file);
        if(mp.containsKey("error")){
            return Result.error(Constants.CODE_10001,mp.get("error"));
        }
        File build = File.builder()
                .name(mp.get("name")) // 防止名字重复
                .userId(userId)
                .url(mp.get("downloadPath"))
                .uploadTime(TimeUtil.now())
                .md5(mp.get("md5"))
                .build();
        fileService.save(build);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result deleteFile(@RequestParam Integer id){
        fileService.removeById(id);
        return Result.success();
    }

    @PutMapping("/updateName")
    public Result reNameFile(@RequestParam Integer FileId,
                             @RequestParam String name){
        File newFile = File.builder().id(FileId.longValue()).name(name).build();
        fileService.updateById(newFile);
        return Result.success();
    }

}
