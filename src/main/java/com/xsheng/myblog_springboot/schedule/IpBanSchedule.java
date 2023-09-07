package com.xsheng.myblog_springboot.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * ip封禁定时清空
 *
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/9/6 15:58:59
 */

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class IpBanSchedule{
    private final Map<String, List<String>> ipBan;

    //表示在每个月的第二天零点（即凌晨）执行任务。
    @Scheduled(cron = "0 0 0 2 * ?")
    private void cleanIpBan(){
        ipBan.clear();
    }
}
