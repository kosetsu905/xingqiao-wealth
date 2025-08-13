package com.xingqiao.file.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.xingqiao.file.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.model.DeleteObjectsRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

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
        try (InputStream inputStream = file.getInputStream()) {
            // 上传文件流到 OSS
            PutObjectRequest putRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    ossPath,
                    inputStream
            );
            // 设置文件元信息（如 MIME 类型）
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            putRequest.setMetadata(metadata);
            ossClient.putObject(putRequest);
            // 生成公共读 URL（若 Bucket 权限为私有，需生成签名 URL）
            return generatePublicUrl(ossPath);
        } finally {
            ossClient.shutdown(); // 关闭客户端（重要：避免资源泄露）
        }
    }

    /**
     * 生成 OSS 文件的公共读 URL（需 Bucket 权限为「公共读」）
     */
    public String generatePublicUrl(String ossPath) {
        OSS ossClient = getOssClient();
        try {
            Date expiration = new Date(System.currentTimeMillis() + 365 * 24 * 3600 * 1000L); // 1 年有效期
            URL url = ossClient.generatePresignedUrl(
                    ossConfig.getBucketName(),
                    ossPath,
                    expiration
            );
            return url.toString();
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
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(
                    ossConfig.getBucketName()
            );
            ossClient.deleteObject(deleteRequest);
        } finally {
            ossClient.shutdown();
        }
    }
}