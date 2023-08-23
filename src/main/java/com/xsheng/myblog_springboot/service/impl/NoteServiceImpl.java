package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.xsheng.myblog_springboot.entity.Note;
import com.xsheng.myblog_springboot.entity.NoteType;
import com.xsheng.myblog_springboot.mapper.NoteMapper;
import com.xsheng.myblog_springboot.mapper.NoteTypeMapper;
import com.xsheng.myblog_springboot.service.INoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsheng.myblog_springboot.uils.FileUtil;
import com.xsheng.myblog_springboot.uils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "note")
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {

    @Resource
    private NoteMapper noteMapper;

    @Resource
    private NoteTypeServiceImpl noteTypeService;

    @Resource
    private NoteTypeMapper noteTypeMapper;
    private final static LambdaQueryWrapper<Note> NOTE_LAMBDA_QUERY_WRAPPER = new LambdaQueryWrapper<>();
    private final static LambdaQueryWrapper<NoteType> NOTE_TYPE_LAMBDA_QUERY_WRAPPER = new LambdaQueryWrapper<>();

    @Override
    public boolean noteExists(Note note) {

        log.info("1{}2{}", note.getUserId(), note.getNoteName());

        NOTE_LAMBDA_QUERY_WRAPPER.eq(Note::getUserId, note.getUserId()).eq(Note::getNoteName, note.getNoteName());
        return noteMapper.exists(NOTE_LAMBDA_QUERY_WRAPPER);
    }

    @Override
    @Cacheable(key = "#id + '-' + #type")
    public List<Note> getAll(Integer id, String type) {

        LambdaQueryWrapper<Note> queryWrapper = NOTE_LAMBDA_QUERY_WRAPPER.eq(Note::getUserId, id)
                .eq(
                        Note::getTypeId, noteTypeMapper.selectOne(
                                NOTE_TYPE_LAMBDA_QUERY_WRAPPER.eq(NoteType::getType, type)
                        ).getId()
                );


        return noteMapper.selectList(queryWrapper);
    }

    @Override
    public boolean noteTextExists(Note note) {

        NOTE_LAMBDA_QUERY_WRAPPER.eq(Note::getUserId, note.getUserId()).eq(Note::getNoteName, note.getNoteName()).eq(Note::getTypeId, note.getTypeId());
        return noteMapper.exists(NOTE_LAMBDA_QUERY_WRAPPER);
    }

    @Override
    @Cacheable(key = "#note")
    public Integer getNoteId(Note note) {

        NOTE_LAMBDA_QUERY_WRAPPER.eq(Note::getUserId, note.getUserId()).eq(Note::getNoteName, note.getNoteName()).eq(Note::getTypeId, note.getTypeId());
        return noteMapper.selectOne(NOTE_LAMBDA_QUERY_WRAPPER).getId();
    }

    @Override
    @CacheEvict(allEntries = true)
    public void addNote(MultipartFile[] file, String type, Integer userId) throws IOException {
        for (MultipartFile multipartFile : file) {
            String originalFilename = multipartFile.getOriginalFilename();
            assert originalFilename != null;

            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            int typeId = noteTypeService.getTypeId(type);

            File fz = FileUtil.convertMultipartFileToFile(multipartFile);
            String text = FileUtil.convertMarkdownFileToString(fz);

            Note note = Note.builder().noteName(title).noteText(text).userId(userId).typeId(typeId).build();


            if (!noteExists(note)) {
                note.setCreateTime(TimeUtil.now());
                note.setUpdateTime(TimeUtil.now());
                save(note);
            } else if (!noteTextExists(note)) {
                note.setId(getNoteId(note));
                note.setUpdateTime(TimeUtil.now());
                updateById(note);
            }
        }

    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteNote(Integer id, String type) {
        noteMapper.deleteById(id);
    }
}
