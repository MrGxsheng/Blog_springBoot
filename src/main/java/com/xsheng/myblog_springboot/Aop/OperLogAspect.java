package com.xsheng.myblog_springboot.Aop;

import com.alibaba.fastjson2.JSON;
import com.xsheng.myblog_springboot.annotation.MyLog;
import com.xsheng.myblog_springboot.entity.SysOperLog;
import com.xsheng.myblog_springboot.service.ISysOperLogService;
import com.xsheng.myblog_springboot.uils.IdUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/8/31 08:39:57
 */


/**
 * 切面处理类，记录操作日志到数据库
 */
@Aspect
@Component
public class OperLogAspect {

    @Autowired
    private ISysOperLogService operLogService;

    //为了记录方法的执行时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 设置操作日志切入点，这里介绍两种方式：
     * 1、基于注解切入（也就是打了自定义注解的方法才会切入）
     *
     * @Pointcut("@annotation(com.xsheng.myblog_springboot.annotation.MyLog)") 2、基于包扫描切入
     * @Pointcut("execution(public * org.wujiangbo.controller..*.*(..))")
     */
//    @Pointcut("@annotation(com.xsheng.myblog_springboot.annotation.MyLog)")//在注解的位置切入代码
    @Pointcut("execution(public * com.xsheng.myblog_springboot.controller.*.*(..))")//从controller切入
    public void operLogPoinCut() {
    }

    @Before("operLogPoinCut()")
    public void beforMethod(JoinPoint point) {
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.xsheng.myblog_springboot.controller..*.*(..))")
    public void operExceptionLogPoinCut() {
    }


    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param result    返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "result")
    public void saveOperLog(JoinPoint joinPoint, Object result) {


        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            MyLog myLog = method.getAnnotation(MyLog.class);

            SysOperLog operlog = new SysOperLog();
            if (myLog != null) {
                operlog.setTitle(myLog.title());//设置模块名称
                operlog.setContent(myLog.content());//设置日志内容
            }
            // 将入参转换成json
            String params = argsArrayToString(joinPoint.getArgs());
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();

            assert request != null;

            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName + "()";
//            SysOperLog log = SysOperLog.builder()
//                    .method(methodName) //设置请求方法
//                    .requestMethod(request.getMethod()) //设置请求方式
//                    .requestParam(params) // 请求参数
//                    .responseResult(JSON.toJSONString(result)) // 返回结果
//                    .operName(IdUtil.getUserAccount(request)) // 获取用户名（真实环境中，肯定有工具类获取当前登录者的账号或ID的，或者从token中解析而来）
//                    .ip(getIp(request))  // IP地址
//                    .ipLocation("未知") // IP归属地（真是环境中可以调用第三方API根据IP地址，查询归属地）
//                    .requestUrl(request.getRequestURI()) // 请求URI
//                    .operTime(TimeUtil.now()) // 时间
//                    .status(200) //操作状态（200正常 500异常）
//                    .takeTime(System.currentTimeMillis() - startTime.get()) //记录方法执行耗时时间（单位：毫秒）
//                    .build();


            operlog.setMethod(methodName); //设置请求方法
            operlog.setRequestMethod(request.getMethod());//设置请求方式
            operlog.setRequestParam(params); // 请求参数
            operlog.setResponseResult(JSON.toJSONString(result)); // 返回结果
            operlog.setOperName(IdUtil.getUserAccount(request)); // 获取用户名（真实环境中，肯定有工具类获取当前登录者的账号或ID的，或者从token中解析而来）
            operlog.setIp(getIp(request)); // IP地址
            operlog.setIpLocation("未知"); // IP归属地（真是环境中可以调用第三方API根据IP地址，查询归属地）
            operlog.setRequestUrl(request.getRequestURI()); // 请求URI
            operlog.setOperTime(TimeUtil.now()); // 时间
            operlog.setStatus(0);//操作状态（0正常 1异常）
            Long takeTime = System.currentTimeMillis() - startTime.get();//记录方法执行耗时时间（单位：毫秒）
            operlog.setTakeTime(takeTime);
            //插入数据库
            operLogService.save(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        SysOperLog operlog = new SysOperLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName + "()";
            // 获取操作
            MyLog myLog = method.getAnnotation(MyLog.class);
            if (myLog != null) {
                operlog.setTitle(myLog.title());//设置模块名称
                operlog.setContent(myLog.content());//设置日志内容
            }
            // 将入参转换成json
            String params = argsArrayToString(joinPoint.getArgs());
            operlog.setMethod(methodName); //设置请求方法
            assert request != null;
            operlog.setRequestMethod(request.getMethod());//设置请求方式
            operlog.setRequestParam(params); // 请求参数
            operlog.setOperName(IdUtil.getUserAccount(request)); // 获取用户名（真实环境中，肯定有工具类获取当前登录者的账号或ID的，或者从token中解析而来）
            operlog.setIp(getIp(request)); // IP地址
            operlog.setIpLocation("湖北武汉"); // IP归属地（真是环境中可以调用第三方API根据IP地址，查询归属地）
            operlog.setRequestUrl(request.getRequestURI()); // 请求URI
            operlog.setOperTime(TimeUtil.now()); // 时间
            operlog.setStatus(500);//操作状态（0正常 1异常）
            operlog.setErrorMsg(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));//记录异常信息
            //插入数据库
            operLogService.save(operlog);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 转换异常信息为字符串
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        message = substring(message, 0, 2000);
        return message;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (o != null) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params.trim();
    }

    //字符串截取
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        } else {
            if (end < 0) {
                end += str.length();
            }

            if (start < 0) {
                start += str.length();
            }

            if (end > str.length()) {
                end = str.length();
            }

            if (start > end) {
                return "";
            } else {
                if (start < 0) {
                    start = 0;
                }

                if (end < 0) {
                    end = 0;
                }
                return str.substring(start, end);
            }
        }
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> returnMap = new HashMap<>();
        for (String key : paramMap.keySet()) {
            returnMap.put(key, paramMap.get(key)[0]);
        }
        return returnMap;
    }

    //根据HttpServletRequest获取访问者的IP地址
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
