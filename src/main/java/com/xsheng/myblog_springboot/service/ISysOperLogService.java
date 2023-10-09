package com.xsheng.myblog_springboot.service;

import com.xsheng.myblog_springboot.entity.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-08-31
 */
public interface ISysOperLogService extends IService<SysOperLog> {
    List<SysOperLog> expiredLog();
}

