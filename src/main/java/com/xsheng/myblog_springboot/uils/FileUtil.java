package com.xsheng.myblog_springboot.uils;

import com.xsheng.myblog_springboot.Comment.Result;
import com.xsheng.myblog_springboot.entity.Note;
import com.xsheng.myblog_springboot.service.IFileService;
import com.xsheng.myblog_springboot.service.INoteService;
import com.xsheng.myblog_springboot.service.INoteTypeService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private static FileUtil fileUtil;

    @Resource
    private INoteTypeService noteTypeService;

    @Resource
    private INoteService noteService;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    @Resource
    private IFileService fileService;


    @PostConstruct
    public void init() {
        fileUtil = this;
    }

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

    // 文件上传
    public static Map<String, String> fileUpload(MultipartFile file) {
        // 初始化
        HashMap<String, String> map = new HashMap<>();

        String originalFilename = file.getOriginalFilename();

        int last = Objects.requireNonNull(originalFilename).lastIndexOf(".");

        String name = originalFilename.substring(0,last);
        String prex = originalFilename.substring(last);

        name = name + "----" + TimeUtil.ns() + prex;

        // 上传
        String url = fileUtil.downloadPath + file.getOriginalFilename();
        String path = fileUtil.uploadPath + file.getOriginalFilename();
        uploadToServer(file, path);

        map.put("downloadPath", url);
        map.put("uploadPath", path);
        map.put("name", name);
        return map;
    }

    // 上传到服务器
    public static void uploadToServer(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            try {
                file.transferTo(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // md5是否已经存在
    public static boolean fileExists(String md5) {
        List<com.xsheng.myblog_springboot.entity.File> files = fileUtil.fileService.getFilesByMd5(md5);

        return files.size() > 0;
    }


}
