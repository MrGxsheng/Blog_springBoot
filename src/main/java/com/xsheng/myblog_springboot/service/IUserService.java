package com.xsheng.myblog_springboot.service;

import com.xsheng.myblog_springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-10
 */
public interface IUserService extends IService<User> {

    User getUserByAccount(String account);

    boolean accountExists(String account);
}
