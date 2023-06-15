package com.xsheng.myblog_springboot.uils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/6/15 10:45:56
 */
public class OssUtil {
    // oss 所在域
    public static String endpint = "https://oss-cn-beijing.aliyuncs.com/";
    // 密钥
    public static String accessKetId = "LTAI5tSRF293qMBMh4Wo7tWk";
    // 密钥
    public static String accessKeySecret = "m7AMYmaTOrQbANujmvvlOIHY8QcrTe";
    // oss对象
    public static String bucket = "hasdsd-markdown";

    private final static String save_context_path = "img/";
    public static String copyEndpint = "oss-cn-beijing.aliyuncs.com/";


    /**
     * 上传文件到OSS
     *
     * @param file 文件
     * @return 文件的访问URL
     */
    public static String upload(MultipartFile file) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpint, accessKetId, accessKeySecret);


        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        fileName = "image-" + TimeUtil.nowDay() + TimeUtil.ns() + prefix;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 上传文件流。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, save_context_path + fileName, inputStream);
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();

        // 返回文件的URL地址
        return "https://"+ bucket  + "." + copyEndpint +  save_context_path + fileName;
    }
}
