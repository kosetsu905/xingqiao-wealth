package com.xingqiao.file.controller;


import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.file.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/oss")
public class OssController {

    private static final Logger log = LoggerFactory.getLogger(OssController.class);

    @Autowired
    private OssService ossService;

    /**
     * 前端文件上传接口（接收 MultipartFile）
     * @param file 上传的文件
     * @param dir 存储目录（如 "user/avatar"）
     * @return OSS 文件 URL
     */
    @PostMapping("/upload")
    public R<Object> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", defaultValue = "common") String dir
    ) {
        try {
            log.info("开始上传文件，文件名: {}, 文件大小: {}, Content-Type: {}", 
                    file.getOriginalFilename(), file.getSize(), file.getContentType());
            
            // 使用原始文件名
            String originalFilename = file.getOriginalFilename();
            // 拼接 OSS 存储路径（如 "common/原始文件名"）
            String ossPath = dir + "/" + originalFilename;

            // 调用 OSS 服务上传
            String ossUrl = ossService.uploadFile(file, ossPath);
            
            // 对URL进行解码，将文件名中的特殊字符转换为中文
            String decodedOssUrl = URLDecoder.decode(ossUrl, StandardCharsets.UTF_8.toString());
            log.info("文件上传成功，文件路径: {}", ossPath);
            //不返回?后面点链接，只返回照片地址
            if(!StringUtils.isEmpty(decodedOssUrl)){
                return R.ok(decodedOssUrl.substring(0,decodedOssUrl.indexOf("?") ), "上传成功");
            }
            return R.fail(decodedOssUrl, "上传失败");
        } catch (Exception e) {
            log.error("文件上传失败，文件名: " + file.getOriginalFilename() + 
                      ", 文件大小: " + file.getSize(), e);
            return R.fail("上传失败：" + e.getMessage());
        }
    }
}