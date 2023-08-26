package com.xsheng.myblog_springboot.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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


    @Override
    @Cacheable(key = "#id + '-' + #type")
    public List<Note> getAll(Integer id, String type) {
        return this.lambdaQuery().eq(Note::getUserId, id)
                .eq(
                        Note::getTypeId,
                        noteTypeService.lambdaQuery()
                                .eq(NoteType::getType, type)
                                .one()
                                .getId()
                ).list();
    }


    /**
     * 检查名字是否重复
     *
     * @param note 笔记实例
     * @return 是否存在
     */
    @Override
    public boolean noteNameExists(Note note) {
        return this.lambdaQuery()
                .eq(Note::getUserId, note.getUserId())
                .eq(Note::getNoteName, note.getNoteName())
                .eq(Note::getTypeId, note.getTypeId())
                .exists();
    }

    @Override
    public boolean noteTextExists(Note note) {
        return this.lambdaQuery()
                .eq(Note::getUserId, note.getUserId())
                .eq(Note::getNoteText, note.getNoteText())
                .eq(Note::getTypeId, note.getTypeId()).exists();
    }

    @Override
    public Integer getNoteId(Note note, String name) {
        LambdaQueryChainWrapper<Note> noteLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(Note::getTypeId, note.getTypeId())
                .eq(Note::getUserId, note.getUserId());
        if (Objects.equals(name, "name")) {
            noteLambdaQueryChainWrapper.eq(Note::getNoteName, note.getNoteName());
        } else {
            noteLambdaQueryChainWrapper.eq(Note::getNoteText, note.getNoteText());
        }
        return noteLambdaQueryChainWrapper.one().getId();
    }

    @Override
    @CacheEvict(allEntries = true)
    public void addNote(MultipartFile[] file, String type, Integer userId) throws IOException {
        for (MultipartFile multipartFile : file) {
            String originalFilename = multipartFile.getOriginalFilename();
            assert originalFilename != null;

            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            Integer typeId = noteTypeService.getTypeId(type);

            File fz = FileUtil.convertMultipartFileToFile(multipartFile);
            String text = FileUtil.convertMarkdownFileToString(fz);

            Note note = Note.builder().noteName(title).noteText(text).userId(userId).typeId(typeId).build();


            boolean nameExists = noteNameExists(note);
            boolean noteTextExists = noteTextExists(note);

            if (!nameExists && !noteTextExists) {
                note.setCreateTime(TimeUtil.now());
                note.setUpdateTime(TimeUtil.now());
                save(note);
            } else if (nameExists ^ noteTextExists) { // 有一个为true更新
                Integer noteId = getNoteId(note, nameExists ? "name" : "text");
                note.setId(noteId);
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
