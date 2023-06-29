package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsheng.myblog_springboot.Comment.BusinessException;
import com.xsheng.myblog_springboot.entity.User;
import com.xsheng.myblog_springboot.mapper.UserMapper;
import com.xsheng.myblog_springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByAccount(String account) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getAccount,account);
        List<User> users = userMapper.selectList(userLambdaQueryWrapper);

        if(users.size() != 1) {
            throw new BusinessException("499", "用户名或密码错误");
        }
        return users.get(0);
    }

    @Override
    public boolean accountExists(String account) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount,account);
        return userMapper.exists(wrapper);
    }
}
