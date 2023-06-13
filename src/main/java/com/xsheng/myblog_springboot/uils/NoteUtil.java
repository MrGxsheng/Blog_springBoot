package com.xsheng.myblog_springboot.uils;

import com.xsheng.myblog_springboot.Comment.DangerousException;
import com.xsheng.myblog_springboot.entity.NoteType;

import java.util.Objects;

import static com.xsheng.myblog_springboot.Comment.Constants.CODE_10001;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/13 13:54:36
 */
public class NoteUtil {

    public static void  verify(NoteType noteType , NoteType noteTypeR){
        if(Objects.equals(noteType.getType(),noteTypeR.getType())){
           throw new  DangerousException(CODE_10001,"类型已经存在",noteTypeR.getId());
        }
    }

}
