package com.xsheng.myblog_springboot.controller;

import cn.hutool.core.img.ImgUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.Image;
import com.xsheng.myblog_springboot.service.IImageService;
import com.xsheng.myblog_springboot.uils.OssUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.util.List;

/**
 * <p>
 *  前端控制器
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
    public Result addImg(@RequestPart("file") MultipartFile file,
                         @PathVariable("userId") Integer userId){
        String upload = OssUtil.upload(file);
        Image builder = Image.builder().imgName(file.getOriginalFilename()).imgPath(upload).userId(userId).uploadTime(TimeUtil.now()).build();
        imageService.save(builder);
        return Result.success();
    }

    @GetMapping("/allImg")
    public Result allImg(@RequestParam Integer userId){
        List<Image> list = imageService.lambdaQuery().eq(Image::getUserId, userId).list();
        int count = list.size();
        return Result.success(list,Integer.toUnsignedLong(count));
    }



}
