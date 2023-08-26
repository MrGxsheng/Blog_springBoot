package com.xsheng.myblog_springboot.config;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/10 20:57:25
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}