package com.xsheng.myblog_springboot.mapper;

import com.xsheng.myblog_springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xsheng
 * @since 2023-06-10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
