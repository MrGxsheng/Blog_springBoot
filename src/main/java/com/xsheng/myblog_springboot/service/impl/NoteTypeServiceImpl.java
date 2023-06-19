package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsheng.myblog_springboot.entity.NoteType;
import com.xsheng.myblog_springboot.mapper.NoteTypeMapper;
import com.xsheng.myblog_springboot.service.INoteTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
@Service
public class NoteTypeServiceImpl extends ServiceImpl<NoteTypeMapper, NoteType> implements INoteTypeService {

    @Resource
    private NoteTypeMapper noteTypeMapper;

    @Override
    public boolean typeExists(String type) {
        LambdaQueryWrapper<NoteType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoteType::getType,type);
        return noteTypeMapper.exists(wrapper);
    }

    @Override
    public int getTypeId(String type) {
        LambdaQueryWrapper<NoteType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoteType::getType,type);
        return noteTypeMapper.selectOne(wrapper).getId();
    }

    @Override
    public String getTypeById(Integer id) {
        LambdaQueryWrapper<NoteType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoteType::getId,id);
        return noteTypeMapper.selectOne(wrapper).getType();
    }
}
