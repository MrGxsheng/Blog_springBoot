package com.xsheng.myblog_springboot.uils;


import com.xsheng.myblog_springboot.entity.Image;
import com.xsheng.myblog_springboot.service.IImageService;
import net.coobird.thumbnailator.Thumbnails;
import com.xsheng.myblog_springboot.service.IFileService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    private IImageService imageService;

    @Resource
    private IFileService fileService;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    // 屏幕宽度
    private static final int SCREEN_WIDTH = 1920;
    // 博客分几栏
    private static final int BLOG_COLUMNS = 2;

    // 支持压缩的类型
    private final String[] SUPPORT_TYPE = {".jpg", ".png", ".jpeg"};

    // 匹配markdown中的本地url格式
    private final static String pattern = "!\\[.*?\\]\\((.*?)\\)";

    @PostConstruct
    public void init() {
        fileUtil = this;
    }

    /**
     * 将MultipartFile转换为File
     *
     * @param multipartFile MultipartFile对象
     * @return 转换后的File对象
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        assert multipartFile.getOriginalFilename() != null;
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String convertMarkdownFileToString(File markdownFile) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(markdownFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(textToUrl(line)).append("\n");
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
    public static Map<String, String> fileUpload(MultipartFile file) throws IOException {
        // 初始化
        HashMap<String, String> map = new HashMap<>();

        String originalFilename = file.getOriginalFilename();

        int last = Objects.requireNonNull(originalFilename).lastIndexOf(".");

        String name = originalFilename.substring(0, last);
        String prex = originalFilename.substring(last);

        name = name + "----" + TimeUtil.ns() + prex;

        // 上传
        String url = fileUtil.downloadPath + file.getOriginalFilename();
        String path = fileUtil.uploadPath + file.getOriginalFilename();

        uploadToServer(file, path);
        java.io.File file1 = convertMultipartFileToFile(file);
        String md = Md5Util.md5(file1);
        boolean exists = FileUtil.fileExistsByMd5(md);

        if(exists){
            map.put("error","别传了，再传都给你删了");
        }
        map.put("md5",md);
        map.put("downloadPath", url);
        map.put("uploadPath", path);
        map.put("name", name);
        return map;
    }

    /**
     * 图片上传到服务器
     *
     * @param file   图片
     * @param userid 用户id
     * @return 信息
     */
    public static Map<String, Object> uploadImg(MultipartFile file, int userid) throws NoSuchAlgorithmException, IOException {
        Map<String, Object> map = new HashMap<>();


        String uuid = IdUtil.uuid();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        String path;
        String url;
        String reduceUrl;

        path = fileUtil.uploadPath + uuid + "-" + originalFilename;
        url = fileUtil.downloadPath + uuid + "-" + originalFilename;

        map.put("url",url);
        // 上传到服务器
        uploadToServer(file, path);

        File jFile = new File(path);
        // 追加md5
        String md5 = Md5Util.md5(jFile);

        // 文件去重
        if (!imgExistsByMd5(md5)) {
            // 设置浓缩图
            reduceUrl = fileUtil.setReduceImg(jFile);

            // 上传到数据库
            Image img = Image.builder()
                    .uploadTime(TimeUtil.now())
                    .md5(md5)
                    .userId(userid)
                    .imgName(originalFilename)
                    .imgPath(url)
                    .reducePath(reduceUrl)
                    .build();
            fileUtil.imageService.save(img);


        } else {
            deleteServerFile(path + ".jpg");
            map.put("msg", "图片已存在");
        }

        // 返回图片id
        map.put("id", fileUtil.imageService.getImgByMd5(md5).get(0).getId());

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

    /**
     * 删除服务上的文件
     * @author Master.Pan
     * @return 布尔值
     */
    public static boolean deleteServerFile(String filePath){
        File file = new File(filePath);
        return  file.exists() && file.isFile() && file.delete();

    }

    /**
     * 通过md5比对确定文件是否存在
     *
     * @param md5 md5
     * @return 存在
     */
    public static boolean fileExistsByMd5(String md5) {
        List<com.xsheng.myblog_springboot.entity.File> files = fileUtil.fileService.getFilesByMd5(md5);

        return files.size() > 0;
    }

    /**
     * 通过md5比对确定图片是否存在
     *
     * @param md5 md5
     * @return 存在
     */
    public static boolean imgExistsByMd5(String md5) {
        List<com.xsheng.myblog_springboot.entity.Image> files = fileUtil.imageService.getImgByMd5(md5);
        return files.size() > 0;
    }

    /**
     * 设置浓缩图 返回url
     *
     * @param file 仅限图片
     * @return url
     */
    public String setReduceImg(File file) throws IOException {

        // 浓缩图路径
        String reducePath = file.getPath() + ".jpg";

        // 获取类型
        String name = reducePath.substring(fileUtil.uploadPath.length());
        String fileName = file.getName();
        String type = getType(fileName);
        if (containsType(type)) {
            // 图片高度
            BufferedImage image = ImageIO.read(file);
            int height = image.getHeight();

            // 压缩
            Thumbnails.of(file).size(SCREEN_WIDTH / BLOG_COLUMNS, height).toFile(reducePath);

            // 返回路径
            return fileUtil.downloadPath + name;
        }

        return null;
    }

    /**
     * 根据文件名获取文件类型
     *
     * @param fileName 文件名
     * @return 类型
     */
    private String getType(String fileName) {
        if (!fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
     * 包含可处理图片
     *
     * @param type 文件类型
     * @return 是否是支持的图片类型
     */
    private boolean containsType(String type) {
        return Arrays.asList(SUPPORT_TYPE).contains(type);
    }


    /**
     * 将blog中的本地图片上传到服务器替换链接
     * // todo 烂尾了 纯属傻逼了
     * @param text 处理前的链接
     * @return 处理好的链接
     */
    private static String textToUrl(String text) throws IOException {
        // 不是图片类型 或者 开头是 http：// 或者 https:// 的 都不转换
        if (!text.matches(pattern) || (text.contains("(http://") || text.contains("(https://"))) return text;

        String start = text.substring(0, text.indexOf('(')); // 去掉右边括号
        String originName = text.substring(text.lastIndexOf('\\') + 1, text.length() - 1);
        //服务器路径
        String cloudUrl = fileUtil.downloadPath + "blog/img/" + originName;

        //本地链接
        String localUrl = text.substring(text.indexOf('('), text.indexOf(')'));


//        uploadToServer(, localUrl);
//
        text = start + '(' + cloudUrl + ')';

        return text;
    }



}
