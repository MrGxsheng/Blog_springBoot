package com.xsheng.myblog_springboot.config;

import com.xsheng.myblog_springboot.Comment.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常接管
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/9/7 21:38:23
 */
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), "异常：" + e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(DangerousException.class)
    public Result handleDangerousException(DangerousException e) {
        return Result.error(e.getCode(), "异常：" + e.getMessage());
    }
}
