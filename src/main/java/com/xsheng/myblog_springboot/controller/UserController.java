package com.xsheng.myblog_springboot.controller;

import cn.hutool.core.bean.BeanUtil;
import com.sun.org.apache.regexp.internal.RE;
import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.User;
import com.xsheng.myblog_springboot.service.IUserService;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import com.xsheng.myblog_springboot.uils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsheng
 * @since 2023-06-10
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        return Result.success(loginR(user));
    }

    // 检测账号是否存在
    @GetMapping
    public Result exists(@RequestParam("account") String account){
        boolean exists = userService.accountExists(account);
        return Result.success(exists);
    }

    //注册
    @PostMapping("/reg")
    public Result register(@RequestBody User user){
        user.setCreateTime(TimeUtil.now());
        String password = user.getPassword();

        //加密的时候再用
//        user.setPassword(UserUtil.encryptPassword(password));
        userService.save(user);

        return Result.success(loginR(user));
    }

    @PutMapping("/update")
    public Result update(@RequestBody Map<String,String> map){
        User user = userService.getUserByAccount(map.get("account"));
        UserUtil.verify(user, BeanUtil.mapToBean(map,User.class,false));

        userService.updateById(user);
        return Result.success();
    }


    public Map<String,Object> loginR(User user){
        Map<String,Object> map = new HashMap<>();
        String account = user.getAccount();
        User userR = userService.getUserByAccount(account);
        UserUtil.verify(user,userR);

        map.put("id",userR.getId());
        map.put("username",userR.getUsername());
        map.put("avatar",userR.getAvatar());

        return map;
    }

}
