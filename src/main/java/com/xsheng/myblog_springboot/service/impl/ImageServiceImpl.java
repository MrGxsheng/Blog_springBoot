package com.xsheng.myblog_springboot.service.impl;

import cn.hutool.core.lang.Dict;
import com.xsheng.myblog_springboot.entity.Image;
import com.xsheng.myblog_springboot.mapper.ImageMapper;
import com.xsheng.myblog_springboot.service.IImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsheng.myblog_springboot.uils.FileUtil;
import com.xsheng.myblog_springboot.uils.NumberUtil;
import com.xsheng.myblog_springboot.uils.OssUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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

    /**
     * 添加图片
     *
     * @param file   图片
     * @param userId 上传人的id
     */
    @Override
    @CacheEvict(allEntries = true)
    public void addImage(MultipartFile[] file, Integer userId) throws NoSuchAlgorithmException, IOException {
        for (MultipartFile multipartFile : file) {
            FileUtil.uploadImg(multipartFile, userId);
        }
    }

    /**
     * 获取全部图片
     *
     * @param userId 获取图片的用户的id
     * @return 返回全部图片及数量
     */
    @Override
    @Cacheable(key = "#userId")
    public Dict getAllImage(Integer userId) {
        List<Image> images = this.lambdaQuery().eq(Image::getUserId, userId).list();
        return new Dict().set("list", images).set("count", images.size());
    }

    @Override
    public List<Image> getImgByMd5(String md5) {
        return this.lambdaQuery().eq(Image::getMd5, md5).list();
    }

    /**
     * 获取count条乱序 图片
     * @param userId 用户id
     * @param count 数量
     * @return 图片列表
     */
    @Override
    public List<Image> getRandomImg(Integer userId, Integer count) {
        Dict allImage = this.getAllImage(userId);
        List<Integer> randomNumberList = NumberUtil.getRandomNumberList(0, Integer.parseInt(allImage.get("count").toString()) - 1, count);
        Object list = allImage.get("list");
        assert list instanceof List;

        List<Image> RandomImg = new ArrayList<>();
        randomNumberList.forEach(num -> RandomImg.add((Image) ((List<?>) list).get(num)));
        return RandomImg;
    }
}
