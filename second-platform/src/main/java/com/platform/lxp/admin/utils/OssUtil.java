package com.platform.lxp.admin.utils;
import com.aliyun.oss.OSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class OssUtil {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.fileHost}")
    private String fileHost;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    public String upload(MultipartFile file) {
        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 新文件名
            String fileName = UUID.randomUUID() + suffix;
            // OSS路径
            String filePath = fileHost + "/" + fileName;

            InputStream inputStream = file.getInputStream();
            // 上传
            ossClient.putObject(bucketName, filePath, inputStream);

            // 返回可访问URL
            return "https://" + bucketName + "." + endpoint + "/" + filePath;
        } catch (IOException e) {
            throw new RuntimeException("上传失败", e);
        }
    }
}
