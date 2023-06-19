package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsheng.myblog_springboot.entity.Note;
import com.xsheng.myblog_springboot.mapper.NoteMapper;
import com.xsheng.myblog_springboot.service.INoteService;
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
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {

    @Resource
    private NoteMapper noteMapper;

    @Override
    public boolean noteExists(Note note) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Note::getUserId,note.getUserId()).eq(Note::getNoteName,note.getNoteName());
        return noteMapper.exists(wrapper);
    }

    @Override
    public boolean noteTextExists(Note note) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Note::getUserId,note.getUserId()).eq(Note::getNoteName,note.getNoteName()).eq(Note::getTypeId,note.getTypeId());
        return noteMapper.exists(wrapper);
    }

    @Override
    public Integer getNoteId(Note note) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Note::getUserId,note.getUserId()).eq(Note::getNoteName,note.getNoteName()).eq(Note::getTypeId,note.getTypeId());
        return noteMapper.selectOne(wrapper).getId();
    }
}
