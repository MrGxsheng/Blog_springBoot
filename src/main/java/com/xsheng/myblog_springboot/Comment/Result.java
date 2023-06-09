package com.xsheng.myblog_springboot.Comment;

import cn.hutool.core.lang.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 结果集
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/3/16 16:49:26
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;
    // 不需要返回值
    public static Result success() {
        return new Result(Constants.CODE_200, "操作成功", null);
    }
    // 需要返回值
    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }
    // 需要返回状态
    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }
    // 不需要返回状态
    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }
    /**
     * 分页
     *
     * @param list  数据
     * @param total 总数
     * @return 成功
     */
    @SuppressWarnings("rawtypes")
    public static Result success(Collection list, Long total) {
        return success(Dict.create().set("list", list).set("total", total));
    }
}


