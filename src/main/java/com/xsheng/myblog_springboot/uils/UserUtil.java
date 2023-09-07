package com.xsheng.myblog_springboot.uils;

import com.xsheng.myblog_springboot.config.DangerousException;
import com.xsheng.myblog_springboot.entity.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 密码比对
     *
     * @param rawPassword 原密码
     * @param encodedPassword   加密后的密码
     * @return
     */
    public static boolean verify(String rawPassword, String encodedPassword){
        return rawPassword.equals(encodedPassword);
    }

    public static void verify(User user , User userR , HttpServletRequest request){
        if(!Objects.equals(user.getPassword(), userR.getPassword())){
            throw  new DangerousException("498", "用户名或密码错误", request, Math.toIntExact(userR.getId()));
        }
    }

    /**
     * 加密密码
     *
     * @param rawPassword 原密码
     * @return 加密后
     */
    public static String encryptPassword(String rawPassword){
        return rawPassword;
    }

}
