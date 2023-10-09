package com.xsheng.myblog_springboot.mapper;

import com.xsheng.myblog_springboot.entity.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author xsheng
 * @since 2023-08-31
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

}
