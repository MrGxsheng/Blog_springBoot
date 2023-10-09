package com.xsheng.myblog_springboot.service.impl;

import com.xsheng.myblog_springboot.entity.SysOperLog;
import com.xsheng.myblog_springboot.mapper.SysOperLogMapper;
import com.xsheng.myblog_springboot.service.ISysOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-08-31
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {


    @Override
    public List<SysOperLog> expiredLog() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(7);
        return this.lambdaQuery().lt(SysOperLog::getOperTime,localDateTime)
                .list();
    }
}
