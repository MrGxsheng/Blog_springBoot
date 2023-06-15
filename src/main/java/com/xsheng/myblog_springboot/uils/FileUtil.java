package com.xsheng.myblog_springboot.uils;

import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.Note;
import com.xsheng.myblog_springboot.service.INoteService;
import com.xsheng.myblog_springboot.service.INoteTypeService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/14 12:19:22
 */
@Component

public class FileUtil {

    @Resource
    private INoteTypeService noteTypeService;

    @Resource
    private INoteService noteService;



//    public Note mdUpload(MultipartFile file,
//                          String type,
//                         int userId){
//        String originalFilename  = file.getOriginalFilename();
//        assert originalFilename != null;
//
//        String title = originalFilename.substring(0,originalFilename.lastIndexOf("."));
//        int typeId = noteTypeService.getTypeId(type);
//
//        Note note = Note.builder().noteName(title).noteText(file.getContentType()).userId(userId).typeId(typeId).build();
//
//
//        if(!noteService.noteExists(note)){
//            note.setCreateTime(TimeUtil.now());
//            note.setUpdateTime(TimeUtil.now());
//            noteService.save(note);
//        }else{
//            note.setId(noteService.getNoteId(note));
//            updateNote(note);
//        }
//        return note;
//    }





    /**
     * 将MultipartFile转换为File
     *
     * @param multipartFile MultipartFile对象
     * @return 转换后的File对象
     * @throws IOException
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        assert multipartFile.getOriginalFilename() != null;
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }



    public static String convertMarkdownFileToString(File markdownFile) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(markdownFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String markdownContent = contentBuilder.toString();
        return convertMarkdownStringToHtml(markdownContent);
    }

    public static String convertMarkdownStringToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
