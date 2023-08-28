package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsheng.myblog_springboot.config.BusinessException;
import com.xsheng.myblog_springboot.entity.Image;
import com.xsheng.myblog_springboot.entity.User;
import com.xsheng.myblog_springboot.mapper.UserMapper;
import com.xsheng.myblog_springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsheng.myblog_springboot.uils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

    @Resource
    private ImageServiceImpl imageService;

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

    @Override
    public void updateAvatar(MultipartFile file, Integer userId) throws NoSuchAlgorithmException, IOException {
        Integer id = Integer.parseInt((FileUtil.uploadImg(file, userId).get("id")).toString());
        String imgPath = imageService.getById(id).getImgPath();
        this.save(this.lambdaQuery()
                .eq(User::getId,userId)
                .one()
                .setAvatar(imgPath));

    }

}
