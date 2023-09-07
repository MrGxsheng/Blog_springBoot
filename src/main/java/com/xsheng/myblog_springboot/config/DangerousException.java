package com.xsheng.myblog_springboot.config;

import com.xsheng.myblog_springboot.uils.IdUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/10 21:01:31
 */
@Getter
@Setter
public class DangerousException extends RuntimeException{

    private String code;
    private String ip;
    private Integer userId;


    public DangerousException(String code, String message, Integer userId) {
        super(message);
        this.code = code;
        this.userId = userId;
    }

    public DangerousException(String code, String message, HttpServletRequest request, Integer userId) {
        super(message);
        this.code = code;
        this.ip = IdUtil.getIp(request);
        this.userId = userId;
    }

    public DangerousException(String code, String message, String ip, Integer userId) {
        super(message);
        this.code = code;
        this.ip = ip;
        this.userId = userId;
    }
}