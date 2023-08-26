package com.xsheng.myblog_springboot.config;

import com.xsheng.myblog_springboot.entity.User;
import com.xsheng.myblog_springboot.mapper.UserMapper;
import com.xsheng.myblog_springboot.service.IUserService;
import com.xsheng.myblog_springboot.uils.IdUtil;
import com.xsheng.myblog_springboot.uils.JwtUtil;
import com.xsheng.myblog_springboot.uils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 拦截器
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/8/25 10:22:57
 */

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {


    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ip
        String ip = IdUtil.getIp(request);

        // 放行OPTIONS请求
        // nginx拦过了，自信去掉
        String method = request.getMethod().toUpperCase();
        if ("OPTIONS".equals(method)) {
            return true;
        }

        //获取token
        String token = request.getHeader("token");
        //token不存在直接报错
        if (token == null) {
            log.error("访问者ip：{}，token不存在", ip);
            throw new BusinessException("499", "Token不存在");
        }
        //获取签发对象id，不存在直接报错
        String userid = null; //获取签发对象的Userid
        try {
            userid = JwtUtil.getAudience(token);//获取token中的签发对象
        } catch (Exception e) {
            log.error("访问者ip：{}，token校验失败", ip);
            throw new DangerousException("499", "数据校验失败", ip, StrUtil.NO_USER);
        }
        //带着token验证载荷username是否正确
        User realUser = null;
        realUser = userService.getById(Integer.parseInt(userid));
        if (realUser == null) { // 用户不存在
            log.error("访问者ip：{}，token认证错误", ip);
            throw new DangerousException("499", "认证错误", request, Integer.parseInt(userid));
        }
        //姓名不匹配返回
        if (!Objects.equals(realUser.getAccount(), JwtUtil.getClaimByAccount(token, "account").asString())) {
            log.error("访问者ip：{}，token认证错误", ip);
            throw new DangerousException("499", "认证错误", request, Integer.parseInt(userid));
        }
        //检查是否过期
        if (JwtUtil.checkDate(token)) {
            log.error("访问者ip：{}，token过期", ip);
            throw new BusinessException("499", "token过期");
        }
        //布尔值验证
        return JwtUtil.verifyToken(token, userid, realUser.getPassword());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
