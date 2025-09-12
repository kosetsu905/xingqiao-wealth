package com.xingqiao.file.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.xingqiao.file.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Slf4j
@Service
public class OssService {

    @Autowired
    private OssConfig ossConfig;

    /**
     * 获取 OSS 客户端实例（每次请求新建，避免连接泄漏）
     */
    private OSS getOssClient() {
        return new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );
    }

    /**
     * 上传 MultipartFile 到 OSS（核心方法）
     * @param file 前端上传的文件
     * @param ossPath OSS 存储路径（如 "user/avatar/123.jpg"）
     * @return OSS 文件 URL
     */
    public String uploadFile(MultipartFile file, String ossPath) throws IOException {
        OSS ossClient = getOssClient();
        try {
            InputStream inputStream;
            // 判断是否为图片文件，如果是则进行无损压缩
            if (isImageFile(file)) {
                inputStream = compressImage(file);
            } else {
                inputStream = file.getInputStream();
            }

            // 上传文件流到 OSS
            PutObjectRequest putRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    ossPath,
                    inputStream
            );
            // 设置文件元信息（如 MIME 类型）
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(inputStream.available());
            putRequest.setMetadata(metadata);
            PutObjectResult result = ossClient.putObject(putRequest);
            log.info("上传结果{}", JSONObject.toJSONString(result));
            // 生成公共读 URL（若 Bucket 权限为私有，需生成签名 URL）
            return generatePublicUrl(ossPath);
        } finally {
            ossClient.shutdown(); // 关闭客户端（重要：避免资源泄露）
        }
    }

    /**
     * 判断文件是否为图片类型
     *
     * @param file MultipartFile文件对象
     * @return 是否为图片类型
     */
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.startsWith("image/") ||
                contentType.equals("application/octet-stream") && file.getOriginalFilename() != null &&
                        (file.getOriginalFilename().toLowerCase().endsWith(".jpg") ||
                                file.getOriginalFilename().toLowerCase().endsWith(".jpeg") ||
                                file.getOriginalFilename().toLowerCase().endsWith(".png") ||
                                file.getOriginalFilename().toLowerCase().endsWith(".gif") ||
                                file.getOriginalFilename().toLowerCase().endsWith(".bmp"))
        );
    }

    /**
     * 对图片进行无损压缩
     *
     * @param file 原始图片文件
     * @return 压缩后的输入流
     * @throws IOException IO异常
     */
    private InputStream compressImage(MultipartFile file) throws IOException {
        // 如果文件大小小于2MB，则不压缩
        if (file.getSize() < 2 * 1024 * 1024) {
            return file.getInputStream();
        }
        
        // 读取原始图片
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            // 如果无法读取为图片，直接返回原始流
            return file.getInputStream();
        }

        // 获取原始图片的宽高
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 先尝试按尺寸压缩
        int maxWidth = 1920;
        int maxHeight = 1080;

        // 如果原始图片小于等于最大宽高，则不按尺寸压缩
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            maxWidth = originalWidth;
            maxHeight = originalHeight;
        }

        // 计算压缩比例
        double widthRatio = (double) originalWidth / maxWidth;
        double heightRatio = (double) originalHeight / maxHeight;
        double ratio = Math.max(widthRatio, heightRatio);

        // 计算压缩后的宽高
        int newWidth = (int) (originalWidth / ratio);
        int newHeight = (int) (originalHeight / ratio);

        InputStream compressedStream;
        byte[] imageBytes;
        String formatName = "jpg";
        if (file.getContentType() != null) {
            if (file.getContentType().contains("png")) {
                formatName = "png";
            } else if (file.getContentType().contains("gif")) {
                formatName = "gif";
            } else if (file.getContentType().contains("bmp")) {
                formatName = "bmp";
            }
        }
        
        do {
            // 创建压缩后的图片
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = compressedImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            // 将压缩后的图片写入字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(compressedImage, formatName, baos);
            baos.flush();
            imageBytes = baos.toByteArray();
            baos.close();
            
            // 检查压缩后的大小，如果仍然大于2MB，则进一步减小尺寸
            if (imageBytes.length > 2 * 1024 * 1024) {
                // 每次将宽高减小10%
                newWidth = (int) (newWidth * 0.9);
                newHeight = (int) (newHeight * 0.9);
            }
        } while (imageBytes.length > 2 * 1024 * 1024 && newWidth > 100 && newHeight > 100); // 保证最小尺寸不小于100x100

        // 返回字节数组输入流
        return new ByteArrayInputStream(imageBytes);
    }

    /**
     * 生成 OSS 文件的公共读 URL（需 Bucket 权限为「公共读」）
     */
    public String generatePublicUrl(String ossPath) {
        OSS ossClient = getOssClient();
        try {
            // 生成URL不使用过期时间，直接使用bucket域名+object名称
            return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + ossPath;
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 删除 OSS 文件
     */
    public void deleteFile(String ossPath) {
        OSS ossClient = getOssClient();
        try {
            ossClient.deleteObject(ossConfig.getBucketName(), ossPath);
        } finally {
            ossClient.shutdown();
        }
    }
}