package com.xsheng.myblog_springboot.Comment;

/**
 * 结果集
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/3/16 16:52:01
 */

public interface Constants {
    String CODE_200 = "200";    //成功
    String CODE_400 = "400";    //参数错误
    String CODE_401 = "401";    // 用户的锅
    String CODE_404 = "404";    //未找到
    String CODE_457 = "457";    // 上传文件异常
    String CODE_465 = "465";    // 重复的文章标题
    String CODE_485 = "485";    // 用户名或密码错误
    String CODE_498 = "498";    // 权限不足
    String CODE_499 = "499";    // token认证失败
    String CODE_500 = "500";    //服务器内部错误

    String CODE_10001 = "10001"; //怎么想都不一样吧
}
