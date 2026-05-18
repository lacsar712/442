package com.classroom.service.impl;

import com.classroom.exception.BusinessException;
import com.classroom.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件服务实现
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${upload.path:/app/uploads}")
    private String uploadPath;

    private static final String[] ALLOWED_EXTENSIONS = {
        "jpg", "jpeg", "png", "gif", "bmp", "webp",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "txt", "zip", "rar", "7z"
    };

    @Override
    public Map<String, String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new BusinessException("文件名不能为空");
        }

        // 获取文件扩展名
        String extension = getFileExtension(originalFilename);
        if (!isAllowedExtension(extension)) {
            throw new BusinessException("不支持的文件类型: " + extension);
        }

        // 生成存储路径
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String newFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        String relativePath = datePath + "/" + newFilename;
        String fullPath = uploadPath + "/" + relativePath;

        try {
            // 创建目录
            Path directory = Paths.get(uploadPath, datePath);
            Files.createDirectories(directory);

            // 保存文件
            File destFile = new File(fullPath);
            file.transferTo(destFile);

            logger.info("文件上传成功: {} -> {}", originalFilename, relativePath);

            Map<String, String> result = new HashMap<>();
            result.put("filePath", relativePath);
            result.put("fileName", originalFilename);
            result.put("url", "/uploads/" + relativePath);
            
            return result;
        } catch (IOException e) {
            logger.error("文件上传失败: {}", e.getMessage());
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream getFileStream(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            throw new BusinessException("文件路径不能为空");
        }

        String fullPath = uploadPath + "/" + filePath;
        File file = new File(fullPath);
        
        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }

        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            logger.error("获取文件流失败: {}", e.getMessage());
            throw new BusinessException("获取文件失败");
        }
    }

    @Override
    public void delete(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return;
        }

        String fullPath = uploadPath + "/" + filePath;
        File file = new File(fullPath);
        
        if (file.exists()) {
            if (file.delete()) {
                logger.info("文件删除成功: {}", filePath);
            } else {
                logger.warn("文件删除失败: {}", filePath);
            }
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
