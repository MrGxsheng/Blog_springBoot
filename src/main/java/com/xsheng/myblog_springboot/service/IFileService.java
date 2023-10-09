package com.xsheng.myblog_springboot.service;

import com.xsheng.myblog_springboot.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-29
 */
public interface IFileService extends IService<File> {
    // md5 -> files
    List<com.xsheng.myblog_springboot.entity.File> getFilesByMd5(String md5);
}
