package com.xsheng.myblog_springboot.service.impl;

import com.xsheng.myblog_springboot.entity.Friend;
import com.xsheng.myblog_springboot.mapper.FriendMapper;
import com.xsheng.myblog_springboot.service.IFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-16
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

}
