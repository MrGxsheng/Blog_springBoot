package com.xsheng.myblog_springboot.Comment;

import lombok.Getter;
import lombok.Setter;

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
public class DangerousException extends RuntimeException {
    private String code;


    private Integer userId;

    public DangerousException(String code, String message, Integer userId) {
        super(message);
        this.code = code;
        this.userId = userId;
    }

//    public DangerousException(String code, String message, HttpServletRequest request, Integer userId) {
//        super(message);
//        this.code = code;
//        this.ip = IdUtil.getIp(request);
//        this.userId = userId;
//    }
}