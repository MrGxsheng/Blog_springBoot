package com.xsheng.myblog_springboot.service.impl;

import com.xsheng.myblog_springboot.entity.Image;
import com.xsheng.myblog_springboot.mapper.ImageMapper;
import com.xsheng.myblog_springboot.service.IImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-15
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {

}
