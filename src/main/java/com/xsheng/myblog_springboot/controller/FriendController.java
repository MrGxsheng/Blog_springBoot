package com.xsheng.myblog_springboot.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.sun.org.apache.regexp.internal.RE;
import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.Friend;
import com.xsheng.myblog_springboot.service.IFriendService;
import com.xsheng.myblog_springboot.uils.FileUtil;
import com.xsheng.myblog_springboot.uils.OssUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xsheng
 * @since 2023-06-16
 */
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final IFriendService friendService;

    @GetMapping("/all")
    public Result allFriend(@RequestParam Integer userId) {
        List<Friend> list = friendService.lambdaQuery().eq(Friend::getUserId, userId).list();
        return Result.success(list);
    }

    @PostMapping("/update")
    public Result updateFriend(@RequestBody Friend friend) {
        List<Friend> list = friendService.lambdaQuery().eq(Friend::getName, friend.getName()).list();
        if (list.size() > 0) {
            return Result.error();
        } else {
            friendService.updateById(friend);
            return Result.success();
        }
    }

    @PostMapping ("/add")
    public Result addFriend(@RequestParam String name,
                            @RequestParam Integer userId,
                            @RequestParam String description,
                            @RequestParam String url,
                            @RequestParam MultipartFile avatar) {
        String avaUrl = OssUtil.upload(avatar);
        Friend friend = Friend.builder()
                .name(name)
                .description(description)
                .url(url)
                .avatar(avaUrl)
                .userId(userId)
                .build();

        friendService.save(friend);
        return Result.success();
    }


    @DeleteMapping("/delete")
    public Result deleteFriend(@RequestParam Integer userId,
                               @RequestParam Integer friendId) {
        Friend friend = friendService.lambdaQuery().eq(Friend::getUserId, userId).eq(Friend::getId, friendId).one();
        return Result.success(friendService.removeById(friend));
    }

    @PutMapping("/update")
    public Result update(@RequestParam Integer friendId,
                         @RequestParam String name,
                         @RequestParam Integer userId,
                         @RequestParam String description,
                         @RequestParam String url,
                         @RequestParam(required = false) MultipartFile avatar){
        Friend build = null;
        if(avatar != null){
            String avaUrl = OssUtil.upload(avatar);
            build = Friend.builder().id(friendId.longValue()).name(name).userId(userId).description(description).url(url).avatar(avaUrl).build();

        }else{
            build = Friend.builder().id(friendId.longValue()).name(name).userId(userId).description(description).url(url).build();
        }
        friendService.updateById(build);
        return Result.success();
    }
}
