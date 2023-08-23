package com.xsheng.myblog_springboot.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsheng.myblog_springboot.entity.Image;
import com.xsheng.myblog_springboot.mapper.ImageMapper;
import com.xsheng.myblog_springboot.service.IImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsheng.myblog_springboot.uils.OssUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-15
 */
@Service
@CacheConfig(cacheNames = "image")
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {


    @Resource
    private ImageMapper imageMapper;
    private static final LambdaQueryWrapper<Image> IMAGE_LAMBDA_QUERY_WRAPPER = new LambdaQueryWrapper<>();

    /**
     * 添加图片
     * @param file 图片
     * @param userId  上传人的id
     */
    @Override
    @CacheEvict(allEntries = true)
    public void addImage(MultipartFile[] file, Integer userId) {
        for (MultipartFile multipartFile : file) {
            String upload = OssUtil.upload(multipartFile);
            Image builder = Image.builder().imgName(multipartFile.getOriginalFilename()).imgPath(upload).userId(userId).uploadTime(TimeUtil.now()).build();
            imageMapper.insert(builder);
        }
    }

    /**
     * 获取全部图片
     * @param userId 获取图片的用户的id
     * @return 返回全部图片及数量
     */
    @Override
    @Cacheable(key = "#userId")
    public Dict getAllImage(Integer userId) {

        LambdaQueryWrapper<Image> queryWrapper = IMAGE_LAMBDA_QUERY_WRAPPER.eq(Image::getUserId, userId);
        List<Image> images = imageMapper.selectList(queryWrapper);

        return new Dict().set("list",images).set("count",images.size());
    }
}
