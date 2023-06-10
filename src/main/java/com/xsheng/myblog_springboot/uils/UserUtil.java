package com.xsheng.myblog_springboot.uils;

import com.xsheng.myblog_springboot.Comment.DangerousException;
import com.xsheng.myblog_springboot.entity.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户工具类
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/10 20:59:14
 */
@Component
public class UserUtil {

    public static void verify(User user , User userR ){
        if(!Objects.equals(user.getPassword(), userR.getPassword())){
            throw  new DangerousException("498", "用户名或密码错误", Math.toIntExact(userR.getId()));
        }
    }
}
