package com.xsheng.myblog_springboot.service;

import cn.hutool.core.lang.Dict;
import com.xsheng.myblog_springboot.entity.File;
import com.xsheng.myblog_springboot.entity.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-15
 */
public interface IImageService extends IService<Image> {
    void addImage(MultipartFile[] file, Integer userId) throws IOException, NoSuchAlgorithmException;
    Dict getAllImage(Integer userId);
    List<Image> getImgByMd5(String md5);
    List<Image> getRandomImg(Integer userId,Integer count);
    void deleteImg(Integer imgId);
}
