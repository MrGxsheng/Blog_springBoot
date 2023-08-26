package com.xsheng.myblog_springboot.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.User;
import com.xsheng.myblog_springboot.service.IUserService;
import com.xsheng.myblog_springboot.uils.JwtUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import com.xsheng.myblog_springboot.uils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
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

    //测试
    @GetMapping("/test")
    public Result test(){
        return Result.success();
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) throws NoSuchAlgorithmException {
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
    public Result register(@RequestBody User user) throws NoSuchAlgorithmException {
        user.setCreateTime(TimeUtil.now());
        String password = user.getPassword();

        //加密的时候再用
        user.setPassword(UserUtil.encryptPassword(password));
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


    private Map<String,Object> loginR(User user) throws NoSuchAlgorithmException {
        Map<String,Object> map = new HashMap<>();
        String account = user.getAccount();
        User userR = userService.getUserByAccount(account);
        UserUtil.verify(user,userR);

        String token = JwtUtil.createToken(userR.getId().toString(),userR.getAccount(),userR.getPassword());

        map.put("token",token);
        map.put("id",userR.getId());
        map.put("username",userR.getUsername());
        map.put("avatar",userR.getAvatar());

        return map;
    }

}
