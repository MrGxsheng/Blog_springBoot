package com.xsheng.myblog_springboot.Aop;

import com.xsheng.myblog_springboot.config.BusinessException;
import com.xsheng.myblog_springboot.entity.User;
import com.xsheng.myblog_springboot.mapper.UserMapper;
import com.xsheng.myblog_springboot.uils.IdUtil;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/9/6 16:30:30
 */
@Aspect
@Component
public class IpAspect {
    @Resource
    private Map<String, List<String>> ipBan;

    @Autowired
    private UserMapper userMapper;
    // 允许错误次数
    public static final int ALLOW_COUNT = 6;

    @Before(value = "execution(* com.xsheng.myblog_springboot.controller.UserController.login(..)) && args(user,request)", argNames = "user,request")
    public void beforeLogin(User user, HttpServletRequest request) {
        // 封禁ip
        String ip = IdUtil.getIp(request);
        List<String> accountList = ipBan.get(ip);
        // 有大量犯罪前科且依旧尝试进入
        if (Objects.nonNull(accountList) && accountList.size() > ALLOW_COUNT) {
            throw new BusinessException("400", "访问被拒绝，清联系管理员");
        }

        // 冻结某账号防止被多ip暴力破解
        int ipBanCount = ipBan.keySet()
                .stream()
                .mapToLong(key ->
                        ipBan.get(key)
                                .stream()
                                .filter(user.getAccount()::equals)
                                .count())
                .mapToInt(count -> (int) count).sum();
        if (ipBanCount > ALLOW_COUNT) {
            throw new BusinessException("400", "账号已被冻结，清联系管理员");
        }
    }

    @AfterReturning(value = "execution(* com.xsheng.myblog_springboot.controller.UserController.login(..)) && args(user,request)", argNames = "user,request")
    public void afterReturningLogin(User user , HttpServletRequest request){
        String ip = IdUtil.getIp(request);
        ipBan.put(ip,new ArrayList<>());
    }

    @AfterThrowing(value = "execution(* com.xsheng.myblog_springboot.controller.UserController.login(..)) && args(user,request)", argNames = "user,request")
    public void afterThrowingLogin(User user , HttpServletRequest request){
        String ip = IdUtil.getIp(request);
        if(Objects.isNull(ipBan.get(ip))) {
            ipBan.put(ip,new ArrayList<>());
        }
        List<String> accountList = ipBan.get(ip);
        accountList.add(user.getAccount());
        ipBan.put(ip,accountList);
    }

}
