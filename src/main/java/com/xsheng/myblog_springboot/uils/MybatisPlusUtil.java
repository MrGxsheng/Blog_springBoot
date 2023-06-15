package com.xsheng.myblog_springboot.uils;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/14 13:30:41
 */
public class MybatisPlusUtil {
    /**
     * 最后拼接一个分页
     *
     * @param currentPage 当前页
     * @param pageSize    页数
     * @return 拼接字符串
     */
    public static String getLimitString(Integer currentPage, Integer pageSize) {
        return "limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
    }
}
