package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsheng.myblog_springboot.entity.File;
import com.xsheng.myblog_springboot.mapper.FileMapper;
import com.xsheng.myblog_springboot.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-29
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Resource FileMapper fileMapper;

    // 根据md5查询
    @Override
    public List<com.xsheng.myblog_springboot.entity.File> getFilesByMd5(String md5) {
        LambdaQueryWrapper<com.xsheng.myblog_springboot.entity.File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getMd5, md5);
        return fileMapper.selectList(wrapper);
    }
}
