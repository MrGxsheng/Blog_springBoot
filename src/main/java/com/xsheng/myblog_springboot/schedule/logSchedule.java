package com.xsheng.myblog_springboot.schedule;

import com.xsheng.myblog_springboot.entity.SysOperLog;
import com.xsheng.myblog_springboot.service.ISysOperLogService;
import com.xsheng.myblog_springboot.service.impl.SysOperLogServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 日志的日常清理
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/9/11 09:37:12
 */
@EnableScheduling
@Configuration
public class logSchedule {
    @Resource
    private ISysOperLogService sysOperLogService;

    /**
     * 日志垃圾回收
     */
    @Scheduled(cron = "0 0 0 7 * ?")
    @PostConstruct
    public void recycleLog(){
        sysOperLogService.removeBatchByIds(sysOperLogService.expiredLog());
    }

}
