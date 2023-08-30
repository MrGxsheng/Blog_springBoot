package com.xsheng.myblog_springboot.controller;

import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xsheng
 * @since 2023-06-15
 */
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload/{userId}")
    public Result addImg(@RequestPart("file") MultipartFile[] file,
                         @PathVariable("userId") Integer userId) throws IOException, NoSuchAlgorithmException {
        imageService.addImage(file, userId);
        return Result.success();
    }

    @GetMapping("/allImg")
    public Result allImg(@RequestParam Integer userId) {
        return Result.success(imageService.getAllImage(userId));
    }

    @GetMapping("/randomImg")
    public Result randomImg(@RequestParam Integer userId,
                            @RequestParam Integer count){
        return Result.success(imageService.getRandomImg(userId,count));
    }

    @DeleteMapping("/deleteImg")
    public Result deleteImg(@RequestParam Integer id){
        return Result.success(imageService.removeById(id));
    }


}
