package com.xsheng.myblog_springboot.service;

import com.xsheng.myblog_springboot.entity.NoteType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsheng
 * @since 2023-06-13
 */
public interface INoteTypeService extends IService<NoteType> {

    boolean typeExists(String type);
    int getTypeId(String type);
    String getTypeById(Integer id);
}
