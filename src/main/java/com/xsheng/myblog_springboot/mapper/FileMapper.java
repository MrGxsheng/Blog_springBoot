package com.xsheng.myblog_springboot.mapper;

import com.xsheng.myblog_springboot.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xsheng
 * @since 2023-06-29
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
